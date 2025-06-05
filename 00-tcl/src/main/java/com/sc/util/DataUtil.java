package com.sc.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author jie173.chen
 * Date 2024/8/9
 * Desc
 */
public class DataUtil {

    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    /**
     * 根据date和sql的变量来替换sql by wuzwb
     *
     * @param sql_task
     * @param date
     * @return
     */
    public static String replaceDateTime(String sql_task, String date){

        //如果sql为空则直接返回
        if (date.equals("") || null == date)
            return sql_task;
        //如果符合条件则处理sql否则直接返回原来的sql
        Map<String,String> dp = doFillMap(sql_task, date);
        if (CollectionUtil.isNotEmpty(dp)) {
            //替换$
            String sql = sql_task;
            for(String d:dp.keySet()){
                sql = sql.replace(d, dp.get(d));
            }
            return sql;
        } else {
            return sql_task;
        }
    }

    /**
     * 根据sql获取fillmap  fillmap 里存储 原表达式和要替换的值by wuzwb
     *
     * @param sql_task
     * @return
     */
    public static Map<String,String> doFillMap(String sql_task, String date){

        Map<String,String> fillMap =new HashMap<>();
        //${yyyy-mm-dd-?}、${yyyy-mm-dd hh:mi:ss.ms-?}等的正则表达式
        Pattern pattern = Pattern.compile("(\\$\\{y{0,4}[:/-]*q{0,2}[:/-]*m{0,2}[:/-]*w{0,2}[:/-]*d{0,2}[: -]*h{0,2}[:-]*(mi)?[:-]*(ss)?[.]*(ms)?([- +](\\d)+?)*?\\})");
        Matcher m = pattern.matcher(sql_task);
        //找到替换表达式${yyyy-?} 或者${yyyy}
        while (m.find()) {

            String date_source = m.group(1);

            //support data plus and subtract
            boolean isDatePlus = false;
            if (date_source.contains("+")){
                isDatePlus = true;
                date_source = date_source.replace("+","-");
            }

            String[] strs = date_source.split("-");
            String datestr = date_source;
            DateTime dt = DateUtils.stringToDateTime(date,DateUtils.DATE_TIME_PATTERN);
            //如果表达式里含有时间差 修改传入时间 ${yyyy-?}
            if (strs.length > 0 && isNumeric(strs[strs.length - 1].replace("}", ""))) {
                Integer lastIndexOf = date_source.lastIndexOf("-");
                //unit 是截取-前两位作为单位
                String unit = date_source.substring(lastIndexOf - 2, lastIndexOf);
                //offset
                Integer number = Integer.parseInt(strs[strs.length - 1].replace("}", ""));
                //实际的日期区域除去 ${}的
                datestr = date_source.substring(date_source.indexOf("{") + 1, lastIndexOf);

                int changeDate;
                if (isDatePlus){
                    changeDate = number;
                }else {
                    changeDate = 0 - number;
                }
                switch (unit) {
                    case "yy" : dt =dt.plusYears(changeDate);break;
                    case "qq" : dt =dt.plusMonths(changeDate * 3);break;
                    case "mm" : dt =dt.plusMonths(changeDate);break;
                    case "ww" : dt =dt.plusWeeks(changeDate);break;
                    case "dd" : dt =dt.plusDays(changeDate);break;
                    case "hh" : dt =dt.plusHours(changeDate);break;
                    case "mi" : dt =dt.plusMinutes(changeDate);break;
                    case "ss" : dt =dt.plusSeconds(changeDate);break;
                    case "ms" :
                        dt = DateUtils.stringToDateTime(DateUtils.format(new Date(),DateUtils.DATE_TIMEMILLS_PATTERN),DateUtils.DATE_TIMEMILLS_PATTERN);
                        dt =dt.plusMillis(changeDate);
                        break;
                    default:
                        break;
                }
            } else {
                //${yyyy}
                datestr = date_source.substring(date_source.indexOf("{") + 1, date_source.lastIndexOf("}"));
            }
            //填充fillMap
            fillMap .put(m.group(1) ,replaceFromdt(dt, datestr));
        }
        return fillMap;
    }


    /**
     * 在单元里替换 by wuzwb
     *
     * @param dt
     * @param str
     * @return
     */
    public static String replaceFromdt(DateTime dt, String str) {
        Integer q = dt.monthOfYear().get() / 3;
        if (dt.monthOfYear().get() % 3 > 0) {
            q = q + 1;
        }
        DateTime mdt = DateUtils.stringToDateTime(DateUtils.format(new Date(),DateUtils.DATE_TIMEMILLS_PATTERN),DateUtils.DATE_TIMEMILLS_PATTERN);
        String re = str.replace("yyyy", fillZero(dt.year().getAsString()))
                .replace("mm", fillZero(dt.monthOfYear().getAsString()))
                .replace("qq", fillZero(q.toString()))
                .replace("ww", fillZero(dt.weekOfWeekyear().getAsString()))
                .replace("dd", fillZero(dt.dayOfMonth().getAsString()))
                .replace("hh", fillZero(dt.hourOfDay().getAsString()))
                .replace("mi", fillZero(dt.minuteOfHour().getAsString()))
                .replace("ss", fillZero(dt.secondOfMinute().getAsString()))
                .replace("ms", fillZero(mdt.millisOfSecond().getAsString()));
        return re;
    }

    /**
     * 如果是单字符的前面加一个0 比如 1 => 01 by wuzwb
     *
     * @param str
     * @return
     */
    public static  String fillZero(String str)  {
        if (str.length() == 1) {
            return "0" + str;
        } else {
            return str;
        }
    }

    /**
     * 判断是否为数字 by wuzwb
     *
     * @param str
     * @return
     */
    public static  Boolean isNumeric(String str){
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }


