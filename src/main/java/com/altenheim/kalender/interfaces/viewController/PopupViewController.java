package com.altenheim.kalender.interfaces.viewController;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.models.CalendarEntriesModel;
import javafx.scene.control.Button;
import javafx.stage.Window;

public interface PopupViewController
{
    void showPasswordCorrectConfirmationDialog();
    void showPasswordWrongDialog();
    void importDialog(EntryFactory entryFactory, Window stage);
    void exportDialog(CalendarEntriesModel allEntries, Window stage);
    void showEntryAddedDialogWithMailOption(String date, String dateEnd, String start, String end, Button sendMailButton);
    boolean isRevalidationWanted();
    void showCalendarImportedError();
    void showCalendarImportedSuccess();
    String showPasswordInputDialog();
    String showChooseCalendarNameDialog();
}
