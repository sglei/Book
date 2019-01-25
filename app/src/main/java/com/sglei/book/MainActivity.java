package com.sglei.book;

import androidx.fragment.app.Fragment;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.sglei.basemodule.base.BaseActivity;
import com.sglei.book.bean.TabEntity;
import com.sglei.routermodule.ReflectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    ;
    private ArrayList<Fragment> mFragments;
    private ArrayList<CustomTabEntity> mTabEntities;

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        List<String> mTitles = Arrays.asList(getResources().getStringArray(R.array.tab_titles));
        List<Integer> mIconSelectedIds = Arrays.asList(R.drawable.tab_home_select, R.drawable.tab_contact_select);
        List<Integer> mIconUnselectedIds = Arrays.asList(R.drawable.tab_home_unselect, R.drawable.tab_contact_unselect);
        mTabEntities = new ArrayList<>();
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++) {
            mTabEntities.add(new TabEntity(mTitles.get(i), mIconSelectedIds.get(i), mIconUnselectedIds.get(i)));
        }
        mFragments.add(ReflectUtils.getFragment("com.sglei.minemodule.view.FragmentMine"));
        mFragments.add(ReflectUtils.getFragment("com.sglei.sortmodule.view.FragmentSort"));

    }

    @Override
    protected void initView() {
        super.initView();
        CommonTabLayout commonTab = findViewById(R.id.tab);
        commonTab.setTabData(mTabEntities, this, R.id.container, mFragments);
        commonTab.setCurrentTab(0);
        commonTab.setTextSelectColor(getResources().getColor(R.color.colorAccent));
        commonTab.setTextUnselectColor(getResources().getColor(R.color.colorPrimary));
    }

    private long mLastClickTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastClickTime > 1000) {
            Toast.makeText(this, "再次点击退出应用", Toast.LENGTH_SHORT).show();
            mLastClickTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
