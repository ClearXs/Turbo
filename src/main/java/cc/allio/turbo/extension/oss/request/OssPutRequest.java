package cc.allio.turbo.extension.oss.request;

import cc.allio.turbo.extension.oss.Path;
import lombok.Builder;
import lombok.Data;

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
    private Path path;

    /**
     * 数据输入流
     */
    private InputStream inputStream;
}
