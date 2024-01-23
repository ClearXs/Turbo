package cc.allio.turbo.common.db.mybatis;

import cc.allio.turbo.common.db.TurboDbConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(TurboDbConfiguration.class)
@AllArgsConstructor
public class AppendArrayTypeHandlerConfiguration {

    private final SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    void setup() {
        org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        // 数组类型
        typeHandlerRegistry.register(Integer[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(int[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(String[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(Float[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(float[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(Double[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(double[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(Short[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(short[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(Character[].class, ArrayTypeHandler.class);
        typeHandlerRegistry.register(char[].class, ArrayTypeHandler.class);
    }
}
