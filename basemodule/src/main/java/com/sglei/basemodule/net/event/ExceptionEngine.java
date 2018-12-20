package com.sglei.basemodule.net.event;

import android.net.ParseException;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.sglei.basemodule.util.Logger;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

/**
 * @creator zane
 * @time 2018/12/19 16:17
 */
public class ExceptionEngine {
    private static final int ERROR_HTTP_401 = 401;
    private static final int ERROR_HTTP_403 = 403;
    private static final int ERROR_HTTP_404 = 404;
    private static final int ERROR_HTTP_408 = 408;
    private static final int ERROR_HTTP_500 = 500;
    private static final int ERROR_HTTP_503 = 503;

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        Logger.e(e.toString());
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case ERROR_HTTP_401:
                    ex.message = "当前请求需要用户验证";
                    break;
                case ERROR_HTTP_403:
                    ex.message = "服务器拒绝了请求";
                    break;
                case ERROR_HTTP_404:
                    ex.message = "请求失败";
                    break;
                case ERROR_HTTP_408:
                    ex.message = "请求超时";
                    break;
                case ERROR_HTTP_500:
                    ex.message = "服务器内部错误";
                    break;
                case ERROR_HTTP_503:
                    ex.message = "服务器当前无法处理请求";
                    break;
                default:
                    ex.message = "未知错误";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseThrowable(resultException, resultException.getCode());
            ex.message = resultException.getMsg();
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "连接超时";
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            ex.message = "未知错误";
            return ex;
        }
    }

    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

    public static class ResponseThrowable extends Exception {
        public int code;
        public String message;

        public ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }
    }
}
