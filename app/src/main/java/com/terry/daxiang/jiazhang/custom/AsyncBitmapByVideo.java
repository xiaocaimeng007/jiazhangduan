package com.terry.daxiang.jiazhang.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.terry.daxiang.jiazhang.utils.ImageUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 视频截取第一帧图片
 *
 * Created by chen_fulei on 2016/11/14.
 */

public class AsyncBitmapByVideo {
    private HashMap<String, SoftReference<Bitmap>> imageCache;
    private Context context;
    private ExecutorService executorService;

    public AsyncBitmapByVideo(Context context){
        this.context = context;
        executorService = Executors.newFixedThreadPool(10);
        imageCache = new HashMap<>();
    }

    public Bitmap LoadBitmapByVideo(final String videoUrl , final ImageView imageView , final ImageCallback imageCallback){

        if (imageCache.containsKey(videoUrl)) {
            SoftReference<Bitmap> softReference = imageCache.get(videoUrl);
            Bitmap b = softReference.get();
            if (b != null) {
                return b;
            }
        }

        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Bitmap) message.obj,imageView, videoUrl);
            }
        };

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = ImageUtils.getVideoThumbnail(videoUrl);
                imageCache.put(videoUrl, new SoftReference<Bitmap>(bitmap));
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
