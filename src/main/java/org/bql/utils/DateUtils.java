package org.bql.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static long currentTime() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public static int currentDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date tem = new Date();
        String to = format.format(tem);
        return Integer.parseInt(to);
    }

    private static Calendar dayEndCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 24);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c;
    }


    /**
     * 当天结束时间
     * @return
     */
    public static int dayEnd() {
        Date date = dayEndCalendar().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String from = format.format(date);
        int to = Integer.parseInt(from);
        return to;
    }

    public static long dayEndMillis() {
        return dayEndCalendar().getTimeInMillis();
    }

    /*
     *当前天+7天
     */
    private static Calendar weekEndCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 7);
        return c;
    }

    /**
     * 今天是星期几
     *
     * @return
     */
    public static int getTodayOnWeek() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int week() {
        Date date = weekEndCalendar().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String from = format.format(date);
        int to = Integer.parseInt(from);
        return to;
    }

    public static long weekMillis() {
        return weekEndCalendar().getTimeInMillis();
    }

    private static Calendar monthEndCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONDAY), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 24);
        return c;
    }

    public static int month() {
        Date date = monthEndCalendar().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String from = format.format(date);
        int to = Integer.parseInt(from);
        return to;
    }

    public static long monthEndMllis() {
        return monthEndCalendar().getTimeInMillis();
    }

    public static int getTime(String date) {
        int time = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat to = new SimpleDateFormat("yyyyMMdd");
            Date date1 = format.parse(date);
            String timeStr = to.format(date1);
            time = Integer.parseInt(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static long getTimeMillis(String date) {
        long time = 0;
        try {
            time = 0L;//org.apache.commons.lang3.time.DateUtils.parseDate(date,"yyyy-MM-dd").getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * amount秒之后
     *
     * @return
     */
    public static long getFutureTimeMillis(int amount) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, amount);
        return c.getTimeInMillis();
    }

    /**
     * 计算时间差
     */
    public static void leadTimer() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date tem = new Date();
//        String to = format.format(tem);
        String fromDate = format.format("2018-04-20 08:00");
        String toDate = format.format(tem);
        long from = format.parse(fromDate).getTime();
        long to = format.parse(toDate).getTime();
        int hours = (int) ((to - from)/(1000 * 60 * 60));
        System.out.println(hours);

    }

    public static void main(String[] args) throws ParseException {
        long start = 1524189654727L;
        long end = currentTime();
        System.out.println((end - start)/(1000 * 60));
    }
}
