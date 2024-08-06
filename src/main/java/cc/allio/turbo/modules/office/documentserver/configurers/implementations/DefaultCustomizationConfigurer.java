package cc.allio.turbo.modules.office.documentserver.configurers.implementations;

import cc.allio.turbo.modules.office.configuration.properties.DocumentProperties;
import cc.allio.turbo.modules.office.documentserver.configurers.CustomizationConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultCustomizationWrapper;
import cc.allio.turbo.modules.office.documentserver.models.configurations.Customization;

public class DefaultCustomizationConfigurer implements CustomizationConfigurer<DefaultCustomizationWrapper> {

    private final DocumentProperties.Customization customizationProperties;

    public DefaultCustomizationConfigurer(DocumentProperties.Customization customizationProperties) {
        this.customizationProperties = customizationProperties;
    }

    @Override
    public void configure(final Customization customization, final DefaultCustomizationWrapper wrapper) {
        // set the submitForm parameter to the customization config
        customization.setAutosave(customizationProperties.getAutosave());
        customization.setComments(customizationProperties.getComments());
        customization.setCompactHeader(customizationProperties.getCompactToolbar());
        customization.setCompatibleFeatures(customizationProperties.getCompatibleFeatures());
        customization.setForcesave(customizationProperties.getForcesave());
        customization.setHelp(customizationProperties.getHelp());
        customization.setHideRightMenu(customizationProperties.getHideRightMenu());
        customization.setHideRulers(customizationProperties.getHideRulers());
        customization.setSubmitForm(customizationProperties.getSubmitForm());
        customization.setAbout(customizationProperties.getAbout());
        customization.setFeedback(customizationProperties.getFeedback());
    }
}
