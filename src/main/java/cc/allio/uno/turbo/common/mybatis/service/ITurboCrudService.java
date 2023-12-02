package cc.allio.uno.turbo.common.mybatis.service;

import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 拓展mybatis-plus service 功能
 *
 * @author j.x
 * @date 2023/11/22 16:05
 * @since 1.0.0
 */
public interface ITurboCrudService<T extends IdEntity> extends IService<T> {


    /**
     * 实体的详情，详细返回的数据接口可能是原实体，可能是复合数据结构
     *
     * @param id  id
     * @param <V> 复合数据类型
     * @return 复合数据示例
     */
    <V extends T> V details(Long id);
}
