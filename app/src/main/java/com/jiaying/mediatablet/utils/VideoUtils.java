package com.jiaying.mediatablet.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import android.os.Build;

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
public class VideoUtils {
    private static final String TAG = "VideoUtils";

    private static final String VIDEO_PATH = Environment.getExternalStorageDirectory().getPath()+"/jiaying";

    public static List<VideoEntity> getLocalVideoList(Activity context) {
        List<VideoEntity> videoList = new ArrayList<>();
        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};


        // MediaStore.Video.Media.DATA：视频文件路径；
        // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
        // MediaStore.Video.Media.TITLE: 视频标题 : testVideo

        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME };

        Cursor cursor = context.managedQuery(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                null, null, null);

        if (cursor == null) {
            Toast.makeText(context, "没有找到可播放视频文件", Toast.LENGTH_SHORT).show();
            return null;
        }
        int num = 0;
        if (cursor.moveToFirst()) {
            do {
                VideoEntity info = new VideoEntity();
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor = context.managedQuery(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    info.setPlay_url(thumbCursor.getString(thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                }
                String play_url = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                if(!TextUtils.isEmpty(play_url)){
                    if(play_url.startsWith(VIDEO_PATH)){
                        info.setPlay_url(play_url);
                        Bitmap cover_bitmap = getVideoThumbnail(info.getPlay_url(),100,100);
                        if(cover_bitmap != null){
                            info.setCover_bitmap(cover_bitmap);
                        }
                        Log.e(TAG, "setPath:" + info.getPlay_url());
                        videoList.add(info);
                        num++;
                    }
                }
                if(num >=20){
                    break;
                }



//                info.setTitle(cursor.getString(cursor
//                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));

//                info.setName(cursor.getString(cursor
//                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
//                Log.e(TAG, "DisplayName:" + info.getDisplayName());
//                info.setMimeType(cursor.getString(cursor
//                        .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));


            } while (cursor.moveToNext());
        }
        return videoList;
    }
    private static  Bitmap getVideoThumbnail(String videoPath, int width, int height) {

        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
                MediaStore.Images.Thumbnails.MICRO_KIND);
//        System.out.println("w" + bitmap.getWidth());
//        System.out.println("h" + bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
