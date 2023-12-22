package cc.allio.turbo.common.web.params;

import cc.allio.turbo.common.constant.Direction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Order {

    /**
     * 排序方式
     */
    @Schema(description = "排序方式")
    private Direction direction;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String property;
}
