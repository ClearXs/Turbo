package cc.allio.uno.turbo.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("org")
public class Org extends TreeEntity {

    private String name;
}
