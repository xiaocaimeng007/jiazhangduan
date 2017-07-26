package com.terry.daxiang.jiazhang.album;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.terry.daxiang.jiazhang.utils.ImageUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 缓存图片的工具
 */
public class BitmapCache extends Activity
{

    public Handler h = new Handler();
    public final String TAG = getClass().getSimpleName();
    private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

    public void put(String path, Bitmap bmp)
    {
        if (!TextUtils.isEmpty(path) && bmp != null)
        {
            imageCache.put(path, new SoftReference<Bitmap>(bmp));
        }
    }

    public void displayBmp(final ImageView iv, final String thumbPath,
                           final String sourcePath, final ImageCallback callback)
    {
        if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath))
        {
            Log.e(TAG, "no paths pass in");
            return;
        }

        final String path;
        final boolean isThumbPath;
        if (!TextUtils.isEmpty(thumbPath))
        {
            path = thumbPath;
            isThumbPath = true;
        } else if (!TextUtils.isEmpty(sourcePath))
        {
            path = sourcePath;
            isThumbPath = false;
        } else
        {
            return;
        }

        if (imageCache.containsKey(path))
        {
            SoftReference<Bitmap> reference = imageCache.get(path);
            Bitmap bmp = reference.get();
            if (bmp != null)
            {
                if (callback != null)
                {
                    callback.imageLoad(iv, bmp, sourcePath);
                }
                iv.setImageBitmap(bmp);
                Log.d(TAG, "hit cache");
                return;
            }
        }
        iv.setImageBitmap(null);

        new Thread()
        {
            Bitmap thumb;

            public void run()
            {

                try
                {
                    if (isThumbPath)
                    {
                        thumb = BitmapFactory.decodeFile(thumbPath);
                        if (thumb == null)
                        {
                            thumb = revitionImageSize(sourcePath);
                        }
                    } else
                    {
                        thumb = revitionImageSize(sourcePath);
                    }
                } catch (Exception e)
                {

                }
                if (thumb == null)
                {
                    thumb = AlbumPicActivity.bimap;
                }
                put(path, thumb);

                if (callback != null)
                {
                    h.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.imageLoad(iv, thumb, sourcePath);
                        }
                    });
                }
            }
        }.start();

    }

    /**
     * 旋转图片
     * @param path
     * @return
     * @throws IOException
     */
    public Bitmap revitionImageSize(String path) throws IOException
    {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true)
        {
            if ((options.outWidth >> i <= 256)
                    && (options.outHeight >> i <= 256))
            {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        /**
         * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转 
         */
        int degree = ImageUtils.readPictureDegree(new File(path).getAbsolutePath());
        /**
         * 把图片旋转为正的方向 
         */
        Bitmap newbitmap = ImageUtils.rotaingImageView(degree, bitmap);
        return newbitmap;
    }

    public interface ImageCallback
    {
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params);
    }
}
