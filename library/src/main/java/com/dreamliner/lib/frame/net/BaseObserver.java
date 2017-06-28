package com.dreamliner.lib.frame.net;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * @author chenzj
 * @Title: BaseObserver
 * @Description: 类的描述 - Observer的基类.用以根据Throwable来简单重写错误信息
 * @date 2017/6/27 18:27
 * @email admin@chenzhongjin.cn
 */
public abstract class BaseObserver<T> extends DisposableObserver<T> {

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onError(Throwable throwable) {

        if (throwable instanceof DreamLinerException) {
            DreamLinerException dreamLinerException = (DreamLinerException) throwable;
            handleError(dreamLinerException.getErrorCode(), dreamLinerException.getErrorMessage());
        } else if (throwable instanceof IOException) {
            if (throwable instanceof UnknownHostException) {
                //服务器异常
                handleError(ErrorCode.INTERRUPTED_IO_EXCEPTION, "网络异常，请稍后重试");
            } else if (throwable instanceof InterruptedIOException) {
                //超时异常
                handleError(ErrorCode.INTERRUPTED_IO_EXCEPTION, "网络异常，请稍后重试");
            } else {
                handleError(ErrorCode.OTHER_IO_EXCEPTION, "网络异常，请稍后重试");
            }
        } else if (throwable instanceof HttpException) {
            //retrofit请求木有返回
            handleError(ErrorCode.HTTP_EXCEPTION, "网络异常，请稍后重试");
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            //解释数据错误
            handleError(ErrorCode.EXCHANGE_DATA_ERROR, "解释数据错误");
        } else {
            handleError(ErrorCode.UNKNOWN_EXCEPTION, "网络异常，请稍后重试");
        }
    }

    @Override
    public void onComplete() {
    }

    public abstract void handleError(int errorCode, String errorMsg);

}