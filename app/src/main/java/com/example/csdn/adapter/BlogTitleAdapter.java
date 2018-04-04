package com.example.csdn.adapter;

/**
 * Created by ZTH on 2018/4/3.
 */

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.csdn.R;
import com.example.csdn.bean.BlogTitleBean;

import java.util.List;

/**
 * Created by lenovo on 2017/11/9.
 */

public class BlogTitleAdapter extends BaseQuickAdapter<BlogTitleBean, BaseViewHolder> {

    public BlogTitleAdapter(@LayoutRes int layoutResId, @Nullable List<BlogTitleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlogTitleBean item) {
        //可链式调用赋值
        helper.setText(R.id.tv_title, item.getTitle());
/*                .addOnClickListener(R.id.iv_img)    //给图标添加点击事件
                .setImageResource(R.id.iv_img, R.mipmap.ic_launcher);*/

        //获取当前条目position
        //int position = helper.getLayoutPosition();
    }
}

