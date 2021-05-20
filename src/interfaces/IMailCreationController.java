package interfaces;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IMailCreationController 
{
    public void sendMail() throws IOException, URISyntaxException;    
}
