package cc.allio.turbo.modules.office.documentserver.vo;

import cc.allio.turbo.modules.office.documentserver.models.filemodel.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
    @JsonProperty("serverVersion")
    private String serverVersion;
    private String key;
    private Integer version;
    private String created;
    private User user;
    private List<ChangesHistory> changes;
}
