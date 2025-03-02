package cc.allio.turbo.modules.ai.driver;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bus.EventBus;
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
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Set;

/**
 * dynamic automation register {@link Driver} bean for generic types
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class DriverBeanRegister implements BeanDefinitionRegistryPostProcessor {

    private final EventBus<DomainEventContext> eventBus;
    private final DriverModelScanner scanner;

    public DriverBeanRegister(EventBus<DomainEventContext> eventBus) {
        this.eventBus = eventBus;
        this.scanner = new DriverModelScanner();
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Set<String> candidates = scanner.scan();
        for (String candidate : candidates) {
            Class<?> driverModelClass = null;
            try {
                driverModelClass = ClassUtils.forName(candidate, ClassUtils.getDefaultClassLoader());
            } catch (ClassNotFoundException ex) {
                log.error("Cannot load driver model class: {}", candidate, ex);
            }
            if (driverModelClass == null) {
                continue;
            }
            String beanName = "Driver" + StringPool.UNDERSCORE + driverModelClass.getSimpleName();
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(DriverFactoryBean.class);
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            constructorArgumentValues.addIndexedArgumentValue(0, driverModelClass);
            constructorArgumentValues.addIndexedArgumentValue(1, eventBus);
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    /**
     * scan annotated {@link DriverModel} classes.
     *
     * @see ClassPathScanningCandidateComponentProvider
     */
    static class DriverModelScanner {

        private final ResourcePatternResolver resourcePatternResolver;
        private final MetadataReaderFactory metadataReaderFactory;

        static final String BACK_PACKAGE = DriverBeanRegister.class.getPackageName();
        static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

        public DriverModelScanner() {
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
                    if (filename != null && filename.contains(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
                        // Ignore CGLIB-generated classes in the classpath
                        continue;
                    }
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    if (isDriverModel(metadataReader)) {
                        ClassMetadata classMetadata = metadataReader.getClassMetadata();
                        candidate.add(classMetadata.getClassName());
                    }
                }
            } catch (IOException ex) {
                log.error("I/O failure during classpath [{}] scanning", BACK_PACKAGE, ex);
            }
            return candidate;
        }

        boolean isDriverModel(MetadataReader metadataReader) {
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            return annotationMetadata.hasAnnotation(DriverModel.class.getName());
        }
    }

}