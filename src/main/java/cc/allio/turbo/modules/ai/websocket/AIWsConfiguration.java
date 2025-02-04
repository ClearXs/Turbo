package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.modules.ai.AIConfiguration;
import cc.allio.turbo.modules.ai.Driver;
import cc.allio.turbo.modules.ai.Input;
import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.auth.jwt.JwtConfiguration;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.UndertowRequestUpgradeStrategy;

import java.util.Map;

@AutoConfiguration
@AutoConfigureAfter({AIConfiguration.class, JwtConfiguration.class})
public class AIWsConfiguration {

    @Bean
    public ChatHandler chatHandler(@Qualifier("Driver_Input") Driver<Input> inputDriver,
                                   @Qualifier("Driver_Output") Driver<Output> outputDriver) {
        return new ChatHandler(inputDriver, outputDriver);
    }

    @Bean
    public HandlerMapping wsHandlerMapping(ChatHandler chatHandler) {
        Map<String, WebSocketHandler> mapping = Maps.newHashMap();
        mapping.put("/chat/**", chatHandler);
        return new SimpleUrlHandlerMapping(mapping, -1);
    }

    @Bean
    public WebSocketHandlerAdapter wsHandlerAdapter() {
        return new WebSocketHandlerAdapter(webSocketService());
    }

    @Bean
    public WebSocketService webSocketService() {
        UndertowRequestUpgradeStrategy strategy = new UndertowRequestUpgradeStrategy();
        return new HandshakeWebSocketService(strategy);
    }
}
