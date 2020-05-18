package com.accommodation.system.utils2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String getCurrentFormatDate() {

        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(dateTimeFormat);
    }

    public static Date getStartTimeToday() {
        LocalDate localDate = LocalDate.now();
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getEndTimeToday() {
        LocalDate localDate = LocalDate.now();
        return Date.from(localDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getTimeBeforeToday(int days) {
        LocalDate localDate = LocalDate.now().minusDays(days);
        localDate.atTime(0, 0, 0);
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
