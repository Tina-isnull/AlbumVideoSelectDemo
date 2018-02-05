package com.example.lcc.albumvideoselectdemo.utils;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by lcc on 2017/12/6.
 */

public class MyApp extends Application {
    private static ImageLoader loader;
    private DisplayMetrics displayMetrics = null;
    protected static MyApp mInstance;
    private static float scale;
    @Override
    public void onCreate() {
        super.onCreate();
        scale = getResources().getDisplayMetrics().density;
        mInstance = this;
    }
    public static MyApp getApp() {
        if (mInstance != null && mInstance instanceof MyApp) {
            return (MyApp) mInstance;
        } else {
            mInstance = new MyApp();
            mInstance.onCreate();
            return (MyApp) mInstance;
        }
    }
    public ImageLoader getLoader(){
        if(loader==null){
            loader=new ImageLoader(this);
        }else{
            return loader;
        }
        return loader;
    }
    public int getScreenHeight() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.widthPixels;
    }

    public void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
        this.displayMetrics = DisplayMetrics;
    }
    /**
     * dp转成px
     *
     * @param dipValue 想要实现的dp值
     * @return px值
     */

    public static int dip2px(int dipValue) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机分辨率 从px转为dp
     */
    private static int px2dp(int pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }
}
