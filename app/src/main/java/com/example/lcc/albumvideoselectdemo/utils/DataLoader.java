package com.example.lcc.albumvideoselectdemo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.lcc.albumvideoselectdemo.bean.AlbumBean;
import com.example.lcc.albumvideoselectdemo.bean.PicVideoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcc on 2017/12/5.
 */

public class DataLoader {
    private ContentResolver mResolver;
    private Context context;
    //图片
    private Uri mPicTable = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    //视频
    private Uri mVideoTable = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    //视频时间
    private float VIDEO_TIME = 10000;
    //图片大小、
    private long PIC_SMALL_SIZE = 1024 * 20;
    //视频大小
    private long VIDEO_BIG_SIZE = 15 * 1024 * 1024;


    public DataLoader(Context context) {
        this.context = context;
        mResolver = context.getContentResolver();
    }

    /**
     * 获得所有图片／以及相册
     *
     * @return
     */
    public Map<String, Object> getPics1() {
        ArrayList<AlbumBean> list = new ArrayList<>();
        HashMap<String, AlbumBean> temporary = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        Cursor mCursor = null;
        ArrayList<PicVideoBean> pics = new ArrayList<>();
        //筛选的行
        String[] mColumns = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_ADDED};
        //筛选的条件
        String mSelect = "(" + MediaStore.Images.Media.MIME_TYPE + "=?or " + MediaStore.Images.Media.MIME_TYPE + "=?or " + MediaStore.Images.Media.MIME_TYPE + "=?)AND " + MediaStore.Images.Media.SIZE + ">?";
        //筛选的条件值
        String[] mSelectArgs = {"image/jpeg", "image/png", "image/webp", String.valueOf(PIC_SMALL_SIZE)};
        mCursor = mResolver.query(mPicTable, mColumns, mSelect, mSelectArgs, MediaStore.Images.Media.DATE_ADDED + " DESC");
        if (mCursor != null && mCursor.getCount() != 0) {

            mCursor.moveToFirst();
            AlbumBean AlbumPicAll = new AlbumBean("最近相册", 0, mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            list.add(0, AlbumPicAll);
            int i = 1;
            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
                PicVideoBean data = new PicVideoBean("", "", "");
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                data.setPath(path);
                data.setmPosition(i + "");
                pics.add(data);
                String AlbumName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                AlbumPicAll.addCount();
                if (temporary.containsKey(AlbumName)) {
                    temporary.get(AlbumName).addCount();
                } else {
                    AlbumBean bean = new AlbumBean(AlbumName, 1, mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    temporary.put(AlbumName, bean);
                    list.add(bean);
                }
                i++;
            }
            map.put("pic", pics);
            map.put("album", list);
        } else {
            return map;
        }

        if (!mCursor.isClosed()) {
            mCursor.close();
            mCursor = null;
        }
        return map;
    }

    /**
     * 获得相册下的图片集合
     */
    public ArrayList<PicVideoBean> getAlbumPics(String albumName) {
        Cursor mCursor = null;
        ArrayList<PicVideoBean> pics = new ArrayList<>();
        //筛选的行
        String[] mColumns = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DATE_ADDED};
        //筛选的条件
        String mSelect1 = "(" + MediaStore.Images.Media.MIME_TYPE + "=?or " + MediaStore.Images.Media.MIME_TYPE + "=?or " + MediaStore.Images.Media.MIME_TYPE + "=?)AND " + MediaStore.Images.Media.SIZE + ">?AND " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?";
        //筛选的条件值
        String[] mSelectArgs1 = {"image/jpeg", "image/png", "image/webp", String.valueOf(PIC_SMALL_SIZE), albumName};
        mCursor = mResolver.query(mPicTable, mColumns, mSelect1, mSelectArgs1, MediaStore.Images.Media.DATE_ADDED + " DESC");
        int i = 1;
        if (mCursor != null && mCursor.getCount() != 0) {
//            mCursor.moveToFirst();
            while (mCursor.moveToNext()) {
                PicVideoBean bean = new PicVideoBean();
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                bean.setPath(path);
                bean.setmPosition(i+"");
                pics.add(bean);
                i++;
            }
        } else {
            return pics;
        }

        if (!mCursor.isClosed()) {
            mCursor.close();
            mCursor = null;
        }
        return pics;
    }

    /**
     * 获得所有视频
     *
     * @return
     */
    public ArrayList<PicVideoBean> getVideos() {
        ArrayList<PicVideoBean> videos = new ArrayList<>();
        String[] mColumns = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION};
        String mSelect = MediaStore.Video.Media.MIME_TYPE + "=?AND " + MediaStore.Video.Media.DURATION + "<?AND " + MediaStore.Video.Media.SIZE + "<?";
        String[] mSelectArgs = {"video/mp4", String.valueOf(VIDEO_TIME), String.valueOf(VIDEO_BIG_SIZE)};
        Cursor mCursor = mResolver.query(mVideoTable, mColumns, mSelect, mSelectArgs, MediaStore.Video.Media.DATE_ADDED + " DESC");
        if (null != mCursor && mCursor.getCount() != 0) {
//            mCursor.moveToFirst();
            while (mCursor.moveToNext()) {
                PicVideoBean bean = new PicVideoBean();
                bean.setPath(mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                bean.setVideoDuration(mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
                videos.add(bean);
            }
        } else {
            return videos;
        }
        if (!mCursor.isClosed()) {
            mCursor.close();
            mCursor = null;
        }
        return videos;
    }


}
