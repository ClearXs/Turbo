package cc.allio.turbo.modules.office.documentserver.configurers;

import cc.allio.turbo.modules.office.documentserver.models.configurations.Embedded;

public interface EmbeddedConfigurer<W> extends Configurer<Embedded, W> {
    void configure(Embedded embedded, W wrapper);
}
