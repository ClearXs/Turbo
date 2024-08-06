package cc.allio.turbo.modules.office.documentserver.command;

import cc.allio.turbo.modules.office.configuration.properties.Docservice;
import cc.allio.turbo.modules.office.configuration.properties.DocumentProperties;
import cc.allio.turbo.modules.office.documentserver.managers.jwt.JwtManager;
import cc.allio.turbo.modules.office.documentserver.util.service.ServiceConverter;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * onlyoffice command manager.
 *
 * @author j.x
 * @date 2024/5/13 19:09
 * @since 0.0.1
 */
public class CommandManager {

    private final InternalCommandExecutor commandExecutor;
    private final Map<Method, Command<?>> commandMap;

    public CommandManager(JwtManager jwtManager, ServiceConverter serviceConverter, DocumentProperties documentProperties) {
        Docservice docservice = documentProperties.getDocservice();
        Docservice.Url url = docservice.getUrl();
        this.commandExecutor =
                new InternalCommandExecutor(
                        documentProperties.getOnlyofficeServerUrl(),
                        url.getCommand(),
                        docservice.getHeader(),
                        jwtManager,
                        serviceConverter);
        this.commandMap = Maps.newHashMap();
    }

    /**
     * get command service the 'meta' command
     *
     * @return the {@link MetaCommand} instance
     */
    public MetaCommand meta() {
        return (MetaCommand) commandMap.computeIfAbsent(Method.drop, k -> new MetaCommand(commandExecutor));
    }

    /**
     * get command service the 'forcesave' command
     *
     * @return the {@link ForceSaveCommand} instance
     */
    public ForceSaveCommand forceSave() {
        return (ForceSaveCommand) commandMap.computeIfAbsent(Method.forcesave, k -> new ForceSaveCommand(commandExecutor));
    }

    /**
     * get command service the 'info' command
     *
     * @return the {@link InfoCommand} instance
     */
    public InfoCommand info() {
        return (InfoCommand) commandMap.computeIfAbsent(Method.info, k -> new InfoCommand(commandExecutor));
    }

    /**
     * get command service the 'drop' command
     * @return the {@link DropCommand} instance
     */
    public DropCommand drop() {
        return (DropCommand) commandMap.computeIfAbsent(Method.drop, k -> new DropCommand(commandExecutor));
    }
}
