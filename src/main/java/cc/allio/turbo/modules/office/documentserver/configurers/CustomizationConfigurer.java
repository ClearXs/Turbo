package cc.allio.turbo.modules.office.documentserver.configurers;

import cc.allio.turbo.modules.office.documentserver.models.configurations.Customization;

public interface CustomizationConfigurer<W> extends Configurer<Customization, W> {
    void configure(Customization customization, W wrapper);
}
