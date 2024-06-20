package cc.allio.turbo.extension.oss.request;

import lombok.Builder;
import lombok.Data;

/**
 * object remove request
 *
 * @author j.x
 * @date 2024/6/20 18:48
 * @since 0.1.1
 */
@Builder
@Data
public class OssRemoveRequest {

    /**
     * 对象标识。
     * <p>比如: test.txt</p>
     */
    private String object;
}
