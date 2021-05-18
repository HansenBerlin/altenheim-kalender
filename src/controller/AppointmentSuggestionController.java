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

    	boolean[] days = new boolean[365];
    	int firstDate = 23;
    	int interval = 60;
    	int spread = 3;
    	int maxOffers = 4;
    	for (int i = 0; i < days.length; i++) 
    	{
			if (Math.random()<= 0.5) 
			{
				days[i]= true;
			}
			else 
			{
				days[i]=false;
				
			}
		}
    	LinkedList<Integer> possible_dates = new LinkedList<Integer>();
    	
    	for (int i = 0; i < spread; i++) 
    	{
			if (days[firstDate+interval-i] && (firstDate+interval-i)%7!=0 && (firstDate+interval-i)>=firstDate && (firstDate+interval-i)<= days.length) {
				possible_dates.add(firstDate+interval-i);
			}
			if (days[firstDate+interval+i] && (firstDate+interval+i)%7!=0 && (firstDate+interval+i)>=firstDate && (firstDate+interval+i)<= days.length) {
				possible_dates.add(firstDate+interval+i);
			}
			if (possible_dates.size()>=maxOffers) {
				break;
			}
		}
    	if (possible_dates.size()==0) 
    	{
			System.out.println("Es gibt keine Termine, die in das Streufeld fallen.");
		}
    	for (int i = 0; i < possible_dates.size(); i++) {
			System.out.println("Der Tag: "+possible_dates.get(i)+" ist frei: "+days[possible_dates.get(i)]);
		}
    	
    	
    	if (possible_dates.get(0)!=null) 
    	{
    		return possible_dates.get(0);
		}
    	else 
		{
    		return 0;
		}
    }

	@Override
	public void testFunctionTwo() {
		// TODO Auto-generated method stub
		
	}


    
}
