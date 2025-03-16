package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.modules.ai.agent.Supervisor;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.DriverConfiguration;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.auth.jwt.JwtCodecConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.UndertowRequestUpgradeStrategy;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;

@AutoConfiguration
@ImportAutoConfiguration({DriverConfiguration.class, JwtCodecConfiguration.class})
@AutoConfigureAfter(DriverConfiguration.class)
@EnableWebSocket
public class WsConfiguration {

    @Bean
    public ChatHandler chatHandler(
            Supervisor supervisor,
            @Qualifier("Driver_Input") Driver<Input> inputDriver,
            @Qualifier("Driver_Output") Driver<Output> outputDriver) {
        return new ChatHandler(supervisor, inputDriver, outputDriver);
    }

    @Bean
    public WebSocketConfigurer webSocketConfigurer(ChatHandler chatHandler) {
        return registry -> {
            WebSocketHandlerRegistration registration = registry.addHandler(chatHandler, "/ai/ws/chat/**");
            // allow all origins
            registration.setAllowedOrigins("*");
        };
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
