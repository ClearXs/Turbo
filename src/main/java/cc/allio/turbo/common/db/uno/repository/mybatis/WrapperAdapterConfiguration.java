package cc.allio.turbo.common.db.uno.repository.mybatis;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;

@org.springframework.context.annotation.Configuration
@AllArgsConstructor
public class WrapperAdapterConfiguration implements InitializingBean {

    SqlSessionFactory sqlSessionFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        WrapperAdapter.initiation(configuration);
    }
}
