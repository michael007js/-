package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.MyRecycleview.ExStaggeredGridLayoutManager;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.SearchAddFriendFriendAdapter;
import com.sss.car.dao.SearchAddFriendAdapterCallBack;
import com.sss.car.model.SearchAddFriendModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 用户搜索页面
 * Created by leilei on 2017/8/30.
 */

public class ActivitySearchAddFriend extends BaseActivity implements SearchAddFriendAdapterCallBack {
    @BindView(R.id.back_activity_search_add_friend)
    LinearLayout back;
    @BindView(R.id.input_activity_search_add_friend)
    EditText input;
    @BindView(R.id.activity_search_add_friend)
    LinearLayout activitySearchAddFriend;
    List<SearchAddFriendModel> list = new ArrayList<>();
    SearchAddFriendFriendAdapter searchAddFriendFriendAdapter;
    @BindView(R.id.list_activity_search_add_friend)
    RecyclerView listActivitySearchHistoryMessage;
    @BindView(R.id.cancel)
    LinearLayout cancel;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {

        cancel=null;
        back = null;
        input = null;
        activitySearchAddFriend = null;
        if (list != null) {
            list.clear();
        }

        list = null;
        if (searchAddFriendFriendAdapter != null) {
            searchAddFriendFriendAdapter.clear();
        }

        searchAddFriendFriendAdapter = null;
        listActivitySearchHistoryMessage = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_friend);
        ButterKnife.bind(this);
        customInit(activitySearchAddFriend, false, true, false, false);


        searchAddFriendFriendAdapter = new SearchAddFriendFriendAdapter(getBaseActivityContext(), list, this);
        listActivitySearchHistoryMessage.setAdapter(searchAddFriendFriendAdapter);
        listActivitySearchHistoryMessage.setLayoutManager(new ExStaggeredGridLayoutManager(1, RecyclerView.VERTICAL));

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * item被选中
     *
     * @param position
     * @param searchAddFriendModel
     */
    @Override
    public void onSelectItem(int position, SearchAddFriendModel searchAddFriendModel) {
        startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                .putExtra("id", searchAddFriendModel.member_id)
        );
    }

    /**
     * 搜索用户
     */
    void searchUser() {

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.searchUser(new JSONObject()
                            .put("keywords", input.getText().toString().trim())
                            .toString(),
                    new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        if (jsonArray.length() > 0) {
                                            list.clear();
                                        }
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            list.add(new SearchAddFriendModel(
                                                    jsonArray.getJSONObject(i).getString("member_id"),
                                                    jsonArray.getJSONObject(i).getString("username"),
                                                    jsonArray.getJSONObject(i).getString("face"),
                                                    jsonArray.getJSONObject(i).getString("account"),
                                                    jsonArray.getJSONObject(i).getString("mobile")
                                            ));
                                        }

                                        searchAddFriendFriendAdapter.refresh(list);

                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: Search User-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Search User-0");
            e.printStackTrace();
        }
    }

    @OnClick({R.id.back_activity_search_add_friend,R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_activity_search_add_friend:
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

}