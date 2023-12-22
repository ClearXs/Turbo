package cc.allio.turbo.modules.auth.constant;

import cc.allio.uno.core.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Supplier;

/**
 * 过期时间
 *
 * @author j.x
 * @date 2023/10/27 14:19
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor
public enum ExpireAt {

    /**
     * 60s
     */
    S_60(() -> DateUtil.plusSeconds(DateUtil.now(), 60).getTime()),

    /**
     * 5 minutes
     */
    M_5(() -> DateUtil.plusMinutes(DateUtil.now(), 5).getTime()),

    /**
     * 10 minutes
     */
    M_10(() -> DateUtil.plusMinutes(DateUtil.now(), 10).getTime()),

    /**
     * 1个小时过期时间
     */
    H_1(() -> DateUtil.plusHours(DateUtil.now(), 1).getTime()),

    /**
     * 6个小时过期时间
     */
    H_6(() -> DateUtil.plusHours(DateUtil.now(), 6).getTime()),

    /**
     * 1天过期时间
     */
    D_1(() -> DateUtil.plusDays(DateUtil.now(), 1).getTime()),

    /**
     * 永远不过期
     */
    NEVER(() -> DateUtil.plusYears(DateUtil.now(), 100).getTime());

    private final Supplier<Long> time;

    /**
     * 求解与当前时间的差值
     *
     * @return 正数
     */
    public Long range() {
        Date now = DateUtil.now();
        return new BigDecimal(time.get() - now.getTime()).abs().longValue();
    }
}
