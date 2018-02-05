package com.example.lcc.albumvideoselectdemo.nineimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lcc.albumvideoselectdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义九宫格布局
 *
 * @function 九宫格布局
 */
public class NineGridView extends ViewGroup {

    public static final int MODE_FILL = 0;          //填充模式，类似于微信
    public static final int MODE_GRID = 1;          //网格模式，类似于QQ，4张图会 2X2布局

    private static ImageLoader mImageLoader;        //全局的图片加载器(必须设置,否者不显示图片)

    private int singleImageSize = 180;              // 单张图片时的最大大小,单位dp
    private float singleImageRatio = 1.0f;          // 单张图片的宽高比(宽/高)
    private int maxImageSize = 9;                   // 最大显示的图片数
    private int gridSpacing = 4;                    // 宫格间距，单位dp
    private int mode = MODE_GRID;                   // 默认使用fill模式

    private int columnCount;    // 列数
    private int rowCount;       // 行数
    private int gridWidth;      // 宫格宽度
    private int gridHeight;     // 宫格高度

    private List<ImageView> imageViews;
    private List<ImageInfo> mImageInfo;
    private NineGridViewAdapter mAdapter;

    private boolean isFirst = true;

    public NineGridView(Context context) {
        this(context, null);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        gridSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gridSpacing, dm);
        singleImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, singleImageSize, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NineGridView);
        gridSpacing = (int) a.getDimension(R.styleable.NineGridView_ngv_gridSpacing, gridSpacing);
        singleImageSize = a.getDimensionPixelSize(R.styleable.NineGridView_ngv_singleImageSize, singleImageSize);
        singleImageRatio = a.getFloat(R.styleable.NineGridView_ngv_singleImageRatio, singleImageRatio);
        maxImageSize = a.getInt(R.styleable.NineGridView_ngv_maxSize, maxImageSize);
        mode = a.getInt(R.styleable.NineGridView_ngv_mode, mode);
        a.recycle();

        imageViews = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int size = mImageInfo.size();
        int totalWidth = width - getPaddingLeft() - getPaddingRight();

        if (mImageInfo != null && mImageInfo.size() > 0) {
//            if (size == 1) {

//
//            if (size == 2) {
//
//                //如果当为2张或者4张图片的时候   就按照总宽度的  1/2
//                gridWidth = gridHeight = (totalWidth - gridSpacing * (columnCount - 1)) / columnCount;
//                //这里无论是几张图片，宽高都按总宽度的 1/3
////                gridWidth = gridHeight = (totalWidth - gridSpacing ) / 2;
//            } else {
//                gridWidth = gridHeight = (totalWidth - gridSpacing * 2) / 3;
//            }

            //一张图片的时候  这个需要进行三种形式的展示  在这里进行测量
            if (size == 1) {

                //获取到一张图片的时候图片的真实的宽和高
                int imageRealWidth = mImageInfo.get(0).getImageViewWidth();
                int imageRealHeight = mImageInfo.get(0).getImageViewHeight();

//                Log.d("NineGridView", "imageRealWidth:" + imageRealWidth);
//                Log.d("NineGridView", "imageRealHeight:" + imageRealHeight);
                if (imageRealHeight == imageRealWidth) {
                    //图片的宽和高度是相等的 这个时候需要画出来的是个正方形
                    gridWidth = gridHeight = singleImageSize;

                } else if (imageRealHeight > imageRealWidth) {
                    //如果高度大于宽度


                    if (imageRealHeight * 9 > imageRealWidth * 16) {
                        // 当比例超过9:16时缩略图显示“长图”标识
//                        这个时候需要进行长图标志
//                        Log.d("NineGridView", "长度标志");
                        gridHeight = singleImageSize;
                        gridWidth = singleImageSize * 9 / 16;
                    } else {
                        gridHeight = singleImageSize;
                        gridWidth = singleImageSize * imageRealWidth / imageRealHeight;
                    }

                } else if (imageRealWidth > imageRealHeight) {
                    //如果宽度大于高度
                    gridWidth = singleImageSize;
                    gridHeight = singleImageSize * imageRealHeight / imageRealWidth;

                }

                width = gridWidth;
                height = gridHeight;

            //两张图片的时候显示规则
            } else if (size == 2) {

                gridWidth = gridHeight = (totalWidth - gridSpacing * (columnCount - 1)) / columnCount;

                width = gridWidth * columnCount + gridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();

                height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();

              //4张图片的显示规则
            } else if (size == 4) {

                gridWidth = gridHeight = (totalWidth - gridSpacing * 2) / 3;

                width = gridWidth * (columnCount + 1) + gridSpacing * (columnCount) + getPaddingLeft() + getPaddingRight();
                height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();

                //其他图片的显示规则
            } else {

                gridWidth = gridHeight = (totalWidth - gridSpacing * 2) / 3;

                width = gridWidth * columnCount + gridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
                height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();

            }
        }

        //得到宽度和高度之后再次重新进行测量
        setMeasuredDimension(width, height);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mImageInfo == null) return;
        int childrenCount = mImageInfo.size();
        for (int i = 0; i < childrenCount; i++) {
            ImageView childrenView = (ImageView) getChildAt(i);

            int rowNum = i / columnCount;
            int columnNum = i % columnCount;
            int left = (gridWidth + gridSpacing) * columnNum + getPaddingLeft();
            int top = (gridHeight + gridSpacing) * rowNum + getPaddingTop();
            int right = left + gridWidth;
            int bottom = top + gridHeight;
            childrenView.layout(left, top, right, bottom);

            if (mImageLoader != null) {
                mImageLoader.onDisplayImage(getContext(), childrenView, mImageInfo.get(i).thumbnailUrl);
            }
        }

