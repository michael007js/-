package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.BezierBannerDot;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedPostsModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.NineAdapter2;
import com.sss.car.dao.CollectBottomDialogCallaback;
import com.sss.car.dao.NineAdapter2OperationCallBack;
import com.sss.car.dictionary.DectionaryDetails;
import com.sss.car.model.CollectModel;
import com.sss.car.model.ShareCollectModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityCollectLable;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityShareCollect;
import com.sss.car.view.ActivitySharePostDetails;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * Created by leilei on 2018/1/16.
 */
@SuppressWarnings("ALL")
@SuppressLint("ValidFragment")
public class FragmentCollect extends BaseFragment {
    @BindView(R.id.listview_activity_share_collect)
    public PullToRefreshListView listviewActivityShareCollect;
    @BindView(R.id.share_activity_share_collect)
    ImageView shareActivityShareCollect;
    @BindView(R.id.edit_activity_share_collect)
    ImageView editActivityShareCollect;
    @BindView(R.id.delete_activity_share_collect)
    ImageView deleteActivityShareCollect;
    @BindView(R.id.parent_bottom_activity_share_collect)
    public LinearLayout parentBottomActivityShareCollect;
    Unbinder unbinder;
    public SSS_Adapter sss_adapter;
    public int p = 1;
    public boolean isEdit = false;
    public List<CollectModel> selectList = new ArrayList<>();
    public List<ShareCollectModel> list = new ArrayList<>();
    YWLoadingDialog ywLoadingDialog;
    public MenuDialog menuDialog;
    boolean isSearchMode;

    String keywords;


    public FragmentCollect(boolean isSearchMode) {
        this.isSearchMode = isSearchMode;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_collect;
    }

    public FragmentCollect() {
    }

