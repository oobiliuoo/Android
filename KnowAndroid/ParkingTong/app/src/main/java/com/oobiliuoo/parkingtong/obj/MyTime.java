package com.oobiliuoo.parkingtong.obj;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author biliu
 */
public class MyTime {
    private String str_year;
    private String str_month;
    private String str_day;
    private String str_hour;
    private String str_minute;
    private String str_second;


    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;


    public MyTime() {

    }

    public String getTime(){
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);

        String time = year + "-" + month + "-" + day + " " + hour + ":" + minute;
        return time;
    }

    public String getXxTime(){
        String time = "" + year;
        time+=change(month);
        time+=change(day);
        time+=change(hour);
        time+=change(minute);
        time+=change(second);
        return time;

    }

    private String change(int num){
        if(num>10){
            return ""+num;
        }
        return "0"+num;
    }


    public static int calTime(String startTime, String endTime){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       // String newDate = df.format(nowDate);
       // String sqlDate1 = df.format(sqlDate);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = df.parse(startTime);
            endDate = df.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long time =  startDate.getTime();
        Long time2 =  endDate.getTime();
        int mMinute = (int) ((time2 - time) / (60*1000));
        return mMinute;
    }


}
