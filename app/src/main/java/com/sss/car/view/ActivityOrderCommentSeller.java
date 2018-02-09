package com.sss.car.view;

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

public class ActivityOrderCommentSeller extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.pic_activity_order_comment_seller)
    SimpleDraweeView picActivityOrderCommentSeller;
    @BindView(R.id.startbar_activity_order_comment_seller)
    RatingBar ratingBar;
    @BindView(R.id.state_activity_order_comment_seller)
    TextView stateActivityOrderCommentSeller;
    @BindView(R.id.input_activity_order_comment_seller)
    EditText inputActivityOrderCommentSeller;
    @BindView(R.id.activity_order_comment_seller)
    LinearLayout activityOrderCommentSeller;

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
        picActivityOrderCommentSeller = null;
        stateActivityOrderCommentSeller = null;
        inputActivityOrderCommentSeller = null;
        activityOrderCommentSeller = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comment_seller);
        ButterKnife.bind(this);
        customInit(activityOrderCommentSeller, false, true, false);
        titleTop.setText("发表评价");
        rightButtonTop.setText("发布");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + getIntent().getExtras().getString("targetPic")), picActivityOrderCommentSeller, 30f);
        ratingBar.setStarCount(5);
        ratingBar.setOnStarChangeListener(new OnChangeListener() {
            @Override
            public void onChange(int star) {
                switch (star) {
                    case 1:
                        stateActivityOrderCommentSeller.setText("非常差");
                        break;
                    case 2:
                        stateActivityOrderCommentSeller.setText("差");
                        break;
                    case 3:
                        stateActivityOrderCommentSeller.setText("一般");
                        break;
                    case 4:
                        stateActivityOrderCommentSeller.setText("好");
                        break;
                    case 5:
                        stateActivityOrderCommentSeller.setText("非常好");
                        break;
                    default:
                        stateActivityOrderCommentSeller.setText("");
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
                if (grade == 0 || StringUtils.isEmpty(inputActivityOrderCommentSeller.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请填写商品评论或对商品进行星级评价");
                    return;
                }
                shop_comment();
                break;
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
                            .put("order_id", getIntent().getExtras().getString("targetOrderId"))
                            .put("member_id", Config.member_id)
                            .put("contents", inputActivityOrderCommentSeller.getText().toString().trim())
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
