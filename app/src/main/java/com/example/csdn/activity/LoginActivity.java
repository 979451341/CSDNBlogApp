package com.example.csdn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.csdn.R;
import com.example.csdn.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZTH on 2018/4/3.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_blog)
    EditText etBlog;
    @BindView(R.id.btn)
    Button btn;

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }



    @OnClick({R.id.et_blog, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_blog:
                break;
            case R.id.btn:
                String blog = etBlog.getText().toString();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("blog", blog);
                startActivity(intent);


                break;
        }
    }
}
