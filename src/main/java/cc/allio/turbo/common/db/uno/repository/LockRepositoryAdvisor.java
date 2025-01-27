package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.aop.TurboAdvisor;
import cc.allio.turbo.common.aop.TurboAdvisorBuilder;
import cc.allio.uno.core.util.concurrent.LockContext;

import java.util.Objects;

/**
 * 基于{@link LockContext}实现的切面。
 * <p>其作用是：在Repository层，对每个方法进行加锁处理。并把{@link LockContext}放入{@link ThreadLocal}中</p>
 *
 * @author j.x
 * @date 2024/2/7 14:05
 * @since 0.1.0
 */
public class LockRepositoryAdvisor extends TurboAdvisor {

    public LockRepositoryAdvisor() {
        this(new LockRepositoryMethodInterceptor(), DEFAULT_MAPPING_NAME);
    }

    public LockRepositoryAdvisor(String... mappingPatternNames) {
        this(new LockRepositoryMethodInterceptor(), mappingPatternNames);
    }

    public LockRepositoryAdvisor(LockRepositoryMethodInterceptor lockRepositoryMethodInterceptor) {
        this(lockRepositoryMethodInterceptor, DEFAULT_MAPPING_NAME);
    }

    public LockRepositoryAdvisor(LockRepositoryMethodInterceptor methodInterceptor, String... mappingPatternNames) {
        super(mappingPatternNames);
        setAdvice(Objects.requireNonNullElseGet(methodInterceptor, LockRepositoryMethodInterceptor::new));
    }

    public static class LockRepositoryAdvisorBuilder extends TurboAdvisorBuilder<LockRepositoryAdvisor> {

        protected LockRepositoryAdvisorBuilder() {
            super(LockRepositoryAdvisor.class, target ->
                    ITurboCrudRepository.class.isAssignableFrom(target.getClass())
                            || ITurboCrudRepositoryService.class.isAssignableFrom(target.getClass()));
        }

        /**
         * 获取构造器实例
         *
         * @return TurboAdvisorBuilder
         */
        public static TurboAdvisorBuilder<LockRepositoryAdvisor> builder() {
            return new LockRepositoryAdvisorBuilder();
        }

        /**
         * 获取构造器实例
         *
         * @return TurboAdvisorBuilder
         */
        public static TurboAdvisorBuilder<LockRepositoryAdvisor> builder(LockRepositoryMethodInterceptor lockRepositoryMethodInterceptor) {
            TurboAdvisorBuilder<LockRepositoryAdvisor> builder = builder();
            return builder.constructParameters(new Object[]{lockRepositoryMethodInterceptor});
        }
    }
}
