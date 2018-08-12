package com.mday.event;

import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 7/18/18.
 */

public class UtilsCourseEvent {
    public static void initTimeCourse(TextView tx, CourseEvent course) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        // TimeZone tz = TimeZone.getDefault();
        SimpleDateFormat output = new SimpleDateFormat("HH'h'mm");
        if (course.getStartDatetime() != null && course.getEndDatetime() != null) {
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = sdf.parse(course.getStartDatetime());
                d2 = sdf.parse(course.getEndDatetime());
                Log.d("", "onBindViewHolder:=== " + d1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String start = output.format(d1);
            String end = output.format(d2);
            tx.setText(start + " - " + end);
        } else {
            tx.setText("");
        }
    }









    public static Date getDateStart(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        // TimeZone tz = TimeZone.getDefault();
        SimpleDateFormat output = new SimpleDateFormat("HH'h'mm");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d1;
    }


    public static String initTimeCourse(CourseEvent course) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        // TimeZone tz = TimeZone.getDefault();
        SimpleDateFormat output = new SimpleDateFormat("HH'h'mm");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(course.getStartDatetime());
            d2 = sdf.parse(course.getEndDatetime());
            Log.d("", "onBindViewHolder:=== " + d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String start = output.format(d1);
        String end = output.format(d2);
        return start + " - " + end;
    }



}

