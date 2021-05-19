package controller;

import java.awt.*;
import java.net.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MailCreationController 
{
    private String recipient;
    private String subject;
    private String body;

    public MailCreationController(String recipient, String subject, String body)
    {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;        
    }
    
    public void sendMail() throws IOException, URISyntaxException
    {
        String uriStr = String.format("mailto:%s?subject=%s&body=%s",
            recipient, encodeUrl(subject), encodeUrl(body));
        Desktop.getDesktop().browse(new URI(uriStr));
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
