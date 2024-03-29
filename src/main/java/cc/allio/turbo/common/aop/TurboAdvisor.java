package cc.allio.turbo.common.aop;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

/**
 * turbo aspect advisor
 * <p><b>每一个bean都需要有自己的advisor</b></p>
 *
 * @author j.x
 * @date 2024/2/6 23:16
 * @since 0.1.0
 */
public abstract class TurboAdvisor extends DefaultPointcutAdvisor {

    protected static final String DEFAULT_MAPPING_NAME = "*";

    protected TurboAdvisor() {
        this("*");
    }

    protected TurboAdvisor(String... mappedNames) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames(mappedNames);
        setPointcut(pointcut);
    }

    /**
     * 在创建aop proxy之前并且在把当前advisor实例加入到{@link AspectJProxyFactory#addAdvisor(Advisor)}之前
     *
     * @param bean bean
     */
    public void preProxyProcess(Object bean) {
    }

    /**
     * 当创建aop proxy成功后进行回调处理
     *
     * @param proxy aop 对象
     * @param bean  bean 对象
     */
    public void postProxyProcess(Object proxy, Object bean) {
    }
}
