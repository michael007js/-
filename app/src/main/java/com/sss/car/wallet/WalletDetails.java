package com.sss.car.wallet;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.DatePicker;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.ListViewUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.xrichtext.util.DateUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewWalletDetails;
import com.sss.car.model.WalletDetailsModel;
import com.sss.car.model.WalletDetails_SubclassModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包==>明细
 * Created by leilei on 2017/10/25.
 */

public class WalletDetails extends BaseActivity implements RefreshLoadMoreLayout.CallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_wallet_details)
    ListViewWalletDetails listviewWalletDetails;
    @BindView(R.id.refresh_wallet_details)
    PullToRefreshScrollView refreshWalletDetails;
    @BindView(R.id.top_wallet_details)
    ImageView topWalletDetails;
    @BindView(R.id.wallet_details)
    LinearLayout walletDetails;

    YWLoadingDialog ywLoadingDialog;
    String start, end;
    int p = 1;

    List<WalletDetailsModel> list = new ArrayList<>();
    List<SSS_Adapter> sssAdapterList = new ArrayList<>();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        listviewWalletDetails = null;
        refreshWalletDetails = null;
        topWalletDetails = null;
        walletDetails = null;
        if (sssAdapterList != null) {
            for (int i = 0; i < sssAdapterList.size(); i++) {
                sssAdapterList.get(i).clear();
            }
        }
        sssAdapterList.clear();
        if (list != null) {
            list.clear();
        }
        list = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_details);
        ButterKnife.bind(this);
        customInit(walletDetails, false, true, false);
        titleTop.setText("明细查询");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listviewWalletDetails.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topWalletDetails.setVisibility(View.VISIBLE);
                    } else {
                        topWalletDetails.setVisibility(View.GONE);
                    }
                }
            });
        }
        refreshWalletDetails.setMode(PullToRefreshBase.Mode.BOTH);
        refreshWalletDetails.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                p = 1;
                walletDetails();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                walletDetails();
            }
        });
        initHead();
//        walletDetails();
    }

    void initHead() {
        View headView = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.fragment_order_head, null);
        LinearLayout click_start_fragment_order_head = $.f(headView, R.id.click_start_fragment_order_head);
        final TextView show_start_fragment_order_head = $.f(headView, R.id.show_start_fragment_order_head);
        LinearLayout click_end_fragment_order_head = $.f(headView, R.id.click_end_fragment_order_head);
        final TextView show_end_fragment_order_head = $.f(headView, R.id.show_end_fragment_order_head);
        TextView click_check_end_fragment_order_head = $.f(headView, R.id.click_check_end_fragment_order_head);
        start= TimeUtils.millis2String(System.currentTimeMillis(),"yyyy-MM-dd");
        end= TimeUtils.millis2String(System.currentTimeMillis(),"yyyy-MM-dd");
        APPOftenUtils.underLineOfTextView(show_start_fragment_order_head).setText(start);
        APPOftenUtils.underLineOfTextView(show_end_fragment_order_head).setText(end);
        click_start_fragment_order_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker(getBaseActivityContext());
                datePicker.setMaxYear(Integer.valueOf(DateUtils.getNowYear()));
                datePicker.setDateListener(new DatePicker.OnDateCListener() {
                    @Override
                    public void onDateSelected(String year, String month, String day) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            long a = simpleDateFormat.parse(year + "-" + month + "-" + day).getTime();
                            long b = new Date().getTime();
                            if (a > b) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "您选择的时间不能大于当前时间");
                                return;
                            }
                            start = year + "-" + month + "-" + day;
                            APPOftenUtils.underLineOfTextView(show_start_fragment_order_head);
                            show_start_fragment_order_head.setText(year + "-" + month + "-" + day);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                datePicker.show();
            }
        });

        click_end_fragment_order_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker(getBaseActivityContext());
                datePicker.setMaxYear(Integer.valueOf(DateUtils.getNowYear()));
                datePicker.setDateListener(new DatePicker.OnDateCListener() {
                    @Override
                    public void onDateSelected(String year, String month, String day) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            long a = simpleDateFormat.parse(year + "-" + month + "-" + day).getTime();
                            long b = new Date().getTime();
                            if (a > b) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "您选择的时间不能大于当前时间");
                                return;
                            }
                            end = year + "-" + month + "-" + day;
                            APPOftenUtils.underLineOfTextView(show_end_fragment_order_head);
                            show_end_fragment_order_head.setText(year + "-" + month + "-" + day);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                datePicker.show();
            }
        });
        click_check_end_fragment_order_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = 1;
                walletDetails();
            }
        });
        listviewWalletDetails.addHead(headView);

    }


    @OnClick({R.id.back_top, R.id.top_wallet_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.top_wallet_details:
                if (listviewWalletDetails != null) {
                    listviewWalletDetails.scrollTo(0, 0);
                }
                break;
        }
    }


    /**
     */
    public void walletDetails() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.walletDetails(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .put("beg",start)
                            .put("end",end)
                            .put("type", getIntent().getExtras().getString("type"))//1收入，2支出，3积分,4资金（不传返回所有类型）
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshWalletDetails != null) {
                                refreshWalletDetails.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshWalletDetails != null) {
                                refreshWalletDetails.onRefreshComplete();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if (p == 1) {
                                            list.clear();
                                        }
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            WalletDetailsModel walletDetailsModel = new WalletDetailsModel();
                                            walletDetailsModel.type = jsonArray.getJSONObject(i).getString("type");
                                            walletDetailsModel.type_name = jsonArray.getJSONObject(i).getString("type_name");
                                            List<WalletDetails_SubclassModel> subclass = new ArrayList<>();
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("subclass");
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                WalletDetails_SubclassModel walletDetails_subclassMode = new WalletDetails_SubclassModel();
                                                walletDetails_subclassMode.id = jsonArray1.getJSONObject(j).getString("id");
                                                walletDetails_subclassMode.remark = jsonArray1.getJSONObject(j).getString("remark");
                                                walletDetails_subclassMode.order_code = jsonArray1.getJSONObject(j).getString("order_code");
                                                walletDetails_subclassMode.money = jsonArray1.getJSONObject(j).getString("money");
                                                walletDetails_subclassMode.total = jsonArray1.getJSONObject(j).getString("total");
                                                walletDetails_subclassMode.rate_price = jsonArray1.getJSONObject(j).getString("rate_price");
                                                walletDetails_subclassMode.integral = jsonArray1.getJSONObject(j).getString("integral");
                                                walletDetails_subclassMode.create_time = jsonArray1.getJSONObject(j).getString("create_time");
                                                walletDetails_subclassMode.type = jsonArray1.getJSONObject(j).getString("type");
                                                subclass.add(walletDetails_subclassMode);
                                            }
                                            walletDetailsModel.subclass = subclass;
                                            list.add(walletDetailsModel);
                                        }

                                        listviewWalletDetails.setList(getBaseActivityContext(), list);
                                    }else {
                                        if (p==1){
                                            listviewWalletDetails.clearOtherView();
                                        }
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        walletDetails();

    }

    @Override
    public void onLoadMore() {
        walletDetails();
    }
}
