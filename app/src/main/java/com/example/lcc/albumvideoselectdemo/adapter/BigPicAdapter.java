package com.example.lcc.albumvideoselectdemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lcc.albumvideoselectdemo.R;
import com.example.lcc.albumvideoselectdemo.bean.PicVideoBean;
import com.example.lcc.albumvideoselectdemo.utils.MyApp;

import java.util.List;

/**
 * Created by lcc on 2018/1/10.
 */

public class BigPicAdapter extends PagerAdapter {
    private Context context;
    private List<PicVideoBean> viewList;

    public BigPicAdapter(Context content, List<PicVideoBean> viewList) {
        this.context = content;
        this.viewList = viewList;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView((View) object);

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_pic, null);
        final ImageView photoView = (ImageView) v.findViewById(R.id.img_id);
        final TextView tv_count = (TextView) v.findViewById(R.id.pager_select);
        final ImageView buttonSelect = (ImageView) v.findViewById(R.id.button_id);
        if (PicVideoAdapter.selectPics.contains(viewList.get(position).getPath())) {
            tv_count.setText((PicVideoAdapter.selectPics.indexOf(viewList.get(position).getPath())+1) + "");
            buttonSelect.setImageResource(R.drawable.select_shape);
            photoView.setColorFilter(0x80000000);
        } else {
            tv_count.setText("");
            buttonSelect.setImageResource(R.drawable.unselect_shape);
            photoView.setColorFilter(null);
        }

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PicVideoAdapter.selectPics.contains(viewList.get(position).getPath())) {
                    PicVideoAdapter.selectPics.remove(viewList.get(position).getPath());
                    buttonSelect.setImageResource(R.drawable.unselect_shape);
                    photoView.setColorFilter(null);
                    tv_count.setText("");
                    notifyDataSetChanged();
                } else {
                    PicVideoAdapter.selectPics.add(viewList.get(position).getPath());
                    buttonSelect.setImageResource(R.drawable.select_shape);
                    photoView.setColorFilter(0x80000000);
                    tv_count.setText((PicVideoAdapter.selectPics.indexOf(viewList.get(position).getPath())+1) + "");
                    notifyDataSetChanged();
                }
            }
        });
        container.addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        MyApp.getApp().getLoader().setImageResource(viewList.get(position).getPath(), photoView);
        return v;
    }

}
