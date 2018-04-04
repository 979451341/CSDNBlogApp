package com.example.csdn.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.example.csdn.R;
import com.example.csdn.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZTH on 2018/4/3.
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.btn_back)
    Button btnBack;

    private String url;

    @Override
    public int getLayout() {
        return R.layout.activity_web;
    }

    @Override
    public void initData() {

        url = getIntent().getStringExtra(MainActivity.bundle_blog_url);

    }

    @Override
    public void initView() {
        WebSettings ws = web.getSettings();
        //是否允许脚本支持
        ws.setJavaScriptEnabled(true);
        web.loadUrl(url);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {

        finish();
    }
}
