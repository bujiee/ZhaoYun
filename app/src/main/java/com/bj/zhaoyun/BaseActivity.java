package com.bj.zhaoyun;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Description：
 * Created by Buuu on 2017/11/14.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initData();
    }

    public abstract int getLayoutId();

    public abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
