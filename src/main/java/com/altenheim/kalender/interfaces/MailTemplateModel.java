package com.altenheim.kalender.interfaces;

import java.io.Serializable;
import java.util.Map;

public interface MailTemplateModel extends Serializable
{
    String getDefaultTemplate();
    void addTemplate(String key, String value);
    void removeTemplate(String key);
    Map<String, String> getTemplates();
    void setTemplates(Map<String, String> map);
}
