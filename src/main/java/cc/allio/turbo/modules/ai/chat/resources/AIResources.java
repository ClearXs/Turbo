package cc.allio.turbo.modules.ai.chat.resources;

import cc.allio.turbo.modules.ai.exception.ResourceParseException;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.exception.Trys;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.list.SingleOrList;
import com.google.common.collect.Lists;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.function.ThrowingFunction;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * read definition of AI resources from files
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class AIResources implements Self<AIResources> {

    static final Yaml YAML_PARSER;

    static {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false);
        loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
        loaderOptions.setAllowRecursiveKeys(true);
        loaderOptions.setCodePointLimit(Integer.MAX_VALUE);
        DumperOptions dumperOptions = new DumperOptions();
        Representer representer = new Representer(dumperOptions);
        YAML_PARSER = new Yaml(new Constructor(loaderOptions), representer, dumperOptions, loaderOptions);
    }

    final AtomicBoolean loaded;
    final Resource<LiteralAgent> agentResource;
    final Resource<LiteralAction> actionResource;

    public AIResources() {
        this.loaded = new AtomicBoolean(false);
        this.agentResource = new AgentResource();
        this.actionResource = new ActionResource();
    }

    /**
     * @see #read(Consumer)
     */
    public void read() {
        read(null);
    }

    /**
     * asynchronous reading AI resource in classpath
     * if read success the parameter {@link #loaded} is true. even once again is not to do anything.
     * read resource will be store {@link #agentResource} and {@link #actionResource}
     * <p>
     * when completion will be invoked {@code callback} function.
     */
    public void read(Consumer<Void> callback) {
        CompletableFuture.runAsync(
                () -> {
                    readNow();
                    if (callback != null) {
                        callback.accept(null);
                    }
                },
                Executors.newVirtualThreadPerTaskExecutor());
    }

    /**
     * immediate reading AI resource in classpath
     */
    public void readNow() {
        if (!isLoaded()) {
            agentResource.readNow();
            actionResource.readNow();
            loaded.compareAndSet(false, true);
        }
    }

    /**
     * check wither load resources
     *
     * @return the ture is load
     */
    public boolean isLoaded() {
        return loaded.get();
    }

    /**
     * detect the {@code target} whether existing in resources. and return the first one.
     *
     * @param target the target class
     * @param name   the target name
     * @param <T>    the resource type
     * @return optional resource
     */
    public <T> Optional<T> detect(Class<T> target, String name) {
        if (target.isAssignableFrom(LiteralAgent.class)) {
            return (Optional<T>) detectOfAgent(name);
        } else if (target.isAssignableFrom(LiteralAction.class)) {
            return (Optional<T>) detectOfAction(name);
        }
        return Optional.empty();
    }

    /**
     * detect {@link LiteralAction}
     *
     * @param actionName the action name
     * @return the Optional {@link LiteralAction}
     */
    public Optional<LiteralAction> detectOfAction(String actionName) {
        return actionResource.getResources()
                .stream()
                .filter(action -> actionName.equals(action.getName()))
                .findFirst();
    }

    /**
     * detect {@link LiteralAgent}
     *
     * @param agentName the agent name
     * @return the Optional {@link LiteralAgent}
     */
    public Optional<LiteralAgent> detectOfAgent(String agentName) {
        return agentResource.getResources()
                .stream()
                .filter(agent -> agentName.equals(agent.getName()))
                .findFirst();
    }

    /**
     * get all agents
     */
    public Collection<LiteralAgent> getAllAgent() {
        return agentResource.getResources();
    }

    /**
     * get all actions
     */
    public Collection<LiteralAction> getAllAction() {
        return actionResource.getResources();
    }

    interface Resource<T> {

        /**
         * read resources
         */
        void readNow();

        /**
         * get read resources.
         */
        List<T> getResources();

        /**
         * read two tile directory
         *
         * @param rootFile the root file directory
         */
        default List<File> consecutiveReadTwoTileDirectory(File rootFile) {
            List<File> directories = Lists.newArrayList();
            if (checkFileDirectoryIsNotEmpty(rootFile)) {
                for (File directory : Objects.requireNonNull(rootFile.listFiles())) {
                    if (checkFileDirectoryIsNotEmpty(directory)) {
                        directories.add(directory);
                    }
                }
            } else {
                log.warn("the path {} non-existing any directories.", rootFile.getPath());
            }
            return directories;
        }

        default boolean checkFileDirectoryIsNotEmpty(File file) {
            return file.isDirectory() && file.listFiles() != null;
        }
    }


    /**
     * read following structure directory.
     * .
     * ├── agent.yaml
     * ├── tools.json
     * └── agentPrompt.st
     */
    static class AgentResource implements Resource<LiteralAgent> {
        private final List<LiteralAgent> agents;
        private final ClassPathResource resource;

        static final String AGENT_FILENAME = "agent.yaml";
        static final String PROMPT_FILENAME = "prompt.st";
        static final String TOOLS_FILENAME = "tools.json";

        public AgentResource() {
            this.agents = new ArrayList<>();
            this.resource = new ClassPathResource("ai/agent");
        }

        @Override
        public void readNow() {
            File agentDirectory = null;
            try {
                agentDirectory = resource.getFile();
            } catch (IOException e) {
                log.warn("resource directory ai/agent not found in classpath");
            }

            if (agentDirectory != null) {
                List<File> agentDirectories = consecutiveReadTwoTileDirectory(agentDirectory);

                for (File directory : agentDirectories) {
                    ResourceDirectoryReader reader = new ResourceDirectoryReader(directory);
                    LiteralAgent literalAgent =
                            Trys.onCapture(
                                    () -> reader.readTo(reader.getFile(AGENT_FILENAME), LiteralAgent.class),
                                    err -> {
                                        log.error("load agent has fatal errors.", err);
                                    });

                    if (literalAgent != null) {
                        String prompt =
                                Trys.onContinue(() -> reader.readToString(reader.getFile(PROMPT_FILENAME)));
                        literalAgent.setPrompt(prompt);

                        List<Map<String, Object>> tools =
                                Trys.onContinue(() -> reader.readToListMap(reader.getFile(TOOLS_FILENAME), String.class, Object.class));
                        literalAgent.setTools(tools);

                        agents.add(literalAgent);
                    }
                }
            }

        }

        @Override
        public List<LiteralAgent> getResources() {
            return agents;
        }
    }

    /**
     * read following structure directory.
     * .
     * ├── action.md
     * └── action.yaml
     */
    static class ActionResource implements Resource<LiteralAction> {

        private final List<LiteralAction> actions;
        private final ClassPathResource resource;

        static final String ACTION_FILENAME = "action.yaml";
        static final String CONTENT_FILENAME = "action.md";

        ActionResource() {
            this.actions = new ArrayList<>();
            this.resource = new ClassPathResource("ai/action");
        }

        @Override
        public void readNow() {
            File actionDirectory = null;
            try {
                actionDirectory = resource.getFile();
            } catch (IOException ex) {
                log.warn("resource directory ai/action not found in classpath");
            }

            if (actionDirectory != null) {
                List<File> actionDirectories = consecutiveReadTwoTileDirectory(actionDirectory);

                for (File directory : actionDirectories) {
                    ResourceDirectoryReader reader = new ResourceDirectoryReader(directory);
                    LiteralAction literalAction =
                            Trys.onCapture(
                                    () -> reader.readTo(reader.getFile(ACTION_FILENAME), LiteralAction.class),
                                    err -> {
                                        log.error("load action has fatal errors.", err);
                                    });

                    if (literalAction != null) {
                        String content = Trys.onContinue(() -> reader.readToString(reader.getFile(CONTENT_FILENAME)));
                        literalAction.setContent(content);
                        actions.add(literalAction);
                    }
                }
            }
        }

        @Override
        public List<LiteralAction> getResources() {
            return actions;
        }
    }

    /**
     * reader specific directory.
     */
    static class ResourceDirectoryReader implements FileReader {
        final File directory;
        final List<File> files;

        public ResourceDirectoryReader(File directory) {
            this.directory = directory;
            File[] files = directory.listFiles();
            if (directory.isDirectory() && files != null) {
                this.files = Lists.newArrayList(files);
            } else {
                this.files = Collections.emptyList();
            }
        }

        /**
         * base on {@link FileExtension} read file to string
         *
         * @param extension the file extension instance
         */
        public SingleOrList<String> readToString(FileExtension extension) {
            return read(extension, extension::readToString);
        }

        public <T> SingleOrList<List<T>> readToList(FileExtension extension, Class<T> elementType) {
            return read(extension, file -> extension.readToList(file, elementType));
        }

        public <K, V> SingleOrList<Map<K, V>> readToMap(FileExtension extension, Class<K> keyType, Class<V> valueType) {
            return read(extension, file -> extension.readToMap(file, keyType, valueType));
        }

        public <K, V> SingleOrList<List<Map<K, V>>> readToListMap(FileExtension extension, Class<K> keyType, Class<V> valueType) {
            return read(extension, file -> extension.readToListMap(file, keyType, valueType));
        }

        /**
         * base on {@link FileExtension} read file to generic type T instance.
         *
         * @param extension the file extension instance
         * @param clazz     the generic type T class
         * @param <T>       the generic type T
         */
        public <T> SingleOrList<T> readTo(FileExtension extension, Class<T> clazz) {
            return read(extension, file -> extension.readTo(file, clazz));
        }


        private <T> SingleOrList<T> read(FileExtension extension, ThrowingFunction<File, T> howRead) {
            return files.stream()
                    .filter(file -> extension.match(file.getName()))
                    .map(howRead)
                    .collect(Collectors.toCollection(SingleOrList::new));
        }

        @Override
        public String readToString(File file) throws IOException, ResourceParseException {
            FileReader reader = FileExtension.getReader(file.getName());
            if (reader == null) {
                throw new ResourceParseException("unsupported file suffix");
            }
            return reader.readToString(file);
        }

        @Override
        public <T> List<T> readToList(File file, Class<T> elementType) throws IOException, ResourceParseException {
            FileReader reader = FileExtension.getReader(file.getName());
            if (reader == null) {
                throw new ResourceParseException("unsupported file suffix");
            }
            return reader.readToList(file, elementType);
        }

        @Override
        public <K, V> Map<K, V> readToMap(File file, Class<K> keyType, Class<V> valueType) throws IOException, ResourceParseException {
            FileReader reader = FileExtension.getReader(file.getName());
            if (reader == null) {
                throw new ResourceParseException("unsupported file suffix");
            }
            return reader.readToMap(file, keyType, valueType);
        }

        @Override
        public <K, V> List<Map<K, V>> readToListMap(File file, Class<K> keyType, Class<V> valueType) throws IOException, ResourceParseException {
            FileReader reader = FileExtension.getReader(file.getName());
            if (reader == null) {
                throw new ResourceParseException("unsupported file suffix");
            }
            return reader.readToListMap(file, keyType, valueType);
        }

        @Override
        public <T> T readTo(File file, Class<T> clazz) throws IOException, ResourceParseException {
            FileReader reader = FileExtension.getReader(file.getName());
            if (reader == null) {
                throw new ResourceParseException("unsupported file suffix");
            }
            return reader.readTo(file, clazz);
        }

        /**
         * base on file get file instance
         *
         * @param filename the filename
         * @throws FileNotFoundException if file not existing.
         */
        public File getFile(String filename) throws FileNotFoundException {
            return files.stream()
                    .filter(f -> filename.equals(f.getName()))
                    .findFirst()
                    .orElseThrow(() -> new FileNotFoundException(String.format("%s file not found", filename)));
        }
    }

    @Getter
    @AllArgsConstructor
    @ToString(of = "name")
    enum FileExtension implements FileReader {

        YAML("yaml"),
        YML("yml"),
        STRING_TEMPLATE("st"),
        MARKDOWN("md"),
        JSON("json");

        private final String name;

        @Override
        public String readToString(File file) throws IOException, ResourceParseException {
            valid(file);
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        }

        @Override
        public <T> List<T> readToList(File file, Class<T> elementType) throws IOException, ResourceParseException {
            valid(file);
            if (this == JSON) {
                return JsonUtils.readList(readToString(file), elementType);
            }
            throw new UnsupportedOperationException(String.format("%s suffix unsupported operation", this.getName()));
        }

        @Override
        public <K, V> Map<K, V> readToMap(File file, Class<K> keyType, Class<V> valueType) throws IOException, ResourceParseException {
            valid(file);
            if (this == JSON) {
                return JsonUtils.readMap(readToString(file), keyType, valueType);
            }
            throw new UnsupportedOperationException(String.format("%s suffix unsupported operation", this.getName()));
        }

        @Override
        public <K, V> List<Map<K, V>> readToListMap(File file, Class<K> keyType, Class<V> valueType) throws IOException, ResourceParseException {
            valid(file);
            if (this == JSON) {
                return JsonUtils.reaListMap(readToString(file), keyType, valueType);
            }
            throw new UnsupportedOperationException(String.format("%s suffix unsupported operation", this.getName()));
        }

        @Override
        public <T> T readTo(File file, Class<T> clazz) throws IOException, ResourceParseException {
            valid(file);
            return switch (this) {
                case YAML, YML -> YAML_PARSER.loadAs(new java.io.FileReader(file), clazz);
                case JSON -> JsonUtils.parse(readToString(file), clazz);
                default ->
                        throw new UnsupportedOperationException(String.format("%s suffix unsupported operation", this.getName()));
            };
        }

        void valid(File file) throws ResourceParseException {
            if (file == null) {
                throw new ResourceParseException("file is null");
            }

            if (file.isDirectory()) {
                throw new ResourceParseException("file is directory");
            }

            String filename = file.getName();

            if (!match(filename)) {
                throw new ResourceParseException(String.format("file extension suffix " +
                        "isn't match '.yaml', '.yml', '.st', '.md', '.json', the file name is %s", filename));
            }
        }

        /**
         * specific filename is match this extension
         *
         * @param filename the filename
         */
        public boolean match(String filename) {
            String suffix = FileReader.getFileSuffix(filename);
            return suffix.equals(name);
        }

        /**
         * according filename suffix get extension file reader instance
         *
         * @param filename the filename
         */
        public static FileReader getReader(String filename) {
            for (FileExtension reader : values()) {
                if (reader.match(filename)) {
                    return reader;
                }
            }
            return null;
        }
    }

    interface FileReader {

        /**
         * read file to string
         *
         * @param file the file instance
         */
        String readToString(File file) throws IOException, ResourceParseException;

        /**
         * read to specific type
         *
         * @param file        the file instance
         * @param elementType the type of v
         * @param <T>         the type generic
         * @return return to instance or null
         */
        <T> List<T> readToList(File file, Class<T> elementType) throws IOException, ResourceParseException;

        /**
         * read to specific type
         *
         * @param file      the file instance
         * @param keyType   the type of key
         * @param valueType the type of value
         * @return return to instance or null
         */
        <K, V> Map<K, V> readToMap(File file, Class<K> keyType, Class<V> valueType) throws IOException, ResourceParseException;

        /**
         * read to specific type
         *
         * @param file      the file instance
         * @param keyType   the type of key
         * @param valueType the type of value
         * @return return to instance or null
         */
        <K, V> List<Map<K, V>> readToListMap(File file, Class<K> keyType, Class<V> valueType) throws IOException, ResourceParseException;

        /**
         * read to specific type
         *
         * @param file  the file instance
         * @param clazz the type of class
         * @param <T>   the type generic
         * @return return to instance or null
         */
        <T> T readTo(File file, Class<T> clazz) throws IOException, ResourceParseException;


        /**
         * get file suffix
         */
        static String getFileSuffix(String filename) {
            return filename.substring(filename.indexOf(StringPool.ORIGIN_DOT) + 1);
        }
    }

    @Data
    public static class LiteralAction {
        private String name;
        private String content;
    }

    @Data
    public static class LiteralAgent {
        private String name;
        private String description;
        private String prompt;
        @Nullable
        private List<String> actions;
        @Nullable
        private List<String> externalTools;
        @Nullable
        private List<Map<String, Object>> tools;
    }
}
