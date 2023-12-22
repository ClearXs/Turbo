package cc.allio.turbo.common.mybatis.id;

import cc.allio.uno.core.util.id.IdGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

public class SnowflakeIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        return IdGenerator.defaultGenerator().getNextId();
    }

    @Override
    public String nextUUID(Object entity) {
        return nextId(entity).toString();
    }
}
