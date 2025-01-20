package cc.allio.turbo.modules.ai.resources;

import cc.allio.turbo.modules.ai.exception.ResourceParseException;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * read definition of AI resources from files
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class AIResources implements Self<AIResources> {

    final Yaml yaml;
    final AtomicBoolean loaded;
    final Map<String, LiteralAction> resourceOfActions;
    final Map<String, LiteralAgent> resourceOfAgents;

    static AIResources resourcesReader;

    static final String YAML_SUFFIX = "yaml";
    static final String YML_SUFFIX = "yml";
    static final String ST_SUFFIX = "st";
    static final String MD_SUFFIX = "md";

    private AIResources() {
        this.yaml = new Yaml();
        this.loaded = new AtomicBoolean(false);
        this.resourceOfActions = Maps.newConcurrentMap();
        this.resourceOfAgents = Maps.newConcurrentMap();
    }

    /**
     * immediate reading AI resource in classpath
     * if read success the parameter {@link #loaded} is true. even once again is not to do anything.
     * read resource will be store {@link #resourceOfActions} and {@link #resourceOfAgents}
     */
    public AIResources readNow() throws ResourceParseException {
        if (isLoaded()) {
            return self();
        }
        readAgentFromClasspathResources();
        readActionFromClasspathResources();
        loaded.compareAndSet(false, true);
        return self();
    }

    /**
     * check wither load resources
     *
     * @return the ture is load
     */
    public boolean isLoaded() {
        return loaded.get();
    }

    public Optional<LiteralAction> getAction(String name) {
        return Optional.ofNullable(resourceOfActions.get(name));
    }

    public Optional<LiteralAgent> getAgent(String name) {
        return Optional.ofNullable(resourceOfAgents.get(name));
    }

    public Collection<LiteralAgent> getAllAgent() {
        return resourceOfAgents.values();
    }

    public Collection<LiteralAction> getAllAction() {
        return resourceOfActions.values();
    }

    /**
     * read following structure directory.
     * .
     * └── sma
     *     ├── agent.yaml
     *     └── prompt.st
     *
     * @throws ResourceParseException if parse error will be throws
     */
    public AIResources readAgentFromClasspathResources() throws ResourceParseException {
        ClassPathResource resource = new ClassPathResource("ai/agent");
        File agentDirector;
        try {
            agentDirector = resource.getFile();
        } catch (IOException e) {
            throw new ResourceParseException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("load agent root path {}", agentDirector.getPath());
        }
        consecutiveReadTwoTileDirectory(
                agentDirector,
                agentDirectory -> {
                    File[] files = agentDirectory.listFiles();
                    File yamlFile = null;
                    File stFile = null;
                    for (File file : files) {
                        if (file.getName().endsWith(YAML_SUFFIX)) {
                            yamlFile = file;
                        } else if (file.getName().endsWith(ST_SUFFIX)) {
                            stFile = file;
                        }
                    }
                    if (yamlFile != null && stFile != null) {
                        LiteralAgent literalAgent = new SimpleFileReader(yamlFile, yaml).readTo(LiteralAgent.class);
                        String prompt = new SimpleFileReader(stFile, yaml).readTo();
                        literalAgent.setPrompt(prompt);
                        resourceOfAgents.computeIfAbsent(literalAgent.name, key -> literalAgent);
                    }
                });
        return self();
    }

    /**
     * read following structure directory.
     * .
     * ├── create
     * │         ├── action.md
     * |         └── action.yaml
     * └── search
     *     ├── action.md
     *     └── action.yaml
     * @throws ResourceParseException if parse error will be throws
     */
    public AIResources readActionFromClasspathResources() throws ResourceParseException {
        ClassPathResource resource = new ClassPathResource("ai/action");

        File actionDirectory;
        try {
            actionDirectory = resource.getFile();
        } catch (IOException e) {
            throw new ResourceParseException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("load agent root path {}", actionDirectory.getPath());
        }

        consecutiveReadTwoTileDirectory(
                actionDirectory,
                agentDirectory -> {
                    File[] files = agentDirectory.listFiles();
                    File yamlFile = null;
                    File mdFile = null;
                    for (File file : files) {
                        if (file.getName().endsWith(YAML_SUFFIX)) {
                            yamlFile = file;
                        } else if (file.getName().endsWith(MD_SUFFIX)) {
                            mdFile = file;
                        }
                    }
                    if (yamlFile != null && mdFile != null) {
                        LiteralAction literalAction = new SimpleFileReader(yamlFile, yaml).readTo(LiteralAction.class);
                        String content = new SimpleFileReader(mdFile, yaml).readTo();
                        literalAction.setContent(content);
                        resourceOfActions.computeIfAbsent(literalAction.name, key -> literalAction);
                    }
                });
        return self();
    }

    void consecutiveReadTwoTileDirectory(File rootFile, ThrowingMethodConsumer<File> fileDirectoryProcessor) throws ResourceParseException {
        if (checkFileDirectoryIsNotEmpty(rootFile)) {
            for (File director : Objects.requireNonNull(rootFile.listFiles())) {
                if (checkFileDirectoryIsNotEmpty(director)) {
                    try {
                        fileDirectoryProcessor.accept(director);
                    } catch (Throwable ex) {
                        throw new ResourceParseException(ex);
                    }
                }
            }
        } else {
            log.warn("the path {} non-existing any directories.", rootFile.getPath());
        }
    }

    static class SimpleFileReader {

        File file;
        Yaml yaml;
        String suffix;

        public SimpleFileReader(File file, Yaml yaml) throws ResourceParseException {
            validate(file);
            this.file = file;
            this.yaml = yaml;
            this.suffix = readToSuffix(file);
        }

        void validate(File file) throws ResourceParseException {
            if (file == null) {
                throw new ResourceParseException("file is null");
            }

            if (file.isDirectory()) {
                throw new ResourceParseException("file is directory");
            }

            String name = file.getName();

            if (!name.endsWith(YAML_SUFFIX)
                    && !name.endsWith(ST_SUFFIX)
                    && !name.endsWith(MD_SUFFIX)
                    && !name.endsWith(YML_SUFFIX)) {
                throw new ResourceParseException(String.format("file extension suffix isn't yaml, st, md. file name is %s", name));
            }
        }

        /**
         * read to string
         */
        public String readTo() throws IOException {
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        }

        /**
         * read to specific type
         *
         * @param clazz the type of class
         * @return return to instance or null
         * @param <T> the type generic
         */
        public <T> T readTo(Class<T> clazz) throws FileNotFoundException {
            if (suffix.equals(YAML_SUFFIX)) {
                return yaml.loadAs(new FileReader(file), clazz);
            }
            return null;
        }

        private String readToSuffix(File file) {
            String name = file.getName();
            return name.substring(name.indexOf(StringPool.ORIGIN_DOT) + 1);
        }
    }

    boolean checkFileDirectoryIsNotEmpty(File file) {
        return file.isDirectory() && file.listFiles() != null;
    }

    public static AIResources getInstance() {
        if (resourcesReader == null) {
            resourcesReader = new AIResources();
        }
        return resourcesReader;
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
        private List<String> actions;
    }
}
