package com.ffmpeg.panel.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.UUID;

public class FileUtil {
    public static void saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "HomeSecurity" + File.separator;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + File.separator + "HomeSecurity" + File.separator;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(filePic)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }


    }


    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }


    public static String file2String(String path) {
        File file = new File(path);
        FileInputStream fis = null;
        StringBuffer content = new StringBuffer();
        try {
            fis = new FileInputStream(file);
            int length = 2 * 1024 * 1024;
            byte[] byteAttr = new byte[length];
            int byteLength = -1;

            while ((byteLength = fis.read(byteAttr, 0, byteAttr.length)) != -1) {

                String encode = "";
                if (byteLength != byteAttr.length) {
                    byte[] temp = new byte[byteLength];
                    System.arraycopy(byteAttr, 0, temp, 0, byteLength);
                    //使用BASE64转译
                    encode = Base64.encodeToString(temp, Base64.DEFAULT);
                    content.append(encode);
                } else {

                    encode = Base64.encodeToString(byteAttr, Base64.DEFAULT);
                    content.append(encode);
                }
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return content.toString();
    }

    /**
     * 获取去最原始的数据信息
     *
     * @return json data
     */
    public static String getAssetOriginalData(Context context, String name) {
        InputStream input = null;
        try {
            //taipingyang.json文件名称
            input = context.getAssets().open(name);
            String json = convertStreamToString(input);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * input 流转换为字符串
     *
     * @param is
     * @return
     */
    private static String convertStreamToString(InputStream is) {
        String s = null;
        try {
            //格式转换
            Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
            if (scanner.hasNext()) {
                s = scanner.next();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static int getFilesSize(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return file.listFiles().length;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static boolean isFileExist(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Android/data/{pakgage}/cache 路径下
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * Android/data/{pakgage}/files 路径下
     *
     * @param context
     * @return
     */
    public static String getDiskFileDir(Context context) {
        return getDiskFileDir(Environment.DIRECTORY_DOWNLOADS, context);
    }

    public static String getDiskFileDir(String type, Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(type).getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }

    /**
     * 删除单个文件M
     *
     * @param filePathName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteSingleFile(String filePathName) {
        File file = new File(filePathName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePathName + "成功！");
                return true;
            } else {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePathName + "失败！");
                return false;
            }
        } else {
            Log.e("--Method--", "Copy_Delete.deleteSingleFile: " + filePathName + "不存在！");
            return false;
        }
    }

    public static void deleteFileDir(String fileDirName) {
        try {
            File file = new File(fileDirName);
            deleteDirWihtFile(file);
        } catch (Exception e) {
        }
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }


}
