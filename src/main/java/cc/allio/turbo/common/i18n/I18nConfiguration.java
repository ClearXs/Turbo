package cc.allio.turbo.common.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class I18nConfiguration {

    @Bean
    public ResourceBundleMessageSource i18nMessageSource() {
        ResourceBundleMessageSource exceptionMessageSource = new ResourceBundleMessageSource();
        exceptionMessageSource.setBasenames("i18n/exception", "i18n/message");
        return exceptionMessageSource;
    }

    @Bean
    public LocaleFormatter localeFormatter() {
        return new LocaleFormatter();
    }

}
