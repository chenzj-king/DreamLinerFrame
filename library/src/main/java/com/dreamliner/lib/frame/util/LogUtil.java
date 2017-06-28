package com.dreamliner.lib.frame.util;

import android.util.Log;

/**
 * @author chenzj
 * @Title: LogUtil
 * @Description: 类的描述 -
 * @date 2017/6/27 18:11
 * @email admin@chenzhongjin.cn
 */
public class LogUtil {

    public static void v(String tag, String msg) {
        trace(Log.VERBOSE, tag, msg);
    }

    public static void d(String tag, String msg) {
        trace(Log.DEBUG, tag, msg);
    }

    public static void i(String tag, String msg) {
        trace(Log.INFO, tag, msg);
    }

    public static void w(String tag, String msg) {
        trace(Log.WARN, tag, msg);
    }

    public static void e(String tag, String msg) {
        trace(Log.ERROR, tag, msg);
    }

    private static void trace(final int type, String tag, final String msg) {
        if (ConfigurationUtil.isDebug()) {
            switch (type) {
                case Log.VERBOSE:
                    Log.v(tag, msg);
                    break;
                case Log.DEBUG:
                    Log.d(tag, msg);
                    break;
                case Log.INFO:
                    Log.i(tag, msg);
                    break;
                case Log.WARN:
                    Log.w(tag, msg);
                    break;
                case Log.ERROR:
                    Log.e(tag, msg);
                    break;
            }
        }
    }
}
