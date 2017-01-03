package com.cylinder.www.facedetect;

import android.softfan.dataCenter.task.DataCenterTaskCmd;

import org.opencv.core.Mat;

/**
 * Created by hipil on 2016/6/3.
 * 这个类是记录认证过程中，献浆员认证过程最相似的一张图片。
 */
public class FaceAuthCmd extends DataCenterTaskCmd {
    private Mat mRgba;

    public Mat getmRgba() {
        return mRgba;
    }

    public void setmRgba(Mat mRgba) {

        this.mRgba = mRgba.clone();

    }


}
