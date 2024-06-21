package cc.allio.turbo.common.db.mybatis.plugins.inner;

import cc.allio.turbo.common.db.event.ThreadLocalWebDomainEventContext;
import cc.allio.turbo.common.db.persistent.PersistentProperties;
import cc.allio.turbo.common.util.WebUtil;
import cc.allio.uno.core.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * tenant处理
 * <a herf="https://gitee.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-tenant">mybatis-plus-sample-tenant</a>
 *
 * @author j.x
 * @date 2023/11/1 09:38
 * @since 0.1.0
 */
public class TurboTenantLineHandler implements TenantLineHandler {

    private final PersistentProperties.Tenant tenant;

    public TurboTenantLineHandler(PersistentProperties.Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public Expression getTenantId() {
        ThreadLocalWebDomainEventContext eventContext = ThreadLocalWebDomainEventContext.getCurrentThreadContext();
        String tenantId = WebUtil.getTenant();
        if (eventContext != null) {
            tenantId = eventContext.getTenantId().orElse(tenantId);
        }
        if (StringUtils.isBlank(tenantId)) {
            return null;
        }
        return new StringValue(tenantId);
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