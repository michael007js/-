package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.KeyboardInput;
import com.blankj.utilcode.customwidget.Layout.LayoutCanRefresh.CanRefreshLayout;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.HideOrShow;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.DymaicAdapter;
import com.sss.car.dao.DymaicOperationCallBack;
import com.sss.car.model.DymaicModel;
import com.sss.car.model.ShareDymaicCommentListModel;
import com.sss.car.view.ActivityDymaicDetails;
import com.sss.car.view.ActivityImages;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 用户信息==>动态fragment
 * Created by leilei on 2017/9/1.
 */

@SuppressLint("ValidFragment")
public class FragmentUserInfoDymaic extends BaseFragment implements LoadImageCallBack, DymaicOperationCallBack
        , KeyboardInput.KeyboardInputOperationCallBack {
    @BindView(R.id.can_content_view)
    PullToRefreshListView listFragmentUserInfoDymaic;
    @BindView(R.id.keyboard_fragment_user_info_dymaic)
    public KeyboardInput keyboard;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    DymaicAdapter dymaicAdapter;
    int p = 1;
    List<DymaicModel> list = new ArrayList<>();
    String targetId = "";

    public FragmentUserInfoDymaic(String targetId) {
        this.targetId = targetId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        targetId = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (dymaicAdapter != null) {
            dymaicAdapter.clear();
        }
        dymaicAdapter = null;
    }

    public FragmentUserInfoDymaic() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_user_info_dymaic;
    }

    @Override
    protected void lazyLoad() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(200);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dymaicAdapter = new DymaicAdapter(getActivity(), list, FragmentUserInfoDymaic.this, FragmentUserInfoDymaic.this);
                            if (listFragmentUserInfoDymaic!=null){
                                listFragmentUserInfoDymaic.setAdapter(dymaicAdapter);
                                listFragmentUserInfoDymaic.setMode(PullToRefreshBase.Mode.BOTH);
                                listFragmentUserInfoDymaic.setEmptyView(LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.empty_view,null));
                                listFragmentUserInfoDymaic.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        p = 1;
                                        dymaicInfo();
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        dymaicInfo();
                                    }
                                });

                                keyboard.setKeyboardInputOperationCallBack(FragmentUserInfoDymaic.this);
                                dymaicInfo();
                            }


                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void stopLoad() {

    }

    /**
     * 获取动态
     *
     * @throws JSONException
     */
    public void dymaicInfo() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getUserDymaic(
                    new JSONObject()
                            .put("p", String.valueOf(p))
                            .put("member_id", Config.member_id)
                            .put("friend_id", targetId).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (listFragmentUserInfoDymaic != null) {
                                listFragmentUserInfoDymaic.onRefreshComplete();
                            }
                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (listFragmentUserInfoDymaic != null) {
                                listFragmentUserInfoDymaic.onRefreshComplete();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        if (jsonArray.length() > 0) {
                                            if (p == 1) {
                                                list.clear();
                                            }
                                            p++;
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                DymaicModel dymaicModel = new DymaicModel();
                                                dymaicModel.trends_pid = jsonArray.getJSONObject(i).getString("trends_pid");
                                                dymaicModel.city_path = jsonArray.getJSONObject(i).getString("city_path");
                                                dymaicModel.contents = jsonArray.getJSONObject(i).getString("contents");
                                                dymaicModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                                dymaicModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                                dymaicModel.looks = jsonArray.getJSONObject(i).getString("looks");
                                                dymaicModel.likes = jsonArray.getJSONObject(i).getString("likes");
                                                dymaicModel.trends_id = jsonArray.getJSONObject(i).getString("trends_id");
                                                dymaicModel.face = jsonArray.getJSONObject(i).getString("face");
                                                dymaicModel.transmit = jsonArray.getJSONObject(i).getString("transmit");
                                                dymaicModel.username = jsonArray.getJSONObject(i).getString("username");
                                                dymaicModel.month = jsonArray.getJSONObject(i).getString("month");
                                                dymaicModel.is_praise = jsonArray.getJSONObject(i).getString("is_praise");
                                                dymaicModel.comment_count = jsonArray.getJSONObject(i).getString("comment_count");
                                                dymaicModel.day = jsonArray.getJSONObject(i).getString("day");
                                                dymaicModel.week = jsonArray.getJSONObject(i).getString("week");
                                                /*获取图片*/
                                                List<String> picture = new ArrayList<>();
                                                JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("picture");
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    picture.add(jsonArray1.getString(j));
                                                }
                                                dymaicModel.picture = picture;

                                                /*获取评论*/
                                                List<ShareDymaicCommentListModel> comment_list = new ArrayList<>();
                                                JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("comment_list");
                                                for (int j = 0; j < jsonArray2.length(); j++) {
                                                    ShareDymaicCommentListModel shareDymaicCommentListModel = new ShareDymaicCommentListModel();
                                                    shareDymaicCommentListModel.comment_id = jsonArray2.getJSONObject(j).getString("comment_id");
                                                    shareDymaicCommentListModel.contents = jsonArray2.getJSONObject(j).getString("contents");
                                                    shareDymaicCommentListModel.create_time = jsonArray2.getJSONObject(j).getString("create_time");
                                                    shareDymaicCommentListModel.trends_id = jsonArray2.getJSONObject(j).getString("trends_id");
                                                    shareDymaicCommentListModel.member_id = jsonArray2.getJSONObject(j).getString("member_id");
                                                    shareDymaicCommentListModel.face = jsonArray2.getJSONObject(j).getString("face");
                                                    shareDymaicCommentListModel.username = jsonArray2.getJSONObject(j).getString("username");
                                                }
                                                dymaicModel.comment_list = comment_list;


                                                /*获取点赞头像*/
                                                List<String> praise_list = new ArrayList<>();
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                JSONArray jsonArray3 = jsonObject1.getJSONArray("likes_list");
                                                for (int j = 0; j < jsonArray3.length(); j++) {
                                                    praise_list.add(jsonArray3.getString(j));

                                                }
                                                dymaicModel.likes_list = praise_list;


                                                list.add(dymaicModel);
                                            }
                                            dymaicAdapter.refresh(list);
                                        }
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }


    /**
     * 动态评论
     *
     * @param content
     * @param textView
     */
    public void comment(DymaicModel model, final int poistion, String content, final EditText textView) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.commentDymaic(
                    new JSONObject()
                            .put("contents", content)
                            .put("trends_id", model.trends_id)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }

                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }

                            if (StringUtils.isEmpty(response)) {
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        keyboard.visibility(View.GONE);
                                        textView.setText("");
                                        p=1;
                                       dymaicInfo();
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }

    /**
     * 动态点赞
     *
     * @param model
     * @param poistion
     * @throws JSONException
     */
    public void praise(DymaicModel model, final int poistion) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.praise(
                    new JSONObject()
                            .put("type", "trends")//trends:动态点赞
                            .put("likes_id", model.trends_id)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        list.get(poistion).is_praise = jsonObject.getJSONObject("data").getString("code");
                                        if ("1".equals(list.get(poistion).is_praise)) {
                                            list.get(poistion).likes = String.valueOf(Integer.valueOf(list.get(poistion).likes) + 1);
                                        } else {
                                            list.get(poistion).likes = String.valueOf(Integer.valueOf(list.get(poistion).likes) - 1);
                                        }
                                        dymaicAdapter.refresh(list, poistion, convertView, parent);
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }

    /**
     * 创建删除对话框
     *
     * @param poistion
     * @param list
     */
    public void createDeleteDialog(final int poistion, final DymaicModel dymaicModel, final List<DymaicModel> list) {

        String[] stringItems = {"确定"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getBaseFragmentActivityContext(), stringItems, null)
                .isTitleShow(true)
                .itemTextColor(Color.parseColor("#e83e41"))
                .setmCancelBgColor(Color.parseColor("#e83e41"))
                .cancelText(Color.WHITE);
        dialog.title("是否要删除该动态?");
        dialog.titleTextSize_SP(14.5f).show();
        stringItems = null;
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dialog.dismiss();
                        deleteDymaic(poistion, dymaicModel, list);
                        break;
                }
            }
        });
    }

    /**
     * 动态删除
     *
     * @param poistion
     * @throws JSONException
     */
    public void deleteDymaic(final int poistion, final DymaicModel dymaicModel, final List<DymaicModel> list) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deleteDymaic(
                    new JSONObject()
                            .put("trends_id", dymaicModel.trends_id)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        list.remove(poistion);
                                        dymaicAdapter.refresh(list);
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }


    /**
     * 动态转发
     *
     * @throws JSONException
     */
    public void transmitDymaic(final DymaicModel dymaicModel, final int poistion) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.transmitDymaic(
                    new JSONObject()
                            .put("trends_pid", dymaicModel.trends_id)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }

                            if (StringUtils.isEmpty(response)) {
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                        list.get(poistion).transmit = String.valueOf(Integer.valueOf(list.get(poistion).transmit) + 1);
                                        dymaicAdapter.refresh(list, poistion, convertView, parent);
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
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


    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }

    @Override
    public void click(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list) {
        if (getBaseFragmentActivityContext() != null) {
            getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDymaicDetails.class)
                    .putExtra("id", shareDymaicModel.trends_id));
        }
    }

    int position;
    View convertView;
    ViewGroup parent;
    DymaicModel dymaicModel;

    @Override
    public void comment(int poistion, DymaicModel dymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent) {
//        this.position = poistion;
//        this.convertView = convertView;
//        this.parent = parent;
//        this.dymaicModel = dymaicModel;
//        EventBus.getDefault().post(new HideOrShow(View.GONE));
//        keyboard.visibility(View.VISIBLE);
        if (getBaseFragmentActivityContext() != null) {
            getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDymaicDetails.class)
                    .putExtra("id", dymaicModel.trends_id));
        }
    }

    @Override
    public void praise(int poistion, DymaicModel dymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent) {
        this.position = poistion;
        this.convertView = convertView;
        this.parent = parent;
        this.dymaicModel = dymaicModel;
        praise(dymaicModel, poistion);

    }


    @Override
    public void delete(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list) {
        createDeleteDialog(poistion, shareDymaicModel, list);
    }

    @Override
    public void share(int poistion, DymaicModel dymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent) {
        this.position = poistion;
        this.convertView = convertView;
        this.parent = parent;
        this.dymaicModel = dymaicModel;
        transmitDymaic(dymaicModel, poistion);
    }


    @Override
    public void onClickImage(int position, String url, List<String> urlList) {
        if (getBaseFragmentActivityContext() != null) {
            List<String> temp=new ArrayList<>();
            for (int i = 0; i < urlList.size(); i++) {
                temp.add(Config.url+urlList.get(i));
            }
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                    .putExtra("current", position));
        }
    }

    @Override
    public void onSend(String content, TextView textView, EditText editText) {

        if (StringUtils.isEmpty(content)) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "您的输入为空");
        } else {
            EventBus.getDefault().post(new HideOrShow(View.VISIBLE));
            comment(dymaicModel, position, content, editText);

        }

    }
}
