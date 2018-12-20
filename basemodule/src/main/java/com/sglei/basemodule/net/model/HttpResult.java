package com.sglei.basemodule.net.model;

/**
 * @creator zane
 * @time 2018/12/19 15:11
 */
public class HttpResult<T> {
    private T result;
    private String error_code;
    private String reason;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isOk() {
        if (error_code == null) {
            error_code = "999";
        }
        return error_code.equals("200") || error_code.isEmpty();
    }
}
