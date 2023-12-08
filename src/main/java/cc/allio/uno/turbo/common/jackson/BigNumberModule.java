package cc.allio.uno.turbo.common.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.math.BigDecimal;

/**
 * 大数处理，避免精度丢失.
 *
 * @author j.x
 * @date 2023/11/14 22:12
 * @since 0.1.0
 */
public class BigNumberModule extends SimpleModule {

    public BigNumberModule() {
        BigNumberSerializer bigNumberSerializer = new BigNumberSerializer(Number.class);
        addSerializer(long.class, bigNumberSerializer);
        addSerializer(Long.class, bigNumberSerializer);
        addSerializer(BigDecimal.class, bigNumberSerializer);
    }
}
