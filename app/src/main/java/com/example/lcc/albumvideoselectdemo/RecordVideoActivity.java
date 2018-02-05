package com.example.lcc.albumvideoselectdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.example.lcc.albumvideoselectdemo.bean.MovieRecorderView;
import com.example.lcc.albumvideoselectdemo.utils.MyApp;
import com.example.lcc.albumvideoselectdemo.utils.PrefUtils;
import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 视频拍摄页面
 * Created by Wood on 2016/4/6.
 */
public class RecordVideoActivity extends BaseActivity{
    private static final int REQ_CODE = 110;
    private static final int RES_CODE = 111;
    /**
     * 录制进度
     */
    private static final int RECORD_PROGRESS = 100;
    /**
     * 录制结束
     */
    private static final int RECORD_FINISH = 101;

    private MovieRecorderView movieRecorderView;
    private GifImageView buttonShoot;
    private RelativeLayout rlBottomRoot;
    private ProgressBar progressVideo;
    private TextView textViewCountDown;
    private TextView textViewReleaseToCancel;//释放取消

    private ImageView finishImg;
    private ImageView flash;
    private TextView recordNotifyText;
    private Boolean isAnimationShow=false;
    /**
     * 是否结束录制
     */
    private boolean isFinish = true;
    /**
     * 是否触摸在松开取消的状态
     */
    private boolean isTouchOnUpToCancel = false;
    /**
     * 当前进度
     */
    private float currentTime = 0;
    private String from;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECORD_PROGRESS:
                    progressVideo.setProgress((int) currentTime);
                    if (currentTime < 1000) {
                        //有分秒
                        textViewCountDown.setText((float) Math.round(((10 - currentTime / 100) * 10)) / 10 + "s");
                    } else {
                        textViewCountDown.setText(currentTime / 100 + "s");
                    }
                    break;
                case RECORD_FINISH:
                    if (isTouchOnUpToCancel) {//录制结束，还在上移删除状态没有松手，就复位录制
                        resetData();
                    } else {//录制结束，在正常位置，录制完成跳转页面
                        isFinish = true;
                        buttonShoot.setEnabled(false);
                        finishActivity();
                    }
                    break;
            }
        }
    };
    /**
     * 按下的位置
     */
    private float startY;
    private PrefUtils prefUtils;
    private Boolean isFirst = true;
    private ImageView img;
    private LinearLayout.LayoutParams shootparams;
    private final int CAMERA_OK=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
//        from=getIntent().getStringExtra("from");
        initView();
    }

    public void initView() {
//弹框
        prefUtils = new PrefUtils();
        movieRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        movieRecorderView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        buttonShoot = (GifImageView) findViewById(R.id.button_shoot);
        rlBottomRoot = (RelativeLayout) findViewById(R.id.rl_bottom_root);
        progressVideo = (ProgressBar) findViewById(R.id.progressBar_loading);
        textViewCountDown = (TextView) findViewById(R.id.textView_count_down);
        img = (ImageView) findViewById(R.id.video_tip);
        textViewCountDown.setText("10.0s");
        recordNotifyText = (TextView) findViewById(R.id.record_notify_text);
        textViewReleaseToCancel = (TextView) findViewById(R.id.textView_release_to_cancel);
        finishImg = (ImageView) findViewById(R.id.record_close);
        flash = (ImageView) findViewById(R.id.record_flash);

        finishImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnLight(movieRecorderView.getCamera(),movieRecorderView.getParameters());
            }
        });
        //控制录制控件和辅助控件的位置
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) movieRecorderView.getLayoutParams();
        layoutParams.height = width * 4 / 3;//根据屏幕宽度设置预览控件的尺寸，为了解决预览拉伸问题
        movieRecorderView.setLayoutParams(layoutParams);

        FrameLayout.LayoutParams rlBottomRootLayoutParams = (FrameLayout.LayoutParams) rlBottomRoot.getLayoutParams();
        rlBottomRootLayoutParams.height = height - layoutParams.height;
        rlBottomRoot.setLayoutParams(rlBottomRootLayoutParams);

        shootparams = (LinearLayout.LayoutParams) buttonShoot.getLayoutParams();

        //处理触摸事件
        buttonShoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    shootparams.height = MyApp.dip2px(80);
                    shootparams.width = MyApp.dip2px(80);
                    shootparams.topMargin = MyApp.dip2px(23);
                    buttonShoot.setLayoutParams(shootparams);
                    try {
                        buttonShoot.setImageDrawable(new GifDrawable(RecordVideoActivity.this.getResources(), R.mipmap.video_button_animation));
                    } catch (IOException e) {
                        Glide.with(RecordVideoActivity.this).load(R.mipmap.video_button_animation).asGif().fitCenter().priority(Priority.HIGH).into(buttonShoot);

                        e.printStackTrace();
                    }
                    isFinish = false;//开始录制
                    startY = event.getY();//记录按下的坐标
                    movieRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            handler.sendEmptyMessage(RECORD_FINISH);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    textViewUpToCancel.setVisibility(View.GONE);
                    textViewReleaseToCancel.setVisibility(View.GONE);
