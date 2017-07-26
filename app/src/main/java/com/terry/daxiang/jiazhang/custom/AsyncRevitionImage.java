package com.terry.daxiang.jiazhang.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.terry.daxiang.jiazhang.utils.ImageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片处理
 *
 * Created by chen_fulei on 2016/11/14.
 */

public class AsyncRevitionImage {
    private HashMap<String, SoftReference<Bitmap>> imageCache;
    private Context context;
    private ExecutorService executorService;

    public AsyncRevitionImage(Context context){
        this.context = context;
        executorService = Executors.newFixedThreadPool(10);
        imageCache = new HashMap<>();
    }

    public void clearCache(){
        if (imageCache != null){
            imageCache.clear();
        }
    }

    public Bitmap revitionImage(final String imagePath, final ImageView imageView , final ImageCallback imageCallback){

        if (imageCache.containsKey(imagePath)) {
            SoftReference<Bitmap> softReference = imageCache.get(imagePath);
            Bitmap b = softReference.get();
            if (b != null) {
                return b;
            }
        }

        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Bitmap) message.obj,imageView, imagePath);
            }
        };

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = ImageUtils.createImageThumbnail(imagePath);
                BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(new File(imagePath)));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 65, bos);
                    bos.flush();

                    imageCache.put(imagePath, new SoftReference<Bitmap>(bitmap));
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (bos != null) {
                        try {
                            bos.close();
                        } catch (IOException e) {
                        }
                    }
                }
                Message message = handler.obtainMessage(0, bitmap);
                handler.sendMessage(message);
            }
        });

        return null;
    }

    /**
     * callback
     */
    public interface ImageCallback {
        public void imageLoaded(Bitmap b, ImageView imageview, String imageUrl);
    }
}
