package cc.allio.uno.turbo.common.web.params;

import cc.allio.uno.core.function.MethodReferenceColumn;
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
 * @since 0.1.0
 */
@Data
public class QueryParam<T extends IdEntity> {

    /**
     * 分页
     */
    @Schema(description = "分页")
    private Page<T> page;

    /**
     * 条件
     */
    @Schema(description = "条件")
    private List<Term> terms;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private List<Order> orders;

    /**
     * 通过函数式参数获取实体term
     *
     * @see #getTerm(String)
     */
    public <K> EntityTerm getTerm(MethodReferenceColumn<K> reference) {
        Term term = getTerm(reference.getColumn());
        if (term != null) {
            return new EntityTerm(term, reference.getEntityType(), reference.getField());
        }
        return null;
    }

    /**
     * 根据字段名称获取term
     *
     * @return term if not null
     */
    public Term getTerm(String field) {
        return terms.stream()
                .filter(term -> term.getField().equals(field))
                .findFirst()
                .orElse(null);
    }
}
