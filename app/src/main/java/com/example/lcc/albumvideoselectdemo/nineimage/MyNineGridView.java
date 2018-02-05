package com.example.lcc.albumvideoselectdemo.nineimage;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by wuyinlei on 2017/10/31.
 *
 * @function 解决内存抖动问题
 */

public class MyNineGridView extends NineGridView {
    public MyNineGridView(Context context) {
        super(context);
    }

    public MyNineGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            super.onLayout(changed, l, t, r, b);
        }
    }
}
