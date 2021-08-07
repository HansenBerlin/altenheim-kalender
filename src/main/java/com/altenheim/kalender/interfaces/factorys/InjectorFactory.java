package com.altenheim.kalender.interfaces.factorys;

import com.altenheim.kalender.implementations.controller.viewController.MainWindowController;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.interfaces.viewController.GuiUpdateController;

public interface InjectorFactory
{
    GuiUpdateController getGuiController();
    InitialSetupController getInitialSettingsLoader();
    SettingsModel getSettingsModel();
    MainWindowController getMainWindowController();
    void createServices();
}
