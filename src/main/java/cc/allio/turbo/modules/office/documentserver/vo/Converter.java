package cc.allio.turbo.modules.office.documentserver.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Converter {
    @JsonProperty("filename")
    private String fileName;
    @JsonProperty("filePass")
    private String filePass;
    @JsonProperty("lang")
    private String lang;
}
