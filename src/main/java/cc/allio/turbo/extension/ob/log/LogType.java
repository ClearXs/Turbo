package cc.allio.turbo.extension.ob.log;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * describe log type
 *
 * @author j.x
 * @date 2024/4/5 22:18
 * @since 0.1.1
 */
@Data
@AllArgsConstructor(staticName = "of")
public class LogType {

    public static final LogType SYSTEM = LogType.of("system");

    /**
     * the type name
     */
    String typeName;
}
