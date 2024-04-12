package cc.allio.turbo.common.db.uno;

import cc.allio.turbo.common.db.entity.Org;
import cc.allio.turbo.common.db.uno.repository.mybatis.WrapperAdapter;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.db.DbCommandExecutor;
import cc.allio.uno.data.test.executor.CommandExecutorSetter;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.MybatisPlusEnv;
import cc.allio.uno.test.testcontainers.ContainerType;
import cc.allio.uno.test.testcontainers.RunContainer;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@RunTest
@MybatisPlusEnv
@RunContainer(ContainerType.MySQL)
public class ITurboCrudTreeRepositoryServiceTest extends BaseTestCase implements CommandExecutorSetter<DbCommandExecutor> {

    private AggregateCommandExecutor commandExecutor;
    private OrgService orgService;

    @BeforeEach
    void init() {
        this.orgService = new OrgService(commandExecutor, Org.class);
        commandExecutor.createTable(Org.class);
        Org o1 = new Org();
        o1.setId(1L);

        Org o2 = new Org();
        o2.setId(2L);
        o2.setParentId(1L);
        orgService.saveBatch(List.of(o1, o2));

        WrapperAdapter.initiation(new Configuration());
    }

    @Test
    void testTreeQuery() {
        List<Org> tree = orgService.tree(Wrappers.lambdaQuery(Org.class));
        assertNotNull(tree);
    }

    @Override
    public void setCommandExecutor(DbCommandExecutor executor) {
        this.commandExecutor = executor;
    }
}
