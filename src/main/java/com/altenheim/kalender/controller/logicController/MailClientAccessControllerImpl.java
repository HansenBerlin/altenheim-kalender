package com.altenheim.kalender.controller.logicController;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import com.altenheim.kalender.interfaces.MailClientAccessController;
import com.altenheim.kalender.interfaces.MailTemplateModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MailClientAccessControllerImpl implements MailClientAccessController
{
    private MailTemplateModel mailTemplates;

    public MailClientAccessControllerImpl(MailTemplateModel mailTemplates)
    {
        this.mailTemplates = mailTemplates;
    }

    public void processMailWrapper(String templateName, String date, String time, String recipient)
    {
        String subject = "Terminanfrage";
        String mailBody = getMailTemplate(templateName);
        String processedBody = processPlaceholders(mailBody, date, time);
        String uriStr = String.format("mailto:%s?subject=%s&body=%s", recipient, encodeUrl(subject), encodeUrl(processedBody));
        try 
        {
            Desktop.getDesktop().browse(new URI(uriStr));
        } 
        catch (IOException | URISyntaxException e) 
        {
            e.printStackTrace();    
        }
    }    

    private String getMailTemplate(String templateName)
    {
        for (var template : mailTemplates.getTemplates().entrySet()) 
        {
            if (template.getKey().equals(templateName))
                return template.getValue();            
        }
        return mailTemplates.getDefaultTemplate();

    }

    private String processPlaceholders(String body, String date, String time) 
    {
        body = body.replace("[Datum]", date);
        body = body.replace("[Uhrzeit]", time);

        return body;
    }

    private String encodeUrl(String uri) 
    {
        try 
        {
            return URLEncoder.encode(uri, "UTF-8").replace("+", "%20");
        } 
        catch (UnsupportedEncodingException e) 
        {
            throw new RuntimeException(e);
        }
    }    
}