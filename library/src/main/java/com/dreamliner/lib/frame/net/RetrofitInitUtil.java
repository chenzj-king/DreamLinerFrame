package com.dreamliner.lib.frame.net;

import android.webkit.URLUtil;

import com.dreamliner.lib.frame.util.CommonUtil;
import com.dreamliner.lib.frame.util.ConfigurationUtil;
import com.dreamliner.lib.frame.util.ValidateUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dreamliner.lib.frame.net.ErrorCode.NET_DISABLE;

/**
 * @author chenzj
 * @Title: RetrofitInitUtil
 * @Description: 类的描述 - retrofit初始化工具类
 * @date 2017/6/27 19:39
 * @email admin@chenzhongjin.cn
 */
public class RetrofitInitUtil {

    private void setCommonSetting(OkHttpClient.Builder okhttpBuilder, Retrofit.Builder retrofitBuilder, String hostUrl) {
        setCommonSetting(okhttpBuilder, retrofitBuilder, hostUrl, 10, 10, 10, true);
    }

    private void setCommonSetting(OkHttpClient.Builder okhttpBuilder, Retrofit.Builder retrofitBuilder, String hostUrl,
                                  Interceptor... interceptors) {
        setCommonSetting(okhttpBuilder, retrofitBuilder, hostUrl, 10, 10, 10, true, interceptors);
    }

    private void setCommonSetting(OkHttpClient.Builder okhttpBuilder, Retrofit.Builder retrofitBuilder, String hostUrl,
                                  int conTimeout, int writeTimeout, int readTimeout, boolean isShowLog,
                                  Interceptor... interceptors) {
        if (!URLUtil.isValidUrl(hostUrl)) {
            throw new IllegalArgumentException("please setup the validUrl");
        } else {
            retrofitBuilder.baseUrl(hostUrl);
        }
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        okhttpBuilder.connectTimeout(conTimeout, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(readTimeout, TimeUnit.SECONDS);

        if (ValidateUtil.isValidate(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                okhttpBuilder.addInterceptor(interceptor);
            }
        }

        //日志拦截器
        if (ConfigurationUtil.isDebug() && isShowLog) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttpBuilder.addInterceptor(loggingInterceptor);
        }
        okhttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (CommonUtil.isNetworkAvailable(ConfigurationUtil.getContext())) {
                    return chain.proceed(chain.request());
                } else {
                    throw new DreamLinerException(NET_DISABLE, "网络连接失败，请开启您的网络连接，并重试！");
                }
            }
        });
    }
}
