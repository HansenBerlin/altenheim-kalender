package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.IWebsiteScraperController;
import com.altenheim.kalender.models.SettingsModel;
    import java.io.BufferedReader;
    import java.io.FileOutputStream;
    import java.io.FileReader;
    import java.io.IOException;
    import java.net.URL;
    import java.nio.channels.Channels;
    import java.nio.channels.ReadableByteChannel;
    import java.util.Timer;
    import java.util.TimerTask;


    public class WebsiteScraperController extends TimerTask implements IWebsiteScraperController
    {
        private SettingsModel settings;

        public WebsiteScraperController(SettingsModel settings)
        {
            this.settings = settings;        
        }

        public void startScraperTask()
        {
            var timer = new Timer();  
            timer.schedule(this, 0, settings.getScrapingInterval());
        }

        public void run() 
        {
            System.out.println("Scraping...");      
        }

		// ####################################main
		public void main(String[] args) throws IOException 
		{
			run();
			URL url = new URL(urlFinder());

			String line = "";
			downloadFile(url, line);
			
			// Ausgabe
			try (BufferedReader br = new BufferedReader(new FileReader("information.ics"))) {
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}

		} // ##################################Main End

		public void downloadFile(URL url, String fileName) throws IOException 
		{ // static?
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			FileOutputStream fos = new FileOutputStream("information.ics");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close(); // optional
        }
        public String urlFinder() 
        {
            String inputa;
            String inputb;
            String semester = "";
            String kurs = "";
            
            inputa = "wi";
            inputb = "semester2 - kursc";
            
            if (inputb.contains("1")) {
                 semester = "1";    
                }
            else if (inputb.contains("2")) {
                 semester = "2";    
                }
            else if (inputb.contains("3")) {
                 semester = "3";    
                }
            else if (inputb.contains("4")) {
                 semester = "4";    
                }
            else if (inputb.contains("5")) {
                 semester = "5";    
                }
            else if (inputb.contains("6")) {
                 semester = "6";    
                }
            else {
            System.out.println("Fehler bei Eingabe des Semesters");
            }
            
            if (inputb.contains("a")) {
                 kurs = "a";    
                }
            else if (inputb.contains("b")) {
                 kurs = "b";    
            }
            else if (inputb.contains("c")) {
                 kurs = "c";    
                }
            else {
                System.out.println("Fehler bei Eingabe des kurses");
                }
         String url = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/"+ inputa + "/semester"+ semester + "/kurs" + kurs;
         return url;
        }
          
}
