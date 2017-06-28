package com.dreamliner.lib.frame.sample

import android.app.Application
import com.dreamliner.lib.frame.netstatus.NetStateReceiver
import com.dreamliner.lib.frame.util.ConfigurationUtil
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


/**
 * @author chenzj
 * @Title: AppContext
 * @Description: 类的描述 -
 * @date 2017/6/27 15:54
 * @email admin@chenzhongjin.cn
 */
class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()

        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(2)
                .tag("DreamLiner")
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        NetStateReceiver.registerNetworkStateReceiver(this)

        ConfigurationUtil.init(this)
        ConfigurationUtil.initThemeColor()
    }
}