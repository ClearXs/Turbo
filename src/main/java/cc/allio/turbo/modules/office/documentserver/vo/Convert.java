package cc.allio.turbo.modules.office.documentserver.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Convert {
    private String url;
    private String outputtype;
    private String filetype;
    private String title;
    private String key;
    private String filePass;
    private Boolean async;
    private String token;
    private String lang;
}
