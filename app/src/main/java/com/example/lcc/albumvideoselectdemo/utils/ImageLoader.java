package com.example.lcc.albumvideoselectdemo.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lcc.albumvideoselectdemo.R;

/**
 * Created by lcc on 2017/12/6.
 */

public class ImageLoader {
    private Context context;

    public ImageLoader(Context context) {
        this.context = context;
    }

    public void setImageResource(String path, ImageView img) {
        Glide.with(context)
                .load(path)
                .error(R.mipmap.ic_launcher)
                .into(img);

    }
    public void setVideoResource(Uri path, ImageView img) {
        Glide.with(context)
                .load(path)
                .error(R.mipmap.ic_launcher)
                .into(img);

    }
}
