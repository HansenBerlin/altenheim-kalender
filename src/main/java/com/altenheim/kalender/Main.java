package com.altenheim.kalender;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import com.altenheim.kalender.controller.logicController.ImportExportTest;

import net.fortuna.ical4j.data.ParserException;

public class Main  
{
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException, ParseException, ParserException 
    {
        //StartJFX.main(args);
        var testimpoerexport = new ImportExportTest();
        testimpoerexport.testExport();
    }
}
