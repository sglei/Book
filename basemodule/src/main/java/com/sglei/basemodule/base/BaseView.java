package com.sglei.basemodule.base;

import android.content.Intent;
import androidx.annotation.LayoutRes;
import com.sglei.basemodule.net.event.ActivityLifeCycleEvent;
import io.reactivex.subjects.PublishSubject;


/**
 * @creator zane
 * @time 2018/12/19 15:25
 */
public interface BaseView {

    @LayoutRes
    int getContentView();

    void showMessage(String error);

    void showProgress();

    void dismissProgress();

    void navigationTo(Class cls);

    void navigationTo(Intent intent);

    PublishSubject<ActivityLifeCycleEvent> getLifecycleSubject();

    BaseActivity getContext();
}
