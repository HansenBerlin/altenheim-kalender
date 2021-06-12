package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.IExportController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.ISettingsController;

public class SettingsController implements ISettingsController
{
    private IIOController ioController;
    private IExportController export;

    public SettingsController(IIOController ioController, IExportController export)
    {
        this.ioController = ioController;
        this.export = export;
    }    
}
