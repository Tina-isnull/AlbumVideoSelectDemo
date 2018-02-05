package com.example.lcc.albumvideoselectdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcc.albumvideoselectdemo.camera.CameraPreview;
import com.example.lcc.albumvideoselectdemo.utils.MyApp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LccameraActivity extends BaseActivity {
    public static final String TAG = "CameraSimple";
    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.Parameters parameters = null;
    private FrameLayout mCameralayout;
    private ImageView mTakePictureBtn;    //拍照
    private TextView mTakeCamera;         //转换摄像头
    private TextView mCaneraCancel;       //取消
    private int mCameraId;
    static final int FOCUS = 1;            // 聚焦
    static final int ZOOM = 2;            // 缩放
    private int mode;
    private float dist;
    private float pointX, pointY;

    // 拍照回调
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        bitmap = rotateBitmapByDegree(bitmap, 90);
                    } else {
                        bitmap = rotateBitmapByDegree(bitmap, -90);
                    }
                    saveImageToGallery(LccameraActivity.this, bitmap);
                }
            }).start();
//
//            File pictureDir = Environment
//                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//
//            final String picturePath = pictureDir
//                    + File.separator
//                    + new DateFormat().format("yyyyMMddHHmmss", new Date())
//                    .toString() + ".jpg";
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    File file = new File(picturePath);
//                    try {
//                        // 获取当前旋转角度, 并旋转图片
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                            bitmap = rotateBitmapByDegree(bitmap, 90);
//                        } else {
//                            bitmap = rotateBitmapByDegree(bitmap, -90);
//                        }
//                        BufferedOutputStream bos = new BufferedOutputStream(
//                                new FileOutputStream(file));
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                        bos.flush();
//                        bos.close();
//                        bitmap.recycle();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();

            mCamera.startPreview();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lccamera);
        if (!checkCameraHardware(this)) {
            Toast.makeText(LccameraActivity.this, "相机不支持", Toast.LENGTH_SHORT)
                    .show();
        } else {
            mCameralayout = (FrameLayout) findViewById(R.id.camera_preview);
            mTakePictureBtn = (ImageView) findViewById(R.id.button_capture);
            mTakeCamera = (TextView) findViewById(R.id.button_camera);
            mCaneraCancel = (TextView) findViewById(R.id.tv_cancel);
            mCaneraCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCamera.takePicture(null, null, mPictureCallback);
                }
            });
            mTakeCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchCamera();
                }
            });

            openCamera();
            mPreview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        // 主点按下
                        case MotionEvent.ACTION_DOWN:
                            //自动对焦
                            mCamera.autoFocus(null);
                            pointX = event.getX();
                            pointY = event.getY();
                            mode = FOCUS;
                            break;
                        // 副点按下
                        case MotionEvent.ACTION_POINTER_DOWN:
                            dist = spacing(event);
                            // 如果连续两点距离大于10，则判定为多点模式
                            if (dist > 10f) {
                                mode = ZOOM;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                            mode = FOCUS;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (mode == FOCUS) {
                                //pointFocus((int) event.getRawX(), (int) event.getRawY());
                            } else if (mode == ZOOM) {
                                float newDist = spacing(event);
                                if (newDist > 10f) {
                                    float tScale = (newDist - dist) / dist;
                                    if (tScale < 0) {
                                        tScale = tScale * 10;
                                    }
                                    addZoomIn((int) tScale);
                                }
                            }
                            break;
                    }

                    return true;
                }
            });
            setCameraDisplayOrientation(this, mCameraId, mCamera);
        }
    }

    // 判断相机是否支持
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    // 获取相机实例
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(mCameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public void initCamera() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = screenWidth = display.getWidth();
        int screenHeight = screenHeight = display.getHeight();
        Camera.Parameters mParameters = mCamera.getParameters();
        mParameters.setPictureSize(screenWidth, screenHeight);
        mCamera.setParameters(mParameters);
    }

    // 开始预览相机
    public void openCamera() {
        if (mCamera == null) {
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(LccameraActivity.this, mCamera);
            mCameralayout.addView(mPreview);
//            startOrientationChangeListener();
//            WindowManager windowManager = getWindowManager();
//            Display display = windowManager.getDefaultDisplay();
//            int screenWidth = screenWidth = display.getWidth();
//            int screenHeight = screenHeight = display.getHeight();
//            Camera.Parameters mParameters = mCamera.getParameters();
//            mParameters.setPictureSize(screenWidth, screenHeight);
//            mCamera.setParameters(mParameters);

            mCamera.startPreview();
        }
    }

    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 释放相机
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    // 切换前置和后置摄像头
    public void switchCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        mCameralayout.removeView(mPreview);
        releaseCamera();
        openCamera();
        setCameraDisplayOrientation(LccameraActivity.this, mCameraId, mCamera);
    }

    // 旋转图片
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    // 设置相机横竖屏
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        Log.d("tina===>", "result=" + result);
        camera.setDisplayOrientation(result);
    }

    //放大缩小
    int curZoomValue = 0;

    private void addZoomIn(int delta) {

        try {
            Camera.Parameters params = mCamera.getParameters();
            Log.d("Camera", "Is support Zoom " + params.isZoomSupported());
            if (!params.isZoomSupported()) {
                return;
            }
            curZoomValue += delta;
            if (curZoomValue < 0) {
                curZoomValue = 0;
            } else if (curZoomValue > params.getMaxZoom()) {
                curZoomValue = params.getMaxZoom();
            }

            if (!params.isSmoothZoomSupported()) {
                params.setZoom(curZoomValue);
                mCamera.setParameters(params);
                return;
            } else {
                mCamera.startSmoothZoom(curZoomValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    // 聚焦回调
//    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
//        @Override
//        public void onAutoFocus(boolean success, Camera camera) {
//            if (success) {
//                mCamera.takePicture(null, null, mPictureCallback);
//            }
//        }
//    };
//保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "lcc";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
