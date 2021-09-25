package com.freevisiontech.mediaproclib;

import android.media.MediaRecorder;
import android.util.Log;
import java.io.File;

public class MediaRecorderUtil {
    private static final String UPDATE_TAG = "update_tag";
    /* access modifiers changed from: private */
    public MediaRecorder mediarecorder = null;
    /* access modifiers changed from: private */
    public String recordFilePath = null;
    private RecorderThread recorderThread = null;

    public void recorderStart(String savepath) {
        this.recordFilePath = savepath;
        this.recorderThread = new RecorderThread();
        this.recorderThread.start();
    }

    public void recorderSave() {
        if (this.mediarecorder != null) {
            this.mediarecorder.stop();
            this.mediarecorder.release();
            this.mediarecorder = null;
            if (this.recorderThread != null) {
                this.recorderThread = null;
            }
            Log.e(UPDATE_TAG, "Thread stop voice and save...");
        }
    }

    class RecorderThread extends Thread {
        RecorderThread() {
        }

        public void run() {
            super.run();
            try {
                File file = new File(MediaRecorderUtil.this.recordFilePath);
                if (MediaRecorderUtil.this.mediarecorder == null) {
                    MediaRecorder unused = MediaRecorderUtil.this.mediarecorder = new MediaRecorder();
                }
                MediaRecorderUtil.this.mediarecorder.setAudioSource(1);
                MediaRecorderUtil.this.mediarecorder.setOutputFormat(6);
                MediaRecorderUtil.this.mediarecorder.setAudioEncoder(3);
                MediaRecorderUtil.this.mediarecorder.setOutputFile(file.getAbsolutePath());
                MediaRecorderUtil.this.mediarecorder.prepare();
                MediaRecorderUtil.this.mediarecorder.start();
                Log.e(MediaRecorderUtil.UPDATE_TAG, "Thread start voice...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
