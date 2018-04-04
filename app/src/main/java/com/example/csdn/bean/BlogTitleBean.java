package com.example.csdn.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZTH on 2018/4/3.
 */

public class BlogTitleBean implements Parcelable {

    private String title;
    private String url;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
    }

    public BlogTitleBean() {
    }

    protected BlogTitleBean(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
    }

    public static final Creator<BlogTitleBean> CREATOR = new Creator<BlogTitleBean>() {
        @Override
        public BlogTitleBean createFromParcel(Parcel source) {
            return new BlogTitleBean(source);
        }

        @Override
        public BlogTitleBean[] newArray(int size) {
            return new BlogTitleBean[size];
        }
    };
}
