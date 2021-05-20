package interfaces;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IMailCreationController 
{
    public void sendMail(String recipient, String subject, String body) throws IOException, URISyntaxException;
    public String processPlaceholders(String body, String date, String time, int template);
}
