package cc.allio.turbo.common.getter;

import org.springframework.context.ApplicationContext;

/**
 * marked get {@link ApplicationContext} Getter interface
 * <p>generally through implementation by aop method interceptor</p>
 *
 * @author j.x
 * @date 2024/3/6 20:01
 * @since 0.1.1
 */
public interface ApplicationContextGetter extends Getter {

    /**
     * get {@link ApplicationContext}
     *
     * @return {@link ApplicationContext}
     */
    default ApplicationContext getApplicationContext() {
        return null;
    }
}
