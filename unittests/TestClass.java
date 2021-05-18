import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import controller.AppointmentEntryFactory;
import java.util.Calendar;

public class TestClass 
{
    @Test
    void testBaseCase() 
    {
        var appointmentFactory = new AppointmentEntryFactory();
        var result = appointmentFactory.createDefinedEntry(new int[]{2000,1,1}, 
            new int[]{2000,1,1}, new int[]{10,00},new int[]{12,00}, "test");
        
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
