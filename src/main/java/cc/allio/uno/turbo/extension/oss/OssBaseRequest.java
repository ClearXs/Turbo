package cc.allio.uno.turbo.extension.oss;

import lombok.Data;

@Data
public class OssBaseRequest {

    /**
     * 对象标识。
     * <p>比如: test.txt</p>
     */
    private String object;
}
