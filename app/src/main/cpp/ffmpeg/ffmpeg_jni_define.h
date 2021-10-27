#ifndef FFMPEGANDROID_FFMPEG_JNI_DEFINE_H
#define FFMPEGANDROID_FFMPEG_JNI_DEFINE_H

#include <android/log.h>

#define TAG "videoClientLog" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型


#define FFMPEG_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_ffmpeg_panel_jni_FFmpegCmd_ ## FUNC_NAME \
    (JNIEnv *env, jclass thiz, ##__VA_ARGS__)\

#endif //FFMPEGANDROID_FFMPEG_JNI_DEFINE_H
