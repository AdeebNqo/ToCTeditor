package models;

import za.co.mahlaza.research.grammarengine.base.models.template.Template;

import java.util.List;


public interface OnTemplateChangeListener {
    void onAddNewTemplate(Template template);
    void onAddNewTemplates(List<Template> templates, String URI);
}
