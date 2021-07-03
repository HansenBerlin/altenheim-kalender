package com.altenheim.kalender.controller.logicController;

import java.awt.*;
import java.awt.event.*;
import java.awt.TrayIcon.MessageType;

public class SystemNotificationsController {

    private SystemTray tray = SystemTray.getSystemTray();
    private TrayIcon trayIcon = null;
   
    public void outputSystemMessage() {
        trayIcon.displayMessage("Hello120", "test", MessageType.NONE);
    }
    
    public boolean initializeSystemTrayAccess() {
        if (SystemTray.isSupported()) {
            Image image = Toolkit.getDefaultToolkit().getImage("Images/Penaut.ico");
            
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                     //Hier könnte man eine Ansicht im Kalender öffnen
                     System.out.println("test");                  
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
            } catch (AWTException e) {
                return false;
            }
            return true;
        }
        return false;
        
    }
    
}
