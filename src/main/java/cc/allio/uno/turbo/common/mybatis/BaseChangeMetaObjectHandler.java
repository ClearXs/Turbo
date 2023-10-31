package cc.allio.uno.turbo.common.mybatis;

import cc.allio.uno.core.util.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 设置基础字段create_time、update_time、update_by、is_deleted等值，使数据提供审计追溯
 *
 * @author j.x
 * @date 2023/10/23 13:37
 * @since 1.0.0
 */
public class BaseChangeMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时间
        this.strictInsertFill(metaObject, "createdTime", Date.class, DateUtil.now());
        // TODO 创建人
//        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
        // 更新时间
        this.strictInsertFill(metaObject, "updatedTime", Date.class, DateUtil.now());
        // TODO 更新人
        // 逻辑删除
        this.strictInsertFill(metaObject, "isDeleted", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间
        this.strictInsertFill(metaObject, "updatedTime", Date.class, DateUtil.now());
        // TODO 更新人
    }
}
