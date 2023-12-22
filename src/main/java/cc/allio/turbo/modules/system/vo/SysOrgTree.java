package cc.allio.turbo.modules.system.vo;

import cc.allio.turbo.common.support.DomainTree;
import cc.allio.turbo.modules.system.entity.SysOrg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Setter
@Getter
public class SysOrgTree extends DomainTree<SysOrgTree, SysOrg> {

    public SysOrgTree(SysOrg expand) {
        super(expand, Comparator.comparing(SysOrgTree::getSort));
    }

    /**
     * 编码
     */
    @Schema(description = "编码")
    private String code;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String des;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 组织类型
     */
    @Schema(description = "组织类型")
    private String type;
}