    @Override
    public void onDestroy() {
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        if (selectList != null) {
            selectList.clear();
        }
        selectList = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        super.onDestroy();
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(100);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listviewActivityShareCollect.setEmptyView(LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.empty_view, null));
                                listviewActivityShareCollect.setMode(PullToRefreshBase.Mode.BOTH);
                                listviewActivityShareCollect.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        if (isSearchMode) {
                                            if (!StringUtils.isEmpty(keywords)) {
                                                search_collect();
                                            } else {
                                                listviewActivityShareCollect.onRefreshComplete();
                                            }
                                        } else {
                                            p = 1;
                                            collect();
                                        }

                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        collect();
                                    }
                                });

                                initAdapter();
                                if (isSearchMode == false) {
                                    collect();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    @Override
    protected void stopLoad() {

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

    @OnClick({R.id.share_activity_share_collect, R.id.edit_activity_share_collect, R.id.delete_activity_share_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_activity_share_collect:
                break;
            case R.id.edit_activity_share_collect:
                if (selectList.size() == 0) {
                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "您没有选中任何收藏");
                    return;
                }
                if (getBaseFragmentActivityContext() != null) {
                    List<String> temp=new ArrayList<>();
                    for (int i = 0; i < selectList.size(); i++) {
                        temp.add(selectList.get(i).id+"|"+selectList.get(i).type);
                    }
                    getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityCollectLable.class)
                            .putStringArrayListExtra("data", (ArrayList<String>) temp));
                }
                break;
            case R.id.delete_activity_share_collect:
                postsCollectCancelCollect(null, null,true);
                break;
        }
    }

    public void search(String keywords) {
        this.keywords = keywords;
        p = 1;
        search_collect();
    }

    /**
     * 初始化适配器
     */
    void initAdapter() {
        sss_adapter = new SSS_Adapter<ShareCollectModel>(getBaseFragmentActivityContext(), R.layout.item_collect_adapter) {

            @Override
            protected void setView(SSS_HolderHelper helper, int position, ShareCollectModel bean, SSS_Adapter instance) {
                if (isEdit) {
                    helper.getView(R.id.cb_item_collect_adapter).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.cb_item_collect_adapter).setVisibility(View.GONE);
                }


                if (StringUtils.isEmpty(bean.label_name)) {
                    helper.getView(R.id.label_parent_item_collect_adapter).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.label_parent_item_collect_adapter).setVisibility(View.VISIBLE);
                }

                if (bean.isChoose) {
                    helper.setBackgroundRes(R.id.cb_item_collect_adapter, io.rong.imkit.R.drawable.rc_item_top_list_selector);
                    helper.setBackgroundRes(R.id.long_click_item_collect_adapter, io.rong.imkit.R.drawable.rc_item_top_list_selector);
                } else {
                    helper.setBackgroundRes(R.id.cb_item_collect_adapter, io.rong.imkit.R.drawable.rc_item_list_selector);
                    helper.setBackgroundRes(R.id.long_click_item_collect_adapter, io.rong.imkit.R.drawable.rc_item_list_selector);
                }

                helper.setChecked(R.id.cb_item_collect_adapter, bean.isChoose);
                addImageViewList(FrescoUtils.showImage(true, 30, 30, Uri.parse(Config.url + bean.face), (SimpleDraweeView) helper.getView(R.id.pic_item_collect_adapter), 0f));

                helper.setText(R.id.name_item_collect_adapter, bean.username);
                helper.setText(R.id.content_item_collect_adapter, bean.title);
                helper.setText(R.id.time_item_collect_adapter, bean.create_time);
                helper.setText(R.id.label_item_collect_adapter, bean.label_name);
                ((InnerGridView) helper.getView(R.id.nine_item_collect_adapter)).setAdapter(new NineAdapter2(300, 300, bean.picture, getBaseFragmentActivityContext(),
                        new LoadImageCallBack() {
                            @Override
                            public void onLoad(ImageView imageView) {
                                addImageViewList(imageView);
                            }
                        },
                        new NineAdapter2OperationCallBack() {
                            @Override
                            public void onClickImage(int position, String url, List<String> urlList) {
                                if (getBaseFragmentActivityContext() != null) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        List<String> temp = new ArrayList<>();
                                        for (int i = 0; i < urlList.size(); i++) {
                                            temp.add(Config.url + urlList.get(i));
                                        }
                                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                                .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                                .putExtra("current", position));
                                    }
                                }
                            }
                        }));
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                if (isSearchMode == false) {
                    helper.setItemChildLongClickListener(R.id.short_click_item_collect_adapter);
                }
                helper.setItemChildClickListener(R.id.short_click_item_collect_adapter);
            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper helper) {
                switch (view.getId()) {
                    case R.id.short_click_item_collect_adapter:
                        if (!isEdit) {
                            if ("community".equals(list.get(position).type)) {
                                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostDetails.class)
                                        .putExtra("community_id", list.get(position).collect_id)
                                        .putExtra("is_show_keyboard", false));
                            } else   if ("book".equals(list.get(position).type)) {
                                startActivity(new Intent(getBaseFragmentActivityContext(), DectionaryDetails.class)
                                        .putExtra("article_id", list.get(position).collect_id)
                                        .putExtra("title", list.get(position).title)
                                );
                            }
                        } else {
                            if (list.get(position).isChoose == true) {
                                list.get(position).isChoose = false;
                            } else {
                                list.get(position).isChoose = true;
                            }

                            if (list.get(position).isChoose) {
                                selectList.add(new CollectModel(list.get(position).collect_id,list.get(position).type));
                            } else {
                                for (int i = 0; i < selectList.size(); i++) {
                                    if (selectList.get(i).id.equals(list.get(position).collect_id)) {
                                        selectList.remove(i);
                                    }
                                }
                            }
                            sss_adapter.setList(list);
                        }
                        break;
                }
            }


            @Override
            public boolean onItemChildLongClick(View view, final int position, SSS_HolderHelper helper) {
                if (!isEdit) {
                    if (menuDialog == null) {
                        menuDialog = new MenuDialog(getActivity());
                    }
                    menuDialog.createCollectBottomDialog(getBaseFragmentActivityContext(), new CollectBottomDialogCallaback() {
                        @Override
                        public void onTransmit() {

                        }

                        @Override
                        public void onEdit() {
                            if (getBaseFragmentActivityContext() != null) {
                                List<String> list = new ArrayList<>();
                                list.add(FragmentCollect.this.list.get(position).collect_id+"|"+FragmentCollect.this.list.get(position).type);
                                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityCollectLable.class)
                                        .putStringArrayListExtra("data", (ArrayList<String>) list));
                            }
                        }

                        @Override
                        public void onDetete() {
                            postsCollectCancelCollect(list.get(position).collect_id, list.get(position).type,false);
                        }

                        @Override
                        public void onMore() {
                            if (isEdit == false) {
                                isEdit = true;
                                sss_adapter.setList(list);
                                parentBottomActivityShareCollect.setVisibility(View.VISIBLE);
                                if (listviewActivityShareCollect != null) {
                                    listviewActivityShareCollect.onRefreshComplete();
                                }
                            }
                        }
                    });
                }

                return true;
            }
        });
        listviewActivityShareCollect.setAdapter(sss_adapter);
    }

    /**
     * 我的收藏标签搜索
     */
    public void search_collect() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.search_collect(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("label_name", keywords)
                            .put("p", p)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (listviewActivityShareCollect != null) {
                                listviewActivityShareCollect.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (listviewActivityShareCollect != null) {
                                listviewActivityShareCollect.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (p == 1) {
                                        list.clear();
                                    }

                                    if (jsonArray.length() > 0) {
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            ShareCollectModel shareCollectModel = new ShareCollectModel();
                                            shareCollectModel.type = jsonArray.getJSONObject(i).getString("type");
                                            shareCollectModel.title = jsonArray.getJSONObject(i).getString("title");
                                            shareCollectModel.label_name = jsonArray.getJSONObject(i).getString("label_name");
                                            shareCollectModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            shareCollectModel.collect_id = jsonArray.getJSONObject(i).getString("collect_id");
                                            shareCollectModel.username = jsonArray.getJSONObject(i).getString("username");
                                            shareCollectModel.face = jsonArray.getJSONObject(i).getString("face");
                                            List<String> picture = new ArrayList<>();
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("picture");
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                picture.add(jsonArray1.getString(j));
                                            }
                                            shareCollectModel.picture = picture;
                                            list.add(shareCollectModel);
                                        }

                                    }
                                    sss_adapter.setList(list);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Collect-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Collect-0");
            e.printStackTrace();
        }
    }

    /**
     * 我的收藏
     */
    public void collect() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.collect(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (listviewActivityShareCollect != null) {
                                listviewActivityShareCollect.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (listviewActivityShareCollect != null) {
                                listviewActivityShareCollect.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (p == 1) {
                                        list.clear();
                                    }

                                    if (jsonArray.length() > 0) {
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            ShareCollectModel shareCollectModel = new ShareCollectModel();
                                            shareCollectModel.collect_id = jsonArray.getJSONObject(i).getString("collect_id");
                                            shareCollectModel.title = jsonArray.getJSONObject(i).getString("title");
                                            shareCollectModel.label_name = jsonArray.getJSONObject(i).getString("label_name");
                                            shareCollectModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            shareCollectModel.type = jsonArray.getJSONObject(i).getString("type");
                                            shareCollectModel.username = jsonArray.getJSONObject(i).getString("username");
                                            shareCollectModel.face = jsonArray.getJSONObject(i).getString("face");
                                            List<String> picture = new ArrayList<>();
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("picture");
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                picture.add(jsonArray1.getString(j));
                                            }
                                            shareCollectModel.picture = picture;
                                            list.add(shareCollectModel);
                                        }

                                    }
                                    sss_adapter.setList(list);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Collect-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Collect-0");
            e.printStackTrace();
        }
    }


    /**
     * 帖子收藏（取消收藏）
     *
     * @param collect_id
     * @param isDouble   是否多选模式
     */
    public void postsCollectCancelCollect(final String collect_id,final String t, final boolean isDouble) {
        final JSONArray jsonArray = new JSONArray();
        final JSONArray type = new JSONArray();
        if (isDouble) {
            if (selectList.size() == 0) {
                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "您没有选中任何收藏");
                return;
            }

            for (int i = 0; i < selectList.size(); i++) {
                jsonArray.put(selectList.get(i).id);
                type.put(selectList.get(i).type);
            }
        } else {
            jsonArray.put(collect_id);
            type.put(t);
        }


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.postsCollectCancelCollect(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", type)
                            .put("collect_id", jsonArray)//此处传要收藏的文章ID
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    selectList.clear();
                                    p = 1;
                                    EventBus.getDefault().post(new ChangedPostsModel());
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Collect-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Collect-0");
            e.printStackTrace();
        }
    }

}
