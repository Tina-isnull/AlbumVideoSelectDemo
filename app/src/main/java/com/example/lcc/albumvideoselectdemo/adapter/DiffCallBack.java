package com.example.lcc.albumvideoselectdemo.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.lcc.albumvideoselectdemo.bean.PicVideoBean;

import java.util.List;

/**
 * Created by lcc on 2018/1/12.
 */

public class DiffCallBack extends DiffUtil.Callback {
    private List<PicVideoBean> mOldDatas, mNewDatas;//看名字
    public DiffCallBack(List<PicVideoBean> mOldDatas, List<PicVideoBean> mNewDatas) {
        this.mOldDatas = mOldDatas;
        this.mNewDatas = mNewDatas;
    }

    @Override
    public int getOldListSize() {
        return mOldDatas != null ? mOldDatas.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewDatas != null ? mNewDatas.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldDatas.get(oldItemPosition).getPath().equals(mNewDatas.get(newItemPosition).getPath());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        PicVideoBean beanOld = mOldDatas.get(oldItemPosition);
        PicVideoBean beanNew = mNewDatas.get(newItemPosition);
        if (!beanOld.getPath().equals(beanNew.getPath())) {
            return false;//如果有内容不同，就返回false
        }
//        if (beanOld.getVideoDuration() != beanNew.getVideoDuration()) {
//            return false;//如果有内容不同，就返回false
//        }
        if (!beanOld.getSelectNumber().equals(beanNew.getSelectNumber())) {
            return false;//如果有内容不同，就返回false
        }
        return true; //默认两个data内容是相同的
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        String beanOldPath = mOldDatas.get(oldItemPosition).getPath();
        String beanNewPath = mNewDatas.get(newItemPosition).getPath();
        String beanOldNumber = mOldDatas.get(oldItemPosition).getSelectNumber();
        String beanNewNumber = mNewDatas.get(newItemPosition).getSelectNumber();
        Bundle bundle = new Bundle();
        if ((beanNewPath == null && beanOldPath !=null) || (beanNewPath != null && beanOldPath == null) || (beanOldPath != null && !beanOldPath.equals(beanNewPath)))
            bundle.putString("path", beanNewPath);
        if(!beanOldNumber.equals(beanNewNumber))
            bundle.putString("number", beanNewNumber);
        if (bundle.size() == 0)
            return null;
        return bundle;
    }
}
