package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_doc")
@Schema(description = "文档")
public class Doc extends TenantEntity {

    /**
     * 文档名称
     */
    @TableField("title")
    @NotBlank
    @Schema(name = "文档名称")
    private String title;

    /**
     * 文档名称
     */
    @TableField("type")
    @Schema(name = "文档类型")
    private String type;

    /**
     * 文档标签
     */
    @TableField("label")
    @Schema(name = "文档标签")
    private String label;

    /**
     * 文档唯一标识
     */
    @Schema(name = "文档唯一标识")
    @TableField("key")
    private String key;

    /**
     * 文件数据
     * <p>形如：[{"id":"1788516493209591809","createUserId":"-1","createTime":"2024-05-09 18:28:55","updateUserId":"-1","updateTime":"2024-05-09 18:28:55","fileName":"iShot_2024-05-07_10.41.22.mp4","filePath":"2024/5/9/753c830bbb1544cbb200c6bb50a8e77f","fileSize":"136295","fileFormat":"mp4","status":"0"}]</p>
     */
    @TableField("file")
    @Schema(name = "文件数据", description = "[{\"id\":\"1788516493209591809\",\"createUserId\":\"-1\",\"createTime\":\"2024-05-09 18:28:55\",\"updateUserId\":\"-1\",\"updateTime\":\"2024-05-09 18:28:55\",\"fileName\":\"iShot_2024-05-07_10.41.22.mp4\",\"filePath\":\"2024/5/9/753c830bbb1544cbb200c6bb50a8e77f\",\"fileSize\":\"136295\",\"fileFormat\":\"mp4\",\"status\":\"0\"}]")
    private String file;

    /**
     * 拥有者
     */
    @TableField("creator")
    @Schema(name = "拥有者")
    private Long creator;

    /**
     * 版本号
     */
    @TableField("doc_version")
    @Schema(name = "版本号")
    private Integer docVersion;

    /**
     * 文件夹id
     */
    @TableField("folder_id")
    @Schema(name = "文件夹id")
    private Long folderId;
}
