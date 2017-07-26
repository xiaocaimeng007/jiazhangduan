package com.terry.daxiang.jiazhang.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.shouye.XingweiJiluSingleActivity;
import com.terry.daxiang.jiazhang.activity.shouye.XingweiXiangqingActivity;
import com.terry.daxiang.jiazhang.bean.ChildActionDayBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.VerticalSeekBar;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/20.
 */

public class Xingwei_Tian_Fragment extends BaseFragment {
    @BindView(R.id.tv_nengli)
    TextView tvNengli;
    @BindView(R.id.ll_nengli)
    LinearLayout llNengli;
    @BindView(R.id.tv_xiguan)
    TextView tvXiguan;
    @BindView(R.id.ll_xiguan)
    LinearLayout llXiguan;
    @BindView(R.id.tv_xingge)
    TextView tvXingge;
    @BindView(R.id.ll_xingge)
    LinearLayout llXingge;
    @BindView(R.id.tv_jiankang)
    TextView tvJiankang;
    @BindView(R.id.ll_jiankang)
    LinearLayout llJiankang;
    @BindView(R.id.tv_gaishan)
    TextView tvGaishan;
    @BindView(R.id.ll_gaishan)
    LinearLayout llGaishan;
//    @BindView(R.id.rv_xingwei_tian)
//    MyRecyclerView rvXingwei;

    @BindView(R.id.pb_progressbar_techang)
    VerticalSeekBar pb_progressbar_techang;
    @BindView(R.id.pb_progressbar_siwei)
    VerticalSeekBar pb_progressbar_siwei;
    @BindView(R.id.pb_progressbar_xingge)
    VerticalSeekBar pb_progressbar_xingge;
    @BindView(R.id.pb_progressbar_xiguang)
    VerticalSeekBar pb_progressbar_xiguang;
    @BindView(R.id.pb_progressbar_nengli)
    VerticalSeekBar pb_progressbar_nengli;

