package com.dreamliner.lib.frame.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author chenzj
 * @Title: CustomViewPager
 * @Description: 类的描述 -
 * @date 2017/6/27 12:49
 * @email admin@chenzhongjin.cn
 */
public class CustomNoDragViewPager extends ViewPager {

    private boolean isPagingEnabled = false;

    public CustomNoDragViewPager(Context context) {
        super(context);
    }

    public CustomNoDragViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
