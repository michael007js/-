package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.OrderCommentListChanged;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrderRetuenOrChange;
import com.sss.car.fragment.FragmentOrder;
import com.sss.car.order_new.OrderModel;
import com.sss.car.rongyun.RongYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;

import static com.sss.car.fragment.FragmentOrder.EXPEND;
import static com.sss.car.fragment.FragmentOrder.INCOME;

/**
 * 退换货申请与补充退换货资料公用界面
 * Created by leilei on 2017/10/12.
 */

@SuppressWarnings("ALL")
public class ActivityOrderReturnsChangeApplyForAndCompleteData extends BaseActivity implements ListViewOrderRetuenOrChange.ListViewOrderRetuenOrChangeCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.order_code)
    TextView orderCode;
    @BindView(R.id.apply_for_type)
    Spinner applyForType;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.click_company)
    LinearLayout clickCompany;
    @BindView(R.id.expressage_code)
    TextView expressageCode;
    @BindView(R.id.click_expressage_code)
    LinearLayout clickExpressageCode;
    @BindView(R.id.reason)
    TextView reason;
    @BindView(R.id.click_reason)
    LinearLayout clickReason;
    @BindView(R.id.photo)
    HorizontalListView photo;
    @BindView(R.id.click_refund)
    TextView clickRefund;
    @BindView(R.id.click_submit)
    TextView clickSubmit;
    @BindView(R.id.activity_order_returns_change_apply_for_and_complete_data)
    LinearLayout activityOrderReturnsChangeApplyForAndCompleteData;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.click_reject)
    TextView clickReject;
    @BindView(R.id.click_agree)
    TextView clickAgree;
    @BindView(R.id.title_company)
    TextView titleCompany;
    @BindView(R.id.title_expressage_code)
    TextView titleExpressageCode;
    @BindView(R.id.title_reason)
    TextView titleReason;
    OrderModel orderModel;
    ListViewOrderRetuenOrChange listViewOrderRetuenOrChange;


    List<String> listSpinner = new ArrayList<>();
    List<String> listPhoto = new ArrayList<>();

    SSS_Adapter sss_adapter;

    boolean isFromBusy = false;
    YWLoadingDialog ywLoadingDialog;


    String sendCompany, sendCode, sendReason, sendResult, sendReturnOrChange/*1退货2换货*/;

    String sendFeedback;

    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.click_result)
    LinearLayout clickResult;
    @BindView(R.id.click_complete_submit)
    TextView clickCompleteSubmit;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        orderCode = null;
        applyForType = null;
        company = null;
        clickCompany = null;
        expressageCode = null;
        clickExpressageCode = null;
        reason = null;
        clickReason = null;
        photo = null;
        clickRefund = null;
        clickSubmit = null;
        activityOrderReturnsChangeApplyForAndCompleteData = null;
        rightButtonTop = null;
        clickReject = null;
        clickAgree = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_returns_change_apply_for_and_complete_data);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        orderModel = getIntent().getParcelableExtra("data");

        listViewOrderRetuenOrChange = $.f(this, R.id.listview);
        listViewOrderRetuenOrChange.setListViewOrderCallBack(this);
        ButterKnife.bind(this);
        orderCode.setText("" + orderModel.order_code);
        customInit(activityOrderReturnsChangeApplyForAndCompleteData, false, true, true);
        initView();


    }


    void initView() {
        if (getIntent().getExtras().getBoolean("isShowclickRefund")) {
            isFromBusy = false;
            clickRefund.setVisibility(View.VISIBLE);
            clickRefund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        LogUtils.e(/*EXPEND:42*/EXPEND+"--"+getIntent().getExtras().getString("type"));
        if (EXPEND == getIntent().getExtras().getInt("what")) {//从买家端中进入
            if ("1".equals(getIntent().getExtras().getString("type"))) {//退货
                isFromBusy = true;
                titleTop.setText("完善退换货资料");
                rightButtonTop.setText("联系卖家");
                clickSubmit.setVisibility(View.VISIBLE);
                clickExpressageCode.setVisibility(View.VISIBLE);
                clickSubmit.setVisibility(View.VISIBLE);
                List<OrderModel> list = new ArrayList<>();
                list.add(orderModel);
                listViewOrderRetuenOrChange.setList(getBaseActivityContext(), list);
                rightButtonTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RongYunUtils.startConversation(getBaseActivityContext(),
                                Conversation.ConversationType.PRIVATE, getIntent().getExtras().getString("targetId"),
                                getIntent().getExtras().getString("targetName"));
                    }
                });
                clickSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        applyForReturnsAndChange();
                    }
                });
                clickReason.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFromBusy) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                        .putExtra("canChange", true)
                                        .putExtra("type", "returnAndChangeReason").putExtra("extra", reason.getText().toString()));
                            }
                        }
                    }
                });
                clickSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completeReturnsAndChange();
                    }
                });
                clickCompany.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFromBusy) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                        .putExtra("canChange", true)
                                        .putExtra("type", "expressageCompany").putExtra("extra", Config.username));
                            }
                        }
                    }
                });
                clickExpressageCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFromBusy) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                        .putExtra("canChange", true)
                                        .putExtra("type", "expressageCode").putExtra("extra", Config.username));
                            }
                        }
                    }
                });

                initSpinner(true, 0);
                initPhotoAdapter();
            } else if ("2".equals(getIntent().getExtras().getString("type"))) {//换货
                isFromBusy = false;
                titleTop.setText("退换货详情");
                rightButtonTop.setText("联系卖家");
                clickResult.setVisibility(View.VISIBLE);
                applyForType.setEnabled(false);
                rightButtonTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RongYunUtils.startConversation(getBaseActivityContext(), Conversation.ConversationType.PRIVATE, getIntent().getExtras().getString("targetId"), getIntent().getExtras().getString("targetName"));
                    }
                });
                clickResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                    .putExtra("canChange", true)
                                    .putExtra("type", "expressageCompany").putExtra("extra", Config.username));
                        }
                    }
                });
                clickSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completeReturnsAndChange();
                    }
                });
                clickCompany.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFromBusy) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                        .putExtra("canChange", true)
                                        .putExtra("type", "expressageCompany").putExtra("extra", Config.username));
                            }
                        }
                    }
                });
                clickExpressageCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFromBusy) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                        .putExtra("canChange", true)
                                        .putExtra("type", "expressageCode").putExtra("extra", Config.username));
                            }
                        }
                    }
                });
                initSpinner(true, 0);
                initPhotoAdapter();
            }else if ("3".equals(getIntent().getExtras().getString("type"))){//申请
                isFromBusy = true;
                titleTop.setText("退换货申请");
                rightButtonTop.setText("联系卖家");
                clickSubmit.setVisibility(View.VISIBLE);
                applyForType.setEnabled(false);
                List<OrderModel> list = new ArrayList<>();
                list.add(orderModel);
                listViewOrderRetuenOrChange.setList(getBaseActivityContext(), list);
                rightButtonTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RongYunUtils.startConversation(getBaseActivityContext(),
                                Conversation.ConversationType.PRIVATE, getIntent().getExtras().getString("targetId"),
                                getIntent().getExtras().getString("targetName"));
                    }
                });
                clickSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        applyForReturnsAndChange();
                    }
                });
                titleExpressageCode.setTextColor(getResources().getColor(R.color.line));
                titleCompany.setTextColor(getResources().getColor(R.color.line));
                clickReason.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFromBusy) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                        .putExtra("canChange", true)
                                        .putExtra("type", "returnAndChangeReason").putExtra("extra", reason.getText().toString()));
                            }
                        }
                    }
                });
                initSpinner(true, 0);
                initPhotoAdapter();
            }else if ("4".equals(getIntent().getExtras().getString("type"))){//完善资料
                isFromBusy = true;
                titleTop.setText("完善退换货资料");
                rightButtonTop.setText("联系卖家");
                clickSubmit.setVisibility(View.VISIBLE);
                clickExpressageCode.setVisibility(View.VISIBLE);
                clickSubmit.setVisibility(View.VISIBLE);
                List<OrderModel> list = new ArrayList<>();
                list.add(orderModel);
                listViewOrderRetuenOrChange.setList(getBaseActivityContext(), list);
                rightButtonTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RongYunUtils.startConversation(getBaseActivityContext(),
                                Conversation.ConversationType.PRIVATE, getIntent().getExtras().getString("targetId"),
                                getIntent().getExtras().getString("targetName"));
                    }
                });
                clickSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        applyForReturnsAndChange();
                    }
                });
                clickReason.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFromBusy) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                        .putExtra("canChange", true)
                                        .putExtra("type", "returnAndChangeReason").putExtra("extra", reason.getText().toString()));
                            }
                        }
                    }
                });
                clickSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completeReturnsAndChange();
                    }
                });
                clickCompany.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFromBusy) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                        .putExtra("canChange", true)
                                        .putExtra("type", "expressageCompany").putExtra("extra", Config.username));
                            }
                        }
                    }
                });
                clickExpressageCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFromBusy) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                        .putExtra("canChange", true)
                                        .putExtra("type", "expressageCode").putExtra("extra", Config.username));
                            }
                        }
                    }
                });

                initSpinner(true, 0);
                initPhotoAdapter();
            }
        } else if (INCOME == getIntent().getExtras().getInt("what")) {//从卖家端中进入
            if ("1".equals(getIntent().getExtras().getString("type"))) {//退货

            } else if ("2".equals(getIntent().getExtras().getString("type"))) {//换货

            }
        }


        if ("1".equals(getIntent().getExtras().getString("type"))) {//退货
            if (EXPEND == getIntent().getExtras().getInt("what")) {//从买家端中进入

            } else if (INCOME == getIntent().getExtras().getInt("what")) {//从卖家端中进入

            }
        } else if ("2".equals(getIntent().getExtras().getString("type"))) {//换货
            getReturnAndChangeOrderInfo();
            clickResult.setVisibility(View.VISIBLE);
            isFromBusy = true;
            titleTop.setText("退换货资料完善");
            rightButtonTop.setText("联系买家");
            rightButtonTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RongYunUtils.startConversation(getBaseActivityContext(), Conversation.ConversationType.PRIVATE, getIntent().getExtras().getString("targetId"), getIntent().getExtras().getString("targetName"));
                }
            });

            clickCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFromBusy) {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                    .putExtra("canChange", true)
                                    .putExtra("type", "expressageCompany").putExtra("extra", Config.username));
                        }
                    }
                }
            });

            clickExpressageCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFromBusy) {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                    .putExtra("canChange", true)
                                    .putExtra("type", "expressageCode").putExtra("extra", Config.username));
                        }
                    }
                }
            });

            clickSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeReturnsAndChange();
                }
            });
            initSpinner(true, 0);
            initPhotoAdapter();
        }
    }


    void initSpinner(boolean isStopOperation, int i) {
        listSpinner.add("退货");
        listSpinner.add("换货");
        applyForType.setAdapter(new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_spinner, listSpinner) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean,SSS_Adapter instance) {
                helper.setText(R.id.text_item_spinner, bean);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        });
        applyForType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (isFromBusy)
                            sendReturnOrChange = "1";//1退货2换货
                        break;
                    case 1:
                        if (isFromBusy)
                            sendReturnOrChange = "2";//1退货2换货
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        applyForType.setEnabled(isStopOperation);
//        applyForType.setSelection(i);
    }


    void initPhotoAdapter() {
        if (isFromBusy) {
            listPhoto.add("default");
        }

        sss_adapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_image) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean,SSS_Adapter instance) {
                if ("default".equals(bean)) {
                    FrescoUtils.showImage(false, 80, 80, Uri.parse("res://" + getBaseActivityContext().getPackageName() + "/" + R.mipmap.logo_add_image), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                } else {
                    if (bean.startsWith("/storage/")) {
                        FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(bean)), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                    } else {
                        FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                    }
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        if (isFromBusy) {
            photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if ("default".equals(listPhoto.get(position))) {
                        APPOftenUtils.createPhotoChooseDialog(0, 9, weakReference, MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                if (resultList == null || resultList.size() == 0) {
                                    return;
                                }
                                for (int i = 0; i < resultList.size(); i++) {
                                    listPhoto.add(resultList.get(i).getPhotoPath());
                                    sss_adapter.setList(listPhoto);
                                }
                            }

                            @Override
                            public void onHanlderFailure(int requestCode, String errorMsg) {

                            }
                        });
                    } else {
                        if (getBaseActivityContext() != null) {
                            List<String> temp = new ArrayList<>();
                            for (int j = 0; j < listPhoto.size(); j++) {
                                if (!"default".equals(listPhoto.get(j))) {
                                    if (listPhoto.get(j).startsWith("/storage/")) {
                                        temp.add(listPhoto.get(j));
                                    } else {
                                        temp.add(Config.url + listPhoto.get(j));
                                    }
                                }
                            }
                            startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                    .putExtra("current", position));
                        }

                    }
                }
            });
        }
        sss_adapter.setList(listPhoto);
        photo.setAdapter(sss_adapter);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "expressageCompany":
                sendCompany = event.type;
                company.setText(event.msg);
                break;
            case "expressageCode":
                sendCode = event.type;
                expressageCode.setText(event.msg);
                break;
            case "returnAndChangeReason":
                sendReason = event.msg;
                reason.setText(event.msg);
                break;
            case "result":
                sendResult = event.msg;
                result.setText(event.msg);
                break;
        }
    }

    @OnClick({R.id.back_top, R.id.click_refund, R.id.click_reject, R.id.click_agree, R.id.click_complete_submit,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_refund:

                break;
            case R.id.click_reject:
                status_exchange("2");//1同意，2拒绝
                break;
            case R.id.click_agree:
                status_exchange("1");//1同意，2拒绝
                break;
            case R.id.click_complete_submit:
                completeReturnsAndChange();

                break;
        }
    }


    /**
     * 申请退换货
     */
    public void applyForReturnsAndChange() {
        if (StringUtils.isEmpty(sendReason)) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写退换货理由");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        JSONArray goods_id = new JSONArray();
        for (int i = 0; i < listViewOrderRetuenOrChange.getList().get(0).goods_data.size(); i++) {
            goods_id.put(listViewOrderRetuenOrChange.getList().get(i).goods_data.get(0).goods_id);
        }

        JSONArray picture = new JSONArray();
        for (int i = 0; i < listPhoto.size(); i++) {
            if (!"default".equals(listPhoto.get(i))) {
                picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(listPhoto.get(i),
                        getWindowManager().getDefaultDisplay().getWidth(),
                        getWindowManager().getDefaultDisplay().getHeight())));
            }
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.applyForReturnsAndChange(
                    new JSONObject()
                            .put("order_id", listViewOrderRetuenOrChange.getList().get(0).order_id)
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .put("cause", sendReason)
                            .put("type", sendReturnOrChange)
                            .put("picture", picture)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    List<String> temp = new ArrayList<>();
                                    for (int i = 0; i < orderModel.goods_data.size(); i++) {
                                        temp.add(orderModel.goods_data.get(i).goods_id);
                                    }
                                    EventBus.getDefault().post(new OrderCommentListChanged(listViewOrderRetuenOrChange.getList().get(0).order_id, temp));
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    finish();
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


    /**
     * 买家获取退换货详情
     */
    public void getReturnAndChangeOrderInfo() {

        OrderModel orderModel = getIntent().getExtras().getParcelable("data");
        if (orderModel == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < orderModel.goods_data.size(); i++) {
            jsonArray.put(orderModel.goods_data.get(i).goods_id);
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getReturnAndChangeOrderInfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("goods_id", jsonArray)
                            .put("order_id", getIntent().getExtras().getString("order_id"))
                            .put("type", getIntent().getExtras().getString("type"))
                            .put("status", getIntent().getExtras().getString("status"))
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {

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

    /**
     * 同意和拒绝订单信息
     *
     * @param status 1同意，2拒绝
     */
    public void status_exchange(String status) {
        if (StringUtils.isEmpty(sendCompany) || StringUtils.isEmpty(sendCode)) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写完快递信息后再提交");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.status_exchange(
                    new JSONObject()
                            .put("exchange_id", getIntent().getExtras().getString("exchange_id"))
                            .put("member_id", Config.member_id)
                            .put("status", status)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    finish();
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

    /**
     * 完善退换货资料
     */
    public void completeReturnsAndChange() {
        if (StringUtils.isEmpty(sendCompany) || StringUtils.isEmpty(sendCode)) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写完快递信息后再提交");
            return;
        }
        JSONArray goods_id = new JSONArray();
        for (int i = 0; i < listViewOrderRetuenOrChange.getList().get(0).goods_data.size(); i++) {
            goods_id.put(listViewOrderRetuenOrChange.getList().get(i).goods_data.get(0).goods_id);
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.completeReturnsAndChange(
                    new JSONObject()
                            .put("order_id", getIntent().getExtras().getString("order_id"))
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .put("type", getIntent().getExtras().getString("type"))
                            .put("expressage", sendCompany)
                            .put("waybill", sendCode)
                            .put("feedback", sendFeedback)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    finish();
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
    public void onName(String targetPic, String targetName, String targetId) {
        switch (getIntent().getExtras().getInt("what")) {
            case FragmentOrder.INCOME:
                startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                        .putExtra("id", targetId));
                break;

            case FragmentOrder.EXPEND:
                if (getBaseActivityContext() != null) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                                .putExtra("shop_id", targetId));
                    }
                }
                break;
        }
    }
}
