package com.terry.daxiang.jiazhang.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.adapter.TongxunluAdapter.ViewHolder1;
import com.terry.daxiang.jiazhang.bean.TeaherPhoneBookBean;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.view.CircleImageView;
import com.terry.daxiang.jiazhang.view.CustomDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/16.
 */
public class TongxunluAdapter extends Adapter<ViewHolder1> {
    List<TeaherPhoneBookBean.Data.Teachers> data;
    Context context;
    LayoutInflater inflater;

    public TongxunluAdapter(Context context, List<TeaherPhoneBookBean.Data.Teachers> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dianhua, parent, false);
        ViewHolder1 holder1 = new ViewHolder1(view);
        return holder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder1 holder, int position) {
        final TeaherPhoneBookBean.Data.Teachers dataBean = data.get(position);
        Glide.with(context).load(Urls.URL_AVATAR_HOST+dataBean.getAvatar()).error(R.mipmap.icon_defualt_head).into(holder.ivIcon);
        holder.tvName.setText(dataBean.getTeachername());
        holder.tvZhiwei.setText(dataBean.getTeachertitle());
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCall(holder.itemView , dataBean.getTeachertel());
            }
        });
        holder.ivCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCall(holder.itemView , dataBean.getTeachertel());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder1 extends ViewHolder {
        @BindView(R.id.iv_icon)
        CircleImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_zhiwei)
        TextView tvZhiwei;
        @BindView(R.id.iv_call)
        ImageView ivCall;

        public ViewHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void sendCall(View view ,final String tel){
        CustomDialog dialog = new CustomDialog(context , view , "确定拨打电话吗？" ,false);
        dialog.setBtnText("确定" , "不打了");
        dialog.setOnSureClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent ts_intent = new Intent(Intent.ACTION_CALL);
                    ts_intent.setData(Uri.parse("tel:" + tel));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(ts_intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }
}