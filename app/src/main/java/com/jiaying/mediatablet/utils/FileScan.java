package com.jiaying.mediatablet.utils;

/**
 * 作者：lenovo on 2016/5/22 00:02
 * 邮箱：353510746@qq.com
 * 功能：
 */

import java.util.HashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.jiaying.mediatablet.entity.VideoEntity;


/**
 * @author function 用于扫描SD卡上的文件
 */
public class FileScan {
    //分别是 公益视频，献浆知识，娱乐视频的路径
    public static final String VIDEO_PATH_GONGYI = Environment.getExternalStorageDirectory().getPath() + File.separator + "jiaying/publicwelfare";
    public static final String VIDEO_PATH_XIANJIANG = Environment.getExternalStorageDirectory().getPath() + File.separator + "jiaying/donation";
    public static final String VIDEO_PATH_YULE = Environment.getExternalStorageDirectory().getPath() + File.separator + "jiaying/entertainment";


    //视频备份的目录
    public static final String VIDEO_PATH_BACKUP = Environment.getExternalStorageDirectory().getPath() + File.separator + "backup";
    private static final String TAG = "FileScan";

    public static List<VideoEntity> getLocalVideoList(String path) {
        MyLog.e(TAG, "scan path:" + path);
        if (TextUtils.isEmpty(path)) {

            return null;
        }
        List<VideoEntity> fileList = new ArrayList<>();
        getFileList(new File(path), fileList);
        return fileList;
    }

    /**
     * @param path
     * @param fileList 注意的是并不是所有的文件夹都可以进行读取的，权限问题
     */
    private static void getFileList(File path, List<VideoEntity> fileList) {
        //如果是文件夹的话
        if (path.isDirectory()) {
            //返回文件夹中有的数据
            File[] files = path.listFiles();
            //先判断下有没有权限，如果没有权限的话，就不执行了
            if (null == files)
                return;

            for (int i = 0; i < files.length; i++) {
                getFileList(files[i], fileList);
            }
        }
        //如果是文件的话直接加入
        else {
            if (!TextUtils.isEmpty(path.getName())) {
                if (path.getName().endsWith("3gp") || path.getName().endsWith("mp4")
                        || path.getName().endsWith("avi") || path.getName().endsWith("wmv")
                        || path.getName().endsWith("FLV") || path.getName().endsWith("flv")) {
                    if (!TextUtils.isEmpty(path.getAbsolutePath())) {
                        MyLog.e(TAG, "path:" + path.getAbsolutePath());
                        //文件名
                        String filePath = path.getAbsolutePath();
                        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                        VideoEntity videoEntity = new VideoEntity();
                        videoEntity.setPlay_url(path.getAbsolutePath());
                        videoEntity.setName(fileName.substring(0, fileName.length() - 4));
                        fileList.add(videoEntity);
                    }
                }
            }
        }
    }


}