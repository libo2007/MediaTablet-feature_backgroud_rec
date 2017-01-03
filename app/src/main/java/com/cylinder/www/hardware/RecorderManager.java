package com.cylinder.www.hardware;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;

import java.nio.Buffer;
import java.nio.ShortBuffer;
import java.util.Date;

import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

/**
 * Recorder controller, used to start,stop record, and combine all the videos
 * together
 *
 * @author xiaodong
 *
 */
public class RecorderManager {

	private static final String LOG_TAG = "RecorderManager";

	private long videoStartTime;
	private int totalTime = 0;
	private boolean isStart = false;

	private IplImage yuvIplimage = null;
	private volatile FFmpegFrameRecorder recorder;
	private int sampleAudioRateInHz = 44100;
	private int frameRate = 20;

	/* audio data getting thread */
	private AudioRecord audioRecord;
	private AudioRecordRunnable audioRecordRunnable;
	private Thread audioThread;
	volatile boolean runAudioThread = true;

	private boolean isFinished = false;

	private String filePath;
	private int width;
	private int height;

	public RecorderManager(String filePath, int width, int height) {
		this.filePath = filePath;
		this.width = width;
		this.height = height;
		this.isStart = false;
	}

	public String getFilePath() {
		return filePath;
	}

	public boolean isStart() {
		return isStart;
	}

	public long getVideoStartTime() {
		return videoStartTime;
	}

	public int checkIfMax(long timeNow) {
		int during = 0;
		if (isStart) {
			during = (int) (totalTime + ((timeNow - videoStartTime) * frameRate / frameRate));
			//System.out.println(during + ",T:" + totalTime + ",N:" + timeNow + ",S:" + videoStartTime);
		} else {
			during = totalTime;
		}
		return during;
	}

	// ---------------------------------------
	// initialize ffmpeg_recorder
	// ---------------------------------------
	public void initRecorder() {
		String ffmpeg_link = filePath;
		Log.w(LOG_TAG, "init recorder");

		if (yuvIplimage == null) {
			yuvIplimage = IplImage.create(width, height, IPL_DEPTH_8U, 2);
			Log.i(LOG_TAG, "create yuvIplimage");
		}

		Log.i(LOG_TAG, "ffmpeg_url: " + ffmpeg_link);
		recorder = new FFmpegFrameRecorder(ffmpeg_link, width, height, 1);
		recorder.setVideoCodec(28);
		recorder.setFormat("mp4");
		recorder.setSampleRate(sampleAudioRateInHz);
		// Set in the surface changed method
		recorder.setFrameRate(frameRate);

		Log.i(LOG_TAG, "recorder initialize success");

		audioRecordRunnable = new AudioRecordRunnable();
		audioThread = new Thread(audioRecordRunnable);
		try {
			recorder.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		audioThread.start();
	}

	// ---------------------------------------------
	// audio thread, gets and encodes audio data
	// ---------------------------------------------
	class AudioRecordRunnable implements Runnable {

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

			// Audio
			int bufferSize;
			short[] audioData;
			int bufferReadResult;

			bufferSize = AudioRecord.getMinBufferSize(sampleAudioRateInHz, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleAudioRateInHz, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

			audioData = new short[bufferSize];

			Log.d(LOG_TAG, "audioRecord.startRecording()");
			audioRecord.startRecording();

			/* ffmpeg_audio encoding loop */
			while (!isFinished) {
				// Log.v(LOG_TAG,"recording? " + recording);
				bufferReadResult = audioRecord.read(audioData, 0, audioData.length);
				if (bufferReadResult > 0) {
					// Log.v(LOG_TAG, "bufferReadResult: " + bufferReadResult);
					// If "recording" isn't true when start this thread, it
					// never get's set according to this if statement...!!!
					// Why? Good question...
					if (isStart) {
						try {
							Buffer[] barray = new Buffer[1];
							barray[0] = ShortBuffer.wrap(audioData, 0, bufferReadResult);
							recorder.record(barray);
							// Log.v(LOG_TAG,"recording " + 1024*i + " to " +
							// 1024*i+1024);
						} catch (FFmpegFrameRecorder.Exception e) {
							Log.v(LOG_TAG, e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
			Log.v(LOG_TAG, "AudioThread Finished, release audioRecord");

			/* encoding finish, release recorder */
			if (audioRecord != null) {
				audioRecord.stop();
				audioRecord.release();
				audioRecord = null;
				Log.v(LOG_TAG, "audioRecord released");
			}
		}
	}

	public void reset() {
		isStart = false;
		totalTime = 0;
	}

	public void startRecord() {
		isStart = true;
		videoStartTime = new Date().getTime();
	}

	public void stopRecord() {
		if (recorder != null && isStart) {
			runAudioThread = false;
			totalTime += new Date().getTime() - videoStartTime;
			videoStartTime = 0;
			isStart = false;
		}
	}

	public void releaseRecord() {
		isFinished = true;
		Log.v(LOG_TAG, "Finishing recording, calling stop and release on recorder");
		if (recorder != null) {
			try {
				recorder.stop();
				recorder.release();
			} catch (FFmpegFrameRecorder.Exception e) {
				e.printStackTrace();
			}
		}
		recorder = null;
	}

	public void onPreviewFrame(byte[] data, Camera camera) {
		int during = checkIfMax(new Date().getTime());
		/* get video data */
		if (yuvIplimage != null && isStart) {
			yuvIplimage.getByteBuffer().put(data);
			//yuvIplimage = rotateImage(yuvIplimage.asCvMat(), 90).asIplImage();
			//Log.v(LOG_TAG, "Writing Frame");
			try {
				//System.out.println(System.currentTimeMillis() - videoStartTime);
				long t = 1000L * during;
				if (recorder.getTimestamp() != t) {
					recorder.setTimestamp(t);
					recorder.record(yuvIplimage);
				}
			} catch (FFmpegFrameRecorder.Exception e) {
				Log.v(LOG_TAG, e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/*public CvMat rotateImage(CvMat input, int angle) {
		CvPoint2D32f center = new CvPoint2D32f(input.cols() / 2.0F, input.rows() / 2.0F);

		CvMat rotMat = cvCreateMat(2, 3, CV_32FC1);
		cv2DRotationMatrix(center, angle, 1, rotMat);
		CvMat dst = cvCreateMat(input.rows(), input.cols(), input.type());
		cvWarpAffine(input, dst, rotMat);
		return dst;
	}*/

}
