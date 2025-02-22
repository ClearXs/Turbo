package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.modules.ai.*;
import cc.allio.turbo.modules.auth.jwt.JwtCodecConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.UndertowRequestUpgradeStrategy;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

import java.util.Map;

@AutoConfiguration
@ImportAutoConfiguration({DriverConfiguration.class, JwtCodecConfiguration.class})
@AutoConfigureAfter(DriverConfiguration.class)
@EnableWebSocket
public class WsConfiguration {

    @Bean
    public ChatHandler chatHandler(@Qualifier("Driver_Input") Driver<Input> inputDriver,
                                   @Qualifier("Driver_Output") Driver<Output> outputDriver,
                                   JwtDecoder jwtDecoder) {
        return new ChatHandler(inputDriver, outputDriver, jwtDecoder);
    }

    @Bean
    public WebSocketConfigurer webSocketConfigurer(ChatHandler chatHandler) {
        return registry -> registry.addHandler(chatHandler, "/ai/ws/chat/**");
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketHandlerAdapter wsHandlerAdapter() {
        return new WebSocketHandlerAdapter(webSocketService());
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketService webSocketService() {
        UndertowRequestUpgradeStrategy strategy = new UndertowRequestUpgradeStrategy();
        return new HandshakeWebSocketService(strategy);
    }
}
