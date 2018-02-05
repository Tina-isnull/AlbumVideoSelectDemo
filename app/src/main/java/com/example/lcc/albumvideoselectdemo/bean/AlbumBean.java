package com.example.lcc.albumvideoselectdemo.bean;

/**
 * Created by lcc on 2017/12/5.
 */

public class AlbumBean {
    private String name;
    private int count;
    private String albumPic;

    public AlbumBean() {
    }

    public AlbumBean(String name, int count, String albumPic) {
        this.name = name;
        this.count = count;
        this.albumPic = albumPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }
    public void addCount(){
        count++;
    }
}
