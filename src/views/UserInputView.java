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
        System.out.println(suggestion.testFunction());
        // starte hier die Logik für den Input
    }
    
}
