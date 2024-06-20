package cc.allio.turbo.extension.oss.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.InputStream;

/**
 * 数据上传请求
 *
 * @author j.x
 * @date 2023/11/17 15:42
 * @since 0.1.0
 */
@Data
@Builder
public class OssPutRequest {

    /**
     * 对象标识。
     * <p>比如: test.txt</p>
     */
    private String object;

    /**
     * 数据输入流
     */
    private InputStream inputStream;
}