    private static String changeShellTime(String currentDataTime) {
        if (StringUtils.isEmpty(currentDataTime)) {
            return DateUtil.now();
        }
        if (currentDataTime.trim().length() <= 10 && currentDataTime.trim().length() > 4) {
            return currentDataTime.trim() + " 00:00:00";
        } else if (currentDataTime.trim().length() <= 4) {
            return DateUtil.now();
        } else {
            return currentDataTime;
        }
    }



    private String wapper(String value, Long depid, Integer jobid, String taskid, String nowTime) {
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
            case "$jobid": {
                return jobid.toString();
            }
            case "$taskid": {
                return taskid;
            }
            case "$depid": {
                return null == depid ? "" : depid.toString();
            }
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
                return DataUtil.replaceDateTime(value, nowTime);
            }
        }
    }


    private static String replaceAllSh(String sql, String currentDataTime) {
        //数据补录，不支持时间替换
        String nowTime = DateUtil.now();
        if (!StringUtils.isEmpty(currentDataTime)
                && !"null".equalsIgnoreCase(currentDataTime)
                && !StringUtils.isEmpty(currentDataTime.trim())) {
            //数据补录--shell传递参数以后。时间不对
            nowTime = changeShellTime(currentDataTime);
        }
        //替换对应的sql语句
//        if (MapUtil.isNotEmpty(sysParams)) {
//            for (Map.Entry<String, String> entery : sysParams.entrySet()) {
//                String key = entery.getKey();
//                String value = entery.getValue();
//                String repallStr = "${" + key + "}";
//                if (sql.contains(repallStr)) {
//                    String res = wapper(value, depid, jobid, taskid, nowTime);
//                    sql = sql.replace(repallStr, res);
//                }
//            }
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        long data = date.getTime() - 24 * 60 * 60 * 1000L;
        sql = sql.replace("${bdp.system.cyctime}", sdf.format(date));
        sql = sql.replace("${bdp.system.bizdate}", sdf1.format(new Date(data)));


        if (StringUtils.isEmpty(currentDataTime) ) {
            //查询到系统动态数据时间的替换
//            if (sql.contains("${bdp.data.start.time}") || sql.contains("${bdp.data.end.time}")) {
//                JobRunHistory jobRunHistory = jobRunHistoryMapper.selctListByJobIdOne(jobid);
//                if (null == jobRunHistory) {
//                    log.error("替换数据动态参数异常！未查询到对应的配置数据。jobid：" + jobid);
//                } else {
//                    //获取对应的时间格式
//                    JobNodeConf workflowConf = jobNodeConfMapper.selectOne(new QueryWrapper<JobNodeConf>()
//                            .eq("job_node_id", workMenuId)
//                            .eq("job_type", 1)
//                            .eq("`key`", "timeFormat"));
//
//                    String cronStartTime = jobRunHistory.getCronStartTime();
//                    String cronEndTime = jobRunHistory.getCronEndTime();
//                    if (workflowConf != null && workflowConf.getValue() != null && !"".equals(workflowConf.getValue())) {
//                        //根据时间格式，转换时间
//                        try {
//                            cronStartTime = TimeUtil.getDateToFormat(TimeUtil.strToDateLong(cronStartTime), workflowConf.getValue());
//                            cronEndTime = TimeUtil.getDateToFormat(TimeUtil.strToDateLong(cronEndTime), workflowConf.getValue());
//                        } catch (Exception e) {
//                            log.warn("时间格式转换错误：\n sql:{} \n {}", sql, e.getMessage());
//                        }
//                    }
//                    sql = sql.replace("${bdp.data.start.time}", cronStartTime);
//                    sql = sql.replace("${bdp.data.end.time}", cronEndTime);
//                }
//            }

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
            }
            if (sql.contains("${yyyymmdd}")) {
                String s = DateUtils.format(new Date(), "yyyyMMdd");
                sql = sql.replace("${yyyymmdd}", s);
            }
            if (sql.contains("${yyyy-mm-dd}")) {
                String s = DateUtils.format(new Date(), "yyyy-MM-dd");
                sql = sql.replace("${yyyy-mm-dd}", s);
            }
        }
        //资源文件的替换
//        sql = repaceAllRescores(sql);
        //先替换表达式${yyyy-?} 或者${yyyy}
        return DataUtil.replaceDateTime(sql, nowTime);
    }


    private static  String replaceByYearAndMonth(String sql,Matcher matcher,String dateFormat){
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


    public static void main(String[] args) {

        System.out.println(replaceAllSh("SELECT * from ods.ods_ddd_pre_platform_input_sr WHERE name = '${yyyy-mm-dd-1}';", "2024-11-11 11:11:11"));
      System.out.println(replaceAllSh("SELECT * from ods.ods_ddd_pre_platform_input_sr WHERE name = '${yyyy-mm-dd+1}';", "2024-11-11 11:11:11"));
////



//        System.out.println(replaceDateTime("SELECT * from ods.ods_ddd_pre_platform_input_sr WHERE name = '${yyyy-mm-dd-1}';", "2024-11-11 11:11:11"));
//        System.out.println(replaceDateTime("SELECT * from ods.ods_ddd_pre_platform_input_sr WHERE name = '${yyyy-mm-dd+1}';", "2024-11-11 11:11:11"));
//        System.out.println(replaceDateTime("${yyyy-mm+1}", "2024-11-11 11:11:11"));
//        System.out.println(replaceDateTime("${yyyy-mm-1}", "2024-11-11 11:11:11"));
//        System.out.println(replaceDateTime("${yyyy+1}", "2024-11-11 11:11:11"));
//        System.out.println(replaceDateTime("${yyyy-1}", "2024-11-11 11:11:11"));
    }

}
