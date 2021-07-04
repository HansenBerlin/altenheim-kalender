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

public class SystemNotificationsController extends TimerTask implements ISystemNotificationsController  {
    private SettingsModel settings;
    private ICalendarEntriesModel administrateEntries;

    private SystemTray tray = SystemTray.getSystemTray();
    private TrayIcon trayIcon = null;
   
    public void startScraperTask()
    {
        var timer = new Timer();
        timer.schedule(this, 0, settings.getEntrySystemMessageIntervalInMills());
    }

    public void run()
    {
        outputEntrysSystemMessage();
    }    
    
    public void outputEntrysSystemMessage() {
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

    private void outputSystemMessageForEntryList(String title, List<Entry<?>> entrys) {
        for (Entry<?> entry : entrys) {
            trayIcon.displayMessage(title, entry.getTitle(), MessageType.INFO);
        }
    }
    
    public void outputSystemMessage(String title, String message) {
        trayIcon.displayMessage(title, message, MessageType.INFO);
    }
    
    public boolean initializeSystemTrayAccess() {
        boolean output = false;
        if (SystemTray.isSupported()) {
            Image image = Toolkit.getDefaultToolkit().getImage("Images/Penaut.ico");
            
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
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

            try {
                tray.add(trayIcon);
                output = true;
            } catch (AWTException e) {
                output = false;
            }
        }
        return output;
        
    }
    
}
