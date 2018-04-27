package com.example.csdn.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.csdn.activity.MainActivity;
import com.example.csdn.bean.BlogTitleBean;
import com.example.csdn.constant.Constants;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ZTH on 2018/4/3.
 */

public class BlogTitleThread extends Thread {

    private ArrayList<BlogTitleBean> dataList = new ArrayList<>();

    private Handler handler;
    public int page;
    public BlogTitleThread(Handler handler, int page){
        super();
        this.handler = handler;
        this.page = page;

    }

    @Override
    public void run() {
        dataList.clear();
        String url = Constants.blog_address+Constants.article+page;
        Connection conn = Jsoup.connect(url);
        // 修改http包中的header,伪装成浏览器进行抓取
        conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20170929 Firefox/32.0");
        Document doc = null;
        try {
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element body = doc.body();
        Element container = body.getElementById("mainBox");
        Element cMain = container.tagName("main");

        Document doccMain = Jsoup.parse(cMain.toString());
        Elements list_item_new = doccMain.getElementsByClass("article-list");

        String old = "";
        for (Element item : list_item_new) {

            Elements links = item.getElementsByTag("a");
            for (Element link : links) {
                String linkHref = link.attr("href");
                String linkText = link.text();

                if (TextUtils.isEmpty(linkHref) || TextUtils.isEmpty(linkText)) {
                    continue;
                }

                BlogTitleBean bean = new BlogTitleBean();
                bean.setTitle(linkText);
                bean.setUrl(linkHref);

                dataList.add(bean);

            }


        }

        Message message = handler.obtainMessage();
        message.what = MainActivity.msg_blog_title;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MainActivity.bundle_blog_title, dataList );
        message.setData(bundle);
        handler.sendMessage(message);



    }



}
