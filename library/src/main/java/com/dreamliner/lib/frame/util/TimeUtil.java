package com.dreamliner.lib.frame.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author chenzj
 * @Title: TimeUtil
 * @Description: 类的描述 - 时间处理工具类
 * @date 2017/6/27 14:02
 * @email admin@chenzhongjin.cn
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public final static String FORMAT_YEAR = "yyyy";
    public final static String FORMAT_MONTH = "MM";
    public final static String FORMAT_DAY = "dd";
    public final static String FORMAT_HOUR = "HH";

    public final static String FORMAT_YEAR_MONTH = "yyyy-MM";
    public final static String FORMAT_MONTH_DAY = "MM-dd";
    public final static String FORMAT_MONTH_DAY_BIAS = "MM/dd";
    public final static String FORMAT_MONTH_DAY_CHINESE = "MM月dd日";
    public final static String FORMAT_HOUR_MINUTE = "HH:mm";

    public final static String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
    public final static String FORMAT_SHORT_YEAR_MONTH_DAY = "yy-MM-dd";

    public final static String FORMAT_MONTH_DAY_HOUR_MINUTE = "MM-dd HH:mm";
    public final static String FORMAT_MONTH_DAY_HOUR_MINUTE_CHINESE = "MM月dd日 hh:mm";

    public final static String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_BIAS = "yyyy/MM/dd HH:mm";
    public final static String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_CHINESE = "yyyy年MM月dd日HH:mm";

    public final static String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_BIAS = "yyyy/MM/dd HH:mm:ss";

    public final static String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

    public static String dateToStr(Date date, String formatType) {
        try {
            return new SimpleDateFormat(formatType).format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static Date longToDate(long timestamp) {
        return new Date(timestamp);
    }

    public static String longToString(long timestamp, String formatType) {
        return dateToStr(longToDate(timestamp), formatType);
    }

    public static Date strToDate(String strTime, String formatType) {
        Date date = null;
        try {
            date = new SimpleDateFormat(formatType).parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long strToLong(String strTime, String formatType) {
        Date date = strToDate(strTime, formatType);
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    public static String getHourMinuteWithAmPm(long time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int amPmVal = calendar.get(Calendar.AM_PM);
        String amPmStr = amPmVal == 0 ? "上午" : amPmVal == 1 ? "下午" : "";
        return amPmStr + format.format(new Date(time));
    }

    private static String[] WEEK_ARRAY = new String[]{"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String getWeekStr(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return WEEK_ARRAY[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String getBrithdayByTimestamp(long timestamp) {
        return longToString(timestamp, FORMAT_YEAR_MONTH_DAY);
    }

    public static String getAgeStr(long timestamp) {
        int age;
        Date birthday = longToDate(timestamp);
        Calendar nowCal = Calendar.getInstance();

        if (nowCal.before(birthday)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = nowCal.get(Calendar.YEAR);
        int monthNow = nowCal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = nowCal.get(Calendar.DAY_OF_MONTH);

        nowCal.setTime(birthday);
        int yearBirth = nowCal.get(Calendar.YEAR);
        int monthBirth = nowCal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = nowCal.get(Calendar.DAY_OF_MONTH);

        age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        if (age < 0 || age > 150) {
            age = 0;
        }
        return String.valueOf(age);
    }

    public static int getGapDay(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar = resetOneDayToBeginTime(fromCalendar);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar = resetOneDayToBeginTime(toCalendar);
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (DAY * 1000));
    }

    public static Calendar resetOneDayToBeginTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static String getChatTimeStr(long timestamp) {
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat("DD");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timestamp);
        int temp = Integer.parseInt(format.format(today)) - Integer.parseInt(format.format(otherDay));

        if (otherDay.getYear() < today.getYear()) {
            temp = -1;
        }
        switch (temp) {
            case 0:
                result = getHourMinuteWithAmPm(timestamp);
                break;
            case 1:
                result = "昨天 " + getHourMinuteWithAmPm(timestamp);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                result = getWeekStr(otherDay) + getHourMinuteWithAmPm(timestamp);
                break;
            default:
                result = dateToStr(otherDay, FORMAT_MONTH_DAY) + " " + getHourMinuteWithAmPm(timestamp);
                break;
        }
        return result;
    }

}
