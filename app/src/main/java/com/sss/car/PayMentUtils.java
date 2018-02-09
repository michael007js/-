package com.sss.car;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.view.ActivityMyDataSynthesizSettingSetPayPassword;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;


/**
 * 本类暂时作废（新类为PayUtils）
 * Created by leilei on 2017/11/28.
 */

@SuppressWarnings("ALL")
public class PayMentUtils {
    /**
     * 弹出支付密码输入框
     *
     * @param title
     * @param activity
     */
    String payMode;
    String is_integral = "0";


    /**
     * 请求可用积分
     * @param ywLoadingDialog
     * @param title
     * @param money
     * @param score
     * @param activity
     * @param onPaymentCallBack
     * @throws JSONException
     */
    public void requestPayment(final YWLoadingDialog ywLoadingDialog, final String title, final int money, final BaseActivity activity, final OnPaymentCallBack onPaymentCallBack) throws JSONException {
        activity.addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.can_integral(
                new JSONObject()
                        .put("member_id", Config.member_id)
                        .toString(), new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        ToastUtils.showShortToast(activity, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String a = "0";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if ("1".equals(jsonObject.getString("status"))) {
                                a=jsonObject.getJSONObject("data").getString("deduction");
                            }

                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            double b=Double.valueOf(a);
                           createPaymentDialog(ywLoadingDialog,title,money,(int) b,activity,onPaymentCallBack);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })));
    }


    private void createPaymentDialog(final YWLoadingDialog ywLoadingDialog, final String title, final int money, final int score, final BaseActivity activity, final OnPaymentCallBack onPaymentCallBack) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_payment_bottom, null);
        TextView close_dialog_payment_bottom = $.f(view, R.id.close_dialog_payment_bottom);
        final TextView money_dialog_payment_bottom = $.f(view, R.id.money_dialog_payment_bottom);
        TextView click_dialog_payment_bottom = $.f(view, R.id.click_dialog_payment_bottom);
        TextView title_dialog_payment_bottom = $.f(view, R.id.title_dialog_payment_bottom);
        TextView score_dialog_payment_bottom_score = $.f(view, R.id.score_dialog_payment_bottom_score);
        TextView cancel_dialog_payment_bottom_score = $.f(view, R.id.cancel_dialog_payment_bottom_score);
        TextView click_next_dialog_payment_bottom = $.f(view, R.id.click_next_dialog_payment_bottom);
        final CheckBox cb_balance_dialog_payment_bottom = $.f(view, R.id.cb_balance_dialog_payment_bottom);
        final CheckBox cb_wx_dialog_payment_bottom = $.f(view, R.id.cb_wx_dialog_payment_bottom);
        final CheckBox cb_zfb_dialog_payment_bottom = $.f(view, R.id.cb_zfb_dialog_payment_bottom);
        final CheckBox cb_score_dialog_payment_bottom_score = $.f(view, R.id.cb_score_dialog_payment_bottom_score);
        final LinearLayout parent_dialog_payment_bottom_score = $.f(view, R.id.parent_dialog_payment_bottom_score);
        score_dialog_payment_bottom_score.setText(score + "");
        money_dialog_payment_bottom.setText(money + "");
        if (!StringUtils.isEmpty(title)) {
            title_dialog_payment_bottom.setText(title);
        }
        click_dialog_payment_bottom.setText("使用积分抵扣");
        close_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        click_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_dialog_payment_bottom_score.setVisibility(View.VISIBLE);
            }
        });
        cancel_dialog_payment_bottom_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_dialog_payment_bottom_score.setVisibility(View.GONE);
            }
        });
        click_next_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (cb_score_dialog_payment_bottom_score.isChecked()){
                  if ( score- money <= 0) {
                      if (onPaymentCallBack != null) {
                          onPaymentCallBack.onErrorMsg("您的积分不够");
                      }
                      return;
                  }
              }
                bottomSheetDialog.dismiss();
                if (cb_balance_dialog_payment_bottom.isChecked()) {
                    if (ywLoadingDialog!=null){
                        ywLoadingDialog.show();
                    }
                    P.e(ywLoadingDialog, Config.member_id, activity, new P.p() {
                        @Override
                        public void exist() {
                            createPasswordInputDialog("请输入您的支付密码", activity, new OnPayPasswordVerificationCallBack() {
                                @Override
                                public void onVerificationPassword(final String password, final PassWordKeyboard passWordKeyboard, final BottomSheetDialog bottomSheetDialog) {
                                    P.r(ywLoadingDialog, Config.member_id, password, activity, new P.r() {
                                        @Override
                                        public void match() {
                                            bottomSheetDialog.dismiss();
                                            passWordKeyboard.setStatus(true);
                                            if (onPaymentCallBack != null) {
                                                passWordKeyboard.setStatus(true);
                                                bottomSheetDialog.dismiss();
                                                if ("1".equals(is_integral)) {
                                                    onPaymentCallBack.onMatch(payMode, is_integral, (money - score));
                                                } else {
                                                    onPaymentCallBack.onMatch(payMode, is_integral, (money));
                                                }

                                            }
                                        }

                                        @Override
                                        public void mismatches() {
                                            passWordKeyboard.setStatus(false);
                                            if (onPaymentCallBack != null) {
                                                onPaymentCallBack.onMismatches();
                                            }
                                        }
                                    });


                                }

                            });
                        }

                        @Override
                        public void nonexistence() {
                            if (activity != null) {
                                activity.  startActivity(new Intent(activity, ActivityMyDataSynthesizSettingSetPayPassword.class)
                                        .putExtra("mode",ActivityMyDataSynthesizSettingSetPayPassword.set));
                            }
                        }
                    });

                } else if (cb_wx_dialog_payment_bottom.isChecked()) {
                    bottomSheetDialog.dismiss();
                    if (onPaymentCallBack != null) {
                        bottomSheetDialog.dismiss();
                        if ("1".equals(is_integral)) {
                            onPaymentCallBack.onWeiXin(payMode, is_integral, (money - score));
                        } else {
                            onPaymentCallBack.onWeiXin(payMode, is_integral, (money));
                        }
                    }
                } else if (cb_zfb_dialog_payment_bottom.isChecked()) {
                    bottomSheetDialog.dismiss();
                    if (onPaymentCallBack != null) {
                        bottomSheetDialog.dismiss();
                        if ("1".equals(is_integral)) {
                            onPaymentCallBack.onZhiFuBao(payMode, is_integral, (money - score));
                        } else {
                            onPaymentCallBack.onZhiFuBao(payMode, is_integral, (money));
                        }
                    }
                }

            }
        });

        cb_balance_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payMode = "balance";
                    cb_wx_dialog_payment_bottom.setChecked(false);
                    cb_zfb_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_wx_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payMode = "we_chat_pay";
                    cb_balance_dialog_payment_bottom.setChecked(false);
                    cb_zfb_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_zfb_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payMode = "ali_pay";
                    cb_balance_dialog_payment_bottom.setChecked(false);
                    cb_wx_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_score_dialog_payment_bottom_score.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_integral = "1";
                    money_dialog_payment_bottom.setText(money - score + "");
                } else {
                    is_integral = "0";
                    money_dialog_payment_bottom.setText(money + "");
                }

                parent_dialog_payment_bottom_score.setVisibility(View.GONE);
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }


    /**
     * 弹出密码输入框
     *
     * @param title
     * @param activity
     * @param onPayPasswordVerificationCallBack
     */
    private void createPasswordInputDialog(String title, final Activity activity, final OnPayPasswordVerificationCallBack onPayPasswordVerificationCallBack) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_password_input, null);
        final PassWordKeyboard PassWordKeyboard = $.f(view, R.id.PassWordKeyboard);
        PassWordKeyboard
                .title(title)
                .titleColor(activity.getResources().getColor(R.color.mainColor))
                .setColor(activity.getResources().getColor(R.color.mainColor))
                .setLoadingDraw(activity, R.mipmap.logo_loading)
                .overridePendingTransition(activity)
                .customFunction("")
                .setOnPassWordKeyboardCallBack(new PassWordKeyboard.OnPassWordKeyboardCallBack() {
                    @Override
                    public void onPassword(String pasword) {

                        if (onPayPasswordVerificationCallBack != null) {
                            onPayPasswordVerificationCallBack.onVerificationPassword(pasword, PassWordKeyboard, bottomSheetDialog);
                        }
                    }

                    @Override
                    public void onFinish() {
                        bottomSheetDialog.dismiss();
                    }

                    @Override
                    public void onCustomFunction() {
                    }
                });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }


    public interface OnPaymentCallBack {
        void onMatch(String payMode, String is_integral, int price);

        void onZhiFuBao(String payMode, String is_integral, int price);

        void onWeiXin(String payMode, String is_integral, int price);

        void onMismatches();

        void onErrorMsg(String msg);
    }
}
