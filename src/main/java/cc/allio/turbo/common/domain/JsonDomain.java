package cc.allio.turbo.common.domain;

import cc.allio.uno.core.util.JsonUtils;

/**
 * 具有Json能力的domain对象
 *
 * @author jiangwei
 * @date 2024/1/18 17:25
 * @since 0.1.0
 */
public abstract class JsonDomain {

    /**
     * 返回json数据
     */
    public String toJson() {
        return JsonUtils.toJson(this);
    }

    /**
     * 根据json串获取实例
     *
     * @param text  text
     * @param clazz clazz对象
     * @param <T>   实际类型
     * @return 实例
     */
    public static <T> T from(String text, Class<T> clazz) {
        return JsonUtils.parse(text, clazz);
    }
}
