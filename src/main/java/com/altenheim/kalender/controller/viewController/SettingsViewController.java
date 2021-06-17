package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.models.SettingsModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SettingsViewController extends ResponsiveController
{ 
    private SettingsModel settings;
    @FXML
    private TextField Studiengang;

    @FXML
    private TextField Kurs;

    @FXML
    void save(ActionEvent event) {
        String userinput = urlbuilder(Studiengang.getText(),Kurs.getText());
         settings.seturlvariables(userinput);
    }
    public SettingsViewController(SettingsModel settings)
    {
        this.settings = settings;
    }
    
    public void changeContentPosition() 
    {
        
    }    
    

        public String urlbuilder(String inputa, String inputb) {
            
            String semester = "";
            String kurs = "";
            
            
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
         return "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/"+ inputa + "/semester"+ semester + "/kurs" + kurs;
         
        }
        
    
    
}


