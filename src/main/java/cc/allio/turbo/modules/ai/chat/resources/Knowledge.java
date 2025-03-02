package cc.allio.turbo.modules.ai.chat.resources;

import lombok.Data;

@Data
public class Knowledge {

    private String key;

    private Object value;

    private Knowledge children;

}
