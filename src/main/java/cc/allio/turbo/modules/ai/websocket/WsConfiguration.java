package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.modules.ai.*;
import cc.allio.turbo.modules.auth.jwt.JwtCodecConfiguration;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.UndertowRequestUpgradeStrategy;

import java.util.Map;

@AutoConfiguration
@ImportAutoConfiguration({DriverConfiguration.class, JwtCodecConfiguration.class})
@AutoConfigureAfter(DriverConfiguration.class)
public class WsConfiguration {

    @Bean
    public ChatHandler chatHandler(@Qualifier("Driver_Input") Driver<Input> inputDriver,
                                   @Qualifier("Driver_Output") Driver<Output> outputDriver,
                                   JwtDecoder jwtDecoder) {
        return new ChatHandler(inputDriver, outputDriver, jwtDecoder);
    }

    @Bean
    public HandlerMapping wsHandlerMapping(ChatHandler chatHandler) {
        Map<String, WebSocketHandler> mapping = Maps.newHashMap();
        mapping.put("/chat/**", chatHandler);
        return new SimpleUrlHandlerMapping(mapping, -1);
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
