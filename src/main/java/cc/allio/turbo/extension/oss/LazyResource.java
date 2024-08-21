package cc.allio.turbo.extension.oss;

import java.util.function.Supplier;

@FunctionalInterface
public interface LazyResource extends Supplier<OssTrait> {

}
