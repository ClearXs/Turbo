package cc.allio.turbo.modules.ai.chat.evaluation;

import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.driver.Topics;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import reactor.core.Disposable;

/**
 * evaluation controller
 *
 * @author j.x
 * @since 0.2.0
 */
public class EvaluationController implements InitializingBean, DisposableBean {

    private final Driver<Output> driver;

    private Disposable disposable;

    public EvaluationController(Driver<Output> driver) {
        this.driver = driver;
    }

    @Override
    public void destroy() throws Exception {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.disposable = driver.subscribeOn(Topics.EVALUATION)
                .observeMany()
                .subscribe(output -> {
                    // do something
                });
    }

}
