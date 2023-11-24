package cc.allio.uno.turbo.common.mybatis.plugins.inner;

import cc.allio.uno.turbo.common.persistent.PersistentProperties;
import cc.allio.uno.turbo.common.util.WebUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * tenant处理
 * <a herf="https://gitee.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-tenant">mybatis-plus-sample-tenant</a>
 *
 * @author j.x
 * @date 2023/11/1 09:38
 * @since 1.0.0
 */
public class TurboTenantLineHandler implements TenantLineHandler {

    private final PersistentProperties.Tenant tenant;

    public TurboTenantLineHandler(PersistentProperties.Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public Expression getTenantId() {
        return new StringValue(WebUtil.getTenant());
    }

    @Override
    public String getTenantIdColumn() {
        return tenant.getField();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return tenant.getIgnoreList().contains(tableName);
    }

}
