package com.sss.car.order_new;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azhong.ratingbar.OnChangeListener;
import com.azhong.ratingbar.RatingBar;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.EventBusModel.OrderCommentListChanged;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;



/**
 * 评论交易订单
 * Created by leilei on 2017/10/10.
 */

public class OrderCommentSeller extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.pic_order_comment_seller)
    SimpleDraweeView picOrderCommentSeller;
    @BindView(R.id.startbar_order_comment_seller)
    RatingBar ratingBar;
    @BindView(R.id.state_order_comment_seller)
    TextView stateOrderCommentSeller;
    @BindView(R.id.input_order_comment_seller)
    EditText inputOrderCommentSeller;
    @BindView(R.id.order_comment_seller)
    LinearLayout OrderCommentSeller;

    int grade = 0;
    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        picOrderCommentSeller = null;
        stateOrderCommentSeller = null;
        inputOrderCommentSeller = null;
        OrderCommentSeller = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_comment_seller);
        ButterKnife.bind(this);
        customInit(OrderCommentSeller, false, true, false);
        titleTop.setText("发表评价");
        rightButtonTop.setText("发布");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        order_comment();
        ratingBar.setStarCount(5);
        ratingBar.setOnStarChangeListener(new OnChangeListener() {
            @Override
            public void onChange(int star) {
                switch (star) {
                    case 1:
                        stateOrderCommentSeller.setText("非常差");
                        grade=1;
                        break;
                    case 2:
                        stateOrderCommentSeller.setText("差");
                        grade=2;
                        break;
                    case 3:
                        stateOrderCommentSeller.setText("一般");
                        grade=3;
                        break;
                    case 4:
                        stateOrderCommentSeller.setText("好");
                        grade=4;
                        break;
                    case 5:
                        stateOrderCommentSeller.setText("非常好");
                        grade=5;
                        break;
                    default:
                        stateOrderCommentSeller.setText("");
                }
            }

        });
    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (grade == 0 || StringUtils.isEmpty(inputOrderCommentSeller.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请填写商品评论或对商品进行星级评价");
                    return;
                }
                shop_comment();
                break;
        }
    }

    public void order_comment() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.order_comment(
                    new JSONObject()
                            .put("order_id", getIntent().getExtras().getString("order_id"))
                            .put("member_id", Config.member_id)
                            .put("type", "1")//1收入，2支出
                            .toString()//用户Id
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
                                    FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + jsonObject.getJSONObject("data").getString("picture")), picOrderCommentSeller, 30f);
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
     * 订单评论
     */
    public void shop_comment() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.shop_comment(
                    new JSONObject()
                            .put("order_id", getIntent().getExtras().getString("order_id"))
                            .put("member_id", Config.member_id)
                            .put("contents", inputOrderCommentSeller.getText().toString().trim())
                            .put("grade", grade)
                            .toString()//用户Id
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
                                    EventBus.getDefault().post(new ChangedOrderModel());
                               EventBus.getDefault().post(new OrderCommentListChanged(getIntent().getExtras().getString("targetOrderId")));
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
}
