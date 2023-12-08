package cc.allio.uno.turbo.extension.oss;

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
@EqualsAndHashCode(callSuper = true)
public class OssPutRequest extends OssBaseRequest {

    /**
     * 数据输入流
     */
    private InputStream inputStream;
}
