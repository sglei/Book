package com.sglei.basemodule.net.event;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.orhanobut.hawk.Hawk;
import com.sglei.basemodule.R;
import com.sglei.basemodule.base.BaseActivity;
import com.sglei.basemodule.util.Logger;
import com.sglei.basemodule.util.NetUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.lang.ref.WeakReference;

/**
 * @creator zane
 * @time 2018/12/19 16:05
 */
public abstract class CommonObserver<T> implements Observer<T> {
    private final WeakReference<BaseActivity> context;
    private final String cacheKey;
    private Disposable disposable;
    private boolean isShowProgress = true;

    public CommonObserver(BaseActivity context) {
        this.cacheKey = null;
        this.context = new WeakReference<>(context);
        isShowProgress = true;
    }

    public CommonObserver(String cacheKey, BaseActivity context) {
        this.cacheKey = cacheKey;
        this.context = new WeakReference<>(context);
    }

    public CommonObserver(BaseActivity context, boolean isShowProgress) {
        this.cacheKey = null;
        this.context = new WeakReference<>(context);
        this.isShowProgress = isShowProgress;
    }

    public CommonObserver(String cacheKey, BaseActivity context, boolean isShowProgress) {
        this.cacheKey = cacheKey;
        this.context = new WeakReference<>(context);
        this.isShowProgress = isShowProgress;

    }

    @Override
    public void onSubscribe(Disposable d) {
        if (isShowProgress) {
            showProgress();
        }
        disposable = d;
        if (!NetUtils.isNetworkAvailable()) {
            if (cacheKey != null) {
                T t = Hawk.get(cacheKey);
                context.get().showMessage(context.get().getString(R.string.error_net));
                onNext(t);
            } else {
                ExceptionEngine.ResponseThrowable netWorkThrowable = new ExceptionEngine.ResponseThrowable(new Throwable(), ExceptionEngine.ERROR.NETWORD_ERROR);
                netWorkThrowable.message = context.get().getString(R.string.error_net);
                onError(netWorkThrowable);
            }
            onComplete();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (isShowProgress) {
            dismissProgress();
        }
        Logger.e(e.toString());
        if (e.getCause() instanceof ServerException
                && ((ServerException) e.getCause()).getCode() > 4001 && ((ServerException) e.getCause()).getCode() < 4008) {//token失效
                navigationToLogin(context.get());
            return;
        }
        if (e instanceof ExceptionEngine.ResponseThrowable) {
            onError((ExceptionEngine.ResponseThrowable) e);
        } else {
            if (cacheKey != null) {
                T t = Hawk.get(cacheKey);
                onNext(t);
            }
            onError(new ExceptionEngine.ResponseThrowable(e, ExceptionEngine.ERROR.UNKNOWN));
        }

        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    public abstract void onError(ExceptionEngine.ResponseThrowable t);

    @Override
    public void onComplete() {
        if (isShowProgress) {
            dismissProgress();
        }
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void showProgress() {
        if (context.get() != null && context.get() instanceof BaseActivity) {
            context.get().showProgress();
        }
    }

    public void dismissProgress() {
        if (context.get() != null && context.get() instanceof BaseActivity) {
            context.get().dismissProgress();
        }
    }


    public static Intent getIntent(Context context, String name) {
        Class clazz = null;
        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException e) {
            Toast.makeText(context, "业务组件单独调试不应该跟其他业务Module产生交互，如果你依然想要在运行期依赖其它组件", Toast.LENGTH_LONG).show();
        }
        return new Intent(context, clazz);
    }

    public static void navigationToLogin(Context context) {
        Intent intent = CommonObserver.getIntent(context, "com.sglei.loginmodule.view.LoginActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("actionCode", 0);
        CommonObserver.startActivity(context, intent);
    }

    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }
}
