package cc.allio.turbo.modules.ai.websocket;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.UndertowWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.NonNull;
import org.xnio.OptionMap;
import org.xnio.Xnio;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class WebsocketIntegrationTest extends BaseTestCase {

    private UndertowHttpServer server;
    private WebSocketClient client;
    private ApplicationContext context;

    private static final String HOST = "localhost";
    private static final Integer PORT = 8000;
    private static final Duration TIMEOUT = Duration.ofMillis(5000);

    @Override
    protected void onInit() throws Throwable {
        this.client = new UndertowWebSocketClient(Xnio.getInstance().createWorker(OptionMap.EMPTY));
        this.server = new UndertowHttpServer();
        server.setHost(HOST);
        server.setPort(PORT);
        this.context =
                new AnnotationConfigApplicationContext(
                        DispatcherConfig.class,
                        WebsocketConfiguration.class,
                        WsConfiguration.class);
        HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context).build();
        server.setHandler(handler);

        server.initServer();
        server.start();
    }

    @Test
    void testEcho() {
        String message = "hello";

        AtomicReference<String> ref = new AtomicReference<>();
        client.execute(getURL("/echo"), session ->
                        session.send(Mono.justOrEmpty(session.textMessage(message)))
                                .thenMany(session.receive().take(1))
                                .map(WebSocketMessage::getPayloadAsText)
                                .doOnNext(ref::set)
                                .then())
                .block(TIMEOUT);

        assertEquals(message, ref.get());
    }

    URI getURL(@NonNull String path) {
        try {
            return new URI("ws://" + HOST + ":" + PORT + path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onDown() throws Throwable {
        server.stop();
    }

    @Configuration
    static class DispatcherConfig {

        @Bean
        public WebFilter contextFilter() {
            return new ServerWebExchangeContextFilter();
        }

        @Bean
        public DispatcherHandler webHandler() {
            return new DispatcherHandler();
        }
    }

    @Configuration
    static class WebsocketConfiguration {

        @Bean
        public HandlerMapping testHandlerMapping() {
            Map<String, WebSocketHandler> map = new HashMap<>();
            map.put("/echo", echoWebSocketHandler());
            return new SimpleUrlHandlerMapping(map);
        }

        @Bean
        public WebSocketHandler echoWebSocketHandler() {
            return session ->
                    session.receive()
                            .map(WebSocketMessage::getPayloadAsText)
                            .flatMap(text -> {
                                WebSocketMessage webSocketMessage = session.textMessage(text);
                                return session.send(Mono.justOrEmpty(webSocketMessage));
                            })
                            .then();
        }
    }

}