//        Log.d("NineGridView", "mImageInfo:" + mImageInfo);
    }

    /**
     * 设置适配器
     */
    public void setAdapter(@NonNull NineGridViewAdapter adapter) {
        Log.d("NineGridView", "调用了几次呢");
//        isFirst = true;
        mAdapter = adapter;
        List<ImageInfo> imageInfo = adapter.getImageInfo();

        if (imageInfo == null || imageInfo.isEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        int imageCount = imageInfo.size();
//        if (maxImageSize > 0 && imageCount > maxImageSize) {
//            imageInfo = imageInfo.subList(0, maxImageSize);
//            imageCount = imageInfo.size();   //再次获取图片数量
//        }

        //默认是3列显示，行数根据图片的数量决定
        rowCount = imageCount / 3 + (imageCount % 3 == 0 ? 0 : 1);
        columnCount = 3;
        //grid模式下，显示4张使用2X2模式
//        if (mode == MODE_GRID) {
        if (imageCount == 4) {  //当有四张的时候   显示两行两列
            rowCount = 2;
            columnCount = 2;
        } else if (imageCount == 2) {
            rowCount = 1;
            columnCount = 2;
        }
//        }

        //保证View的复用，避免重复创建
        if (mImageInfo == null) {
            for (int i = 0; i < imageCount; i++) {
                ImageView iv = getImageView(i);
                if (iv == null) return;
                addView(iv, generateDefaultLayoutParams());
            }
            Log.d("NineGridView", "view不为空");
        } else {
            int oldViewCount = mImageInfo.size();
            int newViewCount = imageCount;
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = oldViewCount; i < newViewCount; i++) {
                    ImageView iv = getImageView(i);
                    if (iv == null) return;
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }

        //取出是否是video标志  这个地方一定是有这个值的  因为在设置adapter的时候已经进行了非空判断
        boolean isVideo = imageInfo.get(0).isVideo;
        boolean isLongPic = imageInfo.get(0).isLongPic;

        if (!isVideo && isLongPic) {
            //长图标志
            View child = getChildAt(0);
            if (child instanceof NineGridViewWrapper) {
                NineGridViewWrapper imageView = (NineGridViewWrapper) child;
                imageView.setLongPic(true);
//                        isFirst = false;
            }
        } else if (isVideo) {
            View child = getChildAt(0);
            if (child instanceof NineGridViewWrapper) {
                NineGridViewWrapper imageView = (NineGridViewWrapper) child;
                imageView.setVideo(true, imageInfo.get(0).getLivelength());
//                        isFirst = false;
            }
        }
//
//        //当只有一张图片的时候  并且图片的比例小于16：9  那么这个时候是个长度 需要长图标志
//        if (imageCount == 1 && !isVideo) {
//            //获取到一张图片的时候图片的真实的宽和高
//            int imageRealWidth = imageInfo.get(0).getImageViewWidth();
//            int imageRealHeight = imageInfo.get(0).getImageViewHeight();
//
//            //如果高度大于宽度
//            if (imageRealHeight * 9 > imageRealWidth * 16) {
//                // 当比例超过9:16时缩略图显示“长图”标识
////                        这个时候需要进行长图标志
//                Log.d("NineGridView", "长度标志");
//                View child = getChildAt(0);
//                if (child instanceof NineGridViewWrapper) {
//                    NineGridViewWrapper imageView = (NineGridViewWrapper) child;
//                    imageView.setLongPic(true);
////                        isFirst = false;
//                }
//            }
//
//        } else if (imageCount == 1 && isVideo){
//            int imageRealWidth = imageInfo.get(0).getImageViewWidth();
//            int imageRealHeight = imageInfo.get(0).getImageViewHeight();
//
//            View child = getChildAt(0);
//            if (child instanceof NineGridViewWrapper) {
//                NineGridViewWrapper imageView = (NineGridViewWrapper) child;
//                imageView.setVideo(true, imageInfo.get(0).getLivelength());
////                        isFirst = false;
//            }
//


        // TODO: 2017/10/11   在这进行是否是视频的标志位的显示  当然存在的还有一个就是点击事件
        //如果是视频 点击应该进入播放视频的界面
        //如果是图片 点击应该进入的是查看大图的界面  这个逻辑在点击的时候要进行判断
//        for (int i = 0; i < adapter.getImageInfo().size(); i++) {
//            View child = getChildAt(i);
//            if (child instanceof NineGridViewWrapper) {
//                NineGridViewWrapper imageView = (NineGridViewWrapper) child;
//                imageView.setMoreNum(adapter.getImageInfo().size() + 20 - maxImageSize);
//            }
//        }

        //修改最后一个条目，决定是否显示更多
//        if (adapter.getImageInfo().size() > maxImageSize) {
//            View child = getChildAt(maxImageSize - 1);
//            if (child instanceof NineGridViewWrapper) {
//                NineGridViewWrapper imageView = (NineGridViewWrapper) child;
//                imageView.setMoreNum(adapter.getImageInfo().size() - maxImageSize);
//            }
//        }

//        }

        mImageInfo = imageInfo;
        requestLayout();

    }

    /**
     * 获得 ImageView 保证了 ImageView 的重用
     */

    private ImageView getImageView(final int position) {
        ImageView imageView;
        if (position < imageViews.size()) {
            imageView = imageViews.get(position);
        } else {
            imageView = mAdapter.generateImageView(getContext());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.onImageItemClick(getContext(), NineGridView.this, position, mAdapter.getImageInfo());
                }
            });
            imageViews.add(imageView);
        }
        return imageView;
    }

    /**
     * 设置宫格间距
     */
    public void setGridSpacing(int spacing) {
        gridSpacing = spacing;
    }

    /**
     * 设置只有一张图片时的宽
     */
    public void setSingleImageSize(int maxImageSize) {
        singleImageSize = maxImageSize;
    }

    /**
     * 设置只有一张图片时的宽高比
     */
    public void setSingleImageRatio(float ratio) {
        singleImageRatio = ratio;
    }

    /**
     * 设置最大图片数
     */
    public void setMaxSize(int maxSize) {
        maxImageSize = maxSize;
    }

    public int getMaxSize() {
        return maxImageSize;
    }

    public static void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public static ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public interface ImageLoader {
        /**
         * 需要子类实现该方法，以确定如何加载和显示图片
         *
         * @param context   上下文
         * @param imageView 需要展示图片的ImageView
         * @param url       图片地址
         */
        void onDisplayImage(Context context, ImageView imageView, String url);

        /**
         * @param url 图片的地址
         * @return 当前框架的本地缓存图片
         */
        Bitmap getCacheImage(String url);
    }
}
