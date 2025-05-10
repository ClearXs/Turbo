package cc.allio.turbo.modules.ai.driver.model;

import cc.allio.turbo.modules.ai.enums.Role;
import lombok.Data;

@Data
public class Order {

    private Long id;
    private String message;
    private Role role;

    public Order() {
    }

    public Order(String message, Role role) {
        this.message = message;
        this.role = role;
    }

    /**
     * create {@link Order} instance, default role is {@link Role#USER}
     *
     * @param msg the message
     * @return
     * @see #to(String, Role)
     */
    public static Order toUser(String msg) {
        return to(msg, Role.USER);
    }

    /**
     * create {@link Order} instance, default role is {@link Role#SYSTEM}
     *
     * @param msg the message
     * @return
     * @see #to(String, Role)
     */
    public static Order toSystem(String msg) {
        return to(msg, Role.SYSTEM);
    }

    /**
     * create {@link Order} instance, default role is {@link Role#ASSISTANT}
     *
     * @param msg the message
     * @return
     * @see #to(String, Role)
     */
    public static Order toAssistant(String msg) {
        return to(msg, Role.ASSISTANT);
    }

    /**
     * create {@link Order} instance, default role is {@link Role#TOOL}
     *
     * @param msg the message
     * @return
     * @see #to(String, Role)
     */
    public static Order toTool(String msg) {
        return to(msg, Role.TOOL);
    }

    /**
     * create {@link Order} instance, default role is {@link Role#INSTRUCTION}
     *
     * @param msg the message
     * @return
     * @see #to(String, Role)
     */
    public static Order toInstruction(String msg) {
        return to(msg, Role.INSTRUCTION);
    }

    /**
     * create {@link Order} instance
     *
     * @param msg  the message
     * @param role the {@link Role}
     * @return
     */
    public static Order to(String msg, Role role) {
        return new Order(msg, role);
    }
}
