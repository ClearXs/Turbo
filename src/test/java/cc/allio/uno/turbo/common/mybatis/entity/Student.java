package cc.allio.uno.turbo.common.mybatis.entity;

import cc.allio.uno.turbo.common.mybatis.constraint.Unique;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student")
public class Student extends IdEntity {

    @Unique
    private String code;

    @Unique
    private String name;
}
