package com.jiaying.mediatablet.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jiaying.mediatablet.activity.MainActivity;

import java.io.IOException;
import java.io.InputStream;

import mobile.android.ch15.play.gif.GifFrames;

/**
 * Created by hipil on 2016/3/31.
 */
public class AniThread extends Thread {
    private Handler showPic;
    private ImageView ivHintFist;
    private GifFrames gifFrames;
    private Context context;
    private int interval;

    public AniThread(Context context, ImageView imageView, String aniName, int interval) {
        this.showPic = new ShowPic();
        this.ivHintFist = imageView;
        this.context = context;
        this.gifFrames = generateGifFrames(aniName);
        this.interval = interval;

    }

    private GifFrames generateGifFrames(String aniName) {
        try {
            //获得gif动画的InpustStream对象
            InputStream is = context.getResources().getAssets().open(aniName);
            //创建GifFrames对象
            gifFrames = GifFrames.createGifFrames(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return gifFrames;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (!isInterrupted()) {
                Bitmap bitmap = gifFrames.getImage();
                gifFrames.nextFrame();
                if (bitmap != null) {
                    Message msg = new Message();
                    msg.obj = bitmap;
                    //在线程中必须使用Handler来更新控件中的内容
                    showPic.sendMessage(msg);
                }
                //每150毫秒播放一帧动画
                sleep(interval);
            }
        } catch (InterruptedException e) {
            this.interrupt();
        } finally {
        }
    }

    // 开始播放动画
    public void startAni() {
        ((MainActivity) (context)).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AniThread.this.ivHintFist.setVisibility(View.VISIBLE);
            }
        });
        this.start();
    }

    // 停止播放动画
    public void finishAni() {

        ((MainActivity) (context)).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AniThread.this.ivHintFist.setVisibility(View.INVISIBLE);
                AniThread.this.interrupt();
            }
        });

    }

    private class ShowPic extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            //将当前帧画到ImageView控件中
            ivHintFist.setImageBitmap(bitmap);
        }
    }
}
