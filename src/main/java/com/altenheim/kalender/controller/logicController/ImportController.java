package com.altenheim.kalender.controller.logicController;

import java.util.List;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;

public class ImportController extends IOController implements IImportController
{
    private ICalendarEntriesModel allEntries;

    public ImportController(ICalendarEntriesModel allEntries, IAppointmentEntryFactory administrateEntries, 
        List<ContactModel> allContacts, SettingsModel settings, List<MailTemplateModel> mailTemplates)
    {
        super(administrateEntries, allContacts, settings, mailTemplates);
        this.allEntries = allEntries;
    }
    
}
