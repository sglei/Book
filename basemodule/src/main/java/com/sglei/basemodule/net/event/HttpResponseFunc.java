package com.sglei.basemodule.net.event;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @creator zane
 * @time 2018/12/19 16:52
 */
public class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(@NonNull Throwable throwable) {
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}