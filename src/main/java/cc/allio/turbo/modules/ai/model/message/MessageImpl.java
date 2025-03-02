package cc.allio.turbo.modules.ai.model.message;

import cc.allio.turbo.modules.ai.enums.Role;
import lombok.Data;

/**
 * call chat message.
 */
@Data
public class MessageImpl implements Message {

    private String id;
    private String model;
    private Role role;
    private String content;
    private String finish;
    private long createAt;

    @Override
    public String content() {
        return content;
    }

    @Override
    public String finish() {
        return finish;
    }

    @Override
    public long createAt() {
        return createAt;
    }

    @Override
    public Role role() {
        return role;
    }
}
