package cc.allio.uno.turbo.common.mybatis.help;

import cc.allio.uno.turbo.common.constant.Direction;
import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import cc.allio.uno.turbo.common.mybatis.params.GeneralParams;
import cc.allio.uno.turbo.common.mybatis.params.Order;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.List;

/**
 * 查询条件示例构建器
 *
 * @author j.x
 * @date 2023/11/22 15:57
 * @since 1.0.0
 */
public final class Conditions {

    /**
     * 通用参数构建{@link QueryWrapper}
     *
     * @param generalParams generalParams
     * @param <T>           某个实体类型
     * @return QueryWrapper instance
     */
    public static <T extends IdEntity> QueryWrapper<T> query(GeneralParams<T> generalParams) {
        T entity = generalParams.getEntity();
        QueryWrapper<T> queryWrapper = null;
        if (entity != null) {
            queryWrapper = Wrappers.query(entity);
        } else {
            queryWrapper = Wrappers.query();
        }
        // 排序
        List<Order> orders = generalParams.getOrders();
        if (CollectionUtils.isNotEmpty(orders)) {
            for (Order order : orders) {
                queryWrapper.orderBy(true, Direction.ASC == order.getDirection(), order.getProperty());
            }
        }
        return queryWrapper;
    }
}
