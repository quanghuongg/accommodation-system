package com.accommodation.system.utils2;

import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimpleTimer {
    private long lastTime = System.currentTimeMillis();

    public SimpleTimer() {
    }

    public long getTimeAndReset() {
        long cur = System.currentTimeMillis();
        long time = cur - this.lastTime;
        this.lastTime = cur;
        return time;
    }

    public long getTime() {
        long cur = System.currentTimeMillis();
        long time = cur - this.lastTime;
        return time;
    }

    public static Pair<Date, Date> getDateRange() {
        Date begining, end;

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
        }

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
        }

        return Pair.of(begining, end);
    }

    public static Pair<Date, Date> getDateRangeForTotalData(int packageId, Date createAt, Date dateOfUpgrade) {
        Date dateFrom, dateTo;
        long day = 24 * 60 * 60 * 1000;
        Date now = new Date(System.currentTimeMillis());
        if (packageId == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createAt);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            calendar.set(Calendar.HOUR_OF_DAY, 0);
//            calendar.add(Calendar.DAY_OF_MONTH, 15);//fix RDNA-876
            dateFrom = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            dateTo = calendar.getTime();
        } else // Update Package -- Invite organization
        {
            if (Utils.isEmpty(dateOfUpgrade)) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateOfUpgrade);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            calendar.set(Calendar.HOUR_OF_DAY, 0);
            int month = (int) (((now.getTime() - calendar.getTime().getTime()) / day) / 30);
            calendar.add(Calendar.MONTH, month);
            dateFrom = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            dateTo = calendar.getTime();
        }

        return Pair.of(dateFrom, dateTo);
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public static Date getLastDateOfMonth(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar);
        return calendar.getTime();
    }

    public static Date getQueryDateFrom(Date timeToGet, Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        LocalDate timeToGetLocalDate = timeToGet.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentTimeLocalDate = currentTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (timeToGetLocalDate.getMonthValue() == currentTimeLocalDate.getMonthValue()) {
            return timeToGet;
        } else {
            calendar.setTime(timeToGet);
            calendar.add(Calendar.MONTH, currentTimeLocalDate.getMonthValue() - timeToGetLocalDate.getMonthValue());
            if (calendar.getTimeInMillis() > currentTime.getTime()) {
                calendar.add(Calendar.MONTH, -1);//Set month before current month
            }
        }
        return calendar.getTime();
    }

    public static boolean isTwoTimeInTheSameDate(Date time1, Date time2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(time1);
        calendar2.setTime(time2);

        if ((calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)) && (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR))) {
            return true;
        }

        return false;
    }

    public static Date getOneDayBefore(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
        return calendar.getTime();
    }
}
