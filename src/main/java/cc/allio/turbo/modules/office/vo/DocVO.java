package cc.allio.turbo.modules.office.vo;

import cc.allio.turbo.modules.office.entity.Doc;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocVO extends Doc {

    /**
     * 是否是分享的
     */
    @Schema(name = "是否是分享的")
    private Boolean shared;

    /**
     * 是否是喜爱的
     */
    @Schema(name = "是否是喜爱的")
    private Boolean favorite;

    /**
     * 是否是常用
     */
    @Schema(name = "是否是常用")
    private Boolean favor;

    /**
     * 协作者
     */
    @Schema(name = "协作者")
    private Long cooperator;

    /**
     * 权限组
     */
    @Schema(name = "权限组")
    private Long permissionGroupId;

}
