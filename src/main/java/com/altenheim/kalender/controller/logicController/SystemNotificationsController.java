package com.altenheim.kalender.controller.logicController;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.TrayIcon.MessageType;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.ISystemNotificationsController;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.Entry;

public class SystemNotificationsController extends TimerTask implements ISystemNotificationsController  
{
    private SettingsModel settings;
    private ICalendarEntriesModel administrateEntries;

    private SystemTray tray = SystemTray.getSystemTray();
    private TrayIcon trayIcon;
   
    public SystemNotificationsController(SettingsModel settings, ICalendarEntriesModel administrateEntries)
    {
        this.settings = settings;
        this.administrateEntries = administrateEntries;
    }

    public void startNotificationTask()
    {
        var timer = new Timer();
        timer.schedule(this, 0, settings.getEntrySystemMessageIntervalInMills());
    }

    public void run()
    {
        prepareSystemMessagesForEntrys();
    }    
    
    private void prepareSystemMessagesForEntrys() 
    {
        var start = LocalDateTime.now();
        var end = start.plusSeconds(settings.getEntrySystemMessageIntervalInMills()/1000);
        var entrys = administrateEntries.getEntrysWithStartInSpecificRange(start, end);
        outputSystemMessageForEntryList("Termin beginnt jetzt", entrys);

        var timeToAdd = settings.getnotificationTimeBeforeEntryInMillis()/1000;
        start = start.plusSeconds(timeToAdd);
        end = end.plusSeconds(timeToAdd);
        entrys = administrateEntries.getEntrysWithStartInSpecificRange(start, end);
        outputSystemMessageForEntryList("Termin beginnt in "+(int)timeToAdd/60+" Minuten", entrys);   
    }

    private void outputSystemMessageForEntryList(String messageTitle, List<Entry<?>> entries) 
    {
        for (Entry<?> entry : entries) 
        {
            trayIcon.displayMessage(messageTitle, entry.getTitle(), MessageType.INFO);
        }
    }
    
    public void outputSystemMessage(String title, String message) 
    {
        trayIcon.displayMessage(title, message, MessageType.INFO);
    }
    
    public boolean initializeSystemTrayAccess() 
    {
        if (SystemTray.isSupported()) 
        {
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("Images/Penaut.ico"));
            
            var listener = new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    //Hier könnte man eine Ansicht im Kalender öffnen                
                }
            };

            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem();
            defaultItem.addActionListener(listener);
            popup.add(defaultItem);

            trayIcon = new TrayIcon(image, "Prof. Penaut");
            trayIcon.setPopupMenu(popup);
            trayIcon.addActionListener(listener);
            trayIcon.setToolTip("HWR Kalender");
            trayIcon.setImageAutoSize(true);

            try 
            {
                tray.add(trayIcon);
                return true;
            } catch (AWTException e) 
            {
                return false;
            }
        }
        return false;
    }
    
}
