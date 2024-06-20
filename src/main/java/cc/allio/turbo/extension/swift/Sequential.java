package cc.allio.turbo.extension.swift;

import cc.allio.uno.core.util.StringUtils;
import lombok.Data;

/**
 * wrap for {@link Swift}, useful utile of generate auto number
 *
 * @author j.x
 * @date 2024/6/20 15:00
 * @since 0.1.1
 */
@Data
public class Sequential {

    private Swift swift;
    private SwiftConfig config;
    private String key;
    private Integer initialValue;
    private Integer length;
    private String prefix;
    private String suffix;
    private Integer step;
    private SwiftGenType genType;

    public Sequential(Swift swift,
                      String key,
                      Integer initialValue,
                      Integer length,
                      String prefix,
                      String suffix,
                      Integer step,
                      SwiftGenType genType) {
        this.swift = swift;
        this.key = key;
        this.initialValue = initialValue;
        this.length = length;
        this.prefix = prefix;
        this.suffix = suffix;
        this.step = step;
        this.genType = genType;

        StringBuilder tpl = new StringBuilder();
        if (StringUtils.isNotEmpty(prefix)) {
            tpl.append(prefix);
        }
        switch (genType) {
            case DAY:
                tpl.append("{yyyy}{MM}{dd}{NO}");
                break;
            case MONTH:
                tpl.append("{yyyy}{MM}{NO}");
                break;
            case YEAR:
                tpl.append("{yyyy}{NO}");
                break;
            case ALWAYS:
            default:
                tpl.append("{NO}");
        }
        if (StringUtils.isNotEmpty(suffix)) {
            tpl.append(suffix);
        }

        SwiftConfig swiftConfig = new SwiftConfig();
        swiftConfig.setTpl(tpl.toString());
        swiftConfig.setInitNumber(initialValue);
        swiftConfig.setStep(step);
        swiftConfig.setChangeType(ChangeType.INCR);
        swiftConfig.setSwiftGenType(genType);
        swiftConfig.setSwiftNumberLen(length);
        this.config = swiftConfig;
    }

    /**
     * generate new auto number
     */
    public String nextNo() {
        return swift.make(config);
    }

    /**
     * reset sequence no
     *
     * @return success if true
     */
    public boolean reset() {
        return swift.reset(config);
    }
}
