package com.altenheim.kalender.interfaces.factorys;


import com.altenheim.kalender.controller.logicController.InitialSetupController;
import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.models.SettingsModelImpl;

public interface InjectorFactory
{
    GuiUpdateController getGuiController();
    InitialSetupController getInitialSettingsLoader();
    SettingsModelImpl getSettingsModel();
    MainWindowController getMainWindowController();
    void createServices();
}
