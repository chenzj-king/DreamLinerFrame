package com.dreamliner.lib.frame.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dreamliner.lib.frame.base.BaseCompatActivity;
import com.dreamliner.lib.frame.netstatus.NetType;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        findViewById(R.id.test_tv).setOnClickListener(v -> showBaseDialog("测试显示", (customDialog, customDialogAction) -> customDialog
                .dismiss()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netStatusChange(NetType netType) {
        Logger.i("netStatusChange=" + netType.name());
    }
}
