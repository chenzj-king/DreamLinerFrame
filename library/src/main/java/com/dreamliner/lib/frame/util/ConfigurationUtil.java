package com.dreamliner.lib.frame.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorRes;

import com.dreamliner.lib.frame.R;

/**
 * @author chenzj
 * @Title: ConfigurationUtil
 * @Description: 类的描述 - 初始化配置工具类
 * @date 2017/6/27 15:45
 * @email admin@chenzhongjin.cn
 */
public class ConfigurationUtil {

    @SuppressLint("StaticFieldLeak")
    public static Context CONFIGURATION_CONTEXT;

    private static boolean IS_DEBUG = false;
    private static String[] PERMISSION_ARRAY = new String[]{};

    //对话框相关资源
    public static CharSequence OK_CONTENT;
    public static CharSequence CANCEL_CONTENT;
    public static int THEME_COLOR_RES;
    public static int WARNING_COLOR_RES;
    public static int CANCEL_COLOR_RES;

    public static void init(Context context) {
        CONFIGURATION_CONTEXT = context;
        //初始化需要使用context的工具类
        SpUtil.init(context);
        PixelUtil.init(context);
        //初始化对话框的提示语
        initThemeColor();
    }

    public static Context getContext() {
        return CONFIGURATION_CONTEXT;
    }

    public static String[] getPermissionArray() {
        return PERMISSION_ARRAY;
    }

    public static void setPermissionArray(String[] permissionArray) {
        PERMISSION_ARRAY = permissionArray;
    }

    public static boolean isDebug() {
        return IS_DEBUG;
    }

    public static void setIsDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }

    /**********************
     *  对话框相关自定义配置
     *********************/
    public static void initThemeColor() {
        initThemeColor(getContext().getString(android.R.string.ok), getContext().getString(android.R.string.cancel),
                R.color.default_theme_color, R.color.default_dialog_warning_color, R.color.default_title_tv_color);
    }

    public static void initThemeColor(CharSequence okContent, CharSequence cancelContent, @ColorRes int themeColor,
                                      @ColorRes int warningColor, @ColorRes int cancelColor) {
        OK_CONTENT = okContent;
        CANCEL_CONTENT = cancelContent;
        THEME_COLOR_RES = themeColor;
        WARNING_COLOR_RES = warningColor;
        CANCEL_COLOR_RES = cancelColor;
    }

    public static void setOkContent(CharSequence okContent) {
        OK_CONTENT = okContent;
    }

    public static void setCancelContent(CharSequence cancelContent) {
        CANCEL_CONTENT = cancelContent;
    }

    public static void setCancelColor(@ColorRes int colorRes) {
        CANCEL_COLOR_RES = colorRes;
    }

    public static void setThemeColor(@ColorRes int colorRes) {
        THEME_COLOR_RES = colorRes;
    }

    public static void setWarningColor(@ColorRes int colorRes) {
        WARNING_COLOR_RES = colorRes;
    }


}
