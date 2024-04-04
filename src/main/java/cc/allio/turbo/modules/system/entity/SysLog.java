package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.entity.MapEntity;
import cc.allio.turbo.modules.system.constant.LogType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统日志
 *
 * @author j.x
 * @date 2024/4/2 18:30
 * @since 0.1.1
 */
@Data
@Schema(description = "系统日志")
@EqualsAndHashCode(callSuper = true)
public class SysLog extends MapEntity {

    /**
     * 日志类型
     */
    @Schema(description = "日志类型")
    private LogType logType;
}
