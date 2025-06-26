package com.sc.util;

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Author chenjie
 * Date 2024/8/8
 * Desc
 */
public class DateUtils {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_PATTERN_NO_FMATE = "yyyyMMdd";
    public static final String DATE_PATTERN_NO_FMATE_TIME = "yyyyMMddHHmmss";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIMEMILLS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public DateUtils() {
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } else {
            return null;
        }
    }

    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        } else {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
            return fmt.parseLocalDateTime(strDate).toDate();
        }
    }

    public static DateTime stringToDateTime(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        } else {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
            return fmt.parseLocalDateTime(strDate).toDateTime();
        }
    }

    public static DateTime stringToLocalDateTime(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        } else {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
            return fmt.parseLocalDateTime(strDate).toDateTime();
        }
    }

    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));
        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(1);
    }

    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar)Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }

        calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), 0, 0, 0);
        calendar.set(14, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }

        calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), 23, 59, 59);
        calendar.set(14, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Date[] getMonthStartAndEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        Timestamp dayStartTime = getDayStartTime(calendar.getTime());
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        Timestamp dayEndTime = getDayEndTime(calendar.getTime());
        return new Date[]{dayStartTime, dayEndTime};
    }

    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(11, 0);
        todayStart.set(12, 0);
        todayStart.set(13, 0);
        todayStart.set(14, 0);
        return todayStart.getTime();
    }

    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(11, 23);
        todayEnd.set(12, 59);
        todayEnd.set(13, 59);
        todayEnd.set(14, 999);
        return todayEnd.getTime();
    }

    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayofweek = cal.get(7);
            if (dayofweek == 1) {
                dayofweek += 7;
            }

            cal.add(5, 2 - dayofweek);
            return getDayStartTime(cal.getTime());
        }
    }

    public static Date getLastWeekDay(Integer week) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dayOfWeek = cal.get(7);
        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }

        cal.add(5, 2 - dayOfWeek);
        Date date = cal.getTime();
        cal.setTime(date);
        cal.add(5, -1 * (8 - week));
        return cal.getTime();
    }

    public static String replaceLastWeek(String sql) {
        if (sql.contains(",lastweek")) {
            String preSql = sql.split(",lastweek")[0];
            String endSql = sql.split(",lastweek")[1];
            if (preSql.contains("${") && endSql.contains("}")) {
                String format = preSql.substring(preSql.lastIndexOf("${")).split("\\$\\{")[1];
                String week = endSql.split("\\}")[0];
                String replaceStr = "${" + format + ",lastweek" + week + "}";
                Date dateWeek = getLastWeekDay(Integer.valueOf(week));
                String dateStr = format(dateWeek, format);
                sql = sql.replace(replaceStr, dateStr);
            }
        }

        if (sql.contains(",lastweek")) {
            sql = replaceLastWeek(sql);
        }

        return sql;
    }

    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(7, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    public static String formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = ms / (long)dd;
        long hour = (ms - day * (long)dd) / (long)hh;
        long minute = (ms - day * (long)dd - hour * (long)hh) / (long)mi;
        Double second = (double)(ms - day * (long)dd - hour * (long)hh - minute * (long)mi) * 1.0D / (double)ss;
        if (day > 0L) {
            return day + "天" + hour + "小时" + minute + "分钟" + second.intValue() + "秒";
        } else if (hour > 0L) {
            return hour + "小时" + minute + "分钟" + second.intValue() + "秒";
        } else if (minute > 0L) {
            return minute + "分钟" + second.intValue() + "秒";
        } else {
            return second > 0.0D ? second.intValue() + "秒" : "0秒";
        }
    }

    public static String secToTime(Long time) {
        long day = 0L;
        long hour = 0L;
        long minute = 0L;
        long second = 0L;
        if (time <= 0L) {
            return "0";
        } else {
            int d = 86400;
            if (time > (long)d) {
                day = time / (long)d;
                time = time % (long)d;
            }

            if (time >= 3600L) {
                hour = time / 3600L;
                time = time % 3600L;
            }

            if (time >= 60L) {
                minute = time / 60L;
                second = time % 60L;
            }

            if (time < 60L) {
                second = time;
            }

            return day == 0L ? hour + "时" + minute + "分" + second + "秒" : day + "天" + hour + "时" + minute + "分" + second + "秒";
        }
    }

}
