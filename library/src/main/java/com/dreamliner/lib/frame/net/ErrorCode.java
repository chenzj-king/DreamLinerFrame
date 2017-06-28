package com.dreamliner.lib.frame.net;

/**
 * @author chenzj
 * @Title: ErrorCode
 * @Description: 类的描述 - 网络相关自编错误码
 * @date 2017/6/27 18:22
 * @email admin@chenzhongjin.cn
 */
public class ErrorCode {

    public final static int NET_DISABLE = -100;                 //网络没有开启
    public final static int NO_INTERNET = -101;                 //开着wifi/2g/3g/4g/但是没有网络信号
    public final static int SOCKET_EXCEPTION = -102;            //断网/网络差/主动取消request引发的异常
    public final static int INTERRUPTED_IO_EXCEPTION = -103;     //超时异常
    public final static int OTHER_IO_EXCEPTION = -104;           //除了上述的其他IO异常
    public final static int HTTP_EXCEPTION = -105;              //retrofit定义的没有response的错误
    public final static int UNKNOWN_EXCEPTION = -106;            //未知的异常
    public final static int EXCHANGE_DATA_ERROR = -108;         //Gson解释JsonString引发的异常
}
