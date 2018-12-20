package com.sglei.basemodule.net.event;

import com.sglei.basemodule.base.BaseView;
import com.sglei.basemodule.net.model.HttpResult;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public abstract class BasePresenterImpl<T extends BaseView> implements BasePresenter {
    public T view;
    private CompositeDisposable compositeDisposable;
    protected PublishSubject<ActivityLifeCycleEvent> lifeCycleSubject;

    public BasePresenterImpl(T view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
        start();
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected void subscribe(Observable<HttpResult<Object>> observable, CommonObserver observer, String cacheKey, boolean forceRefresh) {
        RetrofitCache.load(cacheKey, observable.compose(handle(lifeCycleSubject)).doOnSubscribe(this::addDisposable), forceRefresh).subscribe(observer);
    }

    protected void subscribe(Observable<HttpResult<Object>> observable, CommonObserver subscriber, String cacheKey) {
        subscribe(observable, subscriber, cacheKey, true);
    }

    protected void subscribe(Observable<HttpResult<Object>> observable, CommonObserver subscriber) {
        subscribe(observable, subscriber, "TempKey");
    }

    private <E> ObservableTransformer<HttpResult<E>, E> handle(PublishSubject<ActivityLifeCycleEvent> subject) {
        return observable -> observable.map(new ServerResponseFunc<>())
                .takeUntil(subject.filter(event1 -> event1.equals(ActivityLifeCycleEvent.DESTROY)))
                .onErrorResumeNext(new HttpResponseFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void start() {
        lifeCycleSubject = view.getLifecycleSubject();
    }

    @Override
    public void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        if (view != null) {
            view = null;
        }
    }
}
