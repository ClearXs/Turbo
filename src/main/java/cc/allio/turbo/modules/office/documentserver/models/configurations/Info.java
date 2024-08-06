package cc.allio.turbo.modules.office.documentserver.models.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
@Scope("prototype")
@Getter
@Setter
/* The additional parameters for the document (document owner, folder where the document is stored,
 uploading date, sharing settings) */
public class Info {
    private String owner = "Me";  // the name of the document owner/creator
    private Boolean favorite = null;  // the highlighting state of the Favorite icon
    private String uploaded = getDate();  // the document uploading date

    private String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        return simpleDateFormat.format(new Date());
    }
}
