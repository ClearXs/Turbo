package cc.allio.uno.turbo.common.mybatis.handle;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.turbo.common.util.AuthUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 设置基础字段create_time、update_time、update_by、is_deleted等值，使数据提供审计追溯
 *
 * @author j.x
 * @date 2023/10/23 13:37
 * @since 0.1.0
 */
public class BaseChangeMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentUserId = AuthUtil.getCurrentUserId();
        // 创建时间
        this.strictInsertFill(metaObject, "createdTime", Date.class, DateUtil.now());
        // 创建人
        this.strictInsertFill(metaObject, "createdBy", Long.class, currentUserId);
        // 更新时间
        this.strictInsertFill(metaObject, "updatedTime", Date.class, DateUtil.now());
        // 更新人
        this.strictInsertFill(metaObject, "updatedBy", Long.class, currentUserId);
        // 逻辑删除
        this.strictInsertFill(metaObject, "isDeleted", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentUserId = AuthUtil.getCurrentUserId();
        // 更新时间
        this.setFieldValByName("updatedTime", DateUtil.now(), metaObject);
        // 更新人
        this.setFieldValByName("updatedBy", currentUserId, metaObject);
    }
}
