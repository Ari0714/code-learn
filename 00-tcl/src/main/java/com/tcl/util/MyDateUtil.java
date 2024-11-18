package com.tcl.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author chenjie
 * Date 2024/5/15
 * Desc
 */
public class MyDateUtil {

    static Date date = new Date();

    /**
     * @desc yyyy-MM-dd HH:mm:ss
     * @return: java.lang.String
     */
    public static String getYYYYMMDD_HHMMSS() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * @desc yyyy-MM-dd
     * @return: java.lang.String
     */
    public static String getYYYYMMDD() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * @desc yyyy-MM-dd - -1
     * @return: java.lang.String
     */
    public static String getYYYYMMDDSub1() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format((date.getTime() / 1000 - 86400) * 1000);
    }

    /**
     * @desc 时间戳
     * @return: java.lang.String
     */
    public static Long getTS() {
        return date.getTime();
    }


    /**
     * @desc get specific days... between 【startDate，endDate】
     * @param: startDate
     * @param: endDate
     * @return: java.util.List<java.time.LocalDate>
     */
    private static List<String> getDatesBetween(LocalDate startDate, LocalDate endDate, String unit) {
        List<String> dateList = new ArrayList<>();
        LocalDate currentDate = startDate;

        if ((int) ChronoUnit.DAYS.between(startDate, endDate) < 1) {
            throw new RuntimeException("自动拼接需大于等于1个单位长度");
        }

        if ("day".equals(unit)) {
            // 使用循环逐日生成日期并添加到列表
            while (!currentDate.isAfter(endDate)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dateList.add(currentDate.format(formatter));
                currentDate = currentDate.plusDays(1);
            }
        } else if ("month".equals(unit)) {
            // 使用循环逐日生成日期并添加到列表
            while (!currentDate.isAfter(endDate)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                dateList.add(currentDate.format(formatter));
                currentDate = currentDate.plusMonths(1);
            }
        } else if ("year".equals(unit)) {
            // 使用循环逐日生成日期并添加到列表
            while (!currentDate.isAfter(endDate)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
                dateList.add(currentDate.format(formatter));
                currentDate = currentDate.plusYears(1);
            }
        } else {
            throw new RuntimeException("please input valid timeUnit ！");
        }

        return dateList;
    }

    public static void main(String[] args) {

//        System.out.println(getYYYYMMDDSub1());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse("2022-11-11", formatter);
        LocalDate endDate = LocalDate.parse("2023-11-15", formatter);


        getDatesBetween(startDate, endDate, "year").forEach(x -> System.out.println(x));

    }


}
