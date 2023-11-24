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
}
