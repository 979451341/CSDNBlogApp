package com.example.csdn.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;

/**
 * Created by ZTH on 2018/4/3.
 */

public abstract class BaseActivity extends AutoLayoutActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initData();
        initView();


    }

    public abstract int getLayout();
    public abstract void initData();
    public abstract void initView();
}
