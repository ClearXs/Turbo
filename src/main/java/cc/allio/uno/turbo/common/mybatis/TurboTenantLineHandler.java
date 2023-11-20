package cc.allio.uno.turbo.common.mybatis;

import cc.allio.uno.turbo.common.util.WebUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

import java.util.List;

/**
 * tenant处理
 * <a herf="https://gitee.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-tenant">mybatis-plus-sample-tenant</a>
 *
 * @author j.x
 * @date 2023/11/1 09:38
 * @since 1.0.0
 */
public class TurboTenantLineHandler implements TenantLineHandler {

    private final List<String> ignoreTables = Lists.newArrayList();

    public TurboTenantLineHandler() {
        // 忽略租户的表需要进行添加，cloud版本需要考虑用配置进行过滤
        ignoreTables.add("sys_tenant");
        ignoreTables.add("sys_user_role");
        ignoreTables.add("sys_role_menu");
        ignoreTables.add("sys_cloud_storage_config");
    }

    @Override
    public Expression getTenantId() {
        return new StringValue(WebUtil.getTenant());
    }

    @Override
    public String getTenantIdColumn() {
        return TenantLineHandler.super.getTenantIdColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return ignoreTables.contains(tableName);
    }

}
