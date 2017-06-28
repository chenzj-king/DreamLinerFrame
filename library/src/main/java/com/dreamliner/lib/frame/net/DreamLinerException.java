package com.dreamliner.lib.frame.net;

/**
 * @author chenzj
 * @Title: DreamLinerException
 * @Description: 类的描述 - 自定义Exception,方便做网络请求错误的时候做拦截
 * @date 2017/6/27 18:22
 * @email admin@chenzhongjin.cn
 */
public class DreamLinerException extends RuntimeException {

    private int mErrorCode;
    private String mErrorMessage;

    public DreamLinerException(int errorCode, String errorMessage) {
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}
