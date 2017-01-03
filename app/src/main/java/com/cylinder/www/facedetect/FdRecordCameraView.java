package com.cylinder.www.facedetect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;

import com.cylinder.www.hardware.RecorderManager;

import com.jiaying.mediatablet.entity.CurrentDate;

import com.jiaying.mediatablet.entity.TempVideo;
import com.jiaying.mediatablet.thread.SendVideoThread;



import com.jiaying.mediatablet.utils.BitmapUtils;
import com.jiaying.mediatablet.utils.MyLog;



import com.jiaying.mediatablet.utils.SelfFile;
import com.jiaying.mediatablet.utils.TimeRecord;

import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.Mat;


import java.util.Date;



@SuppressLint({"UseValueOf"})
public class FdRecordCameraView extends JavaCameraView {
    private CvCameraViewListener2 selfListener;

    private boolean minimization = true;
    private boolean sizeChanged = false;

    private Bitmap selfCacheBitmap;
    private Paint selfPaint;
    private String curText;

    private Integer videoWriteLocked = new Integer(1);
    private RecorderManager recorder;

    private int cameraMode = 0;
    private Context context;
    public FdRecordCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    protected boolean initializeCamera(int width, int height) {
        if (super.initializeCamera(width, height)) {

            //肉眼看得见
//            int w = ((mFrameWidth / 5) / 8) * 8 + 215;
//            int h = w * mFrameHeight / mFrameWidth / 2 + 95;

            //肉眼看不见
            //肉眼看得见
            int w = 1;
            int h = 1;


            selfCacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            selfPaint = new Paint();
            selfPaint.setStrokeWidth(0);
            selfPaint.setTextSize(30);
            selfPaint.setColor(Color.GREEN);
            selfPaint.setColor(Color.GREEN);
            selfPaint.setTextAlign(Align.LEFT);
            return true;
        }
        return false;
    }

    @Override
    protected void releaseCamera() {
        synchronized (videoWriteLocked) {
            if (recorder != null) {
                recorder.releaseRecord();
                recorder = null;
            }
        }
        if (selfCacheBitmap != null) {
            selfCacheBitmap.recycle();
            selfCacheBitmap = null;
        }
        super.releaseCamera();
    }

    public void sizeTriggle() {
        //minimization = !minimization;
        //sizeChanged = true;
    }

    public int getCameraMode() {
        return cameraMode;
    }

    public void setCameraMode(int cameraMode) {
        this.cameraMode = cameraMode;
    }

    @Override
    public void enableView() {
        super.enableView();



        TimeRecord.getInstance().setStartVideoDate(CurrentDate.curDate);

        SelfFile.createDir(SelfFile.generateLocalVideoDIR());
        SelfFile.createDir(SelfFile.generateLocalBackupVideoDIR());
        String videoName = SelfFile.generateLocalVideoName();
        synchronized (videoWriteLocked) {
            if (cameraMode == 0) {
                if (recorder == null) {
                    recorder = new RecorderManager(videoName, mFrameWidth, mFrameHeight);
                    recorder.initRecorder();
                    recorder.startRecord();
                }
            }
        }
    }

    @Override
    public void disableView() {
        synchronized (videoWriteLocked) {
            if (recorder != null) {

       TimeRecord.getInstance().setEndVideoDate(CurrentDate.curDate);

                recorder.stopRecord();
                String filePath = recorder.getFilePath();
                recorder.releaseRecord();
                recorder = null;
                TempVideo.lpath = filePath;

                new SendVideoThread(filePath, SelfFile.generateRemoteVideoName()).start();

            }
        }
        super.disableView();
    }

    public Camera getCamera() {
        return mCamera;
    }

    public int getSelfPaintWidth() {
        if (selfCacheBitmap != null) {
            return selfCacheBitmap.getWidth();
        }
        return 0;
    }

    public int getSelfPaintHeight() {
        if (selfCacheBitmap != null) {
            return selfCacheBitmap.getHeight();
        }
        return 0;
    }

    public int getCameraPaintWidth() {
        return mFrameWidth;
    }

    public int getCameraPaintHeight() {
        return mFrameHeight;
    }

    public CvCameraViewListener2 getSelfListener() {
        return selfListener;
    }

    public void setSelfListener(CvCameraViewListener2 selfListener) {
        this.selfListener = selfListener;
    }

    public String getCurText() {
        return curText;
    }

    public void setCurText(String curText) {
        this.curText = curText;
    }

    public void onPreviewFrame(byte[] frame, Camera camera) {
        if (recorder != null) {
            recorder.onPreviewFrame(frame, camera);
        }
        super.onPreviewFrame(frame, camera);
    }

    protected void deliverAndDrawFrame(CvCameraViewFrame frame) {
        Mat modified = null;
        if (sizeChanged) {
            sizeChanged = false;
            selfCacheBitmap.recycle();
            if (minimization) {
                int w = ((mFrameWidth / 5) / 8) * 8;
                int h = w * mFrameHeight / mFrameWidth;
                selfCacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                selfPaint.setTextSize(16);
            } else {
                int w = this.getWidth();
                int h = this.getHeight();
                selfCacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                selfPaint.setTextSize(24);
            }
        }

        if (selfListener != null) {
            modified = selfListener.onCameraFrame(frame);
        }

        boolean bmpValid = true;
        if (modified != null) {
            try {
                Utils.matToBitmap(modified, selfCacheBitmap);
            } catch (Exception e) {
                bmpValid = false;
            }
        }

        if (bmpValid && selfCacheBitmap != null) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null) {

                canvas.drawBitmap(convert(selfCacheBitmap), new Rect(0, 0, selfCacheBitmap.getWidth(), selfCacheBitmap.getHeight()), new Rect(0, 0, selfCacheBitmap.getWidth(), selfCacheBitmap.getHeight()),
                        null);


                if (curText != null) {
                    canvas.drawText(curText, 0, curText.length(), 5, selfCacheBitmap.getHeight() - 5, selfPaint);
                }
                if (mFpsMeter != null) {
                    mFpsMeter.measure();
                    mFpsMeter.draw(canvas, 20, 30);
                }
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private Bitmap convert(Bitmap a) {
        int w = a.getWidth();
        int h = a.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        Matrix m = new Matrix();
//        m.postScale(1, -1);   //镜像垂直翻转
        m.postScale(-1, 1);   //镜像水平翻转
//        m.postRotate(-90);  //旋转-90度
        Bitmap new2 = Bitmap.createBitmap(a, 0, 0, w, h, m, true);
        cv.drawBitmap(new2, new Rect(0, 0, new2.getWidth(), new2.getHeight()), new Rect(0, 0, w, h), null);

        return newb;
    }

}