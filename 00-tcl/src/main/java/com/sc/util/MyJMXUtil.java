package com.sc.util;

/**
 * Author Ari
 * Date 2024/8/8
 * Desc
 */
import cn.hutool.core.collection.CollectionUtil;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyJMXUtil {

    public static void main(String[] args) {

        String sql  = "${yyyy-MM}";

        if (sql.contains("${yyyyMMend}")) {
            String s = DateUtils.format(DateUtils.addDateMonths(new Date(), 1), "yyyyMM") + "01";
            String monthEnd = DateUtils.format(DateUtils.addDateDays(
                    DateUtils.stringToDate(s, "yyyyMMdd"), -1), "yyyyMMdd");
            sql = sql.replace("${yyyyMMend}", monthEnd);
        }
        if (sql.contains("${yyyy-MM-end}")) {
            String s = DateUtils.format(DateUtils.addDateMonths(new Date(), 1), "yyyyMM") + "01";
            String monthEnd = DateUtils.format(DateUtils.addDateDays(
                    DateUtils.stringToDate(s, "yyyyMMdd"), -1), "yyyy-MM-dd");
            sql = sql.replace("${yyyy-MM-end}", monthEnd);
        }
        if (sql.contains(",lastweek")) {
            sql = DateUtils.replaceLastWeek(sql);
        }
        if (sql.contains("${timestamp13}")) {
            long millis = System.currentTimeMillis();
            sql = sql.replace("${timestamp13}", String.valueOf(millis));
        }
        if (sql.contains("${timestamp10}")) {
            long millis = System.currentTimeMillis();
            String millis10 = String.valueOf(millis).substring(0, 10);
            sql = sql.replace("${timestamp10}", String.valueOf(millis10));
        }

        //替换${yyyy-n}${MM-n}${end}
        String reg = "\\$\\{yyyy-\\d{1,2}\\}\\$\\{MM-\\d{1,2}\\}\\$\\{end\\}";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            sql=replaceByYearAndMonth(sql,matcher,"yyyyMMdd");
        }
        //替换${yyyy-n}-${MM-n}-${end}
        String reg1 = "\\$\\{yyyy-\\d{1,2}\\}-\\$\\{MM-\\d{1,2}\\}-\\$\\{end\\}";
        Pattern pattern1 = Pattern.compile(reg1);
        Matcher matcher1 = pattern1.matcher(sql);
        while (matcher1.find()) {
            sql=replaceByYearAndMonth(sql,matcher1,"yyyy-MM-dd");
            System.out.println("matched");
            System.out.println(sql);
        }
        if (sql.contains("${yyyymmdd}")) {
            String s = DateUtils.format(new Date(), "yyyyMMdd");
            sql = sql.replace("${yyyymmdd}", s);
        }
        if (sql.contains("${yyyy-mm-dd}")) {
            String s = DateUtils.format(new Date(), "yyyy-MM-dd");
            sql = sql.replace("${yyyy-mm-dd}", s);
        }

//        System.out.println(sql);



        //date_source0: ${yyyy-mm-dd-1}
        //date_source1: ${yyyy-mm-dd-1}
        //date_source2: ${yyyy-mm-dd-1}
        //unit: dd
        //${yyyy-mm-dd-1}=2024-11-10
        //2024-11-10

        //date_source0: ${yyyy-mm-dd+1}
        //date_source1: ${yyyy-mm-dd-1}
        //date_source2: ${yyyy-mm-dd-1}
        //unit: dd
        //${yyyy-mm-dd-1}=2024-11-12
        //${yyyy-mm-dd+1}
        System.out.println(wapper("${yyyy-mm-dd+1}", "2024-11-11 11:11:11"));
        System.out.println(wapper("${yyyy-mm-dd-1}", "2024-11-11 11:11:11"));
        System.out.println(wapper("${yyyy-mm+1}", "2024-11-11 11:11:11"));
        System.out.println(wapper("${yyyy-mm-1}", "2024-11-11 11:11:11"));

////资源文件的替换
//    sql = repaceAllRescores(sql);


    }


    private static String wapper(String value, String nowTime) {
        //是否是内置的数据
        switch (value) {
            case "$cyctime": {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                return sdf.format(new Date());
            }
            case "$gmtdate": {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                return sdf.format(new Date());
            }
            case "$bizdate": {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                long data = new Date().getTime() - 24 * 60 * 60 * 1000L;
                return sdf.format(new Date(data));
            }
//            case "$jobid": {
//                return jobid.toString();
//            }
//            case "$taskid": {
//                return taskid;
//            }
//            case "$depid": {
//                return null == depid ? "" : depid.toString();
//            }
            case "${bdp.system.cyctime}": {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                return sdf.format(new Date());
            }
            case "${bdp.system.bizdate}": {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                long data = new Date().getTime() - 24 * 60 * 60 * 1000L;
                return sdf.format(new Date(data));
            }
            default: {
                //先替换表达式${yyyy-?} 或者${yyyy}
                String reg = "\\$\\{yyyy-\\d{1,2}\\}\\$\\{MM-\\d{1,2}\\}\\$\\{end\\}";
                Pattern pattern = Pattern.compile(reg);
                Matcher matcher = pattern.matcher(value);
                while (matcher.find()) {
                    return value;
                }
                reg = "\\$\\{yyyy-\\d{1,2}\\}-\\$\\{MM-\\d{1,2}\\}-\\$\\{end\\}";
                pattern = Pattern.compile(reg);
                matcher = pattern.matcher(value);
                while (matcher.find()) {
                    return value;
                }
                return replaceDateTime(value, nowTime);
            }
        }
    }


    public static String replaceDateTime(String sql_task, String date) {
        if (!date.equals("") && null != date) {
            Map<String, String> dp = doFillMap(sql_task, date);
            if (!CollectionUtil.isNotEmpty(dp)) {
                return sql_task;
            } else {
                String sql = sql_task;

                String d;
                for(Iterator var4 = dp.keySet().iterator(); var4.hasNext(); sql = sql.replace(d, (CharSequence)dp.get(d))) {
                    d = (String)var4.next();
                }

                return sql;
            }
        } else {
            return sql_task;
        }
    }

    public static Map<String, String> doFillMap(String sql_task, String date) {
        Map<String, String> fillMap = new HashMap();
        Pattern pattern = Pattern.compile("(\\$\\{y{0,4}[:/-]*q{0,2}[:/-]*m{0,2}[:/-]*w{0,2}[:/-]*d{0,2}[: -]*h{0,2}[:-]*(mi)?[:-]*(ss)?[.]*(ms)?([- +](\\d)+?)*?\\})");

        String date_source;
        String datestr;
        DateTime dt;
        for(Matcher m = pattern.matcher(sql_task); m.find(); fillMap.put(m.group(1), replaceFromdt(dt, datestr))) {

            date_source = m.group(1);
//            System.out.println("date_source0: "+ date_source);

            boolean isDatePlus = false;
            if (date_source.contains("+")){
                isDatePlus = true;
                date_source = date_source.replace("+","-");
            }

//            System.out.println("date_source1: "+ date_source);
            String[] strs = date_source.split("-");
//            Arrays.asList(strs).forEach(x -> System.out.println(x));
            dt = DateUtils.stringToDateTime(date, "yyyy-MM-dd HH:mm:ss");
            if (strs.length > 0 && (isNumeric(strs[strs.length - 1].replace("}", "")))) {
//                System.out.println("date_source2: "+ date_source);
                Integer lastIndexOf = date_source.lastIndexOf("-");
                String unit = date_source.substring(lastIndexOf - 2, lastIndexOf);
                Integer number = Integer.parseInt(strs[strs.length - 1].replace("}", ""));
                datestr = date_source.substring(date_source.indexOf("{") + 1, lastIndexOf);
                byte var13 = -1;
//                System.out.println("unit: "+unit);
                switch(unit.hashCode()) {
                    case 3200:
                        if (unit.equals("dd")) {
                            var13 = 4;
                        }
                        break;
                    case 3328:
                        if (unit.equals("hh")) {
                            var13 = 5;
                        }
                        break;
                    case 3484:
                        if (unit.equals("mi")) {
                            var13 = 6;
                        }
                        break;
                    case 3488:
                        if (unit.equals("mm")) {
                            var13 = 2;
                        }
                        break;
                    case 3494:
                        if (unit.equals("ms")) {
                            var13 = 8;
                        }
                        break;
                    case 3616:
                        if (unit.equals("qq")) {
                            var13 = 1;
                        }
                        break;
                    case 3680:
                        if (unit.equals("ss")) {
                            var13 = 7;
                        }
                        break;
                    case 3808:
                        if (unit.equals("ww")) {
                            var13 = 3;
                        }
                        break;
                    case 3872:
                        if (unit.equals("yy")) {
                            var13 = 0;
                        }
                }

                int changeDate;
                if (isDatePlus){
                    changeDate = number;
                }else {
                    changeDate = 0 - number;
                }
                switch(var13) {
                    case 0:
                        dt = dt.plusYears(changeDate);
                        break;
                    case 1:
                        dt = dt.plusMonths(changeDate * 3);
                        break;
                    case 2:
                        dt = dt.plusMonths(changeDate);
                        break;
                    case 3:
                        dt = dt.plusWeeks(changeDate);
                        break;
                    case 4:
                        dt = dt.plusDays(changeDate);
                        break;
                    case 5:
                        dt = dt.plusHours(changeDate);
                        break;
                    case 6:
                        dt = dt.plusMinutes(changeDate);
                        break;
                    case 7:
                        dt = dt.plusSeconds(changeDate);
                        break;
                    case 8:
                        dt = DateUtils.stringToDateTime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"), "yyyy-MM-dd HH:mm:ss.SSS");
                        dt = dt.plusMillis(0 - number);
                }

            } else {
                datestr = date_source.substring(date_source.indexOf("{") + 1, date_source.lastIndexOf("}"));
            }
        }
//
//        for (Map.Entry<String, String> stringStringEntry : fillMap.entrySet()) {
//            System.out.println(stringStringEntry);
//        }

        return fillMap;



    }

    public static String replaceFromdt(DateTime dt, String str) {
        Integer q = dt.monthOfYear().get() / 3;
        if (dt.monthOfYear().get() % 3 > 0) {
            q = q + 1;
        }

        DateTime mdt = DateUtils.stringToDateTime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"), "yyyy-MM-dd HH:mm:ss.SSS");
        String re = str.replace("yyyy", fillZero(dt.year().getAsString())).replace("mm", fillZero(dt.monthOfYear().getAsString())).replace("qq", fillZero(q.toString())).replace("ww", fillZero(dt.weekOfWeekyear().getAsString())).replace("dd", fillZero(dt.dayOfMonth().getAsString())).replace("hh", fillZero(dt.hourOfDay().getAsString())).replace("mi", fillZero(dt.minuteOfHour().getAsString())).replace("ss", fillZero(dt.secondOfMinute().getAsString())).replace("ms", fillZero(mdt.millisOfSecond().getAsString()));
        return re;
    }

    public static String fillZero(String str) {
        return str.length() == 1 ? "0" + str : str;
    }

    public static Boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public void main(List<String> args) {
        if (args.size() != 2) {
            throw new RuntimeException("参数不对，参数应该为：script  date  \n 如：,${yyyy},${mm-1} 2017-08-02 03:10:00");
        } else {
            String res = replaceDateTime((String)args.get(0), (String)args.get(1));
            System.out.println(res);
        }
    }

    private static String replaceByYearAndMonth(String sql,Matcher matcher,String dateFormat){
        //匹配需要替换的字符串
        String dateMatch = matcher.group();
        //获取年份中的n
        String yearStr="yyyy-\\d{1,2}";
        Pattern yearPattern = Pattern.compile(yearStr);
        Matcher yearMatcher = yearPattern.matcher(dateMatch);
        Integer year=0;
        while (yearMatcher.find()) {
            String yearMatch = yearMatcher.group();
            year=Integer.valueOf(yearMatch.split("-")[1]);
        }
        //获取月份中的n
        String monthStr="MM-\\d{1,2}";
        Pattern monthPattern = Pattern.compile(monthStr);
        Matcher monthMatcher = monthPattern.matcher(dateMatch);
        Integer month=0;
        while (monthMatcher.find()) {
            String monthMatch = monthMatcher.group();
            month=Integer.valueOf(monthMatch.split("-")[1]);
        }
        //获取yyyy-n年
        Calendar rightNowDate = Calendar.getInstance();
        rightNowDate.setTime(new Date());
        rightNowDate.add(Calendar.YEAR,-year);
        Date time = rightNowDate.getTime();
        String s1 = DateUtils.format(DateUtils.addDateMonths(time, 1-month), "yyyyMM") + "01";
        String monthEnd = DateUtils.format(DateUtils.addDateDays(
                DateUtils.stringToDate(s1, "yyyyMMdd"), -1), dateFormat);
        sql = sql.replace(dateMatch, monthEnd);
        return sql;
    }

//    private String repaceAllRescores(String sql) {
//        String reg = "\\$\\{bdp.hdfs.file.[0-9]*\\}";
//        Pattern pattern = Pattern.compile(reg);
//        Matcher matcher = pattern.matcher(sql);
//        while (matcher.find()) {
//            String e = matcher.group();
//            String resId = e.substring(16, e.length() - 1);
//            RealtimeResourceEntity realtimeResourceEntity = realtimeResourceService.getById(resId);
//            if (null != realtimeResourceEntity) {
//                sql = sql.replace(e, dataDevelopmentConfig.getResPath() + "/development/realtimeresource/download?resourceId=" + realtimeResourceEntity.getResourceId());
//            }
//        }
//        return sql;
//    }


}