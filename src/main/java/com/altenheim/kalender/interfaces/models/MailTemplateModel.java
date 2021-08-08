package com.altenheim.kalender.interfaces.models;

import java.io.Serializable;
import java.util.Map;

public interface MailTemplateModel extends Serializable
{
    String getDefaultTemplate();
    Map<String, String> getTemplates();
}
