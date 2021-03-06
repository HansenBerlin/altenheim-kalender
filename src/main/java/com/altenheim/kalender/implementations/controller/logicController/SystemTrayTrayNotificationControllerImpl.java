package com.altenheim.kalender.implementations.controller.logicController;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.TrayIcon.MessageType;
import com.altenheim.kalender.interfaces.viewController.CalendarEntriesController;
import com.altenheim.kalender.interfaces.logicController.SystemTrayNotificationsController;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.calendarfx.model.Entry;

public class SystemTrayTrayNotificationControllerImpl extends TimerTask implements SystemTrayNotificationsController 
{
    private final SettingsModel settings;
    private final CalendarEntriesController administrateEntries;
    private TrayIcon trayIcon;

    public SystemTrayTrayNotificationControllerImpl(SettingsModel settings, CalendarEntriesController administrateEntries) 
    {
        this.settings = settings;
        this.administrateEntries = administrateEntries;
    }

    public void startNotificationTask() 
    {
        var timer = new Timer();
        timer.schedule(this, 0, settings.getNotificationTimeBeforeEntryInMinutes() * 60000);
    }

    public void run() 
    {
        prepareSystemMessagesForEntrys();
    }

    private void prepareSystemMessagesForEntrys() 
    {
        var start = LocalDateTime.now();
        var timeToAdd = settings.getNotificationTimeBeforeEntryInMinutes();
        var end = start.plusMinutes(settings.getEntrySystemMessageIntervalInMinutes());
        var entries = administrateEntries.getEntrysWithStartInSpecificRange(start, end.plusMinutes(timeToAdd));
        var currentEntries = new ArrayList<Entry<String>>();
        for (var entry : entries)
            if (entry.getStartAsLocalDateTime().isAfter(start.minusSeconds(1))
                    && entry.getStartAsLocalDateTime().isBefore(end.plusSeconds(1)))
                currentEntries.add(entry);
        outputSystemMessageForEntryList("Termin beginnt jetzt", currentEntries);

        currentEntries.clear();
        start = start.plusMinutes(timeToAdd);
        end = end.plusMinutes(timeToAdd);
        for (var entry : entries)
            if (entry.getStartAsLocalDateTime().isAfter(start.minusSeconds(1))
                    && entry.getStartAsLocalDateTime().isBefore(end.plusSeconds(1)))
                currentEntries.add(entry);
        outputSystemMessageForEntryList("Termin beginnt in " + (int) timeToAdd  + " Minuten", currentEntries);
    }

    private void outputSystemMessageForEntryList(String messageTitle, List<Entry<String>> entries) 
    {
        for (Entry<String> entry : entries) 
            trayIcon.displayMessage(messageTitle, entry.getTitle(), MessageType.INFO);
    }
    
    public boolean isSystemTrayAccessAllowed() 
    {
        if (SystemTray.isSupported()) 
        {
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Penaut.png"));

            var listener = new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    // Hier k??nnte man eine Ansicht im Kalender ??ffnen
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

            SystemTray tray = SystemTray.getSystemTray();
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
