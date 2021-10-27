package com.ffmpeg.panel.jni;

import android.util.Log;

import androidx.annotation.IntDef;

import com.ffmpeg.panel.listener.OnHandleListener;
import com.ffmpeg.panel.util.AsyncTool;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FFmpegCmd {

    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("ffmpeg_cmd");
    }

    private final static String TAG = FFmpegCmd.class.getSimpleName();

    private final static int RESULT_SUCCESS = 1;

    private final static int RESULT_ERROR = 0;

    private static OnHandleListener mProgressListener;

    private static final int STATE_INIT = 0;

    private static final int STATE_RUNNING = 1;

    private static final int STATE_FINISH = 2;

    private static final int STATE_ERROR = 3;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_INIT, STATE_RUNNING, STATE_FINISH, STATE_ERROR})
    public @interface FFmpegState {
    }

    /**
     * 执行FFmpeg command
     */
    public static void execute(final String[] commands, final OnHandleListener onHandleListener) {
        mProgressListener = onHandleListener;
        AsyncTool.instance().addTask(AsyncTool.ETaskType.IO_BOUND, new Runnable() {
            @Override
            public void run() {
                if (onHandleListener != null) {
                    onHandleListener.onBegin();
                }
                //call JNI interface to execute FFmpeg cmd
                int result = handle(commands);
                if (onHandleListener != null) {
                    onHandleListener.onEnd(result, "");
                }
                mProgressListener = null;
            }
        });
    }

    public static int executeSync(final String[] commands) {
        return handle(commands);
    }

    private native static int handle(String[] commands);

    private native static void cancelTaskJni(int cancel);

    public static void onProgressCallback(int position, int duration, @FFmpegState int state) {
        Log.e(TAG, "onProgress position=" + position
                + "--duration=" + duration + "--state=" + state);
        if (position > duration && duration > 0) {
            return;
        }
        if (mProgressListener != null) {
            if (position > 0 && duration > 0) {
                int progress = position * 100 / duration;
                if (progress < 100 || state == STATE_FINISH || state == STATE_ERROR) {
                    mProgressListener.onProgress(progress, duration);
                }
            } else {
                mProgressListener.onProgress(position, duration);
            }
        }
    }

    public static void onMsgCallback(String msg) {
        if (msg != null && !msg.isEmpty()) {
            Log.e(TAG, "from native msg=" + msg);

            // silence detect callback
            if (msg.startsWith("silence") && mProgressListener != null) {
                mProgressListener.onMsg(msg);
            }
        }
    }
}