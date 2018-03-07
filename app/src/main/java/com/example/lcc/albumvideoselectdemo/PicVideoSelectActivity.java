package com.example.lcc.albumvideoselectdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.example.lcc.albumvideoselectdemo.bean.TemBean;
import com.example.lcc.albumvideoselectdemo.myInterface.ImageDisplayAdapter;
import com.example.lcc.albumvideoselectdemo.utils.DataHelper;
import com.example.lcc.albumvideoselectdemo.utils.MyApp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PicVideoSelectActivity extends AppCompatActivity implements DataHelper.getPicOrVideoListener, PopupWindow.OnDismissListener {
    private RecyclerView mRecyclerView;
    private PicVideoAdapter mAdapter;
    private TextView text_back;
    private TextView text_title;
    private TextView text_other;
    private ArrayList<PicVideoBean> lists;
    private ArrayList<PicVideoBean> lists1 = new ArrayList<>();
    private ArrayList<AlbumBean> Albums;
    private DataHelper helper;
    private int mSelect;
    private TextView mFinish;
    private PopupWindow mPopwindow;
    private PopupWindow mPopwindow_pic;
    private AlbumListAdapter adapter;
    private BigPicAdapter mBigPicAdapter;
    private PhotoAdapter mBigPicAdapter1;
    private ArrayList<PicVideoBean> bigPicList;
    private List<Integer> refreshList = new ArrayList<>();
    private final int MSG_WHAT_CHAT_UPDATE = 0x1;
    private String title = "最近相册";
    //切换相册用的局部刷新
    private Handler updateChatsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_CHAT_UPDATE:
                    DiffUtil.DiffResult result = (DiffUtil.DiffResult) msg.obj;
                    //界面更新
                    result.dispatchUpdatesTo(mAdapter);
//                    mAdapter.setData(lists);
                    lists1.clear();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_video_select);
        helper = new DataHelper(this);
        mSelect = getIntent().getIntExtra("select", 0);
        lists = new ArrayList<>();//照片或者视频的数据集
        Albums = new ArrayList<>();//相册的集合
        bigPicList = new ArrayList<>();//显示大图
        adapter = new AlbumListAdapter(this, Albums);//显示相册的adapter
        mBigPicAdapter = new BigPicAdapter(this, bigPicList);
        mBigPicAdapter1 = new PhotoAdapter(this, bigPicList);//显示大图
        text_back = (TextView) findViewById(R.id.text_back);
        text_title = (TextView) findViewById(R.id.text_title);
        text_other = (TextView) findViewById(R.id.text_other);
        mFinish = (TextView) findViewById(R.id.btn_finish);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_id);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        if (mSelect == 1) {
            text_title.setText("我的相册");
            helper.getPic(this);
            mAdapter = new PicVideoAdapter(this, lists, 1);
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
                mAdapter.selectPics1.clear();

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
                        int selCount = mAdapter.selectPics.size();
                        if (selCount >= 9) {
                            Toast.makeText(PicVideoSelectActivity.this, "超过9张图了", Toast.LENGTH_LONG).show();
                            return;
                        }
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
                        //查看大图
                        showBigPic1(index);
                    } else {
                        //播放视频
                        Intent intent = new Intent(PicVideoSelectActivity.this, VideoPlayActivity.class);
                        intent.putExtra("path", path);
                        startActivity(intent);
                    }
                }
            }


        });
    }


    @Override
    public void getPicOrVideo(ArrayList<PicVideoBean> bean) {
        //占第一位
        lists1.addAll(lists);
        lists.clear();
        PicVideoBean bean1 = new PicVideoBean("", "",0+"");
        lists.addAll(bean);
        lists.add(0, bean1);
        notifyDataSetChanged(lists1, lists);
        //为查看大图赋值
        if (mSelect == 1) {
            bigPicList.clear();
            bigPicList.addAll(lists);
            bigPicList.remove(0);
        }
    }

    @Override
    public void getPicOrVideo(Map<String, Object> bean) {
        Albums.clear();
        Albums.addAll((ArrayList<AlbumBean>) bean.get("album"));
        //当list为空的时候，说明是第一次进入，因为有null的情况，会导致项目崩溃
        if (lists == null || lists.size() == 0) {
            PicVideoBean bean1 = new PicVideoBean("", "",0+"");
            ArrayList<PicVideoBean> picList = (ArrayList<PicVideoBean>) bean.get("pic");
            lists.addAll(picList);
            lists.add(0, bean1);
            mAdapter.notifyDataSetChanged();
        } else {
            //占第一位
            lists1.addAll(lists);
            lists.clear();
            PicVideoBean bean1 = new PicVideoBean("", "",0+"");
            ArrayList<PicVideoBean> picList = (ArrayList<PicVideoBean>) bean.get("pic");
            lists.addAll(picList);
            lists.add(0, bean1);
            notifyDataSetChanged(lists1, lists);
        }
        //为大图赋值
        bigPicList.clear();
        bigPicList.addAll(lists);
        bigPicList.remove(0);
    }

    //局部刷新，不用了
    public void notifyDataSetChanged(final List<PicVideoBean> mOldDatas, final List<PicVideoBean> mNewDatas) {
        //为了防止数据量过大，比对算法耗时，将算法放入新线程执行
        new Thread(new Runnable() {
            @Override
            public void run() {
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallBack(mOldDatas, mNewDatas), true);
                Message message = updateChatsHandler.obtainMessage(MSG_WHAT_CHAT_UPDATE);
                message.obj = result;
                message.sendToTarget();
            }
        }).start();


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
                title = Albums.get(position).getName();
                text_title.setText(title);
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
     * 显示大图(初级)
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

    /**
     * 显示大图
     */
    public void showBigPic1(final int position) {
        refreshList.clear();
        View view = getLayoutInflater().inflate(R.layout.show_big_pic, null);
        final ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager_preview_photo);
        final TextView checkbox_sel_num = (TextView) view.findViewById(R.id.pager_select);//已选相册的序数
        final TextView complete = (TextView) view.findViewById(R.id.pre_tv_to_confirm);//完成数量
        final ImageView mCheckBox = (ImageView) view.findViewById(R.id.button_id);
        ImageView backImg = (ImageView) view.findViewById(R.id.pre_head_view_back);
        TextView backText = (TextView) view.findViewById(R.id.pre_head_view_back_t);
        TextView titleText = (TextView) view.findViewById(R.id.pre_head_view_title);
        titleText.setText(title);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopwindow_pic.isShowing()) {
                    mPopwindow_pic.dismiss();
                }

            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopwindow_pic.isShowing()) {
                    mPopwindow_pic.dismiss();
                }
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopwindow_pic.isShowing()) {
                    mPopwindow_pic.dismiss();
                }
            }
        });
        updateCountView(complete);
        mViewPager.setAdapter(mBigPicAdapter1);
        mViewPager.setCurrentItem(position - 1, true);
        if (mAdapter.selectPics.contains(bigPicList.get(position - 1).getPath())) {
            mCheckBox.setImageResource(R.drawable.select_shape);
            checkbox_sel_num.setVisibility(View.VISIBLE);
            checkbox_sel_num.setText(String.valueOf(mAdapter.selectPics.indexOf(bigPicList.get(position - 1).getPath()) + 1));
        } else {
            mCheckBox.setImageResource(R.drawable.unselect_shape);
            checkbox_sel_num.setVisibility(View.INVISIBLE);
        }
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = mViewPager.getCurrentItem();
                boolean selFlag = mAdapter.selectPics.contains(bigPicList.get(index).getPath());
                if (selFlag) {
                    mAdapter.selectPics1.remove(mAdapter.selectPics.indexOf(bigPicList.get(index).getPath()));
                    mAdapter.selectPics.remove(bigPicList.get(index).getPath());
                    refreshList.add(index + 1);
                    checkbox_sel_num.setVisibility(View.INVISIBLE);
                    mCheckBox.setImageResource(R.drawable.unselect_shape);
                } else {
                    int selCount = mAdapter.selectPics.size();
                    if (selCount >= 9) {
                        Toast.makeText(PicVideoSelectActivity.this, "超过9张图了", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mCheckBox.setImageResource(R.drawable.select_shape);
                    checkbox_sel_num.setVisibility(View.VISIBLE);
                    mAdapter.selectPics.add(bigPicList.get(index).getPath());
                    mAdapter.selectPics1.add(new TemBean(bigPicList.get(index).getPath(), index + 1));
                    checkbox_sel_num.setText(String.valueOf(mAdapter.selectPics.indexOf(bigPicList.get(index).getPath()) + 1));
                }
                updateCountView(complete);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mAdapter.selectPics.contains(bigPicList.get(position).getPath())) {
                    mCheckBox.setImageResource(R.drawable.select_shape);
                    checkbox_sel_num.setVisibility(View.VISIBLE);
                    checkbox_sel_num.setText(String.valueOf(mAdapter.selectPics.indexOf(bigPicList.get(position).getPath()) + 1));
                } else {
                    mCheckBox.setImageResource(R.drawable.unselect_shape);
                    checkbox_sel_num.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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


    class PhotoAdapter extends PagerAdapter {
        private List<PicVideoBean> imgList;

        public PhotoAdapter(Context content, List<PicVideoBean> viewList) {
            this.imgList = viewList;
        }

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            ImageView photoView = new ImageView(PicVideoSelectActivity.this);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            MyApp.getApp().getLoader().setImageResource(imgList.get(position).getPath(), photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private void updateCountView(TextView mCountText) {
        if (mAdapter.selectPics.size() == 0) {
            mCountText.setEnabled(false);
            mCountText.setText("已选" + "(" + mAdapter.selectPics.size() + "/" + 9 + ")");
        } else {
            mCountText.setEnabled(true);
            mCountText.setText("已选" + "(" + mAdapter.selectPics.size() + "/" + 9 + ")");
        }
    }


    @Override
    public void onDismiss() {
        for (int i = 0; i < mAdapter.selectPics.size(); i++) {
            for (int j = 1; j < lists.size(); j++) {
                if (lists.get(j).getPath().equals(mAdapter.selectPics.get(i))) {
                    Bundle bundle = new Bundle();
                    bundle.putString("number", (mAdapter.selectPics.indexOf(mAdapter.selectPics1.get(i).getmPath()) + 1) + "");
                    mAdapter.notifyItemChanged(j, bundle);
                    continue;
                }
            }

        }
        for (int i = 0; i < refreshList.size(); i++) {
            mAdapter.notifyItemChanged(refreshList.get(i));
        }
        mPopwindow_pic = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAdapter.selectPics.clear();
        mAdapter.selectPics1.clear();
    }
}