//                    recordNotifyText.setText(getString(R.string.record_notify));

                    shootparams.height = MyApp.dip2px(60);
                    shootparams.width = MyApp.dip2px(60);
                    shootparams.topMargin = MyApp.dip2px(33);
                    buttonShoot.setLayoutParams(shootparams);
                    Glide.with(RecordVideoActivity.this).load(R.mipmap.video_button_notclicked).into(buttonShoot);
                    if (startY - event.getY() > 100) {//上移超过一定距离取消录制，删除文件
                        if (!isFinish) {
                            resetData();
                        }
                    } else {
                        if (movieRecorderView.getTimeCount() > 300) {//录制时间超过三秒，录制完成
                            handler.sendEmptyMessage(RECORD_FINISH);
                        } else {//时间不足取消录制，删除文件
                            img.setVisibility(View.VISIBLE);
                            Animation animation= AnimationUtils.loadAnimation(RecordVideoActivity.this,R.anim.video_tip_anim);
                            isAnimationShow=prefUtils.getBoolean(RecordVideoActivity.this,"isAnimShow",false);
                            if(isAnimationShow){
                                img.startAnimation(animation);
                            }

                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    img.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            Toast.makeText(RecordVideoActivity.this, "时间太短", Toast.LENGTH_SHORT).show();
                            resetData();
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //根据触摸上移状态切换提示
                    if (startY - event.getY() > 100) {
                        isTouchOnUpToCancel = true;//触摸在松开就取消的位置
                    } else {
                        isTouchOnUpToCancel = false;//触摸在正常录制的位置
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    resetData();
                }
                return true;
            }
        });

        progressVideo.setMax(1000);
        movieRecorderView.setOnRecordProgressListener(new MovieRecorderView.OnRecordProgressListener() {
            @Override
            public void onProgressChanged(int maxTime, int currentTime) {

                RecordVideoActivity.this.currentTime = currentTime;
                handler.sendEmptyMessage(RECORD_PROGRESS);

            }
        });
    }

    /**
     * 闪光灯开关   开->关->自动
     *
     * @param mCamera
     */
    private void turnLight(Camera mCamera, Camera.Parameters parameters) {
        if (mCamera == null || parameters == null
                || parameters.getSupportedFlashModes() == null) {
            return;
        }

        String flashMode = parameters.getFlashMode();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            //关闭状态变打开
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(parameters);
            flash.setImageResource(R.mipmap.photo_shanguan_up);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
            //开启状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            flash.setImageResource(R.mipmap.photo_shanguang_automatic);
            mCamera.setParameters(parameters);
        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            flash.setImageResource(R.mipmap.photo_shanguang_down);
            mCamera.setParameters(parameters);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkCameraPermission();
        isFirst = prefUtils.getBoolean(this, "isGetLength", true);
        if (isFirst) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 检测摄像头和录音权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RecordVideoActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},CAMERA_OK);
        } else {
            resetData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode){
            case CAMERA_OK:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    resetData();
                }else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    Toast.makeText(this, "视频录制和录音没有授权,请在设置中开启授权", Toast.LENGTH_LONG);
                }
                break;
            default:
                break;
        }

    }
    /**
     * 重置状态
     */
    private void resetData() {
        if (movieRecorderView.getRecordFile() != null)
            movieRecorderView.getRecordFile().delete();
        movieRecorderView.stop();
        isFinish = true;
        currentTime = 0;
        progressVideo.setProgress(0);
        textViewCountDown.setText("10.0s");
        buttonShoot.setEnabled(true);
        shootparams.height = MyApp.dip2px(60);
        shootparams.width = MyApp.dip2px(60);
        shootparams.topMargin = MyApp.dip2px(33);
        buttonShoot.setLayoutParams(shootparams);
        Glide.with(RecordVideoActivity.this).load(R.mipmap.video_button_notclicked).into(buttonShoot);
        textViewReleaseToCancel.setVisibility(View.GONE);
        try {
            movieRecorderView.initCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = true;
        movieRecorderView.stop();
    }

    /**
     *  录制完成需要做的事情
     */
    private void finishActivity() {
        if (isFinish) {
            prefUtils.putBoolean(this, "isGetLength", false);
            prefUtils.putBoolean(this, "isAnimShow", true);
            movieRecorderView.stop();
            Intent intent = new Intent(this, VideoPreviewActivity.class);
            intent.putExtra("path", movieRecorderView.getRecordFile().getAbsolutePath());
            startActivityForResult(intent, REQ_CODE);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //提示只出现一次，哪怕第一次他没有到时间就退出了
        prefUtils.putBoolean(this, "isGetLength", false);
        prefUtils.putBoolean(this, "isAnimShow", true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            finish();
        }

    }
}
