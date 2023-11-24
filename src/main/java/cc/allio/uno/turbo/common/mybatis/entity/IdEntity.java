package cc.allio.uno.turbo.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

@Data
public abstract class IdEntity implements Serializable {

    /**
     * 主键
     */
    @TableId(type = ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;
}
