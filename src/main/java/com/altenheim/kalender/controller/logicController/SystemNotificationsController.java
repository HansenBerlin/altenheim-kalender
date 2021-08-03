package com.altenheim.kalender.controller.logicController;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import java.awt.TrayIcon.MessageType;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.ISystemNotificationsController;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.Entry;

public class SystemNotificationsController implements ISystemNotificationsController {
    private SettingsModel settings;
    private ICalendarEntriesModel administrateEntries;

    private TrayIcon trayIcon;

    public SystemNotificationsController(SettingsModel settings, ICalendarEntriesModel administrateEntries) {
        this.settings = settings;
        this.administrateEntries = administrateEntries;
    }

    public void prepareSystemMessagesForEntrys() {
        var start = LocalDateTime.now();
        var timeToAdd = settings.notificationTimeBeforeEntryInMinutes;
        var end = start.plusMinutes(settings.entrySystemMessageIntervalInMinutes);
        var entries = administrateEntries.getEntrysWithStartInSpecificRange(start, end.plusMinutes(timeToAdd));
        var currentEntries = new ArrayList<Entry<String>>();
        for (var entry : entries)
            if (entry.getStartAsLocalDateTime().isAfter(start.minusSeconds(1))
                    && entry.getStartAsLocalDateTime().isBefore(end.plusSeconds(1)))
                currentEntries.add((Entry<String>) entry);
        outputSystemMessageForEntryList("Termin beginnt jetzt", currentEntries);

        currentEntries.clear();
        start = start.plusMinutes(timeToAdd);
        end = end.plusMinutes(timeToAdd);
        for (var entry : entries)
            if (entry.getStartAsLocalDateTime().isAfter(start.minusSeconds(1))
                    && entry.getStartAsLocalDateTime().isBefore(end.plusSeconds(1)))
                currentEntries.add((Entry<String>) entry);
        outputSystemMessageForEntryList("Termin beginnt in " + (int) timeToAdd  + " Minuten", currentEntries);
    }

    private void outputSystemMessageForEntryList(String messageTitle, List<Entry<String>> entries) {
        for (Entry<String> entry : entries) {
            trayIcon.displayMessage(messageTitle, entry.getTitle(), MessageType.INFO);
        }
        trayIcon.displayMessage(messageTitle, "test", MessageType.INFO);
    }
    
    public boolean initializeSystemTrayAccess() {
        if (SystemTray.isSupported()) {
            Image image;
            
            try {
                image = ImageIO.read(new File("src/main/resources/Penaut.png"));
            } catch (IOException e1) {
                image = null;
                e1.printStackTrace();
            }

            var listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Hier könnte man eine Ansicht im Kalender öffnen
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
            try {
                tray.add(trayIcon);
                return true;
            } catch (AWTException e) {
                return false;
            }
        }
        return false;

    }

}
