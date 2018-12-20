package com.sglei.basemodule.net.event;

import android.text.TextUtils;
import com.orhanobut.hawk.Hawk;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @creator zane
 * @time 2018/12/19 16:55
 */
public class RetrofitCache {
    /**
     * @param cacheKey     缓存的Key
     * @param fromNetwork
     * @param forceRefresh 是否强制刷新
     * @param <T>
     * @return
     */
    public static <T> Observable<T> load(final String cacheKey,
                                         Observable<T> fromNetwork, boolean forceRefresh) {
        Observable<T> fromCache = Observable
                .create((ObservableOnSubscribe<T>) e -> {
                            T cache = Hawk.get(cacheKey);
                            if (cache != null) {
                                e.onNext(cache);
                            } else {
                                e.onComplete();
                            }
                        }
                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        //是否缓存
        if (!TextUtils.isEmpty(cacheKey)) {
            /*
             * 这里的fromNetwork 不需要指定Schedule,在handleRequest中已经变换了
             */
            fromNetwork = fromNetwork.map(result -> {
                Hawk.delete(cacheKey);
                Hawk.put(cacheKey, result);
                return result;
            });
        }
        //强制刷新
        if (forceRefresh) {
            return fromNetwork;
        } else {
            return Observable.concat(fromCache, fromNetwork).filter(t -> t != null);

        }
    }
}
