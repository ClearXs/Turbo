package cc.allio.uno.turbo.extension.oss;

import lombok.Data;

import java.io.InputStream;

/**
 * response
 *
 * @author j.x
 * @date 2023/11/17 15:44
 * @since 1.0.0
 */
@Data
public class OssResponse {

    /**
     * 对象标识
     */
    private String object;

    /**
     * 是否成功
     */
    private boolean successful;

    /**
     * 获取的对象输入流
     */
    private InputStream inputStream;
}
