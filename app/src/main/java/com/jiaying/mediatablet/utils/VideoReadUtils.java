package com.jiaying.mediatablet.utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jiaying.mediatablet.entity.VideoEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lenovo on 2016/4/4 18:44
 * 邮箱：353510746@qq.com
 * 功能：
 */
public class VideoReadUtils {
    private static final String TAG = "VideoReadUtils";

//    public static final String VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "jiaying";
    public static final String VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "2015";



    public static List<VideoEntity> getLocalVideoList(String path) {
        List<VideoEntity> videoList = new ArrayList<>();
        File file = new File(path);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File f : fileArray) {
                    if (f.isDirectory()) {
                        MyLog.e(TAG, "文件夹扫描");

                        getLocalVideoList(f.getPath());
                    } else {
                        if (!TextUtils.isEmpty(f.getName())) {
                            if (f.getName().endsWith("3gp") || f.getName().endsWith("mp4")
                                    || f.getName().endsWith("avi") || f.getName().endsWith("wmv")|| f.getName().endsWith("FLV")) {
                                if (!TextUtils.isEmpty(f.getAbsolutePath())) {
                                    MyLog.e(TAG, "path:" + f.getAbsolutePath());
                                    VideoEntity videoEntity = new VideoEntity();
                                    videoEntity.setPlay_url(f.getAbsolutePath());
                                    videoEntity.setName(f.getName());

                                    videoList.add(videoEntity);
                                }
                            }
                        }
                    }
                }
            }
        }

        MyLog.e(TAG, "结束文件扫描");

        return videoList;
    }


}
