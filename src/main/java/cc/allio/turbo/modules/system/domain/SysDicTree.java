package cc.allio.turbo.modules.system.domain;

import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.modules.system.enums.DicType;
import cc.allio.turbo.modules.system.entity.SysDic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

/**
 * 字典树
 *
 * @author j.x
 * @date 2023/11/23 09:13
 * @since 0.1.0
 */
@Getter
@Setter
public class SysDicTree extends TreeDomain<SysDic, SysDicTree> {

    public SysDicTree() {
    }

    public SysDicTree(SysDic expand) {
        super(expand, Comparator.comparing(SysDicTree::getSort));
    }

    /**
     * 字典类型
     */
    @Schema(description = "字典类型")
    private DicType type;

    /**
     * 字典编码
     */
    @Schema(description = "字典类型")
    private String code;

    /**
     * 字典名称
     */
    @Schema(description = "字典名称")
    private String name;

    /**
     * 字典描述
     */
    @Schema(description = "字典描述")
    private String des;

    /**
     * 字典排序
     */
    @Schema(description = "字典排序")
    private Integer sort;

    /**
     * 颜色
     */
    @Schema(description = "颜色")
    private String color;
}
