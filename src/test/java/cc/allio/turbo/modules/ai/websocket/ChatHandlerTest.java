package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.ai.Driver;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.Topics;
import cc.allio.turbo.modules.auth.jwt.TurboJwtDecoder;
import cc.allio.turbo.modules.auth.jwt.TurboJwtEncoder;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ChatHandlerTest extends BaseTestCase {

    ChatHandler chatHandler;

    @Override
    protected void onInit() throws Throwable {
        Driver<Input> inputDriver = Driver.from(Input.class);
        Driver<Output> outputDriver = Driver.from(Output.class);
        chatHandler = new ChatHandler(inputDriver, outputDriver, TurboJwtDecoder.getInstance());
    }

    @Test
    void testAuthentication() {
        HandshakeInfo mockHandshakeInfo = Mockito.mock(HandshakeInfo.class);
        Map<String, Object> attributes = Maps.newConcurrentMap();
        HttpHeaders headers = new HttpHeaders();

        Mockito.when(mockHandshakeInfo.getAttributes()).thenReturn(attributes);
        Mockito.when(mockHandshakeInfo.getHeaders()).thenReturn(headers);


        boolean a1 = chatHandler.isAuthentication(mockHandshakeInfo);
        Assertions.assertFalse(a1);

        attributes.put(WebUtil.X_AUTHENTICATION, "");
        boolean a2 = chatHandler.isAuthentication(mockHandshakeInfo);
        Assertions.assertFalse(a2);

        String validToken = buildToken(new Date().toInstant().plus(1000L, ChronoUnit.MILLIS));
        attributes.put(WebUtil.X_AUTHENTICATION, validToken);
        boolean a3 = chatHandler.isAuthentication(mockHandshakeInfo);
        Assertions.assertTrue(a3);


        String expiredToken = buildToken(new Date().toInstant().minus(1000L, ChronoUnit.MILLIS));
        attributes.put(WebUtil.X_AUTHENTICATION, expiredToken);
        boolean a4 = chatHandler.isAuthentication(mockHandshakeInfo);
        Assertions.assertFalse(a4);
    }

    @Test
    void testCorrectHandle() {
        AtomicReference<FluxSink<WebSocketMessage>> sinkRef = new AtomicReference<>();
        Flux<WebSocketMessage> receive = Flux.create(sinkRef::set);

        WebSocketSession mockWebSocketSession = Mockito.mock(WebSocketSession.class);
        Mockito.when(mockWebSocketSession.receive()).thenReturn(receive);
        Mockito.when(mockWebSocketSession.getId()).thenReturn("1");

        HandshakeInfo mockHandshakeInfo = validHandshakeInfo();
        Mockito.when(mockWebSocketSession.getHandshakeInfo()).thenReturn(mockHandshakeInfo);

        chatHandler.handle(mockWebSocketSession).subscribe();

        sinkToMessage(sinkRef.get(), "hello");

        Driver.from(Input.class)
                .subscribeOn(Topics.USER_INPUT_PATTERNS)
                .observeMany()
                .flatMap(subscription ->
                        Mono.justOrEmpty(subscription.getDomain().flatMap(input -> input.getMessages().stream().findFirst())))
                .take(1L)
                .parallel()
                .as(StepVerifier::create)
                .expectNext("hello")
                .verifyComplete();
    }

    String buildToken(Instant expired) {
        JwtClaimsSet jwtClaimsSet =
                JwtClaimsSet.builder()
                        .expiresAt(expired)
                        .build();
        Jwt jwt =
                TurboJwtEncoder.getInstance().encode(JwtEncoderParameters.from(jwtClaimsSet));
        return jwt.getTokenValue();
    }

    HandshakeInfo validHandshakeInfo() {
        HandshakeInfo mockHandshakeInfo = Mockito.mock(HandshakeInfo.class);
        Map<String, Object> attributes = Maps.newConcurrentMap();
        HttpHeaders headers = new HttpHeaders();

        Mockito.when(mockHandshakeInfo.getAttributes()).thenReturn(attributes);
        Mockito.when(mockHandshakeInfo.getHeaders()).thenReturn(headers);

        String validToken = buildToken(new Date().toInstant().plus(1000L, ChronoUnit.MILLIS));
        attributes.put(WebUtil.X_AUTHENTICATION, validToken);
        return mockHandshakeInfo;
    }

    void sinkToMessage(FluxSink<WebSocketMessage> sink, String msg) {
        Mono.just(1)
                .delayElement(Duration.ofSeconds(1L))
                .then(Mono.fromRunnable(() -> {
                    Message message = Message.fromSingle(msg);
                    byte[] bytes = JsonUtils.toJson(message).getBytes();
                    DefaultDataBuffer dataBuffer = DefaultDataBufferFactory.sharedInstance.wrap(bytes);
                    WebSocketMessage webSocketMessage = new WebSocketMessage(WebSocketMessage.Type.TEXT, dataBuffer);
                    sink.next(webSocketMessage);
                }))
                .subscribe();

    }
}
