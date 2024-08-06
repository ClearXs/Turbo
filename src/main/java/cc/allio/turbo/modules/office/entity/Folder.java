package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TreeEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_folder")
@Schema(description = "文件夹")
public class Folder extends TreeEntity {

    /**
     * 文件夹名称
     */
    @TableField("name")
    @Schema(description = "文件夹名称")
    private String name;
}
