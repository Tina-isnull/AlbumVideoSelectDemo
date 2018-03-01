package com.example.lcc.albumvideoselectdemo.bean;

/**
 * Created by lcc on 2018/2/23.
 */

public class TemBean {
    private String mPath;
    private int mPotion;

    public TemBean(String mPath, int mPotion) {
        this.mPath = mPath;
        this.mPotion = mPotion;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public int getmPotion() {
        return mPotion;
    }

    public void setmPotion(int mPotion) {
        this.mPotion = mPotion;
    }

    @Override
    public String toString() {
        return "TemBean{" +
                "mPath='" + mPath + '\'' +
                ", mPotion='" + mPotion + '\'' +
                '}';
    }
}
