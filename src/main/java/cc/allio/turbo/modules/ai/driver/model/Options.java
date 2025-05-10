package cc.allio.turbo.modules.ai.driver.model;

import lombok.Data;

@Data
public class Options {

    private boolean enableLimitHistoryMessages = true;
    private Integer maxHistoryMessageNums = 4;
}
