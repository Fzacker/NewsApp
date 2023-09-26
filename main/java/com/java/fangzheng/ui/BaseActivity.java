package com.java.fangzheng.ui;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.java.fangzheng.R;
import com.java.fangzheng.utils.StatusBarUtils;

public abstract class BaseActivity  extends AppCompatActivity {
    public Context mContext;
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        StatusBarUtils.setWindowStatusBarColor(this, R.color.transparent);
        setContentView(initView());
        initData();
        initListener();
    }
    //初始化布局
    public abstract int initView();
    //初始化数据
    public abstract void initData();
    //初始化监听
    public abstract void initListener();
}
