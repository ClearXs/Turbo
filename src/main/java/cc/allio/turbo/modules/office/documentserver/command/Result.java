package cc.allio.turbo.modules.office.documentserver.command;

import lombok.Data;

/**
 * command request result
 *
 * @author j.x
 * @date 2024/5/13 19:14
 * @since 0.0.1
 */
@Data
public class Result {

    /**
     * result code
     */
    private ResultCode code;
    private String msg;
}