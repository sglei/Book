package com.sglei.basemodule.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sglei.basemodule.net.event.ActivityLifeCycleEvent;
import com.sglei.basemodule.net.event.BasePresenter;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;


public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {
    public T mPresenter;
    BaseActivity mContext;
    final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();

    // 是否已经加载view
    protected boolean isViewPrepare = false;
    //是否已经加载数据
    protected boolean isDataPrepare = false;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getContentView(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewPrepare = true;
        initView();
        initEvent();
        lazyLoadDataIfPrepared();
    }

    private void lazyLoadDataIfPrepared() {
        if (getUserVisibleHint() && isViewPrepare && !isDataPrepare) {
            initData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared();
        }
    }

    void initView() {
    }

    void initEvent() {
    }

    void initData() {
    }


    /**
     * 获取全局的context
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mContext = (BaseActivity) getActivity();
    }

    @Override
    public void showProgress() {
        mContext.showProgress();
    }

    @Override
    public void dismissProgress() {
        mContext.dismissProgress();
    }

    @Override
    public void showMessage(String error) {
        mContext.showMessage(error);
    }

    @Override
    public PublishSubject<ActivityLifeCycleEvent> getLifecycleSubject() {
        return lifecycleSubject;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public void navigationTo(Intent intent) {
        mContext.navigationTo(intent);
    }

    @Override
    public void navigationTo(Class clz) {
        mContext.navigationTo(clz);
    }

    @Override
    public BaseActivity getContext() {
        return mContext;
    }

    protected void addDisposable(Disposable disposable) {
        if (disposable == null) {
            return;
        }
        mContext.addDisposable(disposable);
    }


}
