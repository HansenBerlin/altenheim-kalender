package com.altenheim.kalender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Calendar;

import com.altenheim.kalender.controller.logicController.AppointmentEntryFactory;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SmartSearchControllerTest 
{
    @Test
    void findAvailableTimeSlot_noPossibleSolution_shouldReturnEmptyList()
    {
        var mockCalenderModels = new ICalendarEntriesModel();
        
        var controller = new SmartSearchControllerTest();
    }
}
