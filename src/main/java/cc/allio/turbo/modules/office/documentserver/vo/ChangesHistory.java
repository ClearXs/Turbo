package cc.allio.turbo.modules.office.documentserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangesHistory {
    private String created;
    private ChangesUser user;
}
