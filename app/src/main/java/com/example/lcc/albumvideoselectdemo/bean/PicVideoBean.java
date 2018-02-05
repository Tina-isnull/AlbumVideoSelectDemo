package com.example.lcc.albumvideoselectdemo.bean;

/**
 * Created by lcc on 2017/12/5.
 */

public class PicVideoBean {
    private String path;
    private String videoDuration;
    private String SelectNumber;

    public PicVideoBean() {
    }

    public PicVideoBean(String path, String videoDuration, String selectNumber) {
        this.path = path;
        this.videoDuration = videoDuration;
        SelectNumber = selectNumber;
    }

    public String getSelectNumber() {
        return SelectNumber;
    }

    public void setSelectNumber(String selectNumber) {
        SelectNumber = selectNumber;
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
                ", SelectNumber='" + SelectNumber + '\'' +
                '}';
    }
}

