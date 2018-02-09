package com.sss.car.view;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.fragment.FragmentOther;
import com.sss.car.fragment.FragmentSaleAndSeckillDiscounts;
import com.sss.car.fragment.FragmentSaleAndSeckillSale;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 淘秒杀淘优惠其他界面
 * Created by leilei on 2017/9/21.
 */

public class ActivitySaleAndSeckill extends BaseActivity {
    @BindView(R.id.back_activity_sale_and_seckill)
    LinearLayout backActivitySaleAndSeckill;
    @BindView(R.id.one_activity_sale_and_seckill)
    TextView oneActivitySaleAndSeckill;
    @BindView(R.id.two_activity_sale_and_seckill)
    TextView twoActivitySaleAndSeckill;
    @BindView(R.id.three_activity_sale_and_seckill)
    TextView threeActivitySaleAndSeckill;
    @BindView(R.id.activity_sale_and_seckill)
    LinearLayout activitySaleAndSeckill;

    @BindView(R.id.parent_activity_sale_and_seckill)
    FrameLayout parentActivitySaleAndSeckill;


    FragmentSaleAndSeckillSale fragmentSaleAndSeckillSale;

    FragmentSaleAndSeckillDiscounts fragmentSaleAndSeckillDiscounts;

    FragmentOther fragmentOther;


    YWLoadingDialog ywLoadingDialog;

    boolean canClick = false;
    int currentPager = 0;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backActivitySaleAndSeckill = null;
        oneActivitySaleAndSeckill = null;
        twoActivitySaleAndSeckill = null;
        threeActivitySaleAndSeckill = null;
        parentActivitySaleAndSeckill = null;
        activitySaleAndSeckill = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_and_seckill);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }
        customInit(activitySaleAndSeckill, false, true, false);
        changeText(1);
        getClassify_id();
    }

    String classify_id;

    void changeText(int what) {
        switch (what) {
            case 1:
                if (canClick) {
                    if (fragmentSaleAndSeckillSale == null) {
                        fragmentSaleAndSeckillSale = new FragmentSaleAndSeckillSale(classify_id, getIntent().getExtras().getString("type"));
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentSaleAndSeckillSale, R.id.parent_activity_sale_and_seckill);
                    }
                    FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillSale);
                    oneActivitySaleAndSeckill.setText("淘秒杀");
                    oneActivitySaleAndSeckill.setTextColor(getResources().getColor(R.color.mainColor));
                    oneActivitySaleAndSeckill.getPaint().setFlags(0);
                    oneActivitySaleAndSeckill.getPaint().setAntiAlias(true);//抗锯齿

                    twoActivitySaleAndSeckill.setTextColor(getResources().getColor(R.color.textColor));
                    twoActivitySaleAndSeckill.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    twoActivitySaleAndSeckill.getPaint().setAntiAlias(true);//抗锯齿
                    twoActivitySaleAndSeckill.setText("淘优惠");

                    threeActivitySaleAndSeckill.setTextColor(getResources().getColor(R.color.textColor));
                    threeActivitySaleAndSeckill.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    threeActivitySaleAndSeckill.getPaint().setAntiAlias(true);//抗锯齿
                    threeActivitySaleAndSeckill.setText("其他");

                }
                break;
            case 2:
                if (fragmentSaleAndSeckillDiscounts == null) {
                    fragmentSaleAndSeckillDiscounts = new FragmentSaleAndSeckillDiscounts(getIntent().getExtras().getString("type"));
                    FragmentUtils.addFragment(getSupportFragmentManager(), fragmentSaleAndSeckillDiscounts, R.id.parent_activity_sale_and_seckill);
                }
                FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillDiscounts);
                if (canClick) {
                    oneActivitySaleAndSeckill.setTextColor(getResources().getColor(R.color.textColor));
                    oneActivitySaleAndSeckill.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    oneActivitySaleAndSeckill.getPaint().setAntiAlias(true);//抗锯齿
                    oneActivitySaleAndSeckill.setText("淘秒杀");

                    twoActivitySaleAndSeckill.setText("淘优惠");
                    twoActivitySaleAndSeckill.setTextColor(getResources().getColor(R.color.mainColor));
                    twoActivitySaleAndSeckill.getPaint().setFlags(0);
                    twoActivitySaleAndSeckill.getPaint().setAntiAlias(true);//抗锯齿

                    threeActivitySaleAndSeckill.setTextColor(getResources().getColor(R.color.textColor));
                    threeActivitySaleAndSeckill.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    threeActivitySaleAndSeckill.getPaint().setAntiAlias(true);//抗锯齿
                    threeActivitySaleAndSeckill.setText("其他");

                }
                break;
            case 3:
                if (canClick) {
                    if (fragmentOther == null) {
                        fragmentOther = new FragmentOther();
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOther, R.id.parent_activity_sale_and_seckill);
                    }
                    FragmentUtils.hideAllShowFragment(fragmentOther);
                    oneActivitySaleAndSeckill.setTextColor(getResources().getColor(R.color.textColor));
                    oneActivitySaleAndSeckill.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    oneActivitySaleAndSeckill.getPaint().setAntiAlias(true);//抗锯齿
                    oneActivitySaleAndSeckill.setText("淘秒杀");

                    twoActivitySaleAndSeckill.setTextColor(getResources().getColor(R.color.textColor));
                    twoActivitySaleAndSeckill.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    twoActivitySaleAndSeckill.getPaint().setAntiAlias(true);//抗锯齿
                    twoActivitySaleAndSeckill.setText("淘优惠");

                    threeActivitySaleAndSeckill.setText("其他");
                    threeActivitySaleAndSeckill.setTextColor(getResources().getColor(R.color.mainColor));
                    threeActivitySaleAndSeckill.getPaint().setFlags(0);
                    threeActivitySaleAndSeckill.getPaint().setAntiAlias(true);//抗锯齿

                    currentPager = 2;
                }
                break;
        }
    }

    @OnClick({R.id.back_activity_sale_and_seckill, R.id.one_activity_sale_and_seckill, R.id.two_activity_sale_and_seckill, R.id.three_activity_sale_and_seckill})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_activity_sale_and_seckill:
                finish();
                break;
            case R.id.one_activity_sale_and_seckill:
                changeText(1);
                break;
            case R.id.two_activity_sale_and_seckill:
                changeText(2);
                break;
            case R.id.three_activity_sale_and_seckill:
                changeText(3);
                break;
        }
    }


    /**
     * 获取classify_id(淘秒杀淘优惠)
     */
    public void getClassify_id() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getClassify_id(
                    new JSONObject()
                            .put("type", getIntent().getExtras().getString("type"))
                            .toString(), new StringCallback() {
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
                                    canClick = true;
                                    classify_id = jsonObject.getJSONObject("data").getString("classify_id");
                                    changeText(1);
                                    if (fragmentSaleAndSeckillSale == null) {
                                        fragmentSaleAndSeckillSale = new FragmentSaleAndSeckillSale(classify_id, getIntent().getExtras().getString("type"));
                                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentSaleAndSeckillSale, R.id.parent_activity_sale_and_seckill);
                                    }
                                    FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillSale);
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:sale-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:sale-0");
            e.printStackTrace();
        }
    }
}
