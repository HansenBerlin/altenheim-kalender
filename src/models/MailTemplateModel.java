package models;

public class MailTemplateModel 
{
    public String getTemplateOne()
    {
        return "Sehr geehrter Typ von nebenan,\nich hätte gerne einen Termin am" +
            "[Datum] um [Uhrzeit].\nVielen Dank und mit freundlichem Gruß\nDein Stalker";
    }

    public String getTemplateTwo()
    {
        return "Sehr geehrte Frau Merkel,\ndanke für 16 Jahre Stillstand Bitte starten Sie" +
            "am [Datum] um [Uhrzeit] mit ihrer Arbeit als Bundeskanzlerin.\nVielen Dank und mit freundlichem Gruß\nEin Bürger";
    }
    
}
