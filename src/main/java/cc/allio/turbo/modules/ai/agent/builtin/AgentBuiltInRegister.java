package cc.allio.turbo.modules.ai.agent.builtin;

import cc.allio.turbo.modules.ai.annotation.DriverModel;
import cc.allio.turbo.modules.ai.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.runtime.tool.ToolRegistry;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bus.Pathway;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Set;

/**
 * scan built-in agent and register them.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class AgentBuiltInRegister implements BeanDefinitionRegistryPostProcessor {

    private final ToolRegistry toolRegistry;
    private final ActionRegistry actionRegistry;

    public AgentBuiltInRegister(ToolRegistry toolRegistry, ActionRegistry actionRegistry) {
        this.toolRegistry = toolRegistry;
        this.actionRegistry = actionRegistry;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BuiltInAgentScanner scanner = new BuiltInAgentScanner();
        Set<String> candidates = scanner.scan();
        for (String candidate : candidates) {
            Class<?> agentClass = null;
            try {
                agentClass = ClassUtils.forName(candidate, ClassUtils.getDefaultClassLoader());
            } catch (ClassNotFoundException ex) {
                log.error("Cannot load driver model class: {}", candidate, ex);
            }
            if (agentClass == null) {
                continue;
            }
            String beanName = "Agent" + StringPool.UNDERSCORE + agentClass.getSimpleName();

            if (registry.containsBeanDefinition(beanName)) {
                log.warn("Agent bean [{}] already exists, skip registration", beanName);
                continue;
            }

            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(agentClass);
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            constructorArgumentValues.addIndexedArgumentValue(0, toolRegistry);
            constructorArgumentValues.addIndexedArgumentValue(1, actionRegistry);
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    /**
     * scan annotated {@link DriverModel} classes.
     *
     * @see ClassPathScanningCandidateComponentProvider
     */
    static class BuiltInAgentScanner {

        private final ResourcePatternResolver resourcePatternResolver;
        private final MetadataReaderFactory metadataReaderFactory;

        static final String BACK_PACKAGE = BuiltInAgentScanner.class.getPackageName();
        static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
        static final String AGENT_BUILT_IN_REGISTER_NAME = AgentBuiltInRegister.class.getSimpleName();
        static final String BUILT_IN_AGENT_SCANNER = BuiltInAgentScanner.class.getSimpleName();

        public BuiltInAgentScanner() {
            this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
            this.metadataReaderFactory = new CachingMetadataReaderFactory();

        }

        /**
         * scan specific {@link #BACK_PACKAGE} pathway. and return eligible class names.
         *
         * @return the class names for {@link Set}
         */
        Set<String> scan() {
            Set<String> candidate = Sets.newHashSet();

            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + Pathway.DOT.transformTo(true, false, BACK_PACKAGE, Pathway.SLASH)
                    + DEFAULT_RESOURCE_PATTERN;

            try {
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    String filename = resource.getFilename();
                    if (filename != null
                            && (filename.contains(ClassUtils.CGLIB_CLASS_SEPARATOR))
                            || filename.contains(AGENT_BUILT_IN_REGISTER_NAME)
                            || filename.contains(BUILT_IN_AGENT_SCANNER)) {
                        // Ignore CGLIB-generated classes in the classpath
                        continue;
                    }
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();


                    candidate.add(classMetadata.getClassName());
                }
            } catch (IOException ex) {
                log.error("I/O failure during classpath [{}] scanning", BACK_PACKAGE, ex);
            }
            return candidate;
        }

    }
}
