package com.example.lcc.albumvideoselectdemo.nineimage;

import java.io.Serializable;


public class ImageInfo implements Serializable {
    public String thumbnailUrl;  //缩略图   也就是小图
    public String bigImageUrl;  //大图  用于展示使用
    public String name;
    public int imageViewHeight;
    public int imageViewWidth;
    public int imageViewX;
    public int imageViewY;
    public String livelength;
    public boolean isVideo = false;  //是否是视频类型
    public boolean isLongPic = false;  //是否是单张图片的长图类型
    public String videopath;  //视频地址
    public ImageInfo(String imagePath, boolean isVideo, boolean isLongPic) {
        this.thumbnailUrl = imagePath;
        this.isVideo = isVideo;
        this.isLongPic = isLongPic;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public void setBigImageUrl(String bigImageUrl) {
        this.bigImageUrl = bigImageUrl;
    }

    public int getImageViewHeight() {
        return imageViewHeight;
    }

    public void setImageViewHeight(int imageViewHeight) {
        this.imageViewHeight = imageViewHeight;
    }

    public int getImageViewWidth() {
        return imageViewWidth;
    }

    public void setImageViewWidth(int imageViewWidth) {
        this.imageViewWidth = imageViewWidth;
    }

    public int getImageViewX() {
        return imageViewX;
    }

    public void setImageViewX(int imageViewX) {
        this.imageViewX = imageViewX;
    }

    public int getImageViewY() {
        return imageViewY;
    }

    public void setImageViewY(int imageViewY) {
        this.imageViewY = imageViewY;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public String getLivelength() {
        return livelength;
    }

    public void setLivelength(String livelength) {
        this.livelength = livelength;
    }

    public void setLongPic(boolean longPic) {
        isLongPic = longPic;
    }

    public boolean isLongPic() {
        return isLongPic;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public String getVideopath() {
        return videopath;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "imageViewY=" + imageViewY +
                ", imageViewX=" + imageViewX +
                ", imageViewWidth=" + imageViewWidth +
                ", imageViewHeight=" + imageViewHeight +
                ", bigImageUrl='" + bigImageUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}
