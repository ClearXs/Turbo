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
            boolean matched = matched(bean);
            if (!matched) {
                return bean;
            }
            List<TurboAdvisor> matchedAdvisors = getMatchedAdvisors(bean);
            return Aspects.create(bean, matchedAdvisors);
        }
        return bean;
    }

    /**
     * 验证bean是否匹配advisor
     *
     * @param bean bean
     * @return true if matched
     */
    boolean matched(Object bean) {
        for (TurboAdvisorBuilder<? extends TurboAdvisor> builder : builders) {
            if (builder.allow(bean)) {
                return true;
            }
        }
        return false;
    }

    List<TurboAdvisor> getMatchedAdvisors(Object bean) {
        var matchedAdvisorBuilder =
                builders.stream()
                        .filter(a -> a.allow(bean))
                        .toList();
        return (List<TurboAdvisor>) matchedAdvisorBuilder.stream().map(TurboAdvisorBuilder::build).toList();
    }
}
