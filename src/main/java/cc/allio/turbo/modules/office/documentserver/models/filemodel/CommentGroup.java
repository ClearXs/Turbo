package cc.allio.turbo.modules.office.documentserver.models.filemodel;

import cc.allio.turbo.modules.office.documentserver.serializers.SerializerFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentGroup {
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = SerializerFilter.class)
    private List<String> view;  // define a list of groups whose comments the user can view
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = SerializerFilter.class)
    private List<String> edit;  // define a list of groups whose comments the user can edit
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = SerializerFilter.class)
    private List<String> remove;  // define a list of groups whose comments the user can remove
}
