package com.sss.car.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.MyRecycleview.ExStaggeredGridLayoutManager;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.adapter.SearchHistoryMessageAdapter;
import com.sss.car.dao.SearchHistoryMessageOperationCallBack;
import com.sss.car.model.SearchHistoryMessageModel;
import com.sss.car.rongyun.RongYunUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * 消息搜索页面
 * Created by leilei on 2017/8/30.
 */

public class ActivitySearchHistoryMessage extends BaseActivity implements SearchHistoryMessageOperationCallBack {
    @BindView(R.id.back_activity_search_history_message)
    LinearLayout back;
    @BindView(R.id.input_activity_search_history_message)
    EditText input;
    @BindView(R.id.voice_search_activity_search_history_message)
    LinearLayout voiceSearch;
    @BindView(R.id.activity_search_history_message)
    LinearLayout activitySearchHistoryMessage;
    List<SearchHistoryMessageModel> list = new ArrayList<>();
    SearchHistoryMessageAdapter searchHistoryMessageAdapter;
    @BindView(R.id.list_activity_search_history_message)
    RecyclerView listActivitySearchHistoryMessage;
    List<JSONObject> temp=new ArrayList<>();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        back = null;
        input = null;
        voiceSearch = null;
        activitySearchHistoryMessage = null;
        if (list!=null){
            list.clear();
        }
        list=null;
        if (temp!=null){
            temp.clear();
        }
        temp=null;
        if (searchHistoryMessageAdapter != null) {
            searchHistoryMessageAdapter.clear();
        }
        searchHistoryMessageAdapter = null;
        listActivitySearchHistoryMessage = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history_message);
        ButterKnife.bind(this);
        customInit(activitySearchHistoryMessage, false, true, false);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误eror-1");
            finish();
        }

        if (StringUtils.isEmpty(getIntent().getExtras().getString("title"))) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误error-2");
            finish();
        }

        searchHistoryMessageAdapter = new SearchHistoryMessageAdapter(getBaseActivityContext(), list, this);
        listActivitySearchHistoryMessage.setAdapter(searchHistoryMessageAdapter);
        listActivitySearchHistoryMessage.setLayoutManager(new ExStaggeredGridLayoutManager(1, RecyclerView.VERTICAL));

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("group".equals(getIntent().getExtras().getString("conversationType"))) {
                    RongYunUtils.getHistoryMessages(Conversation.ConversationType.GROUP,
                            getIntent().getExtras().getString("group_id"), Integer.MAX_VALUE, Integer.MAX_VALUE, new RongIMClient.ResultCallback<List<Message>>() {
                                @Override
                                public void onSuccess(List<Message> messages) {
                                    if (StringUtils.isEmpty(input.getText().toString().trim())) {
                                        return;
                                    }
                                    list.clear();
                                    for (int i = 0; i < messages.size(); i++) {
                                        if (new String(messages.get(i).getContent().encode()).contains(input.getText().toString().trim())) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(new String(messages.get(i).getContent().encode()));
                                                temp.add(jsonObject);
                                                list.add(new SearchHistoryMessageModel(messages.get(i),jsonObject.getString("content")));
                                                LogUtils.e(messages.get(i).getSentTime());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }
                                    searchHistoryMessageAdapter.refresh(list);

                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "error:" + errorCode);
                                }
                            });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick({R.id.back_activity_search_history_message, R.id.voice_search_activity_search_history_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_activity_search_history_message:
                finish();
                break;
            case R.id.voice_search_activity_search_history_message:
                break;
        }
    }

    /**
     * 消息被点击
     *
     * @param position
     * @param list
     */
    @Override
    public void onClickHistroyMessage(int position, List<SearchHistoryMessageModel> list) {
        if ("group".equals(getIntent().getExtras().getString("conversationType"))) {
            RongYunUtils.startConversation(getBaseActivityContext(),
                    Conversation.ConversationType.GROUP,
                    getIntent().getExtras().getString("group_id"),
                    getIntent().getExtras().getString("title"),
                    list.get(position).message.getSentTime()
            );
        }
    }
}
