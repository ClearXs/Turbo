package cc.allio.turbo.modules.development.api;

import cc.allio.turbo.modules.development.code.Instance;
import cc.allio.turbo.modules.development.code.Module;
import cc.allio.uno.core.api.EqualsTo;
import cc.allio.uno.data.orm.dsl.DSLName;

import java.io.Serializable;

/**
 * description {@link Module} and {@link Instance} of key
 *
 * @author j.x
 * @date 2024/7/16 18:55
 * @since 0.1.1
 */
public class Key implements Serializable, EqualsTo<String> {

    private final String original;

    public Key(String original) {
        this.original = original;
    }

    /**
     * format original to first upper case name and all string use for {@link DSLName#HUMP_FEATURE}
     * <p>like as 'low_code' -> 'LowCode'</p>
     *
     * @return format string
     */
    public String getStandard() {
        return DSLName.of(original, DSLName.HUMP_FEATURE, DSLName.FIRST_CHAR_UPPER_CASE_FEATURE).format();
    }

    /**
     * format original string to hump string
     * <p>like as 'low_code' -> 'lowCode' </p>
     *
     * @return format string
     */
    public String getHump() {
        return DSLName.of(original, DSLName.HUMP_FEATURE).format();
    }

    /**
     * get original key name
     *
     * @return original string
     */
    public String get() {
        return original;
    }

    /**
     * get original lower case
     */
    public String getLowerCase() {
        return original.toLowerCase();
    }

    /**
     * get original upper case
     */
    public String getUpperCase() {
        return original.toUpperCase();
    }

    @Override
    public String toString() {
        return original;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && equalsTo(obj.toString());
    }

    @Override
    public boolean equalsTo(String other) {
        return getStandard().equals(new Key(other).getStandard());
    }
}
