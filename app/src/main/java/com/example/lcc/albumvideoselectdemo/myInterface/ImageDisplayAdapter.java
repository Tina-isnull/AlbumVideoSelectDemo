package com.example.lcc.albumvideoselectdemo.myInterface;


import com.example.lcc.albumvideoselectdemo.bean.PicVideoBean;

import java.util.List;

/**
 * Created by lcc on 2017/12/15.
 */

public interface ImageDisplayAdapter {
    void onItemClick(int index, String path);

    //无法实现联动刷新
//    void notifyDataSetChanged(List<PicVideoBean> mOldDatas, List<PicVideoBean> mNewDatas);
}
