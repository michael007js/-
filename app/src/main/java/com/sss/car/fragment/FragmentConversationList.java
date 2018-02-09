package com.sss.car.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.rongyun.ConversationListEx;
import com.sss.car.rongyun.ConversationListFragmentEx;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by leilei on 2017/12/6.
 */

@SuppressWarnings("ALL")
public class FragmentConversationList extends Fragment {

    @BindView(R.id.rc_status_bar_image)
    ImageView rcStatusBarImage;
    @BindView(R.id.rc_status_bar_text)
    TextView rcStatusBarText;
    @BindView(R.id.rc_status_bar)
    LinearLayout rcStatusBar;
    @BindView(R.id.rc_list)
    InnerListview rcList;
    @BindView(R.id.rc_empty_tv)
    TextView rcEmptyTv;
    @BindView(R.id.rc_conversation_list_empty_layout)
    LinearLayout rcConversationListEmptyLayout;
    @BindView(R.id.rc_content)
    RelativeLayout rcContent;
    Unbinder unbinder;
    SSS_Adapter sss_adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversationlist, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public FragmentConversationList() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        getList();


    }

    void initAdapter(){

        rcList.setAdapter(sss_adapter);

    }
    /**
     * 初始化会话列表
     *
     * @return
     */
    private ConversationListFragmentEx initConversationList() {


        RongIM.getInstance().setMessageAttachedUserInfo(true);
        ConversationListFragmentEx listFragment = new ConversationListFragmentEx();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter("targetId", Config.member_id)
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false") //设置讨论会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false") //设置系统会话是否聚合显示
                .build();
        listFragment.setUri(uri);
        return listFragment;
    }




    public void getList(){
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations.size()>0){
                    rcConversationListEmptyLayout.setVisibility(View.GONE);
                    sss_adapter.setList(conversations);
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
