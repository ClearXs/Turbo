package cc.allio.turbo.modules.office.documentserver.vo;

import lombok.Data;

import java.util.List;

@Data
public class HistoryList {

    private Integer currentVersion;
    private List<HistoryInfo> history;

    @Data
    public static class HistoryInfo {
        private String created;
        private String key;
        private HistoryUser user;
        private Integer version;
        private List<ChangesHistory> changes;
        private String serverVersion;
    }

    @Data
    public static class HistoryUser {
        private String id;
        private String name;
    }
}
