package com.terry.daxiang.jiazhang.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.adapter.TongxunluAdapter;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.bean.TeaherPhoneBookBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CustomDialog;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/15.
 */
public class TongxunluFragment extends BaseFragment {
    @BindView(R.id.tv_kefu)
    TextView tvKefu;
    @BindView(R.id.tv_zhaosheng)
    TextView tvZhaosheng;
    @BindView(R.id.tv_tousu)
    TextView tvTousu;
    @BindView(R.id.rv_dianhua)
    MyRecyclerView rvDianhua;
    @BindView(R.id.header)
    RecyclerViewHeader header;

    private final int RESULT_SCHOOLPHONE_DATA = 0x001;
    private final int RESULT_TEACHERPHONE_DATA = 0x002;

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_tongxunlu, null);
    }

    @Override
    public void init() {
        loadData();
        rvDianhua.setLayoutManager(new LinearLayoutManager(getActivity()));
        header.attachTo(rvDianhua);
    }

    private void loadData(){
        //老师的电话
        RequestService.doGet("?action=getTeaherPhoneBook&uid="+ SharedPrefUtils.getString(getActivity() , SharedPrefUtils.APP_USER_ID)
           ,RESULT_TEACHERPHONE_DATA , this);
        //园所电话
        RequestService.doGet("?action=getSchoolPhoneBook" ,RESULT_SCHOOLPHONE_DATA , this);
    }

    @OnClick({R.id.tv_kefu , R.id.tv_zhaosheng, R.id.tv_tousu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_kefu:
                String kefu = tvKefu.getText().toString().trim().replaceAll("-","");
                sendCall(kefu);
                break;

            case R.id.tv_zhaosheng:
                String zs = tvZhaosheng.getText().toString().trim().replaceAll("-","");
                sendCall(zs);
                break;

            case R.id.tv_tousu:
                String ts = tvZhaosheng.getText().toString().trim().replaceAll("-","");
                sendCall(ts);
                break;
        }
    }

    private void sendCall(final String tel){
        CustomDialog dialog = new CustomDialog(getActivity() , tvKefu , "确定拨打电话吗？" ,false);
        dialog.setBtnText("确定" , "不打了");
        dialog.setOnSureClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent ts_intent = new Intent(Intent.ACTION_CALL);
                    ts_intent.setData(Uri.parse("tel:" + tel));
                    startActivity(ts_intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_SCHOOLPHONE_DATA:
                try{
                    if (resultBean.isSuccess()){
                        JSONObject j_obj = new JSONObject(resultBean.getResult());
                        tvKefu.setText(j_obj.optString("园所电话"));
                        tvZhaosheng.setText(j_obj.optString("招生热线"));
                        tvTousu.setText(j_obj.optString("投诉热线"));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_TEACHERPHONE_DATA:
                try {
                    if (resultBean.isSuccess()){
                        TeaherPhoneBookBean teacherbean = JsonParser.get(new JSONObject(responseStr), TeaherPhoneBookBean.class , new TeaherPhoneBookBean());
                        if (teacherbean == null){
                            teacherbean = JsonParser.get(new JSONObject(responseStr), TeaherPhoneBookBean.class , new TeaherPhoneBookBean());
                        }
                        TongxunluAdapter adapter = new TongxunluAdapter(getActivity(), teacherbean.getData().get(0).getTeachers());
                        rvDianhua.setAdapter(adapter);
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
}
