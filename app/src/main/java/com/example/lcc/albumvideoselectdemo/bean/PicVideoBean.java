package com.example.lcc.albumvideoselectdemo.bean;

/**
 * Created by lcc on 2017/12/5.
 */

public class PicVideoBean {
    private String path;
    private String videoDuration;
    private String mPosition;

    public PicVideoBean() {
    }

    public PicVideoBean(String path, String videoDuration,String positon) {
        this.path = path;
        this.videoDuration = videoDuration;
        this.mPosition=positon;
    }

    public String getmPosition() {
        return mPosition;
    }

    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    @Override
    public String toString() {
        return "PicVideoBean{" +
                "path='" + path + '\'' +
                ", videoDuration='" + videoDuration + '\'' +
                '}';
    }
}

