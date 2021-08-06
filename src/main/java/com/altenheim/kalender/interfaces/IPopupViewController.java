package com.altenheim.kalender.interfaces;

import javafx.scene.control.Button;
import javafx.stage.Window;

public interface IPopupViewController 
{
    void showConfirmationDialog();
    void showCancelDialog();
    void importDialog(IEntryFactory entryFactory, Window stage);
    void exportDialog(ICalendarEntriesModel allEntries, Window stage);
    void showEntryAddedDialogWithMailOption(String date, String dateEnd, String start, String end, String title, Button sendMailButton);
    boolean isRevalidationWanted();
    String showPasswordInputDialog();
    String showChooseCalendarNameDialog();
}
