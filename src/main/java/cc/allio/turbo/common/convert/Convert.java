package cc.allio.turbo.common.convert;

import java.util.List;

/**
 * Convert 文件基类
 * 更多的用法需自行实现
 *
 * @param <D> 数据传输对象
 * @param <E> 实体对象
 * @param <V> 视图对象
 */
public interface Convert<E, V, D> {

    /**
     * 将数据传输对象转换为实体对象
     *
     * @param d
     * @return E
     */
    E dto2Entity(D d);

    /**
     * 将实体对象转换为视图对象
     *
     * @param e
     * @return D
     */
    V entity2VO(E e);

    /**
     * 将实体对象集合转换为视图对象集合
     *
     * @param list
     * @return List<D>
     */
    List<V> entity2VOList(List<E> list);

    List<D> vo2DTOList(List<V> list);

    default Integer booleanToInteger(Boolean value) {
        if (value == null) {
            return null;
        }
        return value ? 1 : 0;
    }

    default Boolean integerToBoolean(Integer value) {
        if (value == null) {
            return null;
        }
        return value == 0 ? false : true;
    }
}
