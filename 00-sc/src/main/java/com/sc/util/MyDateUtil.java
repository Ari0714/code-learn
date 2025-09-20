package com.sc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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


    public static void main(String[] args) {

//        System.out.println(getYYYYMMDDSub1());

    }


}
