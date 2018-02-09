package com.sss.car.order_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.DatePicker;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.xrichtext.util.DateUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrder;
import com.sss.car.custom.ListViewOrderCheckInfo;
import com.sss.car.custom.ListViewWalletDetails;
import com.sss.car.model.*;
import com.sss.car.utils.CarUtils;
import com.sss.car.view.ActivityImages;

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

import static android.R.attr.mode;
import static android.R.id.list;

/**
 * 我的订单==>明细查询
 * Created by leilei on 2017/10/25.
 */

@SuppressWarnings("ALL")
public class OrderCheckInfo extends BaseActivity implements RefreshLoadMoreLayout.CallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_order_check_info)
    ListViewOrderCheckInfo listviewOrderCheckInfo;
    @BindView(R.id.refresh_order_check_info)
    PullToRefreshScrollView refreshOrderCheckInfo;
    @BindView(R.id.order_check_info)
    LinearLayout orderCheckInfo;

    YWLoadingDialog ywLoadingDialog;
    String start, end;
    int p = 1;
    Gson gson = new Gson();
    List<OrderCheckInfoModel> list = new ArrayList<>();
    List<SSS_Adapter> sssAdapterList = new ArrayList<>();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
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
        setContentView(R.layout.order_check_info);
        ButterKnife.bind(this);
        customInit(orderCheckInfo, false, true, false);
        titleTop.setText("订单查询");
        refreshOrderCheckInfo.setMode(PullToRefreshBase.Mode.BOTH);
        refreshOrderCheckInfo.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                p = 1;
                order_query();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                order_query();
            }
        });
        initHead();
        listviewOrderCheckInfo.setOnListViewOrderCheckInfoCallBack(new ListViewOrderCheckInfo.OnListViewOrderCheckInfoCallBack() {
            @Override
            public void onItem(String type, String order_id, String shop_id, int status, OrderCheckInfoModel orderCheckInfoModel,int position) {
                if ("1".equals(type)) {
                    CarUtils.orderJump(getBaseActivityContext(),"goods",status,order_id,orderCheckInfoModel.member_id.equals(Config.member_id),
                            orderCheckInfoModel.goods_comment,orderCheckInfoModel.is_comment,orderCheckInfoModel.exchange_id,orderCheckInfoModel.exchange_status);
                }else  if ("2".equals(type)) {
                    CarUtils.orderJump(getBaseActivityContext(),"service",status,order_id,orderCheckInfoModel.member_id.equals(Config.member_id),
                            orderCheckInfoModel.goods_comment,orderCheckInfoModel.is_comment,orderCheckInfoModel.exchange_id,orderCheckInfoModel.exchange_status);
                }

            }

            @Override
            public void onShop(String shop_id) {

            }

            @Override
            public void onUser(String shop_id, String target_name) {

            }

            @Override
            public void onQR(String qr) {
                List<String> temp = new ArrayList<>();
                temp.add(Config.url + qr);
                startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                        .putStringArrayListExtra("data", (ArrayList<String>) temp)
                        .putExtra("current", 0));
            }
        });
    }

    void initHead() {
        View headView = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.fragment_order_head, null);
        LinearLayout click_start_fragment_order_head = $.f(headView, R.id.click_start_fragment_order_head);
        final TextView show_start_fragment_order_head = $.f(headView, R.id.show_start_fragment_order_head);
        LinearLayout click_end_fragment_order_head = $.f(headView, R.id.click_end_fragment_order_head);
        final TextView show_end_fragment_order_head = $.f(headView, R.id.show_end_fragment_order_head);
        TextView click_check_end_fragment_order_head = $.f(headView, R.id.click_check_end_fragment_order_head);
        start = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd");
        end = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd");
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
                            if (simpleDateFormat.parse(year + "-" + month + "-" + day).getTime() > simpleDateFormat.parse(end).getTime()) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "您选择的时间不能大于结束时间");
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
                            if (simpleDateFormat.parse(year + "-" + month + "-" + day).getTime() < simpleDateFormat.parse(start).getTime()) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "您选择的时间不能大于结束时间");
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
                order_query();
            }
        });
        listviewOrderCheckInfo.setHeadView(headView);

    }


    @OnClick({R.id.back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
        }
    }


    /**
     * 我的收入订单
     */
    public void order_query() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.order_query(
                    new JSONObject()
                            .put("p", p)
                            .put("beg", start)
                            .put("end", end)
                            .put("member_id", Config.member_id)//用户Id
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshOrderCheckInfo != null) {
                                refreshOrderCheckInfo.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshOrderCheckInfo != null) {
                                refreshOrderCheckInfo.onRefreshComplete();
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
                                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), OrderCheckInfoModel.class));
                                            }
                                            listviewOrderCheckInfo.setList(getBaseActivityContext(), list);
                                    }


                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        order_query();

    }

    @Override
    public void onLoadMore() {
        order_query();
    }
}
