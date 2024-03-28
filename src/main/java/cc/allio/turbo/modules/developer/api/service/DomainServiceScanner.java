package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.modules.developer.api.DomainObject;
import cc.allio.turbo.modules.developer.api.annotation.Domain;
import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.core.util.ClassUtils;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.data.repository.config.RepositoryConfigurationSourceSupport;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.*;

/**
 * 扫描cc.allio.turbo路径下所有class对象。
 * <p>并且把继承自{@link IDomainService}的接口并且该接口上具有{@link Domain}的注解加入Spring Bean Factory中。</p>
 *
 * @author j.x
 * @date 2024/2/28 23:48
 * @see Domain
 * @since 0.1.1
 */
@Slf4j
public class DomainServiceScanner extends ClassPathBeanDefinitionScanner {

    private static final String BACK_PACKAGE = "cc.allio.turbo";

    private static final BeanNameGenerator BEAN_NAME_GENERATOR = AnnotationBeanNameGenerator.INSTANCE;
    private final ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
    private final ClassLoader classLoader;

    static final TypeFilter SYNTHETICAL = new CombinationTypeFilter();

    public DomainServiceScanner(BeanDefinitionRegistry registry) {
        this(registry, ClassUtils.getDefaultClassLoader());
    }

    public DomainServiceScanner(BeanDefinitionRegistry registry, ClassLoader classLoader) {
        super(registry, false);
        addIncludeFilter(SYNTHETICAL);
        this.classLoader = classLoader;
    }

    /**
     * 基于{@link #BACK_PACKAGE}进行扫描
     * <p>
     * {@link #scan(String...)}
     */
    public Set<DomainBeanDefinition> scanCandidate() {
        return scanCandidate(BACK_PACKAGE);
    }

    /**
     * 给定扫描的包名，并返回扫描到的BeanDefinition，不会把Bean注册到Bean Registry。
     *
     * @param basePackages 包扫描数组
     * @return BeanDefinitionHolder for set
     * @see #doScan(String...)
     */
    public Set<DomainBeanDefinition> scanCandidate(String... basePackages) {
        return doScanCandidate(basePackages);
    }

