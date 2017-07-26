package com.terry.daxiang.jiazhang.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen_fulei on 2016/12/14.
 */

public class TesebanActivity extends BaseTestActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rv_guanzhu)
    MyRecyclerView rvGuanzhu;

    private final int RESULT_ALL_DATA = 0x001;
    private final Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoucang);
        ButterKnife.bind(this);
        setTextview(tvTitle,"特色班");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvGuanzhu.setLayoutManager(layoutManager);

        RequestService.doGet(Urls.getTSB("155" , SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID) , "1" , ""), RESULT_ALL_DATA , this);
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA:
                try {
                    if (resultBean.isSuccess()){
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }

                        TesebanAdapter adapter = new TesebanAdapter(activity , listBean.getData());
                        rvGuanzhu.setAdapter(adapter);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        ToastUtil.show(getString(R.string.string_load_error));
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }


    class TesebanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private Context context ;
        private LayoutInflater inflater;
        private ArrayList<ArticleListBean.Data> datas;
        public TesebanAdapter(Context context , ArrayList<ArticleListBean.Data> datas){
            this.context = context;
            this.datas = datas;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_tianjia_guanzhu, parent, false);
            ViewHodler1 hodler1 = new ViewHodler1(view);
            hodler1.tv_tianjia.setVisibility(View.GONE);
            return hodler1;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ArticleListBean.Data data = datas.get(position);
            ((ViewHodler1)holder).itemView.setTag(data);
            ((ViewHodler1)holder).tv_name.setText(data.getTitle());
            ((ViewHodler1)holder).tv_text.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", data.getTitle());
                    bundle.putString("content_cid" ,data.getId()+"");
                    bundle.putBoolean("isdashang" , true);
                    startActivity(XiangqingActivity.class, bundle);
                }
            });
        }

        class ViewHodler1 extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_name)
            TextView tv_name;
            @BindView(R.id.tv_text)
            TextView tv_text;
            @BindView(R.id.tv_tianjia)
            TextView tv_tianjia;
            public ViewHodler1(View itemView){
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
