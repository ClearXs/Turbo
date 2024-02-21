package cc.allio.turbo.modules.system.domain;

import cc.allio.turbo.common.db.constraint.Sortable;
import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.modules.system.entity.SysCategory;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

/**
 * 种类树
 *
 * @author jiangwei
 * @date 2024/1/11 11:42
 * @since 0.1.0
 */
@Getter
@Setter
public class SysCategoryTree extends TreeDomain<SysCategory, SysCategoryTree> {

    public SysCategoryTree() {
    }

    public SysCategoryTree(SysCategory expand) {
        super(expand, Comparator.comparing(SysCategoryTree::getSort));
    }

    /**
     * 分类名称
     */
    @TableField("name")
    @Schema(description = "分类名称")
    @NotNull
    private String name;

    /**
     * 字典编码
     */
    @TableField("code")
    @Schema(description = "分类标识")
    @NotNull
    @Unique
    private String code;

    /**
     * 字典排序
     */
    @TableField("sort")
    @Schema(description = "字典排序")
    @Sortable
    private Integer sort;

    /**
     * 功能标识
     */
    @TableField("func_code")
    @Schema(description = "功能标识")
    @Sortable
    private String funcCode;
}
