package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.common.aop.AdvisorPredicate;
import cc.allio.turbo.common.aop.TurboAdvisorBuilder;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudTreeRepositoryService;
import cc.allio.turbo.common.getter.ApplicationContextGetter;
import cc.allio.turbo.modules.developer.api.DomainObject;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.function.lambda.*;
import com.google.common.collect.Queues;

import java.util.Queue;

/**
 * <b>领域对象的业务行为。</b>
 * <ul>
 *     <li>通过可编码的方式，让领域对象能够进行业务行为操作。</li>
 *     <li><b>该接口允许被继承。被继承或者实现处需要加上{@link cc.allio.turbo.modules.developer.api.annotation.Domain}注解，用于扫描被发现加入到Spring容器中</b></li>
 *     <li><b>如果是继承接口的实现类，需要继承自{@link DeclareDomainCrudTreeRepositoryServiceImpl}，所有的构造参数由容器自动添加</b></li>
 *     <li><b>如果多继承，只会选取扫描到的第一个</b></li>
 * </ul>
 *
 * @author jiangwei
 * @date 2024/2/27 17:33
 * @see DeclareDomainCrudTreeRepositoryServiceImpl
 * @see DomainServiceRegistryImpl
 * @see DomainServiceScanner
 * @since 0.1.1
 */
public interface IDomainService<T extends DomainObject>
        extends ITurboCrudTreeRepositoryService<T>, AdvisorPredicate, ApplicationContextGetter, BoSchemaGetter {

    /**
     * @see #aspectOn(String, ThrowingMethodConsumer)
     */
    default <T> void aspectOn(ThrowingMethodConsumer<T> domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        String domainMethodName = domainMethod.getMethodName();
        aspectOn(domainMethodName, callback);
    }

    /**
     * @see #aspectOn(String, ThrowingMethodConsumer)
     */
    default <T> void aspectOn(ThrowingMethodSupplier<T> domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        String domainMethodName = domainMethod.getMethodName();
        aspectOn(domainMethodName, callback);
    }

    /**
     * @see #aspectOn(String, ThrowingMethodConsumer)
     */
    default <T, R> void aspectOn(ThrowingMethodFunction<T, R> domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        String domainMethodName = domainMethod.getMethodName();
        aspectOn(domainMethodName, callback);
    }

    /**
     * @see #aspectOn(String, ThrowingMethodConsumer)
     */
    default <T1, T2> void aspectOn(ThrowingMethodBiConsumer<T1, T2> domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        String domainMethodName = domainMethod.getMethodName();
        aspectOn(domainMethodName, callback);
    }

    /**
     * @see #aspectOn(String, ThrowingMethodConsumer)
     */
    default <T1, T2, R> void aspectOn(ThrowingMethodBiFunction<T1, T2, R> domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        String domainMethodName = domainMethod.getMethodName();
        aspectOn(domainMethodName, callback);
    }

    /**
     * @see #aspectOn(String, ThrowingMethodConsumer)
     */
    default <T1, T2, T3> void aspectOn(ThrowingMethodTerConsumer<T1, T2, T3> domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        String domainMethodName = domainMethod.getMethodName();
        aspectOn(domainMethodName, callback);
    }

    /**
     * @see #aspectOn(String, ThrowingMethodConsumer)
     */
    default <T1, T2, T3, R> void aspectOn(ThrowingMethodTerFunction<T1, T2, T3, R> domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        String domainMethodName = domainMethod.getMethodName();
        aspectOn(domainMethodName, callback);
    }

    /**
     * @see #aspectOn(String, ThrowingMethodConsumer)
     */
    default <T1, T2, T3, T4> void aspectOn(ThrowingMethodQueConsumer<T1, T2, T3, T4> domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        String domainMethodName = domainMethod.getMethodName();
        aspectOn(domainMethodName, callback);
    }

    /**
     * @see #aspectOn(String, ThrowingMethodConsumer)
     */
    default <T1, T2, T3, T4, R> void aspectOn(ThrowingMethodQueFunction<T1, T2, T3, T4, R> domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {
        String domainMethodName = domainMethod.getMethodName();
        aspectOn(domainMethodName, callback);
    }

    /**
     * <b>领域切面</b>。
     * <p>在领域对象的领域行为调用前执行。</p>
     * <p>据含有的特性</p>
     * <ul>
     *     <li>用完即毁</li>
     *     <li>切面行为在队列中等待执行</li>
     * </ul>
     *
     * @param domainMethod 领域行为
     * @param callback     当给定领域行为发生时的回调
     */
    default void aspectOn(String domainMethod, ThrowingMethodConsumer<OptionalContext> callback) {

    }

    /**
     * 获取领域切面的队列
     */
    default Queue<DomainAspect> getDomainAspects() {
        return Queues.newArrayDeque();
    }

    @Override
    default boolean test(TurboAdvisorBuilder<?> turboAdvisorBuilder) throws Throwable {
        // 如果该实例是bean，则不使用默认的Advisor
        return false;
    }
}
