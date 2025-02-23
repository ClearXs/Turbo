package cc.allio.turbo.modules.development.dto;

import cc.allio.turbo.common.db.constant.StorageType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParseDDLDTO {
    @NotNull
    private String ddlText;

    @NotNull
    private StorageType engine;

}
