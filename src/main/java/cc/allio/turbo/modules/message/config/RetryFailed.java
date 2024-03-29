package cc.allio.turbo.modules.message.config;

import lombok.Data;

/**
 * 消息重试
 *
 * @author j.x
 * @date 2024/3/28 23:03
 * @since 0.1.1
 */
@Data
public class RetryFailed {

    /**
     * 消息重试策略
     * <p>IGNORE 忽略 EXECUTE_IMMEDIATELY 立即执行 DELAYED_EXECUTION 延迟执行</p>
     */
    private RetryStrategy strategy;

    /**
     * Optional.重试次数
     */
    private Integer count;

    /**
     * Optional.延迟时间
     */
    private Long timeout;
}
