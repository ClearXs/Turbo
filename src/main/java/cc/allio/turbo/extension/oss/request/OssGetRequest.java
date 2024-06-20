package cc.allio.turbo.extension.oss.request;

import lombok.Builder;
import lombok.Data;

/**
 * 数据获取请求
 *
 * @author j.x
 * @date 2023/11/17 16:15
 * @since 0.1.0
 */
@Builder
@Data
public class OssGetRequest {

    /**
     * 对象标识。
     * <p>比如: test.txt</p>
     */
    private String object;
}
