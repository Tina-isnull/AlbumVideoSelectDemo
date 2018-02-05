package com.example.lcc.albumvideoselectdemo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lcc on 2017/12/6.
 */

public class TimeFormat {
    public static String dataFormat(String time) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");
        String data = mFormat.format(new Date(time));
        return data;
    }
    public static String duringFormat(long time) {
        SimpleDateFormat mFormat = new SimpleDateFormat("mm:ss");
        String data = mFormat.format(new Date(time));
        return data;
    }
}
