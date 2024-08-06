package cc.allio.turbo.modules.office.documentserver.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Action {
    private String userid;
    private cc.allio.turbo.modules.office.documentserver.models.enums.Action type;
}
