package com.example.lcc.albumvideoselectdemo.nineimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.lcc.albumvideoselectdemo.R;

public class NineGridViewWrapper extends ImageView {

    private int moreNum = 0;              //显示更多的数量
    private String livetime = "";              //显示视频的时长

    private boolean isVideo = false;  //是否是视频的标志

    private boolean isLongPic = false;              //是否显示为长图标志

    private int maskColor = 0x00000000;   //默认的遮盖颜色
    private float textSize = 13;          //显示文字的大小单位sp
    private int textColor = 0xFFFFFFFF;   //显示文字的颜色

    private TextPaint textPaint;              //文字的画笔
    private String msg = "";                  //要绘制的文字

    public NineGridViewWrapper(Context context) {
        this(context, null);
    }

    public NineGridViewWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridViewWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //转化单位
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getContext().getResources().getDisplayMetrics());

        textPaint = new TextPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);  //文字居中对齐
        textPaint.setAntiAlias(true);                //抗锯齿
        textPaint.setTextSize(textSize);             //设置文字大小
        textPaint.setColor(textColor);               //设置文字颜色
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isVideo) {
            canvas.drawColor(maskColor);
//            float baseY = getHeight()  - (textPaint.ascent() + textPaint.descent()) -  MyApp.dip2px(5);
//            float baseX = getWidth() - (textPaint.ascent() + textPaint.descent()) -  MyApp.dip2px(5);
            if (!TextUtils.isEmpty(livetime)) {

                Paint pFont = new Paint();
                Rect rect = new Rect();
                pFont.getTextBounds(livetime, 0, livetime.length(), rect);

                float baseY = getHeight() - rect.height() - dip2px(3);
                float baseX = getWidth() - rect.width() + (textPaint.ascent() + textPaint.descent())/2  - dip2px(10);

                canvas.drawText(livetime, baseX, baseY, textPaint);
            }

            Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.details_live_go);//图片
//            currentX=screenWidth/2-bm.getWidth()/2;  //图片的X坐标
//            currentY=screenHeight/2-bm.getHeight()/2; //图片的Y坐标
//            canvas.drawBitmap(bm, currentX, currentY, textPaint);
            // 将画布坐标系移动到画布中央
            canvas.translate(getWidth() / 2 - bm.getWidth() / 2, getHeight() / 2 - bm.getHeight() / 2);

            // 指定图片绘制区域(全部的bitmap需要画)
            Rect src = new Rect(0, 0, bm.getWidth(), bm.getHeight());

            // 指定图片在屏幕上显示的区域
            Rect dst = new Rect(0, 0, dip2px(34), dip2px(34));

            canvas.drawBitmap(bm, src, dst, textPaint);

//            isVideo = false;

//            invalidate();

//            canvas.drawText(livetime,10,20,20,20,textPaint);
//            canvas.drawBitmap(bm,(getWidth() - bm.getWidth()) / 2,(getHeight()-bm.getHeight())/2,textPaint);
        } else if (isLongPic) {
            // TODO: 2017/10/30 开始进行长图标志
            Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.pic_long);//图片
//            currentX=screenWidth/2-bm.getWidth()/2;  //图片的X坐标
//            currentY=screenHeight/2-bm.getHeight()/2; //图片的Y坐标
//            canvas.drawBitmap(bm, currentX, currentY, textPaint);
            // 将画布坐标系移动到画布中央
            canvas.translate(getWidth() - bm.getWidth() - 5, getHeight() - bm.getHeight() - 5);

            // 指定图片绘制区域(全部的bitmap需要画)
            Rect src = new Rect(0, 0, bm.getWidth(), bm.getHeight());

            // 指定图片在屏幕上显示的区域
            Rect dst = new Rect(0, 0, dip2px(29), dip2px(15));

            canvas.drawBitmap(bm, src, dst, textPaint);

//            isLongPic = false;

//            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    /**
                     * 默认情况下，所有的从同一资源（R.drawable.XXX）加载来的drawable实例都共享一个共用的状态，
                     * 如果你更改一个实例的状态，其他所有的实例都会收到相同的通知。
                     * 使用使 mutate 可以让这个drawable变得状态不定。这个操作不能还原（变为不定后就不能变为原来的状态）。
                     * 一个状态不定的drawable可以保证它不与其他任何一个drawabe共享它的状态。
                     * 此处应该是要使用的 mutate()，但是在部分手机上会出现点击后变白的现象，所以没有使用
                     * 目前这种解决方案没有问题
                     */
//                    drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    //这个地方设置的是当手指在该图片的时候是否显示一个灰色--->代表着用户已经点击了  友好的交互
//                    drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp = getDrawable();
                if (drawableUp != null) {
//                    drawableUp.mutate().clearColorFilter();
                    drawableUp.clearColorFilter();
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }

    public int getMoreNum() {
        return moreNum;
    }

    public void setMoreNum(int moreNum) {
        this.moreNum = moreNum;
        msg = "+" + moreNum;
        invalidate();
    }

    public void setLongPic(boolean isLongPic) {
        this.isLongPic = isLongPic;
        invalidate();
    }

    public int getMaskColor() {
        return maskColor;
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
        invalidate();
    }

    public void setVideo(boolean isVideo, String livelength) {
        this.livetime = livelength;
        this.isVideo = isVideo;
        invalidate();
    }
    /**
     * dp转成px
     */

    public int dip2px(int dipValue) {
        return (int) (dipValue * getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 从px转为dp
     */
    private int px2dp(int pxValue) {
        return (int) (pxValue / getResources().getDisplayMetrics().density + 0.5f);
    }
}
