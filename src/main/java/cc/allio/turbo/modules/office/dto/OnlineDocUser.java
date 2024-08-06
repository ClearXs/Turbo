package cc.allio.turbo.modules.office.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OnlineDocUser {

    /**
     * 用户id
     */
    @Schema(name = "用户id")
    private Long userId;

    /**
     * 用户名称
     */
    @Schema(name = "用户名称")
    private String userName;

    /**
     * 文档key
     */
    @Schema(name = "文档key")
    private String docKey;
}
