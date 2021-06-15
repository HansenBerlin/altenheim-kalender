package com.altenheim.kalender.controller.logicController;

import java.util.List;
import com.altenheim.kalender.interfaces.IAppointmentEntryFactory;
import com.altenheim.kalender.interfaces.IExportController;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;

public class ExportController extends IOController implements IExportController
{

    public ExportController(IAppointmentEntryFactory administrateEntries, List<ContactModel> allContacts, 
        SettingsModel settings, List<MailTemplateModel> mailTemplates) 
    {
        super(administrateEntries, allContacts, settings, mailTemplates);
    }    
}
