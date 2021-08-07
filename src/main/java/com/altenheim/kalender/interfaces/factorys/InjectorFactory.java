package com.altenheim.kalender.interfaces.factorys;

import com.altenheim.kalender.implementations.controller.viewController.MainWindowController;
import com.altenheim.kalender.interfaces.viewController.GuiUpdateController;

public interface InjectorFactory
{
    GuiUpdateController getGuiController();
    InitialSetupController getInitialSettingsLoader();
    MainWindowController getMainWindowController();
    void createServices();
}
