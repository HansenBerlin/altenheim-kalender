package views;

import interfaces.IAppointmentSuggestionController;

public class UserInputView 
{
    private IAppointmentSuggestionController suggestion;

    public UserInputView(IAppointmentSuggestionController suggestion)
    {
        this.suggestion = suggestion;  
        // Übergebenes Objekt deiner Controllerklasse ermöglicht dir hier direkt
        // den Aufruf. Alle Objekte werden in der Main gebaut. Da dieses Objekt
        // wiederum alle Termine enthält sind alle Abhängigkeiten entkoppelt
    }

    public void askForUserInputInLoop()
    {
    	
		int firstDate = 20;
		int interval = 60;
		int spread = 3;
		int maxOffers = 4;
		int appointmentDuration = 147;
		int travelTime = 38;
		int institutionOpen = 11;
		int institutionClose = 17;
        System.out.println(suggestion.testFunction(firstDate, interval, spread, maxOffers, appointmentDuration, travelTime, institutionOpen, institutionClose));
        suggestion.testFunctionTwo();
//        System.out.println(suggestion.testFunction());

        // starte hier die Logik für den Input
    }
    
}
