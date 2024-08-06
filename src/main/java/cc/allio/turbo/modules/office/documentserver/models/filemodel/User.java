package cc.allio.turbo.modules.office.documentserver.models.filemodel;

import cc.allio.turbo.modules.office.documentserver.models.AbstractModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter
@Setter
public class User extends AbstractModel {
    private String id;
    private String name;
    private String group;

    // the user configuration parameters
    public void configure(final int idParam, final String nameParam, final String groupParam) {
        this.id = "uid-" + idParam;  // the user id
        this.name = nameParam;  // the user name
        this.group = groupParam;  // the group the user belongs to
    }
}
