package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.Topics;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.auth.jwt.TurboJwtEncoder;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

public class ChatHandlerTest extends BaseTestCase {

    ChatHandler chatHandler;

    @Override
    protected void onInit() throws Throwable {
        Driver<Input> inputDriver = Driver.from(Input.class);
        Driver<Output> outputDriver = Driver.from(Output.class);
        chatHandler = new ChatHandler(inputDriver, outputDriver);
    }

    @Test
    void testCorrectHandle() {
        WebSocketSession mockSession = Mockito.mock(WebSocketSession.class);
        Mockito.when(mockSession.getId()).thenReturn("1");

        validSessionInfo(mockSession);

        Mono.delay(Duration.ofSeconds(1L))
                .then(Mono.fromRunnable(() -> {
                    assertDoesNotThrow(() -> chatHandler.handleMessage(mockSession, new TextMessage("hello")));
                }))
                .subscribe();

        Driver.from(Input.class)
                .subscribeOn(Topics.USER_CHAT_INPUT_PATTERNS)
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

    void validSessionInfo(WebSocketSession mockSession) {
        Map<String, Object> attributes = Maps.newConcurrentMap();
        HttpHeaders headers = new HttpHeaders();

        Mockito.when(mockSession.getAttributes()).thenReturn(attributes);
        Mockito.when(mockSession.getHandshakeHeaders()).thenReturn(headers);

        String validToken = buildToken(new Date().toInstant().plus(1000L, ChronoUnit.MILLIS));
        attributes.put(WebUtil.X_AUTHENTICATION, validToken);
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
