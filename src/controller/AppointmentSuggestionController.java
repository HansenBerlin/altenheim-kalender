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
        
        // �bergebens Interface beinhaltet Arrays f�r random generierte Termine
        // und f�r jeden 7ten Tag ('Sonntag') Kannste dir mit den ensprechenden Methoden
        // komplett rausholen oder nur spezifische Tage
    }

    public int testFunction()
    {

    	boolean[] days = new boolean[365];
    	int firstDate = 23;
    	int interval = 60;
    	int spread = 2;
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
    	LinkedList<Integer> m�gliche_termine = new LinkedList<Integer>();
    	
    	for (int i = 0; i < spread; i++) 
    	{
			if (days[firstDate+interval-i] && (firstDate+interval-i)%7!=0 && (firstDate+interval-i)>=firstDate && (firstDate+interval-i)<= days.length) {
				m�gliche_termine.add(firstDate+interval-i);
			}
			if (days[firstDate+interval+i] && (firstDate+interval+i)%7!=0 && (firstDate+interval+i)>=firstDate && (firstDate+interval+i)<= days.length) {
				m�gliche_termine.add(firstDate+interval+i);
			}
			if (m�gliche_termine.size()>=maxOffers) {
				break;
			}
		}
    	if (m�gliche_termine.size()==0) 
    	{
			System.out.println("Es gibt keine Termine, die in das Streufeld fallen.");
		}
    	for (int i = 0; i < m�gliche_termine.size(); i++) {
			System.out.println("Der Tag: "+m�gliche_termine.get(i)+" ist frei: "+days[m�gliche_termine.get(i)]);
		}
    	
    	
    	if (m�gliche_termine.get(0)!=null) 
    	{
    		return m�gliche_termine.get(0);
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
