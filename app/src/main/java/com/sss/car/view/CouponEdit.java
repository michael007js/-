package com.sss.car.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.DatePicker;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.xrichtext.util.DateUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedCoupon;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.CouponGetModel_List;
import com.sss.car.model.CouponModel2;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.R.id.list;

/**
 * Created by leilei on 2018/1/15.
 */

@SuppressWarnings("ALL")
public class CouponEdit extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.pic)
    SimpleDraweeView pic;
    @BindView(R.id.one)
    TextView one;
    @BindView(R.id.two)
    TextView two;
    @BindView(R.id.three)
    TextView three;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.start)
    TextView startTime;
    @BindView(R.id.end)
    TextView endTime;
    @BindView(R.id.coupon_edit)
    LinearLayout couponEdit;
    String start, end;
    DatePicker datePicker;
    YWLoadingDialog ywLoadingDialog;
    CouponGetModel_List couponGetModel_list;
    int couponCount;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_edit);
        ButterKnife.bind(this);
        datePicker = new DatePicker(getBaseActivityContext());
        titleTop.setText("编辑优惠券");
        rightButtonTop.setText("保存");
        customInit(couponEdit, false, true, false);
        rightButtonTop.setTextColor(getResources().getColor(R.color.black));
        couponDetails();
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(number.getText().toString().trim())) {
                    couponCount = 0;
                    count.setText("使用数量" + 0);
                    return;
                }
                if ("0".equals(number.getText().toString().trim())) {
                    number.setText("");
                    couponCount = 0;
                    count.setText("使用数量" + 0);
                    return;
                }
                couponCount = Integer.parseInt(number.getText().toString().trim());
                count.setText("使用数量" + couponCount);
            }
        });
    }

    @OnClick({R.id.back_top, R.id.start, R.id.end, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.start:
                datePicker.setMaxYear(Integer.valueOf(DateUtils.getNowYear()));
                datePicker.setDateListener(new DatePicker.OnDateCListener() {
                    @Override
                    public void onDateSelected(String year, String month, String day) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        if (StringUtils.isEmpty(end)) {
                            start = year + "-" + month + "-" + day;
                            startTime.setText("优惠券开始日期" + year + "-" + month + "-" + day);
                        } else {
                            try {
                                if (simpleDateFormat.parse(year + "-" + month + "-" + day).getTime() > simpleDateFormat.parse(end).getTime()) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "您选择的时间不能大于结束时间");
                                    return;
                                }
                                start = year + "-" + month + "-" + day;
                                startTime.setText("优惠券开始日期" + year + "-" + month + "-" + day);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                datePicker.show();
                break;
            case R.id.end:
                datePicker.setMaxYear(Integer.valueOf(DateUtils.getNowYear()));
                datePicker.setDateListener(new DatePicker.OnDateCListener() {
                    @Override
                    public void onDateSelected(String year, String month, String day) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        if (StringUtils.isEmpty(start)) {
                            end = year + "-" + month + "-" + day;
                            endTime.setText("优惠券结束日期" + year + "-" + month + "-" + day);
                        } else {
                            try {
                                if (simpleDateFormat.parse(year + "-" + month + "-" + day).getTime() < simpleDateFormat.parse(start).getTime()) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "您选择的时间不能大于结束时间");
                                    return;
                                }
                                end = year + "-" + month + "-" + day;
                                endTime.setText("优惠券结束日期" + year + "-" + month + "-" + day);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                datePicker.show();
                break;
            case R.id.right_button_top:
                set_coupon();

                break;
        }
    }


    /**
     * 优惠券详情
     */
    public void couponDetails() {
        couponGetModel_list = getIntent().getExtras().getParcelable("data");
        one.setText(couponGetModel_list.name);
        two.setText(couponGetModel_list.describe);
        three.setText(couponGetModel_list.name);
        start = couponGetModel_list.start_time;
        end = couponGetModel_list.end_time;
        count.setText("使用数量" + couponGetModel_list.number);
    }

    /**
     * 获取加入优惠券店铺
     */
    public void set_coupon() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.set_coupon(
                    new JSONObject()
                            .put("id", couponGetModel_list.id)
                            .put("member_id",Config.member_id)
                            .put("number",couponCount)
                            .put("start_time",start)
                            .put("end_time",end)
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
                                    EventBus.getDefault().post(new ChangedCoupon());
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
            e.printStackTrace();
        }
    }
}
