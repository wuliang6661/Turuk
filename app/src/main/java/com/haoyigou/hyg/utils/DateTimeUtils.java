package com.haoyigou.hyg.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by kristain on 15/12/17.
 */
public class DateTimeUtils {


    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式：yyyy-MM-dd HH:mm
     **/
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式：yyyy-MM-dd
     **/
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 日期格式：yyyy-MM-dd
     **/
    public static final String DE_YYYY_MM_DD = "yyyyMMdd";


    /**
     * 日期格式：HH:mm:ss
     **/
    public static final String DF_HH_MM_SS = "HH:mm:ss";

    /**
     * 日期格式：HH:mm
     **/
    public static final String DF_HH_MM = "HH:mm";

    /**
     * 日期格式：yyyy.MM.dd
     **/
    public static final String YYYY_MM_DD = "yyyy.MM.dd";
    public static final String YY_MM_DD = "yy.MM.dd";
    public static final String MM_DD = "MM.dd";

    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY_MM_DD_HH_MM = "yyyy.MM.dd HH:mm";
    public static final String YYYY = "yyyy";
    public static final String MM = "MM";

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * Log输出标识
     **/
    private static final String TAG = DateTimeUtils.class.getSimpleName();

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatFriendly1(long date) {
        if (date == 0) {
            return null;
        }
        long diff = weeHours(new Date(), 0).getTime() - date * 1000;
        //  long diff = new Date().getTime()- date * 1000;
        long r = 0;
        if (diff > day) {
            r = (diff / day);
            if (r == 1) {
                return "昨天";
            }
            if (r == 0) {
                return "今天";
            }
        }

        return "";
    }


    /**
     * 将日期信息转换成今天、明天、后天、星期
     *
     * @param date
     * @return
     */
    public static String getDateDetail(String date) {
        Calendar today = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            today.setTime(df.parse(df.format(weeHours(new Date(), 0))));
            today.set(Calendar.HOUR, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            target.setTime(df.parse(date));
            target.set(Calendar.HOUR, 0);
            target.set(Calendar.MINUTE, 0);
            target.set(Calendar.SECOND, 0);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis();
        int xcts = (int) (intervalMilli / day);
        if (xcts == 0) {
            return "今天";
        } else if (xcts == 1) {
            return "明天";
        }
        return "";

    }

    /**
     * 将日期信息转换成今天、明天、后天、星期
     *
     * @param date
     * @return
     */
    public static String getDateDetail1(String date) {
        Calendar today = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            today.setTime(df.parse(df.format(weeHours(new Date(), 1))));
            today.set(Calendar.HOUR, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            target.setTime(df.parse(date));
            target.set(Calendar.HOUR, 0);
            target.set(Calendar.MINUTE, 0);
            target.set(Calendar.SECOND, 0);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis();
        int xcts = (int) (intervalMilli / day);
        if (xcts == -1) {
            return "昨天";
        }
        if (xcts == 0) {
            return "今天";
        }
        return "";

    }

    /**
     * 凌晨
     *
     * @param date
     * @return
     * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
     * 1 返回yyyy-MM-dd 23:59:59日期
     */
    public static Date weeHours(Date date, int flag) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        //时分秒（毫秒数）
        long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        //凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);

        if (flag == 0) {
            return cal.getTime();
        } else if (flag == 1) {
            //凌晨23:59:59
            cal.setTimeInMillis(cal.getTimeInMillis() + 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000);
        }
        return cal.getTime();
    }

    /**
     * 2016-6-16转换成2016-6-16 00:00 凌晨
     * flag=0 :是
     * 将String类型的时间转换成
     *
     * @param str
     */
    public static String WeeDateToStirng(String str, int flag) {
        Date date = weeHours(parseDate(str, DF_YYYY_MM_DD), flag);
        return formatDateTime(date, DF_YYYY_MM_DD_HH_MM_SS);
    }

    public static void main(String[] str) {
        System.out.print(getFirstDayOfLastDayInMonth(2016, 2));
    }

    /**
     * 获取离月底还有几天
     *
     * @return
     */
    public static int getDayOfMonth() {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day_of_month = aCalendar.get(Calendar.DAY_OF_MONTH);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day - day_of_month + 1;
    }

    /**
     * 获取当前月份的第一天和最后一天
     *
     * @return 第一天和最后一天
     */
    public static String getFirstDayOfLastDayInMonth() {
        String firstday, lastday;
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        aCalendar.add(Calendar.MONTH, 0);
        aCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Calendar aCalendar1 = Calendar.getInstance(Locale.CHINA);
        aCalendar1.add(Calendar.MONTH, 1);
        aCalendar1.set(Calendar.DAY_OF_MONTH, 0);
        firstday = formatDateTime(aCalendar.getTime(), YYYY_MM_DD);
        lastday = formatDateTime(aCalendar1.getTime(), MM_DD);
        return firstday + "-" + lastday;
    }

    /**
     * 根据年月获取第一天和最后一天
     */
    public static String getFirstDayOfLastDayInMonth(int year, int month) {
        String firstday, lastday;
        Calendar aCalendar1 = Calendar.getInstance(Locale.CHINA);
        aCalendar1.set(Calendar.YEAR, year);
        aCalendar1.set(Calendar.MONTH, month - 1);
        aCalendar1.set(Calendar.DAY_OF_MONTH, aCalendar1.getMinimum(Calendar.DATE));

        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        aCalendar.set(Calendar.YEAR, year);
        aCalendar.set(Calendar.MONTH, month - 1);
        aCalendar.set(Calendar.DAY_OF_MONTH, aCalendar.getActualMaximum(Calendar.DATE));

        firstday = formatDateTime(aCalendar1.getTime(), YYYY_MM_DD);
        lastday = formatDateTime(aCalendar.getTime(), MM_DD);
        return firstday + "-" + lastday;
    }

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatFriendly(long date) {
        if (date == 0) {
            return null;
        }
        long diff = new Date().getTime() - date * 1000;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            diff = diff - r * year;
            long m = 0;
            if (diff > month) {
                m = (diff / month);
                return r + "年" + m + "个月";
            }
            return r + "年";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天";
        }
        return "刚刚";
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param dateL 日期
     * @return
     */
    public static String formatDateTime(long dateL) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date date = new Date(dateL);
        return sdf.format(date);
    }


