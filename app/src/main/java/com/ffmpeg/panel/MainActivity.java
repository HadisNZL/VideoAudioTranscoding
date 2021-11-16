package com.ffmpeg.panel;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ffmpeg.panel.jni.FFmpegCmd;
import com.ffmpeg.panel.listener.OnHandleListener;
import com.ffmpeg.panel.util.FFmpegUtil;
import com.ffmpeg.panel.util.FileUtil;

import java.io.File;

/**
 * fork 更新
 * https://www.cnblogs.com/youcong/p/10257394.html
 */
public class MainActivity extends AppCompatActivity {
    private TextView ttt, ttt01;

    private static final String TAG = "FFmpegCmd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onPermissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        ttt = findViewById(R.id.ttt);
        ttt01 = findViewById(R.id.ttt01);
        ttt.setOnClickListener(v -> {
            startVideoMerge();
        });
        ttt01.setOnClickListener(v -> {
            startAudioTranscode();
        });
    }

    public void onPermissionRequests(String... permission) {
        ActivityCompat.requestPermissions(this,
                permission,
                1000);
    }

    private void startVideoMerge() {
//        TxtUtil.writeTxtToFile("file '" + FileUtil.getDiskFileDir(getApplicationContext()) + "/final.mp4'", FileUtil.getDiskFileDir(getApplicationContext()), "filelist.concat");
        String srcPath = FileUtil.getDiskFileDir(getApplicationContext()) + File.separator + "filelist.concat";
        String extractVideo = FileUtil.getDiskFileDir(getApplicationContext()) + File.separator + "video_merge.mp4";
        Log.d(TAG, "-->" + srcPath + "\n" + new File(srcPath).exists() + "\n" + extractVideo);
        String[] commandLine = FFmpegUtil.videoMerge(srcPath, extractVideo);
        FFmpegCmd.execute(commandLine, new OnHandleListener() {
            @Override
            public void onBegin() {
                Log.d(TAG, "-->onBegin");
            }

            @Override
            public void onMsg(String msg) {
                Log.d(TAG, "-->onMsg:" + msg);
            }

            @Override
            public void onProgress(int progress, int duration) {
                Log.d(TAG, "-->onProgress:" + progress + ",duration:" + duration);
            }

            @Override
            public void onEnd(int resultCode, String resultMsg) {
                Log.d(TAG, "-->onEnd:" + resultCode + ",resultMsg:" + resultMsg);

            }
        });
    }


    private void startAudioTranscode() {
        String srcPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "BBBBB.mp4";
        String extractVideo = FileUtil.getDiskFileDir(getApplicationContext()) + File.separator + "final01.mp4";
        Log.d(TAG, "-->" + srcPath + "\n" + new File(srcPath).exists() + "\n" + extractVideo);
        String[] commandLine = FFmpegUtil.transformHLAudio(srcPath, extractVideo);
        FFmpegCmd.execute(commandLine, new OnHandleListener() {
            @Override
            public void onBegin() {
                Log.d(TAG, "-->onBegin");
                ttt01.setText("开始转码");
            }

            @Override
            public void onMsg(String msg) {
                Log.d(TAG, "-->onMsg:" + msg);
            }

            @Override
            public void onProgress(int progress, int duration) {
                Log.d(TAG, "-->onProgress:" + progress + ",duration:" + duration);
                ttt01.setText(progress + "%");
            }

            @Override
            public void onEnd(int resultCode, String resultMsg) {
                Log.d(TAG, "-->onEnd:" + resultCode + ",resultMsg:" + resultMsg);
                if (resultCode == 0) {
                    ttt01.setText("转码成功,再次转码");
                } else {
                    ttt01.setText("转码失败，请重试");
                }
            }
        });
    }
}