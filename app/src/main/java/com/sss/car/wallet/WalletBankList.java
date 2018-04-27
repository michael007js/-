package com.sss.car.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.BindCardModel;
import com.sss.car.EventBusModel.ChangedWalletModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.BankModel;
import com.sss.car.view.ActivityBangCardBind;
import com.sss.car.view.ActivityWeb;
import com.sss.car.view.WalletAddBank;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.R.id.card;
import static com.sss.car.R.id.holder;
import static com.sss.car.R.id.idcard;

/**
 * Created by leilei on 2018/1/28.
 */

public class WalletBankList extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    @BindView(R.id.wallet_bank_list)
    LinearLayout walletBankList;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    YWLoadingDialog ywLoadingDialog;
    List<BankModel> bankModelList = new ArrayList<>();
    Gson gson = new Gson();
    SSS_Adapter sss_adapter;
    int p = 1;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        gson = null;
        if (bankModelList != null) {
            bankModelList.clear();
        }
        bankModelList = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BindCardModel bindCardModel) {
        p = 1;
        bindBankCards();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_bank_list);
        ButterKnife.bind(this);
        titleTop.setText("选择银行卡");
        customInit(walletBankList, false, true, true);
        APPOftenUtils.underLineOfTextView(rightButtonTop).setTextColor(getResources().getColor(R.color.mainColor));
        rightButtonTop.setText("添加");
        listview.setEmptyView(LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.empty_view, null));
        initAdapter();
        bindBankCards();
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<BankModel>(getBaseActivityContext(), R.layout.item_bank_is_bind) {
            @Override
            protected void setView(final SSS_HolderHelper helper, int position, final BankModel bean, SSS_Adapter instance) {
                helper.setText(R.id.name, bean.bank_name);
                helper.setText(R.id.number, bean.card_num);
                helper.getView(R.id.click).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        set_default(bean);
                    }
                });
                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        del_card(bean.card_id);
                    }
                });

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {


            }
        };
        listview.setAdapter(sss_adapter);
    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                startActivity(new Intent(getBaseActivityContext(), ActivityBangCardBind.class)
                        .putExtra("isHidemobile",getIntent().getExtras().getBoolean("isHidemobile"))
                );
//                if (getBaseActivityContext() != null) {
//                    startActivity(new Intent(getBaseActivityContext(), ActivityWeb.class)
//                            .putExtra("type",ActivityWeb.BANK_BIND));
//                }


                //                        if (getBaseActivityContext() != null) {
//                            startActivity(new Intent(getBaseActivityContext(), WalletAddBank.class));
//                        }
                break;
        }
    }


    void bindBankCards() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.isBindBankCards(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (p == 1) {
                                        bankModelList.clear();
                                    }
                                    if (jsonArray.length() > 0) {
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            bankModelList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), BankModel.class));
                                        }
                                    }
                                    sss_adapter.setList(bankModelList);
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

    void set_default(final BankModel bankModel) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.set_default(
                    new JSONObject()
                            .put("card_id", bankModel.card_id)
                            .put("member_id", Config.member_id)
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
                                    EventBus.getDefault().post(bankModel);
                                    finish();
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


    void del_card(String cate_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.del_card(
                    new JSONObject()
                            .put("card_id", cate_id)
                            .put("member_id", Config.member_id)
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
                                    p = 1;
                                    bindBankCards();
                                    EventBus.getDefault().post(new BindCardModel());
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
}
