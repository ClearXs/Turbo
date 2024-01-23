package cc.allio.turbo.common.db.mapper;

import cc.allio.uno.test.*;
import cc.allio.uno.test.env.annotation.MybatisPlusEnv;
import cc.allio.turbo.common.db.TurboDbConfiguration;
import cc.allio.turbo.common.db.entity.Org;
import cc.allio.turbo.common.db.persistent.PersistentConfiguration;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;

@RunTest(components = {TurboDbConfiguration.class, PersistentConfiguration.class})
@MybatisPlusEnv(basePackages = "cc.allio.uno.turbo.common.mybatis.**")
public class OrgMapperTest extends BaseTestCase {

    @Inject
    private OrgMapper orgMapper;

    @Test
    void testTree(@Parameter CoreTest coreTest) {
        assertDoesNotThrow(() -> orgMapper.selectTree(Wrappers.emptyWrapper()));

        assertDoesNotThrow(() -> orgMapper.selectTree(Wrappers.<Org>lambdaQuery().eq(Org::getName, "字典1")));

    }

}
