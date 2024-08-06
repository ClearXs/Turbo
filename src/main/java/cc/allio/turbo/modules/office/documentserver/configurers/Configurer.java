package cc.allio.turbo.modules.office.documentserver.configurers;

public interface Configurer<O, W> {
    void configure(O instance, W wrapper);
}
