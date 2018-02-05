package com.example.lcc.albumvideoselectdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lcc.albumvideoselectdemo.adapter.PicVideoAdapter;
import com.example.lcc.albumvideoselectdemo.nineimage.ImageInfo;
import com.example.lcc.albumvideoselectdemo.nineimage.NineGridClickViewAdapter;
import com.example.lcc.albumvideoselectdemo.nineimage.NineGridView;
import com.example.lcc.albumvideoselectdemo.utils.MyApp;

import java.util.ArrayList;
import java.util.List;

public class ShowPicsActivity extends AppCompatActivity implements NineGridView.ImageLoader{
    private NineGridView nineGridView;
    private List<ImageInfo> lists=new ArrayList<>();
    private TextView text_title;
    private TextView text_other;
    private TextView text_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pics);
        nineGridView = (NineGridView) findViewById(R.id.nineGridId);
        text_title= (TextView) findViewById(R.id.text_title);
        text_other= (TextView) findViewById(R.id.text_other);
        text_back= (TextView) findViewById(R.id.text_back);
        text_other.setVisibility(View.INVISIBLE);
        text_title.setText("已选相册");
        text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for (int i=0;i< PicVideoAdapter.selectPics.size();i++){
            ImageInfo info=new ImageInfo(PicVideoAdapter.selectPics.get(i),false,false);
            lists.add(info);
        }
        nineGridView.setImageLoader(this);
        nineGridView.setAdapter(new NineGridClickViewAdapter(this, lists));
    }

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        MyApp.getApp().getLoader().setImageResource(url,imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }

}
