package com.altenheim.kalender.implementations.controller.models;

import com.altenheim.kalender.interfaces.models.MailTemplateModel;
import java.util.HashMap;
import java.util.Map;

public class MailTemplateModelImpl implements MailTemplateModel
{
    private Map<String, String> templates = new HashMap<>();

    public MailTemplateModelImpl()
    {
        templates.put("Standard Template", getDefaultTemplate());
    }

    public String getDefaultTemplate() 
    {
        return """
                Sehr geehrte Damen und Herren,\s
                ich hätte gerne einen Termin am [Datum] um [Uhrzeit].
                Vielen Dank und mit freundlichem Gruß,
                """;
    }

    public Map<String, String> getTemplates() { return templates; }
}
