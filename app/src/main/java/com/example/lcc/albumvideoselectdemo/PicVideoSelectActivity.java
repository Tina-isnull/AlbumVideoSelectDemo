package com.example.lcc.albumvideoselectdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcc.albumvideoselectdemo.adapter.AlbumListAdapter;
import com.example.lcc.albumvideoselectdemo.adapter.BigPicAdapter;
import com.example.lcc.albumvideoselectdemo.adapter.DiffCallBack;
import com.example.lcc.albumvideoselectdemo.adapter.PicVideoAdapter;
import com.example.lcc.albumvideoselectdemo.bean.AlbumBean;
import com.example.lcc.albumvideoselectdemo.bean.PicVideoBean;
import com.example.lcc.albumvideoselectdemo.myInterface.ImageDisplayAdapter;
import com.example.lcc.albumvideoselectdemo.utils.DataHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class PicVideoSelectActivity extends AppCompatActivity implements DataHelper.getPicOrVideoListener, PopupWindow.OnDismissListener {
    private RecyclerView mRecyclerView;
    private PicVideoAdapter mAdapter;
    private TextView text_back;
    private TextView text_title;
    private TextView text_other;
    private ArrayList<PicVideoBean> lists;
    private ArrayList<AlbumBean> Albums;
    private DataHelper helper;
    private int mSelect;
    private Button mFinish;
    private PopupWindow mPopwindow;
    private PopupWindow mPopwindow_pic;
    private AlbumListAdapter adapter;
    private BigPicAdapter mBigPicAdapter;
    private ArrayList<PicVideoBean> bigPicList;
    private final int MSG_WHAT_CHAT_UPDATE=0x1;
    private Handler updateChatsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_WHAT_CHAT_UPDATE:
                    DiffUtil.DiffResult result = (DiffUtil.DiffResult)msg.obj;
                    //界面更新
                    result.dispatchUpdatesTo(mAdapter);
                    mAdapter.setData();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_video_select);
        helper = new DataHelper(this);
        mSelect = getIntent().getIntExtra("select", 0);
        lists = new ArrayList<>();
        Albums = new ArrayList<>();
        bigPicList = new ArrayList<>();
        adapter = new AlbumListAdapter(this, Albums);
        mBigPicAdapter = new BigPicAdapter(this, bigPicList);
        text_back = (TextView) findViewById(R.id.text_back);
        text_title = (TextView) findViewById(R.id.text_title);
        text_other = (TextView) findViewById(R.id.text_other);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_id);
        mFinish = (Button) findViewById(R.id.btn_finish);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        if (mSelect == 1) {
            text_title.setText("我的相册");
            helper.getPic(this);
            mAdapter = new PicVideoAdapter(this, lists,1);
        } else {
            text_title.setText("我的视频");
            helper.getVideo(this);
            mAdapter = new PicVideoAdapter(this, lists, 2);
        }
        mRecyclerView.setAdapter(mAdapter);
        text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mAdapter.selectPics.clear();

            }
        });
        text_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelect == 1) {
                    showPop();
                } else {
                    Toast.makeText(PicVideoSelectActivity.this, "视频没有相册", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PicVideoSelectActivity.this, ShowPicsActivity.class);
                startActivity(intent);
            }
        });
        mAdapter.setImageDisplyAdapter(new ImageDisplayAdapter() {
            @Override
            public void onItemClick(int index, String path) {
                if (path.equals("")) {
                    if (mSelect == 1) {
                        //相机
                        Intent intent = new Intent(PicVideoSelectActivity.this, LccameraActivity.class);
                        startActivity(intent);
                    } else {
                        //视频
                        Intent intent = new Intent(PicVideoSelectActivity.this, RecordVideoActivity.class);
                        startActivity(intent);
                    }
                } else {
                    if (mSelect == 1) {
                        //相机
                        showBigPic(index);
                    } else {
                        //视频
                        Intent intent = new Intent(PicVideoSelectActivity.this, VideoPlayActivity.class);
                        intent.putExtra("path", path);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void notifyDataSetChanged(final List<PicVideoBean> mOldDatas, final List<PicVideoBean> mNewDatas) {
                //为了防止数据量过大，比对算法耗时，将算法放入新线程执行
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallBack(mOldDatas, mNewDatas),true);
                        Message message = updateChatsHandler.obtainMessage(MSG_WHAT_CHAT_UPDATE);
                        message.obj = result;
                        message.sendToTarget();
                    }
                }).start();


            }
        });
    }


    @Override
    public void getPicOrVideo(ArrayList<PicVideoBean> bean) {
        //占第一位
        lists.clear();
        PicVideoBean bean1 = new PicVideoBean();
        bean.add(0, bean1);
        lists.addAll(bean);
        //为查看大图赋值
        if (mSelect == 1) {
            bigPicList.addAll(lists);
            bigPicList.remove(0);
            mBigPicAdapter.notifyDataSetChanged();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getPicOrVideo(Map<String, Object> bean) {
        Albums.clear();
        Albums.addAll((ArrayList<AlbumBean>) bean.get("album"));
        adapter.notifyDataSetChanged();

        //占第一位
        lists.clear();
        PicVideoBean bean1 = new PicVideoBean("", "", "");
        ArrayList<PicVideoBean> picList = (ArrayList<PicVideoBean>) bean.get("pic");
        picList.add(0, bean1);
        lists.addAll(picList);
        //为大图赋值
        bigPicList.addAll(lists);
        bigPicList.remove(0);
        mBigPicAdapter.notifyDataSetChanged();

        mAdapter.notifyDataSetChanged();


    }

    /**
     * 显示相册
     */
    public void showPop() {
        //获得相册的数据
        View view = getLayoutInflater().inflate(R.layout.pop_album, null);
        ListView listView = (ListView) view.findViewById(R.id.listView_id);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                text_title.setText(Albums.get(position).getName());
                if (Albums.get(position).getName().equals("最近相册")) {
                    helper.getPic(PicVideoSelectActivity.this);
                } else {
                    helper.getAlbumPic(PicVideoSelectActivity.this, Albums.get(position).getName());
                }
                if (mPopwindow != null) {
                    mPopwindow.dismiss();
                }

            }
        });
        if (mPopwindow == null) {
            mPopwindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
//            mPopwindow.setBackgroundDrawable(new ColorDrawable(0xffffff));
            mPopwindow.setOutsideTouchable(true);


        }
        if (mPopwindow.isShowing()) {
            mPopwindow.dismiss();
        } else {
            mPopwindow.showAtLocation(text_title, Gravity.TOP, 0, 0);
        }

    }

    /**
     * 显示大图
     */
    public void showBigPic(final int position) {
        View view = getLayoutInflater().inflate(R.layout.pic_show, null);
        final ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager_id);
        mViewPager.setAdapter(mBigPicAdapter);
        mViewPager.setCurrentItem(position - 1, true);
        if (mPopwindow_pic == null) {
            mPopwindow_pic = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopwindow_pic.setOutsideTouchable(true);
            mPopwindow_pic.setOnDismissListener(this);
        }
        if (mPopwindow_pic.isShowing()) {
            mPopwindow_pic.dismiss();
        } else {
            mPopwindow_pic.showAtLocation(text_title, Gravity.TOP, 0, 0);
        }
    }
    @Override
    public void onDismiss() {
        mAdapter.notifyDataSetChanged();
        mPopwindow_pic = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAdapter.selectPics.clear();
    }
}
