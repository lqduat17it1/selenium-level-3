package utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateTimeUtils {

    public static LocalDate getNextFriday() {
        return LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
    }

}
