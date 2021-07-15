package com.altenheim.kalender.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MailTemplateModel implements Serializable
{
    Map<String, String> templates = new HashMap<String, String>();

    public void initialize (){
        templates.put("templateOne" , getTemplateOne());
        templates.put("templateTwo" , getTemplateTwo());
    }
    
    public String getTemplateOne()
    {
        return "Sehr geehrter Typ von nebenan,\nich hätte gerne einen Termin am " +
            "[Datum] um [Uhrzeit].\nVielen Dank und mit freundlichem Gruß\nDein Stalker";
    }

    public String getTemplateTwo()
    {
        return "Sehr geehrte Frau Merkel,\ndanke für 16 Jahre Stillstand Bitte starten Sie " +
            "am [Datum] um [Uhrzeit] mit ihrer Arbeit als Bundeskanzlerin.\nVielen Dank und mit freundlichem Gruß\nEin Bürger";
    }
    
    public void addTemplate (String key, String value) {templates.put(key, value);}
    public void removeTemplate (String key) {templates.remove(key);}
    public Map<String, String> getTemplates (){return templates;}
    public void setTemplates (Map<String, String> map){
        this.templates = map;
    }



}