    private final int RESULT_ALL_DATA = 0x001;
    private ChildActionDayBean listBean;
    private xingweiTianAdapter adapter;
    ArrayList<ChildActionDayBean.Actiontypes> alldatas = new ArrayList<>();

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_xingwei_tian, null);
    }

    @Override
    public void init() {
//        rvXingwei.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvNengli.setText("0");
        tvGaishan.setText("0");
        tvJiankang.setText("0");
        tvXingge.setText("0");
        tvXiguan.setText("0");

        loadNetData();

        pb_progressbar_nengli.canTouchEvent(false);
        pb_progressbar_siwei.canTouchEvent(false);
        pb_progressbar_techang.canTouchEvent(false);
        pb_progressbar_xiguang.canTouchEvent(false);
        pb_progressbar_xingge.canTouchEvent(false);
    }
    private void loadNetData(){
        RequestService.doGet(Urls.getChildDayActionReport(SharedPrefUtils.getString(getActivity(),SharedPrefUtils.APP_USER_ID)), RESULT_ALL_DATA , this);
    }

    @OnClick({R.id.ll_nengli, R.id.ll_xiguan, R.id.ll_xingge, R.id.ll_jiankang, R.id.ll_gaishan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_nengli:
                try {
                    alldatas.clear();
                    for (ChildActionDayBean.Actiontypes type : listBean.getActiontypes()){
                        if ("特长".equals(type.getName())){
                            alldatas.add(type);
                            break;
                        }
                    }
                    if (alldatas.size() <1){
                        ChildActionDayBean.Actiontypes actiontypes = new ChildActionDayBean.Actiontypes();
                        alldatas.add(actiontypes);
                    }
                    showXingweiTian(alldatas.get(0));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.ll_xiguan:
                try {
                    alldatas.clear();
                    for (ChildActionDayBean.Actiontypes type : listBean.getActiontypes()){
                        if ("思维".equals(type.getName())){
                            alldatas.add(type);
                            break;
                        }
                    }
                    if (alldatas.size() <1){
                        ChildActionDayBean.Actiontypes actiontypes = new ChildActionDayBean.Actiontypes();
                        alldatas.add(actiontypes);
                    }

                    showXingweiTian(alldatas.get(0));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.ll_xingge:
                try {
                    alldatas.clear();
                    for (ChildActionDayBean.Actiontypes type : listBean.getActiontypes()){
                        if ("性格".equals(type.getName())){
                            alldatas.add(type);
                            break;
                        }
                    }
                    if (alldatas.size() <1){
                        ChildActionDayBean.Actiontypes actiontypes = new ChildActionDayBean.Actiontypes();
                        alldatas.add(actiontypes);
                    }
                    showXingweiTian(alldatas.get(0));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.ll_jiankang:
                try {
                    alldatas.clear();
                    for (ChildActionDayBean.Actiontypes type : listBean.getActiontypes()){
                        if ("习惯".equals(type.getName())){
                            alldatas.add(type);
                            break;
                        }
                    }
                    if (alldatas.size() <1){
                        ChildActionDayBean.Actiontypes actiontypes = new ChildActionDayBean.Actiontypes();
                        alldatas.add(actiontypes);
                    }
                    showXingweiTian(alldatas.get(0));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.ll_gaishan:
                try {
                    alldatas.clear();
                    for (ChildActionDayBean.Actiontypes type : listBean.getActiontypes()){
                        if ("能力".equals(type.getName())){
                            alldatas.add(type);
                            break;
                        }
                    }
                    if (alldatas.size() <1){
                        ChildActionDayBean.Actiontypes actiontypes = new ChildActionDayBean.Actiontypes();
                        alldatas.add(actiontypes);
                    }
                    showXingweiTian(alldatas.get(0));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    public void start(String s) {
        Bundle bundle = new Bundle();
        bundle.putString("title", s);
        startActivity(XingweiXiangqingActivity.class, bundle);
    }

    private void showView(){
        if (listBean != null){
            Intent intent = new Intent(XingweiJiluSingleActivity.ACTION_XINGWEI_USER_INFO);
            intent.putExtra("xw_hildname" , listBean.getChildname());
            intent.putExtra("xw_childavator", listBean.getChildavator());
            getActivity().sendBroadcast(intent);

            for (ChildActionDayBean.Actiontypes actiontype : listBean.getActiontypes()){
                if ("能力".equals(actiontype.getName())){
                    tvNengli.setText(actiontype.getItemcount());
                    pb_progressbar_nengli.setProgress(actiontype.getPercent());
                }

                if ("特长".equals(actiontype.getName())){
                    tvGaishan.setText(actiontype.getItemcount());
                    pb_progressbar_techang.setProgress(actiontype.getPercent());
                }

                if ("思维".equals(actiontype.getName())){
                    tvJiankang.setText(actiontype.getItemcount());
                    pb_progressbar_siwei.setProgress(actiontype.getPercent());
                }

                if ("性格".equals(actiontype.getName())){
                    tvXingge.setText(actiontype.getItemcount());
                    pb_progressbar_xingge.setProgress(actiontype.getPercent());
                }

                if ("习惯".equals(actiontype.getName())){
                    tvXiguan.setText(actiontype.getItemcount());
                    pb_progressbar_xiguang.setProgress(actiontype.getPercent());
                }
            }

        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes) {
            case RESULT_ALL_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        listBean = JsonParser.get(new JSONObject(resultBean.getResult()), ChildActionDayBean.class, new ChildActionDayBean());
                        if (listBean == null) {
                            listBean = JsonParser.get(new JSONObject(responseStr), ChildActionDayBean.class, new ChildActionDayBean());
                        }
                        showView();
                        alldatas.clear();
                        for (ChildActionDayBean.Actiontypes type : listBean.getActiontypes()){
                            if ("特长".equals(type.getName())){
                                alldatas.add(type);
                                break;
                            }
                        }

                        if (alldatas.size() <1){
                            ChildActionDayBean.Actiontypes actiontypes = new ChildActionDayBean.Actiontypes();
                            alldatas.add(actiontypes);
                        }

                        showXingweiTian(alldatas.get(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        ToastUtil.show(getString(R.string.string_load_error));
    }


    @BindView(R.id.txt_xinwei_content)
    TextView txt_xinwei_content;
    @BindView(R.id.elv_xingwei)
    ExpandableListView elv_xingwei;

    private void showXingweiTian(ChildActionDayBean.Actiontypes alldata){

        try {
            String name = alldata.getName();
            if (TextUtils.isEmpty(name)){
                name = "";
            }
            txt_xinwei_content.setText(name);

            ExpandableAdapter expandableAdapter= new ExpandableAdapter(getActivity() , alldata.getItems());
            elv_xingwei.setAdapter(expandableAdapter);
            for (int i = 0 ; i< expandableAdapter.getGroupCount(); i++){
                elv_xingwei.expandGroup(i);
            }
            elv_xingwei.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return true;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    class xingweiTianAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private ArrayList<ChildActionDayBean.Actiontypes> datas;
        private LayoutInflater inflater;

        public xingweiTianAdapter(Context context , ArrayList<ChildActionDayBean.Actiontypes> datas){
            this.context = context;
            this.datas = datas;
            inflater = LayoutInflater.from(context);
        }

        public void setDatas(ArrayList<ChildActionDayBean.Actiontypes> newdata){
            this.datas.clear();
            this.datas.addAll(newdata);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_xingwei_tian, parent, false);
            ViewHolder1 viewHolder1 = new ViewHolder1(view);
            return viewHolder1;
        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            try {
                final ChildActionDayBean.Actiontypes alldata = datas.get(position);
                ((ViewHolder1) holder).itemView.setTag(alldata);
                ((ViewHolder1) holder).txt_xinwei_content.setText(alldata.getName());
                ExpandableAdapter expandableAdapter= new ExpandableAdapter(context , alldata.getItems());
                ((ViewHolder1) holder).elv_xingwei.setAdapter(expandableAdapter);
                for (int i = 0 ; i< expandableAdapter.getGroupCount(); i++){
                    ((ViewHolder1) holder).elv_xingwei.expandGroup(i);
                }
                ((ViewHolder1) holder).elv_xingwei.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        class ViewHolder1 extends RecyclerView.ViewHolder{

            @BindView(R.id.txt_xinwei_content)
            TextView txt_xinwei_content;
            @BindView(R.id.elv_xingwei)
            ExpandableListView elv_xingwei;
            public ViewHolder1(View view){
                super(view);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    class ExpandableAdapter extends BaseExpandableListAdapter{

        private Context context;
        private ArrayList<ChildActionDayBean.Actiontypes.Items> Items ;

        public ExpandableAdapter(Context context, ArrayList<ChildActionDayBean.Actiontypes.Items> Items){
            this.context = context;
            this.Items = Items;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return Items == null ? 0 : Items.size();
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(context , R.layout.item_xingwei_tian_parent , null);
            }
            try {
                ChildActionDayBean.Actiontypes.Items data = Items.get(groupPosition);
                TextView txt_xinwei_content = (TextView) convertView.findViewById(R.id.txt_xinwei_content);
                txt_xinwei_content.setText(data.getName());
            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            try {
                return Items == null ? 0 : Items.get(groupPosition).getItemcontents().size();
            }catch (Exception e){
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(context , R.layout.item_xingwei_tian_chrild , null);
            }

            try {
                ChildActionDayBean.Actiontypes.Items.Itemcontents itemcontents = Items.get(groupPosition).getItemcontents().get(childPosition);
                TextView txt_xinwei_content = (TextView) convertView.findViewById(R.id.txt_xinwei_content);
                txt_xinwei_content.setText(itemcontents.getTitle());
                TextView txt_xinwei_date = (TextView) convertView.findViewById(R.id.txt_xinwei_date);
                txt_xinwei_date.setText(itemcontents.getDate());
            }catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
