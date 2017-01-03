package com.jiaying.mediatablet.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 作者：lenovo on 2016/3/19 19:52
 * 邮箱：353510746@qq.com
 * 功能：视频实体
 */
public class VideoEntity implements Serializable {
    private String play_url;
    private String cover_url;
    private Bitmap cover_bitmap;
    private String name;

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public Bitmap getCover_bitmap() {
        return cover_bitmap;
    }

    public void setCover_bitmap(Bitmap cover_bitmap) {
        this.cover_bitmap = cover_bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}