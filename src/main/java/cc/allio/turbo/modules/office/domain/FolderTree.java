package cc.allio.turbo.modules.office.domain;

import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.modules.office.entity.Folder;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Setter
@Getter
public class FolderTree extends TreeDomain<Folder, FolderTree> {

    public FolderTree() {
    }

    public FolderTree(Folder folder) {
        super(folder, Comparator.comparingLong(FolderTree::getId));
    }

    /**
     * 文件夹名称
     */
    @TableField("name")
    @Schema(description = "文件夹名称")
    private String name;
}