    /**
     * 根据毫秒转日期
     *
     * @param dateL
     * @return
     */
    public static String formatDateByMill(long dateL) {
        TimeZone timeZone = null;
        if (StringUtils.isEmpty("GMT+8:00")) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone("GMT+8:00");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        df.setTimeZone(timeZone);
        return df.format(new Date(dateL * 1000));
    }

    /**
     * 根据毫秒转日期
     *
     * @param dateL
     * @return
     */
    public static String formatDateByMill(long dateL, String fomate) {
        TimeZone timeZone = null;
        if (StringUtils.isEmpty("GMT+8:00")) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone("GMT+8:00");
        }
        SimpleDateFormat df = new SimpleDateFormat(fomate);
        df.setTimeZone(timeZone);
        return df.format(new Date(dateL));
    }


    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param dateL    日期
     * @param formater
     * @return
     */
    public static String formatDateTime(long dateL, String formater) {
        TimeZone timeZone = null;
        if (StringUtils.isEmpty("GMT+8:00")) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone("GMT+8:00");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date(dateL));
    }


    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param date     日期
     * @param formater
     * @return
     */
    public static String formatDateTime(Date date, String formater) {
        TimeZone timeZone = null;
        if (StringUtils.isEmpty("GMT+8:00")) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone("GMT+8:00");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }


    /**
     * 将日期转为指定格式
     *
     * @param date     日期
     * @param formater
     * @return
     */
    public static String formatTime(long date, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(date);
    }

    /**
     * 将日期字符串转成日期
     *
     * @param strDate 字符串日期
     * @return java.util.date日期类型
     */
    public static Date parseDate(String strDate) {

        DateFormat dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            Log.v(TAG, "parseDate failed !");

        }
        return returnDate;

    }

    /**
     * 将日期字符串转成日期
     *
     * @param strDate 字符串日期
     * @return java.util.date日期类型  yyyyMMdd
     */
    public static String parseDate1(String strDate) {

        try {
            SimpleDateFormat format = new SimpleDateFormat(DF_YYYY_MM_DD);
            Date date = format.parse(strDate);//有异常要捕获
            format = new SimpleDateFormat(DE_YYYY_MM_DD);
            return format.format(date);
        } catch (ParseException e) {
            Log.v(TAG, "parseDate failed !");

        }
        return strDate;

    }

    /**
     * 将日期字符串转成日期
     *
     * @param strDate 字符串日期
     * @return java.util.date日期类型
     */
    public static Date parseDate(String strDate, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            Log.v(TAG, "parseDate failed !");

        }
        return returnDate;

    }

    /**
     * 获取系统当前日期
     *
     * @return
     */
    public static Date gainCurrentDate() {
        return new Date();
    }

    /**
     * 获取系统当前日期
     *
     * @return
     */
    public static String gainCurrentDate(String formater) {
        return formatDateTime(new Date(), formater);
    }

    /**
     * 获取上一天
     *
     * @return
     */
    public static String getLastDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("MM月dd日").format(cal.getTime());
        return yesterday;
    }

    /**
     * 获取下一天
     *
     * @param date
     * @return
     */
    public static String getNextDate(String date) {
        return formatDateTime(addDateTime(parseDate(date, DF_YYYY_MM_DD), 24), DF_YYYY_MM_DD);
    }

    /**
     * 获取系统当前月份
     *
     * @return
     */
    public static String gainCurrentMonth() {
        return formatDateTime(new Date(), YYYYMM);
    }

    /**
     * 获取系统当前年 月 日
     * 格式：yyyy.MM.dd
     *
     * @return
     */
    public static String gainCurrentMonthforDay() {
        return formatDateTime(new Date(), YYYY_MM_DD);
    }

    /**
     * 获取系统当前年 月 日
     * 格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentMonthforDay() {
        return formatDateTime(new Date(), DF_YYYY_MM_DD);
    }

    /**
     * 获取系统当前月份(不加年份)
     *
     * @return
     */
    public static String gainCurrentMonth1() {
        return formatDateTime(new Date(), MM);
    }

    public static String gainCurrentMonth1(int position) {
        if (position < 10) {
            return "0" + position;
        }
        return position + "";
    }

    /**
     * 获取系统当前年份
     *
     * @return
     */
    public static String gainCurrentYear() {
        return formatDateTime(new Date(), YYYY);
    }

    public static String gainCurrentMonth(int position) {
        String year = formatDateTime(new Date(), YYYY);
        if (position < 10) {
            return year + "0" + position;
        }
        return year + position;
    }

    /**
     * 获取当前月中一个月的天数
     *
     * @return 一个月的天数
     */
    public static int getDayToMonth() {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }

    /**
     * 获取上一月份日期
     *
     * @param date
     * @return
     */
    public static String getLastMonth(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(4);
        if ("01".equals(month)) {
            return (Integer.parseInt(year) - 1) + "12";
        }
        if ("10".equals(month)) {
            return year + "09";
        }
        if ("11".equals(month)) {
            return year + "10";
        }
        if ("12".equals(month)) {
            return year + "11";
        }
        return year + "0" + (Integer.parseInt(month) - 1);
    }

    /**
     * 格式化月份
     *
     * @param month
     * @return
     */
    public static String formatMonth(int month) {
        if (month < 10) {
            return "0" + month;
        }
        return month + "";
    }

    /**
     * 格式化日期
     *
     * @param day
     * @return
     */
    public static String formatDay(int day) {
        if (day < 10) {
            return "0" + day;
        }
        return day + "";
    }

    /**
     * 获取下一月份日期
     *
     * @param date
     * @return
     */
    public static String getNextMonth(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(4);
        if ("12".equals(month)) {
            return (Integer.parseInt(year) + 1) + "01";
        }
        if ("09".equals(month)) {
            return year + "10";
        }
        if ("10".equals(month) || "11".equals(month)) {
            return year + (Integer.parseInt(month) + 1);
        }
        return year + "0" + (Integer.parseInt(month) + 1);
    }


    /**
     * 验证日期是否比当前日期早
     *
     * @param target1 比较时间1
     * @param target2 比较时间2
     * @return true 则代表target1比target2晚或等于target2，否则比target2早
     */
    public static boolean compareDate(Date target1, Date target2) {
        boolean flag = false;
        try {
            String target1DateTime = DateTimeUtils.formatDateTime(target1,
                    DF_YYYY_MM_DD_HH_MM_SS);
            String target2DateTime = DateTimeUtils.formatDateTime(target2,
                    DF_YYYY_MM_DD_HH_MM_SS);
            if (target1DateTime.compareTo(target2DateTime) <= 0) {
                flag = true;
            }
        } catch (Exception e1) {
            System.out.println("比较失败，原因：" + e1.getMessage());
        }
        return flag;
    }


    /**
     * 获取当天晚上18点的时间
     */
    public static Date getSixHour() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 18);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 获取昨日的时间
     *
     * @return
     */
    public static Date gethour() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        return calendar.getTime();
    }

    /**
     * 对日期进行增加操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date addDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }

        return new Date(target.getTime() + (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 对日期进行相减操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date subDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }

        return new Date(target.getTime() - (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 获取合适型两个时间差
     * <p>time0和time1格式都为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time0     时间字符串0
     * @param time1     时间字符串1
     * @param precision 精度
     *                  <p>precision = 0，返回null</p>
     *                  <p>precision = 1，返回天</p>
     *                  <p>precision = 2，返回天和小时</p>
     *                  <p>precision = 3，返回天、小时和分钟</p>
     *                  <p>precision = 4，返回天、小时、分钟和秒</p>
     *                  <p>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</p>
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(String time0, String time1, int precision) {
        return ConvertUtils.millis2FitTimeSpan(Math.abs(string2Millis(time0, DF_YYYY_MM_DD_HH_MM_SS) - string2Millis(time1, DF_YYYY_MM_DD_HH_MM_SS)), precision);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.getDefault()).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
