package com.terry.daxiang.jiazhang.album;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.terry.daxiang.jiazhang.utils.ImageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 图片处理和转换工具
 * @author honey_chen
 *         BKBitmap.bmp
 */
public class BKBitmap
{
    public static int max = 0;
    public static boolean act_bool = true;
    public static List<Bitmap> bmp = new ArrayList<Bitmap>();

    // 图片SD地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
    public static List<String> drr = new ArrayList<String>();

    public static Bitmap revitionImageSize(String path, boolean isFromCamera) throws IOException
    {
        boolean isss = true;
        do
        {
            File fff = new File(path);
            if (fff.length() > 0)
            {
                isss = false;
            }
        } while (isss);
        BufferedOutputStream bos = null;
        Bitmap bitmap = null;
        try
        {
            bitmap = createImageThumbnail(path);
            bos = new BufferedOutputStream(new FileOutputStream(new File(path)));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 65, bos);
            bos.flush();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                } catch (IOException e)
                {
                }
            }
        }
        return bitmap;
    }

    public static Bitmap createImageThumbnail(String filePath)
    {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 1024);
        opts.inJustDecodeBounds = false;
        try
        {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception e)
        {
            // TODO: handle exception
        }

        /**
         * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
         */
        int degree = ImageUtils.readPictureDegree(new File(filePath).getAbsolutePath());
        /**
         * 把图片旋转为正的方向
         */
        if (null != bitmap)
        {
            Bitmap newbitmap = ImageUtils.rotaingImageView(degree, bitmap);
            return newbitmap;
        }

        return bitmap;
    }


    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels)
    {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8)
        {
            roundedSize = 1;
            while (roundedSize < initialSize)
            {
                roundedSize <<= 1;
            }
        } else
        {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels)
    {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound)
        {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1))
        {
            return 1;
        } else if (minSideLength == -1)
        {
            return lowerBound;
        } else
        {
            return upperBound;
        }
    }
}
