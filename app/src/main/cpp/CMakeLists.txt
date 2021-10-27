
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)
#project(Intro C)

#add libavcodec
add_library(libffmpeg
        SHARED
        IMPORTED
        )

SET_TARGET_PROPERTIES(
        libffmpeg
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/ffmpeg/prebuilt/${ANDROID_ABI}/libffmpeg.so
)


include_directories(ffmpeg/)

add_library(
        ffmpeg_cmd
        SHARED
        ffmpeg/ffmpeg_cmd.c ffmpeg/ffmpeg.c ffmpeg/cmdutils.c ffmpeg/ffmpeg_filter.c ffmpeg/ffmpeg_hw.c ffmpeg/ffmpeg_opt.c
)

#include_directories(ffmpeg/ffmpeg_jni_define.h)

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

target_link_libraries( # Specifies the target library.
        ffmpeg_cmd
        libffmpeg
        ${log-lib})

set(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/lib)