package cc.allio.turbo.modules.office.documentserver.models.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter
@Setter
public class Logo {  // the image file at the top left corner of the Editor header
    @Value("${turbo.office.document.logo.image}")
    private String image;  // the path to the image file used to show in common work mode
    @Value("${turbo.office.document.logo.image-embedded}")
    private String imageEmbedded;  // the path to the image file used to show in the embedded mode
    @Value("${turbo.office.document.logo.url}")
    private String url;  // the absolute URL which will be used when someone clicks the logo image
}
