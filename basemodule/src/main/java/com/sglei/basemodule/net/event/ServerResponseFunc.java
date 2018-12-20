package com.sglei.basemodule.net.event;

import com.sglei.basemodule.net.model.HttpResult;
import com.sglei.basemodule.util.Logger;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class ServerResponseFunc<T> implements Function<HttpResult<T>, T> {
    @Override
    public T apply(@NonNull HttpResult<T> response) {
        //对返回码进行判断，如果不是0，则证明服务器端返回错误信息了，便根据跟服务器约定好的错误码去解析异常
        Logger.i(getClass().getName(), response.isOk() + "====responseIsOk");
        if (!response.isOk()) {
            //如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
            throw new ServerException(Integer.valueOf(response.getError_code()), response.getReason());
        }
        //服务器请求数据成功，返回里面的数据实体
        return response.getResult() == null ? (T) response.getReason() : response.getResult();
    }
}