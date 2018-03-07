package com.example.lcc.albumvideoselectdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lcc.albumvideoselectdemo.R;
import com.example.lcc.albumvideoselectdemo.bean.AlbumBean;
import com.example.lcc.albumvideoselectdemo.utils.MyApp;

import java.util.List;

/**
 * Created by lcc on 2017/12/25.
 */

public class AlbumListAdapter extends BaseAdapter {
    private List<AlbumBean> datas;
    private Context mContext;
    private LayoutInflater inflater;

    public AlbumListAdapter(Context mContext, List<AlbumBean> bean) {
        this.mContext = mContext;
        datas = bean;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mvh = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.album_item, null);
            mvh = new ViewHolder();
            mvh.img = (ImageView) convertView.findViewById(R.id.img_id);
            mvh.tv = (TextView) convertView.findViewById(R.id.tv_id);
            mvh.count = (TextView) convertView.findViewById(R.id.tv_count_id);
            convertView.setTag(mvh);
        } else {
            mvh = (ViewHolder) convertView.getTag();
        }
        MyApp.getApp().getLoader().setImageResource(datas.get(position).getAlbumPic(), mvh.img);
        mvh.tv.setText(datas.get(position).getName());
        mvh.count.setText(datas.get(position).getCount()+"张照片");
        return convertView;
    }

    class ViewHolder {
        private ImageView img;
        private TextView tv;
        private TextView count;
    }
}
