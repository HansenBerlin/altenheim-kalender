package com.altenheim.kalender.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MailTemplateModel implements Serializable 
{
    Map<String, String> templates = new HashMap<String, String>();

    public MailTemplateModel() 
    {
        templates.put("Standard Template", getDefaultTemplate());
    }

    public String getDefaultTemplate() 
    {
        return "Sehr geehrte Damen und Herren, \nich hätte gerne einen Termin am "
                + "[Datum] um [Uhrzeit].\nVielen Dank und mit freundlichem Gruß,\n";
    }

    public void addTemplate(String key, String value) 
    {
        templates.put(key, value);
    }

    public void removeTemplate(String key) { templates.remove(key); }
    public Map<String, String> getTemplates() { return templates; }

    public void setTemplates(Map<String, String> map) 
    {
        this.templates = map;
    } 
}