    /**
     * 区分于{@link #doScan(String...)}。
     * <p>扫描指定的包后经过过滤后返回</p>
     *
     * @param basePackages basePackages
     * @return {@link DomainBeanDefinition} of sets
     * @see #filtering(Set)
     */
    protected Set<DomainBeanDefinition> doScanCandidate(String... basePackages) {
        Assert.notEmpty(basePackages, "At least one base package must be specified");
        Set<DomainBeanDefinition> beanDefinitions = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
                candidate.setScope(scopeMetadata.getScopeName());
                String beanName = BEAN_NAME_GENERATOR.generateBeanName(candidate, getRegistry());
                if (candidate instanceof AbstractBeanDefinition abstractBeanDefinition) {
                    postProcessBeanDefinition(abstractBeanDefinition, beanName);
                }
                if (candidate instanceof AnnotatedBeanDefinition annotatedBeanDefinition) {
                    AnnotationConfigUtils.processCommonDefinitionAnnotations(annotatedBeanDefinition);
                }
                DomainBeanDefinition domainBeanDefinition = new DomainBeanDefinition(candidate);
                domainBeanDefinition.setDomainName(beanName);
                String domainClassName = candidate.getBeanClassName();
                try {
                    var domainServiceClass = (Class<? extends IDomainService<? extends DomainObject>>) classLoader.loadClass(domainClassName);
                    domainBeanDefinition.setDomainServiceClass(domainServiceClass);
                    var domainObjectClass = ReflectTools.getGenericTypeBySpecificType(domainServiceClass, IDomainService.class, DomainObject.class);
                    // 剔除不存在领域对象范型的类
                    if (domainObjectClass == null) {
                        continue;
                    }
                    domainBeanDefinition.setDomainObjectClass(domainObjectClass);
                } catch (Throwable ex) {
                    log.error("Load domain class {} but has error, now abandon it", domainClassName, ex);
                }
                beanDefinitions.add(domainBeanDefinition);
            }
        }

        // 去除具备相同类型的多个实现
        return filtering(beanDefinitions);
    }

    /**
     * 去除具备相同类型的多个实现
     *
     * <ul>
     *     <li>比如说ITestDomainService、TestDomainServiceImpl1、TestDomainServiceImpl1</li>
     *     <li>去处abstract class</li>
     *     <li>具备多个以找到的第一个实现类为准</li>
     * </ul>
     *
     * @param original 原始未滤除的
     * @return 滤除的
     */
    Set<DomainBeanDefinition> filtering(Set<DomainBeanDefinition> original) {
        Queue<DomainBeanDefinition> filterDefinition = Queues.newArrayDeque();
        // 记录查找到的具有同一接口类型，其中Key为原始的接口
        MultiValueMap<Class<?>, DomainBeanDefinition> sameDefinition = new LinkedMultiValueMap<>();
        for (DomainBeanDefinition domainBeanDefinition : original) {
            if (domainBeanDefinition.isAbstract()) {
                continue;
            }
            var domainServiceClass = domainBeanDefinition.getDomainServiceClass();
            if (domainServiceClass.isInterface()) {
                sameDefinition.add(domainServiceClass, domainBeanDefinition);
            } else {
                Class<? extends DomainObject> domainObjectClass = domainBeanDefinition.getDomainObjectClass();
                Class<? extends IDomainService<?>> primary = findPrimary(domainServiceClass, domainObjectClass);
                if (primary != null) {
                    sameDefinition.add(primary, domainBeanDefinition);
                }
            }
        }
        // 遍历sameDefinition基于相似性判断并选择DomainBeanDefinition入队
        for (Map.Entry<Class<?>, List<DomainBeanDefinition>> domainLists : sameDefinition.entrySet()) {
            List<DomainBeanDefinition> alike = domainLists.getValue();
            alike.stream()
                    .reduce((d1, d2) -> {
                        Similarity s1 = getSimilarity(d1);
                        Similarity s2 = getSimilarity(d2);
                        int compared = s1.compare(s2);
                        return compared > 0 ? d1 : d2;
                    })
                    .stream()
                    .findFirst()
                    .ifPresent(filterDefinition::add);
        }
        return Sets.newCopyOnWriteArraySet(filterDefinition);
    }

    Similarity getSimilarity(DomainBeanDefinition domainBeanDefinition) {
        Class<? extends IDomainService<? extends DomainObject>> domainServiceClass = domainBeanDefinition.getDomainServiceClass();
        if (domainServiceClass.isInterface()) {
            return Similarity.ITL_SIMILARITY;
        }
        return Similarity.IMPL_SIMILARITY;
    }

    /**
     * 查找给定某个Class对象是否存在着只继承IDomainService的接口
     *
     * @param maybeDomainService maybeDomainService
     * @param domainObjectClass  domainObjectClass
     * @return find primary IDomainService or null
     */
    Class<? extends IDomainService<?>> findPrimary(Class<?> maybeDomainService, Class<? extends DomainObject> domainObjectClass) {
        if (maybeDomainService.isInterface()) {
            // IDomainService为null并且没有包含范型接口
            if (IDomainService.class.isAssignableFrom(maybeDomainService) &&
                    ReflectTools.containsGenericType(maybeDomainService, IDomainService.class, domainObjectClass)) {
                return (Class<? extends IDomainService<?>>) maybeDomainService;
            }
        } else {
            // 查找实现的接口和
            Class<?>[] interfaces = maybeDomainService.getInterfaces();
            for (Class<?> itf : interfaces) {
                Class<? extends IDomainService<?>> primary = findPrimary(itf, domainObjectClass);
                if (primary != null) {
                    return primary;
                }
            }
            // 查找父类
            Class<?> superclass = maybeDomainService.getSuperclass();
            if (superclass != null && !Object.class.isAssignableFrom(superclass)) {
                return findPrimary(superclass, domainObjectClass);
            }
        }
        return null;
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        // 忽略父类只判断是否是class，因为在之前已经判断当前candidate是domain service interface
        return true;
    }

    /**
     * 参考自spring data repository scanner filter
     *
     * @see RepositoryConfigurationSourceSupport#getCandidates(ResourceLoader)
     */
    public static class DomainServiceTypeFilter extends AssignableTypeFilter {

        /**
         * Creates a new {@link DomainServiceTypeFilter}.
         */
        public DomainServiceTypeFilter() {
            super(IDomainService.class);
        }
    }

    /**
     * 如果扫描的类是{@link IDomainService}的实现类，需要判断它是否继承了{@link DeclareDomainCrudTreeRepositoryServiceImpl}
     */
    public static class ImplDeclareDomainTypeFilter extends AssignableTypeFilter {

        public ImplDeclareDomainTypeFilter() {
            super(DeclareDomainCrudTreeRepositoryServiceImpl.class);
        }

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            ClassMetadata metadata = metadataReader.getClassMetadata();
            // 如果是非接口或者非抽象类则判断该类型是否是DeclareDomainCrudTreeRepositoryServiceImpl的子类型
            if (metadata.isConcrete()) {
                String superClassName = metadata.getSuperClassName();
                return Boolean.TRUE.equals(matchSuperClass(superClassName));
            }
            // 如果是接口则说明是IDomainService的子类型
            return metadata.isInterface();
        }
    }

    public static class CombinationTypeFilter implements TypeFilter {

        private final List<TypeFilter> internalFilters;

        public CombinationTypeFilter() {
            this.internalFilters = List.of(
                    new AnnotationTypeFilter(Domain.class, true, true),
                    new DomainServiceTypeFilter(),
                    new ImplDeclareDomainTypeFilter()
            );
        }

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            return internalFilters.stream()
                    .allMatch(filter -> {
                        try {
                            return filter.match(metadataReader, metadataReaderFactory);
                        } catch (IOException e) {
                            return false;
                        }
                    });
        }
    }

    enum Similarity {
        // 接口相似性
        ITL_SIMILARITY,
        // 实现类的
        IMPL_SIMILARITY;

        /**
         * 比较相似性大小。实现类优先级更高
         *
         * @param similarity similarity
         * @return 1 highly 0 equivalent -1 lowly
         */
        int compare(Similarity similarity) {
            if (this == similarity) {
                return 0;
            } else if (this == ITL_SIMILARITY && similarity == IMPL_SIMILARITY) {
                return -1;
            } else if (this == IMPL_SIMILARITY && similarity == ITL_SIMILARITY) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

