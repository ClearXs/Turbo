package cc.allio.turbo.common.aop;

import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.List;

/**
 * 基于{@link TurboAdvisor}的动态创建aop对象
 *
 * @author jiangwei
 * @date 2024/2/6 23:17
 * @since 0.1.0
 */
public class TurboAspectProcessor implements BeanPostProcessor {

    private final List<TurboAdvisorBuilder<? extends TurboAdvisor>> builders = Lists.newArrayList();

    public TurboAspectProcessor(List<TurboAdvisorBuilder<? extends TurboAdvisor>> builders) {
        this.builders.addAll(builders);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (CollectionUtils.isNotEmpty(builders)) {
            boolean matched = builders.stream().anyMatch(a -> a.allow(bean.getClass()));
            if (!matched) {
                return bean;
            }
            // 匹配当前bean类型的advisor
            List<TurboAdvisorBuilder<? extends TurboAdvisor>> matchedAdvisorBuilder = builders.stream().filter(a -> a.allow(bean.getClass())).toList();
            List<TurboAdvisor> matchedAdvisors = (List<TurboAdvisor>) matchedAdvisorBuilder.stream().map(TurboAdvisorBuilder::build).toList();
            return Aspects.create(bean, matchedAdvisors);
        }
        return bean;
    }

}
