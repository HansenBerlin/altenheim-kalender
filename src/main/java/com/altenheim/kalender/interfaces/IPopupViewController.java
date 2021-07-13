package com.altenheim.kalender.interfaces;

import javafx.stage.Window;

public interface IPopupViewController 
{
    public boolean isRevalidationWanted();
    public void showConfirmationDialog();
    public void showCancelDialog();    
    public String showPasswordInputDialog();
    public void importDialog(IImportController importController, IEntryFactory entryFactory, Window stage);
    public void exportDialog(IExportController exportController, ICalendarEntriesModel allEntries, Window stage);    
}
