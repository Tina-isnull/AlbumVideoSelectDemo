package com.example.lcc.albumvideoselectdemo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.lcc.albumvideoselectdemo.bean.AlbumBean;
import com.example.lcc.albumvideoselectdemo.bean.PicVideoBean;

import java.util.ArrayList;
import java.util.Map;

/**
 * 图片视频选择工具类
 * Created by lcc on 2017/12/5.
 */

public class DataHelper {
    private DataLoader loader;
    private Handler handler;

    public DataHelper(Context context) {
        loader = new DataLoader(context);
        handler = new Handler(Looper.getMainLooper());
    }

    public void getPic(final getPicOrVideoListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String,Object> pics = loader.getPics1();
                if (null != pics && pics.size() != 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.getPicOrVideo(pics);
                        }
                    });
                }

            }
        }).start();

    }

    public void getVideo(final getPicOrVideoListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<PicVideoBean> videos = loader.getVideos();
                if (null != videos && videos.size() != 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.getPicOrVideo(videos);
                        }
                    });
                }

            }
        }).start();

    }

    public void getAlbumPic(final getPicOrVideoListener listener, final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<PicVideoBean> pics = loader.getAlbumPics(name);
                if (null != pics && pics.size() != 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.getPicOrVideo(pics);
                        }
                    });
                }

            }
        }).start();

    }


    public interface getPicOrVideoListener {
        void getPicOrVideo(ArrayList<PicVideoBean> bean);
        void getPicOrVideo(Map<String,Object> bean);
    }
}
