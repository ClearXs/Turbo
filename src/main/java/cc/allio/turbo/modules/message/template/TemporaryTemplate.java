package cc.allio.turbo.modules.message.template;

import lombok.Data;

import java.util.List;

@Data
public class TemporaryTemplate {

    private String title;
    private String subtitle;
    private String template;
    private List<Variable> variableList;
    private Extension extension;
}
