package controller;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import interfaces.IAppointmentEntryFactory;
import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
import interfaces.ICalendarEntryModel;

public class AppointmentSuggestionController implements IAppointmentSuggestionController
{
    private ICalendarEntriesModel allEntries;
    private IAppointmentEntryFactory administrateEntries;
    
    public AppointmentSuggestionController(ICalendarEntriesModel allEntries, IAppointmentEntryFactory administrateEntries)
    {
        this.allEntries = allEntries;
        this.administrateEntries = administrateEntries;
        
        // übergebens Interface beinhaltet Arrays für random generierte Termine
        // und für jeden 7ten Tag ('Sonntag') Kannste dir mit den ensprechenden Methoden
        // komplett rausholen oder nur spezifische Tage
    }

    public int testFunction()
    {

    	boolean[] tage = new boolean[365];
    	int erster_Termin = 23;
    	int intervall = 60;
    	int streuung = 2;
    	int max_Terminevorschläge = 3;
    	for (int i = 0; i < tage.length; i++) 
    	{
			if (Math.random()<= 0.5) 
			{
				tage[i]= true;
			}
			else 
			{
				tage[i]=false;
				
			}
		}
    	LinkedList<Integer> mögliche_termine = new LinkedList<Integer>();
    	if (tage[erster_Termin+intervall] && erster_Termin+intervall%7!=0) {
			mögliche_termine.add(erster_Termin+intervall);
		}
    	for (int i = 1; i < streuung; i++) 
    	{
			if (tage[erster_Termin+intervall-i] && (erster_Termin+intervall-i)%7!=0) {
				mögliche_termine.add(erster_Termin+intervall-i);
			}
			if (tage[erster_Termin+intervall+i] && (erster_Termin+intervall+i)%7!=0) {
				mögliche_termine.add(erster_Termin+intervall+i);
			}
			if (mögliche_termine.size()>=max_Terminevorschläge) {
				break;
			}
		}
    	if (mögliche_termine.size()==0) 
    	{
			System.out.println("Es gibt keine Termine, die in das Streufeld fallen.");
		}
    	for (int i = 0; i < mögliche_termine.size(); i++) {
			System.out.println("Der Tag: "+mögliche_termine.get(i)+" ist frei: "+tage[mögliche_termine.get(i)]);
		}
    	
    	
    	if (mögliche_termine.get(0)!=null) 
    	{
    		return mögliche_termine.get(0);
		}
    	else 
		{
    		return 0;
		}
        int days = 0;
        for (ICalendarEntryModel entry : allEntries.getAllRandomDates()) 
        {
            if (entry != null)            
                days += entry.getStartDate().get(Calendar.DAY_OF_YEAR);  
        }
        return days;
    }
}
