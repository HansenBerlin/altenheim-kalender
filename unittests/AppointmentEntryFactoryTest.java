import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import controller.AppointmentEntryFactory;
import java.util.Calendar;

public class AppointmentEntryFactoryTest 
{
    @Test
    void createEntries_twoArrays_arraysShouldDiffer()
    {
        var appointmentFactory = new AppointmentEntryFactory();
        var arrayOne = appointmentFactory.createEntrys();
        var arrayTwo = appointmentFactory.createEntrys();

        Assert.assertNotEquals(arrayOne, arrayTwo);        
    }

    @Test
    void createEntries_noParams_arrayShouldNotContainNullObjects()
    {
        boolean isNull = false;
        var appointmentFactory = new AppointmentEntryFactory();
        var arrayOne = appointmentFactory.createEntrys();

        for (var entry : arrayOne) 
        {
            if (entry == null)
                isNull = true;            
        }

        Assert.assertFalse(isNull);    
    }

    @Test
    void createDefinedEntry_standardDates_accordingDatesInDateObjects() 
    {
        var appointmentFactory = new AppointmentEntryFactory();
        var result = appointmentFactory.createDefinedEntry(new int[]{2000,1,1}, 
            new int[]{2000,1,1}, new int[]{10,00},new int[]{12,00}, "test", 0);
        
        Assertions.assertAll(            
                        () -> assertEquals("test", result.getAppointmentEntryName()),
                        () -> assertEquals(1, result.getStartDate().get(Calendar.DAY_OF_YEAR)),
                        () -> assertEquals(1, result.getStartDate().get(Calendar.MONTH)+1),
                        () -> assertEquals(2000, result.getStartDate().get(Calendar.YEAR)),
                        () -> assertEquals(10, result.getStartDate().get(Calendar.HOUR_OF_DAY)),
                        () -> assertEquals(00, result.getStartDate().get(Calendar.MINUTE)),
                        () -> assertEquals(1, result.getEndDate().get(Calendar.DAY_OF_YEAR)),
                        () -> assertEquals(1, result.getEndDate().get(Calendar.MONTH)+1),
                        () -> assertEquals(2000, result.getEndDate().get(Calendar.YEAR)),
                        () -> assertEquals(12, result.getEndDate().get(Calendar.HOUR_OF_DAY)),
                        () -> assertEquals(00, result.getEndDate().get(Calendar.MINUTE))
                    );    
    }   
}
