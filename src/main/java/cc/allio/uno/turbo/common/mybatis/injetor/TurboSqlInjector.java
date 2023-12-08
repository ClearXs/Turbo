package cc.allio.uno.turbo.common.mybatis.injetor;

import cc.allio.uno.turbo.common.mybatis.injetor.methods.Tree;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

/**
 * 拓展mybatis plus单表操作
 *
 * @author j.x
 * @date 2023/11/27 18:34
 * @since 0.1.0
 */
public class TurboSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        var methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new Tree());
        return methodList;
    }
}
