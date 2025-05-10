package cc.allio.turbo.common.aop;

import cc.allio.turbo.common.db.uno.repository.LockRepositoryAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Order(Integer.MIN_VALUE)
@Configuration
public class TurboAspectConfiguration {

    @Bean
    public TurboAdvisorBuilder<LockRepositoryAdvisor> lockRepositoryAdvisorTurboAdvisorBuilder() {
        return LockRepositoryAdvisor.LockRepositoryAdvisorBuilder.builder();
    }

    @Bean
    public TurboAspectProcessor turboAspectProcessor(List<TurboAdvisorBuilder<? extends TurboAdvisor>> builders) {
        return new TurboAspectProcessor(builders);
    }
}
