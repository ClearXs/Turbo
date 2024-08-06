package cc.allio.turbo.modules.office.documentserver.vo;

import lombok.Data;

@Data
public class HistoryData {

    private String changesUrl;
    private String fileType;
    private String key;
    private Previous previous;
    private String token;
    private String url;
    private Integer version;

    @Data
    public static class Previous {
        private String fileType;
        private String key;
        private String url;
    }
}
