package com.cylinder.www.facedetect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.softfan.dataCenter.DataCenterClientService;
import android.softfan.dataCenter.DataCenterException;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.dataCenter.task.IDataCenterNotify;
import android.softfan.util.textUnit;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.constants.Constants;
import com.jiaying.mediatablet.db.DataPreference;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.PersonInfo;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.mediatablet.utils.MyLog;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FdAuthActivity implements CvCameraViewListener2, IDataCenterNotify {

    private static final String TAG = "FdAuthActivity";
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    public static final int JAVA_DETECTOR = 0;
    public static final int NATIVE_DETECTOR = 1;

    private Fragment selfFragment;
    private Mat mRgba;

    private Mat copy;

    private float similarity = 0f;

    public Mat getSimilarmRgba() {
        return similarmRgba;
    }

    private Integer integer = new Integer(0);
    private Boolean changePic = true;
    private Boolean faceAuthentication = false;

    public void setSimilarmRgba(Mat similarmRgba) {
        synchronized (integer) {
            if (changePic) {
                this.similarmRgba = similarmRgba;
            }
        }
    }

    private Mat similarmRgba;

    private Mat mGray;
    private Mat faceRgba;
    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;
    private DetectionBasedTracker mNativeDetector;

    private int mDetectorType = JAVA_DETECTOR;
    private String[] mDetectorName;

    private float mRelativeFaceSize = 0.2f;
    private int mAbsoluteFaceSize = 0;

    private FdAuthCameraView mOpenCvCameraView;

    private String curPerson = "";

    private int sendCount = 0;
    private int validCount = 0;

    private BaseLoaderCallback mLoaderCallback;

    PersonInfo personInfo = DonorEntity.getInstance().getIdentityCard();

    private int cameraMode;

    //人脸识别率
    private float face_rate;

    //几张图像高于人脸识别率后就认为是同一个人
    private int face_send_num;

    public FdAuthActivity(Fragment _selfFragment, int cameraMode, float face_rate, int face_send_num) {

        this.face_rate = face_rate;
        this.face_send_num = face_send_num;
        MyLog.e(TAG, "face_rate=" + face_rate + ",face_send_num=" + face_send_num);

        this.selfFragment = _selfFragment;
        this.cameraMode = cameraMode;

        mLoaderCallback = new BaseLoaderCallback(selfFragment.getActivity()) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        Log.e(TAG, "OpenCV loaded successfully");

                        // Load native library after(!) OpenCV initialization
                        System.loadLibrary("detection_based_tracker");
                        mJavaDetector = new CascadeClassifier(Constants.cascadePath);
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else {
                            Log.e(TAG, "Loaded cascade classifier from " + Constants.cascadePath);
                            mNativeDetector = new DetectionBasedTracker(Constants.cascadePath, 0);
                        }
                        mOpenCvCameraView.setCameraIndex(1);
                        mOpenCvCameraView.enableView();
                    }
                    break;
                    default: {
                        super.onManagerConnected(status);
                    }
                    break;
                }
            }
        };

        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";
        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */

    public void onCreate(View view) {
        Log.i(TAG, "called onCreate");
        mOpenCvCameraView = (FdAuthCameraView) view.findViewById(R.id.fdAuthCameraView);
        if (mOpenCvCameraView != null) {

            mOpenCvCameraView.setCameraMode(cameraMode);


            mOpenCvCameraView.setZOrderOnTop(true);

            mOpenCvCameraView.setX(-240);

            //mOpenCvCameraView.setScaleX(0.2f);
            //mOpenCvCameraView.setScaleY(0.2f);
            mOpenCvCameraView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            mOpenCvCameraView.setCvCameraViewListener(this);
            mOpenCvCameraView.setSelfListener(this);

            mOpenCvCameraView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        if (mOpenCvCameraView != null) {
                            mOpenCvCameraView.sizeTriggle();
                        }
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    }
                    return true;
                }
            });
        }
    }

    public void onPause() {
        Log.i(TAG, "called onPause");
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onResume() {
        Log.i(TAG, "called onResume");
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, selfFragment.getActivity(), mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        Log.i(TAG, "called onDestroy");
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onStop() {
        Log.i(TAG, "called onStop");
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onDestroyView() {
        Log.i(TAG, "called onDestroyView");
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
        faceRgba = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
        faceRgba.release();
    }


    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        int w = mOpenCvCameraView.getSelfPaintWidth();
        if (w < 1) {
            return null;
        }
        int h = mOpenCvCameraView.getSelfPaintHeight();
        if (h < 1) {
            return null;
        }
        int w1 = mOpenCvCameraView.getCameraPaintWidth();
        if (w1 < 1) {
            return null;
        }
        int h1 = mOpenCvCameraView.getCameraPaintHeight();
        if (h1 < 1) {
            return null;
        }

        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();
        try {
            if (mDetectorType == JAVA_DETECTOR) {
                if (mJavaDetector != null)
                    mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());// TODO: objdetect.CV_HAAR_SCALE_IMAGE
            } else if (mDetectorType == NATIVE_DETECTOR) {
                if (mNativeDetector != null)
                    mNativeDetector.detect(mGray, faces);
            } else {
                Log.e(TAG, "Detection method is not selected!");
            }

            try {
                faceRgba.release();

                //这里的faceRgba是要在预览界面显示的图片
                Imgproc.resize(mRgba, faceRgba, new Size(w, h));

                //在图片上扫描到的所有人脸的矩形区域
                Rect[] facesArray = faces.toArray();
                DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();

                for (int i = 0; i < facesArray.length; i++) {
                    if (clientService != null) {
                        if (clientService.getApDataCenter().sizeOfSendCmd() < 5) {
                            if (clientService.getApDataCenter().sizOfWaitCmds() < 3) {

                                //这里的copy是人脸区域
                                copy = new Mat(mRgba, facesArray[i]);

                                try {
                                    //byte[] byteArray = new byte[(int) (copy.total() * copy.channels())];
                                    //copy.get(0, 0, byteArray);
                                    MatOfByte mob = new MatOfByte();

                                    Imgcodecs.imencode(".jpg", copy, mob);
                                    byte[] byteArray = mob.toArray();

                                    //DataCenterTaskCmd retcmd = new DataCenterTaskCm0komd();
                                    FaceAuthCmd retcmd = new FaceAuthCmd();

                                    //整个场景
                                    //retcmd.setmRgba(mRgba);

                                    //人脸区域
                                    retcmd.setmRgba(copy);

                                    retcmd.setSelfNotify(this);
                                    retcmd.setCmd("faceRecognition");
                                    retcmd.setHasResponse(true);
                                    retcmd.setLevel(2);
                                    HashMap<String, Object> values = new HashMap<>();
                                    values.put("face", byteArray);
                                    values.put("face_w", copy.cols());
                                    values.put("face_h", copy.rows());
                                    values.put("faceType", copy.type());
                                    values.put("date", new Date(System.currentTimeMillis()));
                                    values.put("donorId", personInfo.getId());
                                    retcmd.setValues(values);
                                    clientService.getApDataCenter().addSendCmd(retcmd);
                                } finally {
                                    copy.release();
                                }
                            }
                            //faceRgba = copy;
                        }
                    }

                    Point tl = facesArray[i].tl();
                    tl.x = tl.x * w / w1;
                    tl.y = tl.y * h / h1;
                    Point br = facesArray[i].br();
                    br.x = br.x * w / w1;
                    br.y = br.y * h / h1;
                    Imgproc.rectangle(faceRgba, tl, br, FACE_RECT_COLOR, 1);
                }
            } finally {
                faces.release();
            }
        } catch (Exception e) {
            return null;
        }

        //if (curPerson != null) {
        if (sendCount > 0) {
//            mOpenCvCameraView.setCurText("匹配:" + curPerson + "   识别率:" + (validCount * 100 / sendCount) + "%");

            mOpenCvCameraView.setCurText("    rate:" + (validCount * 100 / sendCount) + "%");

        }
        //Imgproc.putText(mRgba, curPerson, new Point(5, 20), 1/* CV_FONT_HERSHEY_COMPLEX */, 1, FACE_RECT_COLOR);//new Scalar(255, 0, 0, 255), 3);
        //}

        return faceRgba;
    }

    public void onSend(DataCenterTaskCmd selfCmd) throws DataCenterException {
    }

    public void onResponse(DataCenterTaskCmd selfCmd, DataCenterTaskCmd responseCmd) throws DataCenterException {
        try {
            boolean b = selfCmd instanceof FaceAuthCmd;

            if (b) {
                final FaceAuthCmd faceAuthCmd = (FaceAuthCmd) selfCmd;
                final Object num = responseCmd.getValue("num");
                sendCount++;

                selfFragment.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            if (!textUnit.isEmptyValue(num)) {
                                float personnum = Float.parseFloat(num.toString());


                                if (personnum >= face_rate) {
                                    //记录相似度最高的一张图片,上传服务器备存.
                                    if (personnum > similarity) {

                                        similarity = personnum;
                                        setSimilarmRgba(faceAuthCmd.getmRgba());
                                    }
                                    validCount++;
                                    if (validCount >= face_send_num) {
                                        setFaceAuthentication(true);
                                    }
                                    curPerson = "本人(" + num.toString() + ")";
                                } else {
                                    curPerson = num.toString();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }

        } catch (Exception e) {
        }
    }


    /**
     * 当检测人脸匹配3次，就认证通过
     */
    public boolean isFaceAuthentication() {

        return faceAuthentication;
    }

    private void setFaceAuthentication(Boolean b) {
        synchronized (integer) {
            changePic = false;
            faceAuthentication = b;
        }

    }


    public void onFree(DataCenterTaskCmd selfCmd) {

    }

    public void onTimeout(DataCenterTaskCmd selfCmd) {
    }

    @Override
    public void onSended(DataCenterTaskCmd selfCmd) throws DataCenterException {

    }

    @Override
    public void onSendTimeout(DataCenterTaskCmd selfCmd) {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd newcmd = selfCmd.copy();
        clientService.getApDataCenter().addSendCmd(newcmd);
    }

    @Override
    public void onResponseTimeout(DataCenterTaskCmd selfCmd) {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd newcmd = selfCmd.copy();
        clientService.getApDataCenter().addSendCmd(newcmd);
    }

    @Override
    public void onAdd(DataCenterTaskCmd dataCenterTaskCmd, List<DataCenterTaskCmd> list) {

    }

}
