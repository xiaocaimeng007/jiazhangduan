package com.terry.daxiang.jiazhang.album;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * grid图片适配器
 */
public class ImageGridAdapter extends BaseAdapter
{

    private TextCallback textcallback = null;
    final String TAG = getClass().getSimpleName();
    Activity act;
    List<ImageItem> dataList;
    Map<String, String> map = new HashMap<String, String>();
    BitmapCache cache;
    private Handler mHandler;
    private int selectTotal = 0;
    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback()
    {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params)
        {
            if (imageView != null && bitmap != null)
            {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag()))
                {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else
                {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else
            {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

    public static interface TextCallback
    {
        public void onListen(int count);
    }

    public void setTextCallback(TextCallback listener)
    {
        textcallback = listener;
    }

    public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler)
    {
        this.act = act;
        dataList = list;
        cache = new BitmapCache();
        this.mHandler = mHandler;
    }

    @Override
    public int getCount()
    {
        int count = 0;
        if (dataList != null)
        {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    class Holder
    {
        private ImageView iv;
        private ImageView selected;
        private TextView text;
    }

    /**
     * get item view
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final Holder holder;

        if (convertView == null)
        {
            holder = new Holder();
            convertView = View.inflate(act, R.layout.item_image_grid, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.image);
            holder.selected = (ImageView) convertView
                    .findViewById(R.id.isselected);
            holder.text = (TextView) convertView
                    .findViewById(R.id.item_image_grid_text);
            convertView.setTag(holder);
        } else
        {
            holder = (Holder) convertView.getTag();
        }

        final ImageItem item = dataList.get(dataList.size() - 1 - position);

        holder.iv.setTag(item.imagePath);
        cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
                callback);
        if (item.isSelected)
        {
            holder.selected.setImageResource(R.mipmap.album_select);
            holder.text.setBackgroundResource(R.drawable.album_bg);
        } else
        {
            holder.selected.setImageResource(R.drawable.shape_while_dp);
            holder.text.setBackgroundColor(0x00000000);
        }
        holder.iv.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String path = dataList.get(dataList.size() - 1 - position).imagePath;

                if ((BKBitmap.drr.size() + selectTotal) < 9)
                {
                    item.isSelected = !item.isSelected;
                    if (item.isSelected)
                    {
                        holder.selected.setImageResource(R.mipmap.album_select);
                        holder.text.setBackgroundResource(R.drawable.album_bg);
                        selectTotal++;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);
                        map.put(path, path);

                    } else if (!item.isSelected)
                    {
                        holder.selected.setImageResource(R.drawable.shape_while_dp);
                        holder.text.setBackgroundColor(0x00000000);
                        selectTotal--;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);
                        map.remove(path);
                    }
                } else if ((BKBitmap.drr.size() + selectTotal) >= 9)
                {
                    if (item.isSelected == true)
                    {
                        item.isSelected = !item.isSelected;
                        holder.selected.setImageResource(R.drawable.shape_while_dp);
                        holder.text.setBackgroundColor(0x00000000);
                        selectTotal--;
                        map.remove(path);

                    } else
                    {
                        Message message = Message.obtain(mHandler, 0);
                        message.sendToTarget();
                    }
                }
            }

        });

        return convertView;
    }
}
