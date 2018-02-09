package com.sss.car.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.model.ContactVo;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.HistroyModel;
import com.sss.car.model.PhoneBookModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.R.id.get;

/**
 * Created by leilei on 2017/12/26.
 */

public class ActivityPhoneBook extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_activity_phone_book)
    PullToRefreshListView listviewActivityPhoneBook;
    @BindView(R.id.activity_phone_book)
    LinearLayout activityPhoneBook;
    List<ContactVo> list = new ArrayList<>();
    Gson gson = new Gson();
    YWLoadingDialog ywLoadingDialog;
    List<PhoneBookModel> phoneBookModels = new ArrayList<>();
    SSS_Adapter sss_adapter;




    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        ButterKnife.bind(this);
        titleTop.setText("手机通讯录");
        customInit(activityPhoneBook, false, true, false);
        list = PhoneUtils.getPhoneContacts(getBaseActivityContext());
        initAdapter();
        JSONArray jsonAray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            jsonAray.put(list.get(i).getMoble());
        }
        address_book(jsonAray);
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }


    void initAdapter(){
        sss_adapter=new SSS_Adapter<PhoneBookModel>(getBaseActivityContext(),R.layout.item_phone_book) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, final PhoneBookModel bean, SSS_Adapter instance) {
                addImageViewList(FrescoUtils.showImage(false,40,40, Uri.parse(Config.url+bean.face),((SimpleDraweeView)helper.getView(R.id.pic)),9999f));
                helper.setText(R.id.name,bean.username);
                helper.getView(R.id.add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            friendOperation(new JSONObject().put("type", "1")//1添加操作  2删除操作
                                    .put("friend_id", bean.member_id)
                                    .put("status", "1"), "添加",position);//1关注  2特别关心   3黑名单
                        } catch (JSONException e) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:friend operation-3");
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        listviewActivityPhoneBook.setAdapter(sss_adapter);
    }

    void address_book(JSONArray jsonAray) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.address_book(
                    new JSONObject()
                            .put("mobile", jsonAray)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            listviewActivityPhoneBook.onRefreshComplete();
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
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
                            listviewActivityPhoneBook.onRefreshComplete();
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                    }
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {

                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                phoneBookModels.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PhoneBookModel.class));
                                            }
                                        }
                                        sss_adapter.setList(phoneBookModels);
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }


    /**
     * 好友操作
     *
     * @param send
     * @throws JSONException
     */
    void friendOperation(final JSONObject send, String meaning, final int position){

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.friendOperation(
                    send
                            .put("member_id", Config.member_id)
                            .toString(), meaning, new StringCallback() {
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
                            if (!StringUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:friend operation-0");
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:friend operation-1");
            e.printStackTrace();
        }
    }
}
