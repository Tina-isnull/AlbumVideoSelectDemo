package com.example.lcc.albumvideoselectdemo.adapter;

import android.content.Context;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcc.albumvideoselectdemo.PicVideoSelectActivity;
import com.example.lcc.albumvideoselectdemo.R;
import com.example.lcc.albumvideoselectdemo.bean.PicVideoBean;
import com.example.lcc.albumvideoselectdemo.bean.TemBean;
import com.example.lcc.albumvideoselectdemo.myInterface.ImageDisplayAdapter;
import com.example.lcc.albumvideoselectdemo.utils.MyApp;
import com.example.lcc.albumvideoselectdemo.utils.TimeFormat;
import com.example.lcc.albumvideoselectdemo.view.SquareRelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc on 2017/12/4.
 */

public class PicVideoAdapter extends RecyclerView.Adapter<PicVideoAdapter.MyHolderView> {
    private Context context;
    private ArrayList<PicVideoBean> list;
    //select 1为图片 2为视频
    private int select;
    public static List<String> selectPics = new ArrayList<>();
    public static List<TemBean> selectPics1 = new ArrayList<>();
    private ImageDisplayAdapter adapter;

    public PicVideoAdapter(Context context, ArrayList<PicVideoBean> list, int select) {
        this.context = context;
        this.list = list;
        this.select = select;

    }

    public void setImageDisplyAdapter(ImageDisplayAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolderView view = null;
        if (viewType == 0) {
            view = new MyHolderView(LayoutInflater.from(context).inflate(R.layout.grid_camera, parent, false));
        } else {
            view = new MyHolderView(LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false));
        }
        return view;
    }

    @Override
    public void onBindViewHolder(final MyHolderView holder, final int position) {
        if (position == 0) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.onItemClick(0, "");
                }
            });
        } else {
            if (select == 1) {
                MyApp.getApp().getLoader().setImageResource(list.get(position).getPath(), holder.pic);
                holder.time.setVisibility(View.GONE);
                if (selectPics.contains(list.get(position).getPath())) {
                    selectPics1.get(selectPics.indexOf(list.get(position).getPath())).setmPotion(position);
                    holder.button.setImageResource(R.drawable.select_shape);
                    holder.pic.setColorFilter(0x80000000);
                    holder.pager.setText((selectPics.indexOf(list.get(position).getPath()) + 1) + "");
//                    holder.pager.setText(list.get(position).getSelectNumber());

                } else {
                    holder.button.setImageResource(R.drawable.unselect_shape);
                    holder.pic.setColorFilter(null);
                    holder.pager.setText("");
                }
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectPics.contains(list.get(position).getPath())) {
                            int start = selectPics.indexOf(list.get(position).getPath());
                            selectPics1.remove(selectPics.indexOf(list.get(position).getPath()));
                            selectPics.remove(list.get(position).getPath());
                            holder.button.setImageResource(R.drawable.unselect_shape);
                            holder.pic.setColorFilter(null);
                            holder.pager.setText("");
                            for (int i = start; i < selectPics1.size(); i++) {
                                for(int j=1;j<list.size();j++){
                                    if(list.get(j).getPath().equals(selectPics1.get(i).getmPath())){
                                        Bundle bundle = new Bundle();
                                        bundle.putString("number", (selectPics.indexOf(selectPics1.get(i).getmPath()) + 1) + "");
                                        notifyItemChanged(j, bundle);
                                        continue;
                                    }
                                }

                            }

                        } else {
                            int selCount = selectPics.size();
                            if (selCount >= 9) {
                                Toast.makeText(context, "超过9张图了", Toast.LENGTH_LONG).show();
                                return;
                            }
                            selectPics.add(list.get(position).getPath());
                            selectPics1.add(new TemBean(list.get(position).getPath(), position));
                            holder.button.setImageResource(R.drawable.select_shape);
                            holder.pic.setColorFilter(0x80000000);
                            holder.pager.setText((selectPics.indexOf(list.get(position).getPath()) + 1) + "");

                        }
                    }
                });

                holder.pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.onItemClick(position, list.get(position).getPath());
                    }
                });

            } else {
                MyApp.getApp().getLoader().setVideoResource(Uri.fromFile(new File(list.get(position).getPath())), holder.pic);
                holder.time.setVisibility(View.VISIBLE);
                holder.time.setText(TimeFormat.duringFormat(Long.parseLong(list.get(position).getVideoDuration())));
                holder.pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.onItemClick(position, list.get(position).getPath());
                    }
                });
            }

        }
    }

    @Override
    public void onBindViewHolder(MyHolderView holder, int position, List<Object> payloads) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position);
        else {
            Bundle bundle = (Bundle) payloads.get(0);
            for (String key : bundle.keySet()) {
                switch (key) {
//                    case "path":
//                        MyApp.getApp().getLoader().setImageResource(list.get(position).getPath(), holder.pic);
//                        break;
                    case "number":
                        holder.button.setImageResource(R.drawable.select_shape);
                        holder.pic.setColorFilter(0x80000000);
                        holder.pager.setText((CharSequence) bundle.get(key));
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public void setData(ArrayList<PicVideoBean> newlist) {
        list.clear();
        list.addAll(newlist);
    }

    class MyHolderView extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView time;
        private TextView pager;
        private ImageView button;
        private SquareRelativeLayout layout;

        public MyHolderView(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.img_id);
            time = (TextView) itemView.findViewById(R.id.text_id);
            pager = (TextView) itemView.findViewById(R.id.pager_select);
            button = (ImageView) itemView.findViewById(R.id.button_id);
            layout = (SquareRelativeLayout) itemView.findViewById(R.id.sr_first);
        }
    }

}
