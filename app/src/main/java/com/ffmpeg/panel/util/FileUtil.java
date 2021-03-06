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
                    //??????BASE64??????
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
     * ?????????????????????????????????
     *
     * @return json data
     */
    public static String getAssetOriginalData(Context context, String name) {
        InputStream input = null;
        try {
            //taipingyang.json????????????
            input = context.getAssets().open(name);
            String json = convertStreamToString(input);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * input ?????????????????????
     *
     * @param is
     * @return
     */
    private static String convertStreamToString(InputStream is) {
        String s = null;
        try {
            //????????????
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
     * Android/data/{pakgage}/cache ?????????
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
     * Android/data/{pakgage}/files ?????????
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
     * ??????????????????M
     *
     * @param filePathName ??????????????????????????????
     * @return ??????????????????????????????true???????????????false
     */
    public static boolean deleteSingleFile(String filePathName) {
        File file = new File(filePathName);
        // ????????????????????????????????????????????????????????????????????????????????????
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: ??????????????????" + filePathName + "?????????");
                return true;
            } else {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: ??????????????????" + filePathName + "?????????");
                return false;
            }
        } else {
            Log.e("--Method--", "Copy_Delete.deleteSingleFile: " + filePathName + "????????????");
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
                file.delete(); // ??????????????????
            else if (file.isDirectory())
                deleteDirWihtFile(file); // ??????????????????????????????
        }
        dir.delete();// ??????????????????
    }


}
