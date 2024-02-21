package cc.allio.turbo.common.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("org")
public class Org extends TreeEntity {

    private String name;
}
