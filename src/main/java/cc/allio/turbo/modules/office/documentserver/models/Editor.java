package cc.allio.turbo.modules.office.documentserver.models;

import cc.allio.turbo.modules.office.documentserver.models.filemodel.FileModel;
import cc.allio.turbo.modules.office.documentserver.vo.Mentions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Editor {

    private FileModel model;
    /**
     * onlyoffice server url
     */
    private String documentServerUrl;
    private String docserviceApiUrl;
    private String dataInsertImage;
    private String dataCompareFile;
    private String dataMailMergeRecipients;
    private List<Mentions> usersForMentions;
}
