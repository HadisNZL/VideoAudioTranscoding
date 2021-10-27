package com.ffmpeg.panel.listener;

public interface OnHandleListener {

    void onBegin();

    void onMsg(String msg);

    void onProgress(int progress, int duration);

    void onEnd(int resultCode, String resultMsg);
}
