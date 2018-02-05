package com.example.lcc.albumvideoselectdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.example.lcc.albumvideoselectdemo.nineimage.ImageInfo;
import com.example.lcc.albumvideoselectdemo.nineimage.NineGridClickViewAdapter;
import com.example.lcc.albumvideoselectdemo.nineimage.NineGridView;
import com.example.lcc.albumvideoselectdemo.utils.MyApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private Button picButton;
    private Button videoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picButton = (Button) findViewById(R.id.btn_pic_id);
        videoButton = (Button) findViewById(R.id.btn_video_id);
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PicVideoSelectActivity.class);
                intent.putExtra("select",1);
                startActivityForResult(intent,0);


            }
        });
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PicVideoSelectActivity.class);
                intent.putExtra("select",2);
                startActivityForResult(intent,1);

            }
        });
    }
}
