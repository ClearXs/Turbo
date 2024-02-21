package cc.allio.turbo.common.i18n;

import lombok.Getter;

/**
 * 描述i18n的code接口
 * <b>描述信息可以使用%s进行占位处理</b>
 *
 * @author jiangwei
 * @date 2024/1/19 17:13
 * @since 0.1.0
 */
public interface I18nCode {

    /**
     * 获取i18n中定义的key，用于获取对应的国际化信息
     *
     * @return i18n key
     */
    String getKey();

    /**
     * 获取描述
     *
     * @return 描述
     */
    String getDescription();

    /**
     * 获取分组信息
     */
    String getGroup();

    /**
     * 获取子分组
     */
    String getSubGroup();

    /**
     * 获取描述信息占位符替换的参数集
     */
    default Object[] getParams() {
        return new Object[0];
    }

    static I18nCode of(String key) {
        return new I18nCodeImpl(key);
    }

    static I18nCode of(String key, String description) {
        return new I18nCodeImpl(key, description);
    }

    static I18nCode of(String key, String description, String group) {
        return new I18nCodeImpl(key, description, group);
    }

    static I18nCode of(String key, String description, String group, String subGroup) {
        return new I18nCodeImpl(key, description, group, subGroup);
    }

    static I18nCode of(I18nCode i18nCode, Object[] params) {
        return new I18nCodePramsImpl(i18nCode, params);
    }


    @Getter
    class I18nCodeImpl implements I18nCode {

        private String key;
        private String description;
        private String group;
        private String subGroup;

        public I18nCodeImpl(String key) {
            this.key = key;
        }

        public I18nCodeImpl(String key, String description) {
            this.key = key;
            this.description = description;
        }

        public I18nCodeImpl(String key, String description, String group) {
            this.key = key;
            this.description = description;
            this.group = group;
        }

        public I18nCodeImpl(String key, String description, String group, String subGroup) {
            this.key = key;
            this.description = description;
            this.group = group;
            this.subGroup = subGroup;
        }
    }

    @Getter
    class I18nCodePramsImpl extends I18nCodeImpl {

        private Object[] params;

        public I18nCodePramsImpl(String key) {
            super(key);
        }

        public I18nCodePramsImpl(String key, String description) {
            super(key, description);
        }

        public I18nCodePramsImpl(String key, String description, String group) {
            super(key, description, group);
        }

        public I18nCodePramsImpl(String key, String description, String group, String subGroup) {
            super(key, description, group, subGroup);
        }

        public I18nCodePramsImpl(I18nCode i18nCode, Object[] params) {
            super(i18nCode.getKey(), i18nCode.getDescription(), i18nCode.getGroup(), i18nCode.getSubGroup());
            this.params = params;
        }
    }
}
