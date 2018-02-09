package com.sss.car.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.DianRuiApplicationManageHelper;
import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.ActivityManagerUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.commodity.Commodity;
import com.sss.car.coupon.CouponMy;
import com.sss.car.dictionary.DictionaryMy;
import com.sss.car.model.MyFragmentUserInfoModel;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.view.ActivityCollect;
import com.sss.car.view.ActivityDraftMy;
import com.sss.car.view.ActivityHistroy;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityMyData;
import com.sss.car.view.ActivityMyDataSynthesizeSetting;
import com.sss.car.view.ActivityOrderMy;
import com.sss.car.view.ActivityQR;
import com.sss.car.view.ActivityShareInteractionManage;
import com.sss.car.view.ActivityShoppingCart;
import com.sss.car.view.ActivityUserInfo;
import com.sss.car.view.LoginAndRegister;
import com.sss.car.wallet.WalletMy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;

/**
 * Created by leilei on 2017/8/15.
 */

@SuppressWarnings("ALL")
public class FragmentMy extends BaseFragment {
    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.username_fragment_my)
    TextView usernameFragmentMy;
    @BindView(R.id.synthesis_setting)
    LinearLayout synthesisSetting;
    @BindView(R.id.my_data)
    TextView myData;
    @BindView(R.id.certification_type_fragment_my)
    TextView certificationTypeFragmentMy;
    @BindView(R.id.credits_fragment_my)
    TextView creditsFragmentMy;
    @BindView(R.id.qR_code_fragment_my)
    ImageView qRCodeFragmentMy;
    @BindView(R.id.friend_show_fragment_my)
    TextView friendShowFragmentMy;
    @BindView(R.id.friend_fragment_my)
    LinearLayout friendFragmentMy;
    @BindView(R.id.attention_show_fragment_my)
    TextView attentionShowFragmentMy;
    @BindView(R.id.attention_fragment_my)
    LinearLayout attentionFragmentMy;
    @BindView(R.id.fans_show_fragment_my)
    TextView fansShowFragmentMy;
    @BindView(R.id.fans_fragment_my)
    LinearLayout fansFragmentMy;
    @BindView(R.id.interest_show_fragment_my)
    TextView interestShowFragmentMy;
    @BindView(R.id.interest_fragment_my)
    LinearLayout interestFragmentMy;
    @BindView(R.id.trace_fragment_my)
    LinearLayout traceFragmentMy;
    @BindView(R.id.shopping_cart_fragment_my)
    LinearLayout shoppingCartFragmentMy;
    @BindView(R.id.my_fav_fragment_my)
    LinearLayout myFavFragmentMy;
    @BindView(R.id.my_order_fragment_my)
    LinearLayout myOrderFragmentMy;
    @BindView(R.id.my_commodity_fragment_my)
    LinearLayout myCommodityFragmentMy;
    @BindView(R.id.my_wallet_fragment_my)
    LinearLayout myWalletFragmentMy;
    @BindView(R.id.interactive_management_fragment_my)
    LinearLayout interactiveManagementFragmentMy;
    @BindView(R.id.my_book_fragment_my)
    LinearLayout myBookFragmentMy;
    @BindView(R.id.customer_service_fragment_my)
    LinearLayout customerServiceFragmentMy;
    @BindView(R.id.my)
    LinearLayout my;
    Unbinder unbinder;
    @BindView(R.id.pic_fragment_my)
    ImageView picFragmentMy;
    YWLoadingDialog ywLoadingDialog;
    MyFragmentUserInfoModel myFragmentUserInfoModel = new MyFragmentUserInfoModel();
    @BindView(R.id.shop_coupont_fragment_my)
    LinearLayout shopCoupontFragmentMy;
    @BindView(R.id.group_show_fragment_my)
    TextView groupShowFragmentMy;
    @BindView(R.id.group_fragment_my)
    LinearLayout groupFragmentMy;
    @BindView(R.id.user_id)
    TextView userId;
    @BindView(R.id.simpleDraweeView2)
    SimpleDraweeView simpleDraweeView2;
    @BindView(R.id.draft_cart_fragment_my)
    LinearLayout draftCartFragmentMy;
    @BindView(R.id.my_commodity_fragment_my_line)
    TextView myCommodityFragmentMyLine;
    @BindView(R.id.shop_coupont_fragment_my_line)
    TextView shopCoupontFragmentMyLine;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.update)
    LinearLayout update;
    @BindView(R.id.click_id)
    LinearLayout clickId;
    @BindView(R.id.kill)
    TextView kill;

    public FragmentMy() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_my;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(200);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                version.setText("V" + AppUtils.getAppVersionName(getBaseFragmentActivityContext()));
                                showUserBaseInfo();
                                getUserInfo(true);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }

    }

    /**
     * 显示基本用户信息
     */
    public void showUserBaseInfo() {
        usernameFragmentMy.setText(Config.nikename);
        picFragmentMy.setTag(R.id.glide_tag, Config.url + Config.face);
        addImageViewList(GlidUtils.downLoader(true, picFragmentMy, getBaseFragmentActivityContext()));
//        try {
//            addImageViewList(GlidUtils.glidLoad(false, qRCodeFragmentMy, getBaseFragmentActivityContext(),
//                    QRCodeUtils.createQRCode(Config.member_id, 20)));
//        } catch (WriterException e) {
//            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "二维码生成错误: user-0");
//            e.printStackTrace();
//        }
    }

    @Override
    protected void stopLoad() {
        clearRequestCall();
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.kill, R.id.click_id, R.id.username_fragment_my, R.id.update, R.id.pic_fragment_my, R.id.logout, R.id.draft_cart_fragment_my, R.id.shop_coupont_fragment_my, R.id.group_fragment_my, R.id.my_data, R.id.synthesis_setting, R.id.qR_code_fragment_my, R.id.friend_fragment_my, R.id.attention_fragment_my, R.id.fans_fragment_my, R.id.interest_fragment_my, R.id.trace_fragment_my, R.id.shopping_cart_fragment_my, R.id.my_fav_fragment_my, R.id.my_order_fragment_my, R.id.my_commodity_fragment_my, R.id.my_wallet_fragment_my, R.id.interactive_management_fragment_my, R.id.my_book_fragment_my, R.id.customer_service_fragment_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.kill:
                APPOftenUtils.createAskDialog(getBaseFragmentActivityContext(), "确认要退出?", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        if (getBaseFragmentActivityContext() != null) {
//                            APPOftenUtils.smallBackToDesktop(getBaseFragmentActivityContext());
                            RongYunUtils.logout();
                            RongYunUtils.disconnect(true);
                            APPOftenUtils.initRongYunPushService(getBaseFragmentActivityContext());
                            SPUtils spUtils=new SPUtils(getBaseFragmentActivityContext(),Config.defaultFileName, Context.MODE_PRIVATE);
                            spUtils.put("password","");
                            ActivityManagerUtils.getActivityManager().finishAllActivity();
                        }
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;
                    }
                });

                break;
            case R.id.username_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
                            .putExtra("id", myFragmentUserInfoModel.member_id));
                }
                break;
            case R.id.click_id:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
                            .putExtra("id", myFragmentUserInfoModel.member_id));
                }
                break;
            case R.id.update:
                DianRuiApplicationManageHelper.checkUpdate(getBaseFragmentActivityContext(), Config.app_id, true, true);
                break;
            case R.id.logout:
                APPOftenUtils.createAskDialog(getBaseFragmentActivityContext(), "确认要注销?", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;
                        RongYunUtils.logout();
                        RongYunUtils.disconnect(true);
                        APPOftenUtils.initRongYunPushService(getBaseFragmentActivityContext());
                        ActivityManagerUtils.getActivityManager().finishAllActivity();
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), LoginAndRegister.class)
                                    .putExtra("isShowBack", false)
                                    .putExtra("isClearUserInfo", true));
                        }
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;
                    }
                });


                break;
            case R.id.pic_fragment_my:
