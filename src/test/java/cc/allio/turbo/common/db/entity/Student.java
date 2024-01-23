package cc.allio.turbo.common.db.entity;

import cc.allio.turbo.common.db.constraint.Unique;
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
