package com.example.csdn.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.csdn.R;
import com.example.csdn.adapter.BlogTitleAdapter;
import com.example.csdn.base.BaseActivity;
import com.example.csdn.bean.BlogTitleBean;
import com.example.csdn.interfaces.EndLessOnScrollListener;
import com.example.csdn.util.BlogTitleThread;
import com.example.csdn.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.recyc_blog_title)
    RecyclerView recycBlogTitle;
    @BindView(R.id.layout_swipe_refresh)
    SwipeRefreshLayout layoutSwipeRefresh;
    @BindView(R.id.btn_back_top)
    Button btnBackTop;

    private ArrayList<BlogTitleBean> datas = null;
    private BlogTitleAdapter adapter;

    public final static int msg_blog_title = 1001;
    public final static int msg_blog_more = 1002;

    public final static String bundle_blog_title = "blog_data";
    public final static String bundle_blog_url = "blog_data_url";
    BlogTitleThread titleThread;
    private Handler handler;

    LinearLayoutManager layoutManager;
    int page = 1;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        //模拟的数据（实际开发中一般是从网络获取的）
        datas = new ArrayList<>();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case msg_blog_title:
                        Bundle bundle = msg.getData();
                        ArrayList<BlogTitleBean> array = (ArrayList<BlogTitleBean>) bundle.get(bundle_blog_title);
                        if (array == null || array.size() == 0) {
                            titleThread.start();
                            ToastUtil.showToast("您的博客加载完毕！");
                            break;
                        }
                        for (BlogTitleBean bean : array) {
                            datas.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                        ToastUtil.showToast("加载成功！");
                        if (layoutSwipeRefresh.isRefreshing()) {
                            layoutSwipeRefresh.setRefreshing(false);
                        }

                        break;
                    case msg_blog_more:

                        break;
                }
            }
        };

        titleThread = new BlogTitleThread(handler, page);
        titleThread.start();

    }

    @Override
    public void initView() {

        //创建布局管理
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycBlogTitle.setLayoutManager(layoutManager);

        //创建适配器
        adapter = new BlogTitleAdapter(R.layout.item_blog_title, datas);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra(bundle_blog_url, datas.get(position).getUrl());
                startActivity(intent);
            }
        });


        //给RecyclerView设置适配器
        recycBlogTitle.setAdapter(adapter);
        //监听SwipeRefreshLayout.OnRefreshListener
        layoutSwipeRefresh.setOnRefreshListener(this);

        /**
         * 监听addOnScrollListener这个方法，新建我们的EndLessOnScrollListener
         * 在onLoadMore方法中去完成上拉加载的操作
         * */
        recycBlogTitle.addOnScrollListener(new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
            }
        });

        layoutSwipeRefresh.setRefreshing(true);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    //每次上拉加载的时候，就加载十条数据到RecyclerView中
    private void loadMoreData() {
        page = page + 1;
        titleThread.page = page;
        titleThread.start();
    }

    /**
     * 重写SwipeRefreshLayout.OnRefreshListener的OnRefresh方法
     * 在这里面去做下拉刷新的操作
     */
    @Override
    public void onRefresh() {

        page = 1;
        datas.clear();
        adapter.notifyDataSetChanged();
        titleThread.page = page;
        titleThread.start();
    }


    @OnClick(R.id.btn_back_top)
    public void onViewClicked() {
        smoothMoveToPosition(recycBlogTitle, 0);

    }


    /**
     * 滑动到指定位置
     *
     * @param mRecyclerView
     * @param position
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);

        }
    }

}
