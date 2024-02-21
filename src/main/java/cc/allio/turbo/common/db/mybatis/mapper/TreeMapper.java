package cc.allio.turbo.common.db.mybatis.mapper;

import cc.allio.turbo.common.db.entity.TreeEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基于树结构快速搜索mapper
 *
 * @param <T> 树实体类型，子类必须要继承
 * @author j.x
 * @date 2023/11/27 18:29
 * @since 0.1.0
 */
public interface TreeMapper<T extends TreeEntity> extends BaseMapper<T> {

    /**
     * 树型查找
     */
    List<T> selectTree(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
}
