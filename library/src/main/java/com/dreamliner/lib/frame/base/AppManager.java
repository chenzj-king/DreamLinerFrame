package com.dreamliner.lib.frame.base;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chenzj
 * @Title: AppManager
 * @Description: 类的描述 - app程序管理类
 * @date 2015/6/28
 * @email admin@chenzhongjin.cn
 */
public enum AppManager {

    INSTACE;

    private List<Activity> mActivities = new LinkedList<>();

    public int size() {
        return mActivities.size();
    }

    public synchronized Activity getForwardActivity() {
        return size() > 0 ? mActivities.get(size() - 1) : null;
    }

    public synchronized void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public synchronized void removeActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    public synchronized void clear() {
        for (int i = mActivities.size() - 1; i > -1; i--) {
            Activity activity = mActivities.get(i);
            removeActivity(activity);
            activity.finish();
            i = mActivities.size();
        }
    }

    public synchronized void clearToTop() {
        for (int i = mActivities.size() - 2; i > -1; i--) {
            Activity activity = mActivities.get(i);
            removeActivity(activity);
            activity.finish();
            i = mActivities.size() - 1;
        }
    }

    public synchronized List<Activity> getActivities() {
        return mActivities;
    }
}

