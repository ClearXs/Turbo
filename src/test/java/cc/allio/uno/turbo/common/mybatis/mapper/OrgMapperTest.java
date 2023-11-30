package cc.allio.uno.turbo.common.mybatis.mapper;

import cc.allio.uno.test.*;
import cc.allio.uno.test.env.annotation.MybatisPlusEnv;
import cc.allio.uno.turbo.common.mybatis.MybatisConfiguration;
import cc.allio.uno.turbo.common.mybatis.entity.Org;
import cc.allio.uno.turbo.common.persistent.PersistentConfiguration;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;

@RunTest(components = {MybatisConfiguration.class, PersistentConfiguration.class})
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
