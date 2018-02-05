package com.example.lcc.albumvideoselectdemo.nineimage;

import android.content.Context;
import java.util.List;

/**
 * Created by wuyinlei on 2017/3/21.
 */

public class NineGridClickViewAdapter extends NineGridViewAdapter {

    private Context mContext;

    public NineGridClickViewAdapter(Context context, List<ImageInfo> imageInfo) {
        super(context, imageInfo);
        this.mContext = context;
    }

    @Override
    protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<ImageInfo> imageInfo) {

        String name = imageInfo.get(0).getName();

//        ArrayList<FoodViewPic> piclist = new ArrayList<>();
//
//        int size = imageInfo.size();
//
//        boolean isSuccess = false;
//
//        if (size == 1) {
//
//            ImageInfo singleImageInfo = imageInfo.get(0);
//            boolean isVideo = singleImageInfo.isVideo;
//            if (isVideo) {
//                Intent intent = new Intent(mContext, VideoPlayActivity.class);
//                intent.putExtra("video_url", singleImageInfo.getVideopath());
//                if (!TextUtils.isEmpty(singleImageInfo.getName())) {
//                    intent.putExtra("video_name", singleImageInfo.getName());
//                }
//                mContext.startActivity(intent);
//            } else {
//                isSuccess = true;
//            }
//
//
//        } else {
//            isSuccess = true;
//        }
//
//        if (isSuccess) {
//            for (ImageInfo info : imageInfo) {
//                piclist.add(new FoodViewPic(info.getThumbnailUrl(), info.getBigImageUrl(), info.getName()));
//            }
//
//            Intent intent = new Intent(context, FoodImageActivity.class);
//            intent.putExtra("position", index + "");
//            intent.putExtra("name", name);
//            //传递一个对象数组
//            Bundle bu = new Bundle();
//            bu.putSerializable("piclist", (Serializable) piclist);
//            intent.putExtras(bu);
//            context.startActivity(intent);
//        }


    }

    @Override
    protected void onImageItemClick(Context context, NineMyInfoGridView nineGridView, int index, List<ImageInfo> imageInfo) {
        String name = imageInfo.get(0).getName();
//
//        ArrayList<FoodViewPic> piclist = new ArrayList<>();
//
//        for (ImageInfo info : imageInfo) {
//            piclist.add(new FoodViewPic(info.getThumbnailUrl(), info.getBigImageUrl(), info.getName()));
//        }
//
//        Intent intent = new Intent(context, FoodImageActivity.class);
//        intent.putExtra("position", index + "");
//        intent.putExtra("name", name);
//        //传递一个对象数组
//        Bundle bu = new Bundle();
//        bu.putSerializable("piclist", (Serializable) piclist);
//        intent.putExtras(bu);
//        context.startActivity(intent);
    }

    /**
     * 获得状态栏的高度
     */
    public int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


}
