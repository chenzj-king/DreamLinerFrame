package com.dreamliner.lib.frame.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @author chenzj
 * @Title: PixelUtil
 * @Description: 类的描述 - 像素相关的转换工具类
 * @date 2017/6/27 13:25
 * @email admin@chenzhongjin.cn
 */
public class PixelUtil {

    /**
     * The mContext.
     */
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    static void init(Context context) {
        mContext = context;
    }

    /**
     * dp转 px.
     *
     * @param value the value
     * @return the int
     */
    public static int dp2px(float value) {
        final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    /**
     * px转dp.
     *
     * @param value the value
     * @return the int
     */
    public static int px2dp(float value) {
        final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
        return (int) ((value * 160) / scale + 0.5f);
    }

    /**
     * sp转px.
     *
     * @param value the value
     * @return the int
     */
    public static int sp2px(float value) {
        Resources r;
        if (mContext == null) {
            r = Resources.getSystem();
        } else {
            r = mContext.getResources();
        }
        float spvalue = value * r.getDisplayMetrics().scaledDensity;
        return (int) (spvalue + 0.5f);
    }

    /**
     * px转sp.
     *
     * @param value the value
     * @return the int
     */
    public static int px2sp(float value) {
        final float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value / scale + 0.5f);
    }

    public static int dpToPx(int dp) {
        int px = Math.round(dp * getPixelScaleFactor());
        return px;
    }

    public static int pxToDp(int px) {
        int dp = Math.round(px / getPixelScaleFactor());
        return dp;
    }

    private static float getPixelScaleFactor() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
