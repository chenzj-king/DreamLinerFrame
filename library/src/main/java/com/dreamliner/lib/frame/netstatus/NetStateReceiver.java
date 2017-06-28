package com.dreamliner.lib.frame.netstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.dreamliner.lib.frame.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * @author chenzj
 * @Title: NetStateReceiver
 * @Description: 类的描述 - 网络状态改变的广播
 * @date 2017/6/27 15:46
 * @email admin@chenzhongjin.cn
 */
public class NetStateReceiver extends BroadcastReceiver {

    private static BroadcastReceiver mBroadcastReceiver;

    private static BroadcastReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (NetStateReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
            //通过EventBus的方式来通知其他context网络改变情况
            if (!CommonUtil.isNetworkAvailable(context)) {
                EventBus.getDefault().post(NetType.NONE);
            } else {
                EventBus.getDefault().post(CommonUtil.getAPNType(context));
            }
        }
    }

    public static void checkNetworkState(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.sendBroadcast(intent);
    }

    public static void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (null != mBroadcastReceiver) {
            try {
                mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