//                if (getBaseFragmentActivityContext() != null) {
//                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
//                            .putExtra("id", Config.member_id)
//                            .putExtra("nikename", Config.username));
//                }
                List<String> list = new ArrayList<>();
                list.add(Config.url + Config.face);
                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                        .putStringArrayListExtra("data", (ArrayList<String>) list)
                        .putExtra("current", 0));


                break;
            case R.id.my_data:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMyData.class));
                }
                break;
            case R.id.synthesis_setting:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMyDataSynthesizeSetting.class));
                }
                break;
            case R.id.qR_code_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityQR.class)
                            .putExtra("id", myFragmentUserInfoModel.account)
                            .putExtra("username", myFragmentUserInfoModel.username)
                            .putExtra("pic", myFragmentUserInfoModel.face)
                            .putExtra("qr", myFragmentUserInfoModel.qr));
                }

                break;
            case R.id.friend_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteractionManage.class)
                            .putExtra("mode", "1"));
                }
                break;
            case R.id.attention_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteractionManage.class)
                            .putExtra("mode", "2"));
                }
                break;
            case R.id.fans_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteractionManage.class)
                            .putExtra("mode", "3"));
                }
                break;

            case R.id.group_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteractionManage.class)
                            .putExtra("mode", "4"));
                }
                break;
            case R.id.interest_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteractionManage.class)
                            .putExtra("mode", "5"));
                }
                break;
            case R.id.trace_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityHistroy.class));
                }
                break;
            case R.id.draft_cart_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDraftMy.class));
                }
                break;
            case R.id.shopping_cart_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShoppingCart.class));
                }
                break;
            case R.id.my_fav_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityCollect.class));
                }
                break;
            case R.id.my_order_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderMy.class));
                }
                break;
            case R.id.my_commodity_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), Commodity.class));
                }
                break;
            case R.id.shop_coupont_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), CouponMy.class));
                }
                break;
            case R.id.my_wallet_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), WalletMy.class));
                }
                break;
            case R.id.interactive_management_fragment_my:


                break;
            case R.id.my_book_fragment_my:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), DictionaryMy.class));
                }

                break;
            case R.id.customer_service_fragment_my:
                randomService();


                break;
        }
    }

    public void getUserInfo(final boolean showDialog) {
        if (showDialog) {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ywLoadingDialog = null;
            if (getBaseFragmentActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
                ywLoadingDialog.show();
            }
        }
        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getUserInfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseFragmentActivityContext() != null && showDialog) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseFragmentActivityContext() != null && showDialog) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        if (myFragmentUserInfoModel != null) {
                                            Config.username= jsonObject.getJSONObject("data").getString("username");
                                            myFragmentUserInfoModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                            myFragmentUserInfoModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                            myFragmentUserInfoModel.face = jsonObject.getJSONObject("data").getString("face");
                                            myFragmentUserInfoModel.username = jsonObject.getJSONObject("data").getString("username");
                                            myFragmentUserInfoModel.account = jsonObject.getJSONObject("data").getString("account");
                                            myFragmentUserInfoModel.is_auth = jsonObject.getJSONObject("data").getString("is_auth");
                                            myFragmentUserInfoModel.auth_type = jsonObject.getJSONObject("data").getString("auth_type");
                                            myFragmentUserInfoModel.friend = jsonObject.getJSONObject("data").getString("friend");
                                            myFragmentUserInfoModel.attention = jsonObject.getJSONObject("data").getString("attention");
                                            myFragmentUserInfoModel.interest = jsonObject.getJSONObject("data").getString("interest");
                                            myFragmentUserInfoModel.fans = jsonObject.getJSONObject("data").getString("fans");
                                            myFragmentUserInfoModel.group = jsonObject.getJSONObject("data").getString("group");
                                            myFragmentUserInfoModel.qr = jsonObject.getJSONObject("data").getString("qr_code");
                                            if (creditsFragmentMy != null) {
                                                creditsFragmentMy.setText(jsonObject.getJSONObject("data").getString("credit"));
                                            }
                                            if (certificationTypeFragmentMy != null) {
                                                certificationTypeFragmentMy.setText(myFragmentUserInfoModel.auth_type + "®");
                                            }
                                            if (friendShowFragmentMy != null) {
                                                friendShowFragmentMy.setText(myFragmentUserInfoModel.friend);
                                            }
                                            if (attentionShowFragmentMy != null) {
                                                attentionShowFragmentMy.setText(myFragmentUserInfoModel.attention);
                                            }
                                            if (fansShowFragmentMy != null) {
                                                fansShowFragmentMy.setText(myFragmentUserInfoModel.fans);
                                            }
                                            if (interestShowFragmentMy != null) {
                                                interestShowFragmentMy.setText(myFragmentUserInfoModel.interest);
                                            }
                                            if (groupShowFragmentMy != null) {
                                                groupShowFragmentMy.setText(myFragmentUserInfoModel.group);
                                            }
                                            if (userId != null) {
                                                userId.setText(myFragmentUserInfoModel.account);
                                            }
                                            if (usernameFragmentMy != null) {
                                                usernameFragmentMy.setText(myFragmentUserInfoModel.username);
                                            }
                                            if (picFragmentMy != null) {
                                                picFragmentMy.setTag(R.id.glide_tag, Config.url + myFragmentUserInfoModel.face);
                                                addImageViewList(GlidUtils.downLoader(true, picFragmentMy, getBaseFragmentActivityContext()));
                                            }
                                            if (qRCodeFragmentMy != null) {
                                                qRCodeFragmentMy.setTag(R.id.glide_tag, Config.url + myFragmentUserInfoModel.qr);
                                                addImageViewList(GlidUtils.downLoader(false, qRCodeFragmentMy, getBaseFragmentActivityContext()));
                                            }
                                            if (!myFragmentUserInfoModel.shop_id.equals("0")) {
                                                if (myCommodityFragmentMyLine != null) {
                                                    myCommodityFragmentMyLine.setVisibility(View.VISIBLE);
                                                }
                                                if (myCommodityFragmentMy != null) {
                                                    myCommodityFragmentMy.setVisibility(View.VISIBLE);
                                                }
                                                if (shopCoupontFragmentMyLine != null) {
                                                    shopCoupontFragmentMyLine.setVisibility(View.VISIBLE);
                                                }
                                                if (shopCoupontFragmentMy != null) {
                                                    shopCoupontFragmentMy.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }

                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null && showDialog) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: user-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseFragmentActivityContext() != null && showDialog) {
                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: user-0");
            }
            e.printStackTrace();
        }
    }

    public void randomService() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.randomService(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        if ("0".equals(jsonObject.getJSONObject("data").getString("member_id"))) {
                                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "目前暂无客服在线");
                                        } else {
                                            RongYunUtils.startConversation(getBaseFragmentActivityContext(), Conversation.ConversationType.PRIVATE, jsonObject.getJSONObject("data").getString("member_id"), "6");//客服传6，商品详情客服传3，群组传5
                                        }
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: user-0");
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: user-0");
            e.printStackTrace();
        }
    }
}
