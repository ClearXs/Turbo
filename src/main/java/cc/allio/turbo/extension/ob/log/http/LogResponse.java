package cc.allio.turbo.extension.ob.log.http;

import cc.allio.turbo.common.db.entity.MapEntity;
import cc.allio.turbo.extension.ob.log.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * open observer response
 *
 * @author j.x
 * @date 2024/4/5 22:23
 * @since 0.1.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogResponse extends MapEntity {

    private Long took;
    private List<Hit> hits;
    private Long total;
    private Long scanSize;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Hit extends MapEntity {
        private LogType logType;
    }
}
