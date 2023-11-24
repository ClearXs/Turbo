package cc.allio.uno.turbo.common.mybatis.params;

import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 定义统一查询参数
 * <ol>
 *     <li>排序</li>
 *     <li>用于过滤的实体</li>
 * </ol>
 *
 * @author j.x
 * @date 2023/11/22 15:37
 * @since 1.0.0
 */
@Data
public class GeneralParams<T extends IdEntity> {

    /**
     * 分页
     */
    @Schema(description = "分页")
    Page<T> page;

    /**
     * 过滤实体
     */
    @Schema(description = "过滤实体")
    private T entity;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private List<Order> orders;
}
