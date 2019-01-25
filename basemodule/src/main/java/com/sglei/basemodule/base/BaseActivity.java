package com.sglei.basemodule.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.sglei.basemodule.R;
import com.sglei.basemodule.net.event.ActivityLifeCycleEvent;
import com.sglei.basemodule.net.event.BasePresenter;
import com.sglei.basemodule.util.NetUtils;
import com.sglei.basemodule.util.ToastUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.List;

/**
 * @creator zane
 * @time 2018/12/20 11:51
 */
abstract public class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView, EasyPermissions.PermissionCallbacks {

    static {
        // 开始矢量图权限
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    T mPresenter;
    BaseActivity mContext;
    final PublishSubject<ActivityLifeCycleEvent> lifeCycleSubject = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initData();
        initView();
        initEvent();
    }

    protected void initData() {
        mContext = this;
    }

    protected void initView() {

    }

    protected void initEvent() {

    }

    @Override
    public void showMessage(String error) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void navigationTo(Class cls) {
        navigationTo(new Intent(this, cls));
    }

    @Override
    public void navigationTo(Intent intent) {
        if (NetUtils.isNetworkAvailable()) {
            startActivity(intent);
        } else {
            ToastUtils.showToast(R.string.error_net);
        }
    }

    @Override
    public PublishSubject<ActivityLifeCycleEvent> getLifecycleSubject() {
        return null;
    }

    @Override
    public BaseActivity getContext() {
        return mContext;
    }

    protected void addDisposable(Disposable disposable) {
        if (disposable != null) {
            compositeDisposable.add(disposable);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //获取权限成功
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //获取权限失败
        StringBuilder sb = new StringBuilder();
        for (String str : perms) {
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        //用户点击拒绝并不在询问时候调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show();
            new AppSettingsDialog.Builder(this)
                    .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("好")
                    .setNegativeButton("不行")
                    .build()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        compositeDisposable.clear();
    }
}
