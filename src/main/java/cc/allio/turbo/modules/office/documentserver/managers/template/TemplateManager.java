package cc.allio.turbo.modules.office.documentserver.managers.template;

import cc.allio.turbo.modules.office.documentserver.models.filemodel.Template;

import java.util.List;

// specify the template manager functions
public interface TemplateManager {
    List<Template> createTemplates(String fileName);  // create a template document with the specified name

    String getTemplateImageUrl(String fileName);  // get the template image URL for the specified file
}
