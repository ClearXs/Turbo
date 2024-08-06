package cc.allio.turbo.modules.office.documentserver.configuration;

import cc.allio.turbo.modules.office.documentserver.callbacks.CallbackHandler;
import cc.allio.turbo.modules.office.documentserver.callbacks.implementations.CorruptedCallback;
import cc.allio.turbo.modules.office.documentserver.callbacks.implementations.EditCallback;
import cc.allio.turbo.modules.office.documentserver.callbacks.implementations.ForcesaveCallback;
import cc.allio.turbo.modules.office.documentserver.callbacks.implementations.SaveCallback;
import cc.allio.turbo.modules.office.documentserver.command.CommandManager;
import cc.allio.turbo.modules.office.service.IDocChangesService;
import cc.allio.turbo.modules.office.service.IDocService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CallbackConfiguration {

    @Bean
    public CallbackHandler callbackHandler(IDocChangesService docChangesService) {
        return new CallbackHandler(docChangesService);
    }

    @Bean
    public EditCallback editCallback(CommandManager commandManager) {
        return new EditCallback(commandManager);
    }

    @Bean
    public SaveCallback saveCallback(IDocService docService) {
        return new SaveCallback(docService);
    }

    @Bean
    public ForcesaveCallback forcesaveCallback(IDocService docService) {
        return new ForcesaveCallback(docService);
    }

    @Bean
    public CorruptedCallback corruptedCallback(SaveCallback saveCallback) {
        return new CorruptedCallback(saveCallback);
    }
}