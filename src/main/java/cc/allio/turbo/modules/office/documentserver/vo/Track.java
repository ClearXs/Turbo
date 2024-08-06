package cc.allio.turbo.modules.office.documentserver.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private String filetype;
    private String url;
    private String key;
    private String changesurl;
    private History history;
    private String token;
    private Integer forcesavetype;
    private Integer status;
    private List<String> users;
    private List<Action> actions;
    private String userdata;
    private String lastsave;
    private Boolean notmodified;
}
