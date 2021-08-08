package com.altenheim.kalender.implementations.controller.logicController;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import com.altenheim.kalender.interfaces.logicController.MailClientAccessController;
import com.altenheim.kalender.interfaces.models.MailTemplateModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public record MailClientAccessControllerImpl(
        MailTemplateModel mailTemplates) implements MailClientAccessController
{

    public void processMailWrapper(String templateName, String date, String time, String recipient) {
        String subject = "Terminanfrage";
        String mailBody = getMailTemplate(templateName);
        String processedBody = processPlaceholders(mailBody, date, time);
        String uriStr = String.format("mailto:%s?subject=%s&body=%s", recipient, encodeUrl(subject), encodeUrl(processedBody));
        try {
            Desktop.getDesktop().browse(new URI(uriStr));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String getMailTemplate(String templateName) {
        for (var template : mailTemplates.getTemplates().entrySet()) {
            if (template.getKey().equals(templateName))
                return template.getValue();
        }
        return mailTemplates.getDefaultTemplate();

    }

    private String processPlaceholders(String body, String date, String time) {
        body = body.replace("[Datum]", date);
        body = body.replace("[Uhrzeit]", time);

        return body;
    }

    private String encodeUrl(String uri) {
        return URLEncoder.encode(uri, StandardCharsets.UTF_8).replace("+", "%20");
    }
}