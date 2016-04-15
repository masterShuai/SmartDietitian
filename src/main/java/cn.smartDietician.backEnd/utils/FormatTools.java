package cn.smartDietician.backEnd.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/5/19.
 */
public class FormatTools {
    public static boolean IsNumber(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();

    }

    public static Date getYesterdayZero(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getTomorrowZero(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDateZero(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //
    public static Date parseDate(String dateString, Date failSafeDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStr = null;
        try {
            dateStr = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            formatter = new SimpleDateFormat("yyyy/MM/dd");
            dateStr = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateStr == null)
            dateStr = failSafeDate;
        return dateStr;
    }

    /**
     * 默认格式化格式为:yyyy.MM.dd hh:mm
     *
     * @param datetime
     * @return
     */
    public static String formatDatetime(Date datetime) {
        return formatDatetime(datetime, "yyyy.MM.dd hh:mm");
    }

    public static String formatShortDatetime(Date datetime) {
        return formatDatetime(datetime, "yyyy-MM-dd");
    }

    public static String formatMondaySortDatetime() {
        Calendar cal = Calendar.getInstance();
        System.out.println("今天的日期: " + cal.getTime());

        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
        cal.add(Calendar.DATE, -day_of_week);
        return formatShortDatetime(cal.getTime());
    }

    public static String formatDatetime(Date datetime, String pattern) {
        if (datetime == null) {
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(datetime);
    }

    public static Date parseDatetime(String datetime, String pattern) {
        if (datetime == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        try {
            return formatter.parse(datetime);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDatetime(String datetime) {
        return parseDatetime(datetime, "yyyy.MM.dd hh:mm");
    }
}
