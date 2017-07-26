package com.terry.daxiang.jiazhang.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.terry.daxiang.jiazhang.custom.MyAsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by chen_fulei on 2016/11/14.
 */

public class FileUtils {
    public final static String cacheFolder = "daxiang/cache";
    //发送生成的图片视频等
    public final static String up_cacheFolder = "daxiang/upcache";

    //发送生成的图片视频等
    public final static String fabu_cacheFolder = "daxiang/fasongcache";

    /**
     * 检查SD卡是否存在
     *
     * @return
     */
    public static boolean checkSDCard() {

        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 保存文件至本地
     *
     * @param fileData
     *            要保存的内容
     * @param folderPath
     *            保存地址
     * @param fileName
     *            保存的文件名
     */
    public static void saveFileCache(byte[] fileData, String folderPath,
                                     String fileName) {
        File folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folderPath, fileName);
        ByteArrayInputStream is = new ByteArrayInputStream(fileData);
        OutputStream os = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    os.write(buffer, 0, len);
                }
                os.flush();
            } catch (Exception e) {
                throw new RuntimeException("FileUtils", e);
            } finally {
                closeIO(is, os);
            }
        }
    }

    /**
     * 保存圖片
     *
     * @param bm
     * @param picName
     * @param folderPath
     */
    public static void saveBitmap(Bitmap bm, String picName, String folderPath) {
        try {
            File f = getSaveFile(folderPath, picName + ".jpg");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从指定文件夹获取文件
     *
     * @param folderPath
     * @param fileName
     * @return 如果文件不存在则创建,如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileName) {
        File file = new File(getSavePath(folderPath) + File.separator
                + fileName);
        if (!file.exists()) {
            try {
                Log.e("", "create New File:" + file.length());
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取SD卡下指定文件夹的绝对路径
     *
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * 获取文件夹对象
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(getSDCardPath() + File.separator + folderName
                + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取SD卡绝对路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 输入流转byte[]
     */
    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        byte[] in2b = null;
        BufferedInputStream in = new BufferedInputStream(inStream);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int rc = 0;
        try {
            while ((rc = in.read()) != -1) {
                swapStream.write(rc);
            }
            in2b = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(inStream, in, swapStream);
        }
        return in2b;
    }

    /**
     * 把uri转为File对象
     */
    public static File uri2File(Activity aty, Uri uri) {
        if (DensityUtils.getSDKVersion() < 11) {
            // 在API11以下可以使用：managedQuery
            String[] proj = { MediaStore.Images.Media.DATA };
            @SuppressWarnings("deprecation")
            Cursor actualimagecursor = aty.managedQuery(uri, proj, null, null,
                    null);
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor
                    .getString(actual_image_column_index);
            return new File(img_path);
        } else {
            // 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
            String[] projection = { MediaStore.Images.Media.DATA };
            CursorLoader loader = new CursorLoader(aty, uri, projection, null,
                    null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }
    }

    /**
     * 图片写入文件
     *
     * @param bitmap
     *            图片
     * @param filePath
     *            文件路径
     * @return 是否写入成功
     */
    public static boolean bitmapToFile(Bitmap bitmap, String filePath) {
        boolean isSuccess = false;
        if (bitmap == null) {
            return isSuccess;
        }
        File file = new File(filePath.substring(0,
                filePath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filePath),
                    8 * 1024);
            isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeIO(out);
        }
        return isSuccess;
    }

    /**
     * 关闭流
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException("FileUtils", e);
            }
        }
    }

    /**
     * 刪除指定文件的
     *
     * @param fileName
     */
    public static void delFile(String fileName, String folderPath) {
        File file = getSaveFile(folderPath, fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    /**
     * 清空缓存文件（异步）
     */
    public static void cleanCacheFiles() {
        cleanCacheFiles(cacheFolder);
    }

    /**
     * 清空缓存文件
     *
     * @param cache
     */
    public static void cleanCacheFiles(final String cache) {
        File file = FileUtils.getSaveFolder(cache);
        final File[] fileList = file.listFiles();
        if (fileList != null) {
            // 异步删除全部文件
            MyAsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    for (File f : fileList) {
                        if (f.isFile()) {
                            f.delete();
                        } else if (f.isDirectory()) {
                            cleanCacheFiles(cache + File.separator
                                    + f.getName());
                        }
                    }
                }// end run()
            });
        }// end if
    }

    /**
     * 从assets文件夹中读取文件
     *
     * @param context
     * @param file
     * @return
     * @throws IOException
     */
    public static String readForAssets(Context context, String file) {
        AssetManager assetManager = context.getAssets();

        try {
            InputStream inputStream = assetManager.open(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            byte[] data = out.toByteArray(); // 得到二进制数据
            out.close();
            inputStream.close();
            return new String(data);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 解压Assets中的文件
     * @param context 上下文对象
     * @param assetName 压缩包文件名
     * @param outputDirectory 输出目录
     * @throws IOException
     */
    public static void unZip(Context context, String assetName,String outputDirectory) throws IOException {
        //创建解压目标目录
        File file = new File(outputDirectory);
        //如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        InputStream inputStream = null;
        //打开压缩文件
        inputStream = context.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        //使用1Mbuffer
        byte[] buffer = new byte[1024];
        //解压时字节计数
        int count = 0;
        //如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            //如果是一个目录
            if (zipEntry.isDirectory()) {
                //String name = zipEntry.getName();
                //name = name.substring(0, name.length() - 1);
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                file.mkdir();
            } else {
                //如果是文件
                file = new File(outputDirectory + File.separator
                        + zipEntry.getName());
                //创建该文件
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((count = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.close();
            }
            //定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    /**
     * ===================================================== HCBitmapFinal 专用
     * */
    private static final long POLY64REV = 0x95AC9329AC4BC9B5L;
    private static final long INITIALCRC = 0xFFFFFFFFFFFFFFFFL;
    private static long[] sCrcTable = new long[256];

    /**
     * 获取bitmap的字节大小
     *
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static byte[] getBytes(String in) {
        byte[] result = new byte[in.length() * 2];
        int output = 0;
        for (char ch : in.toCharArray()) {
            result[output++] = (byte) (ch & 0xFF);
            result[output++] = (byte) (ch >> 8);
        }
        return result;
    }

    public static boolean isSameKey(byte[] key, byte[] buffer) {
        int n = key.length;
        if (buffer.length < n) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            if (key[i] != buffer[i]) {
                return false;
            }
        }
        return true;
    }

    public static byte[] makeKey(String httpUrl) {
        return getBytes(httpUrl);
    }

    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    static {
        // 参考 http://bioinf.cs.ucl.ac.uk/downloads/crc64/crc64.c
        long part;
        for (int i = 0; i < 256; i++) {
            part = i;
            for (int j = 0; j < 8; j++) {
                long x = ((int) part & 1) != 0 ? POLY64REV : 0;
                part = (part >> 1) ^ x;
            }
            sCrcTable[i] = part;
        }
    }

    /**
     * A function thats returns a 64-bit crc for string
     *
     * @param in
     *            input string
     * @return a 64-bit crc value
     */
    public static final long crc64Long(String in) {
        if (in == null || in.length() == 0) {
            return 0;
        }
        return crc64Long(getBytes(in));
    }

    public static final long crc64Long(byte[] buffer) {
        long crc = INITIALCRC;
        for (int k = 0, n = buffer.length; k < n; ++k) {
            crc = sCrcTable[(((int) crc) ^ buffer[k]) & 0xff] ^ (crc >> 8);
        }
        return crc;
    }

    /**
     * 复制单个文件
     * @param oldPath
     * @param newPath
     * @return boolean
     */
    public static void CopyFile(File oldPath, String newPath) throws Exception {
        InputStream inStream = null;
        FileOutputStream fs = null;
        if (oldPath.exists()) { //文件存在时
            inStream = new FileInputStream(oldPath); //读入原文件
            fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[2048];
            int byteread = 0;
            while ( (byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fs.flush();
            fs.close();
            inStream.close();
        }
    }
}
