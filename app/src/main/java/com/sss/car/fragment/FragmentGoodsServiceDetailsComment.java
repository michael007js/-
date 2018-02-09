package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.NineAdapter2;
import com.sss.car.dao.CustomRefreshLayoutCallBack3;
import com.sss.car.model.GoodsServiceCommentModel;
import com.sss.car.model.GoodsServiceCommentTotalModel;
import com.sss.car.view.ActivityImages;

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
 * 车品车服评论公用页面
 * Created by leilei on 2017/9/16.
 */

@SuppressLint("ValidFragment")
public class FragmentGoodsServiceDetailsComment extends BaseFragment  {
    @BindView(R.id.linearLayout_fragment_goods_service_details_comment)
    LinearLayout linearLayoutFragmentGoodsServiceDetailsComment;
    @BindView(R.id.parent_fragment_goods_service_details_comment)
    PullToRefreshScrollView parentFragmentGoodsServiceDetailsComment;
    Unbinder unbinder;
    @BindView(R.id.listview_fragment_goods_service_details_comment)
    InnerListview listviewFragmentGoodsServiceDetailsComment;

    View view;
    TextView all_fragment_goods_service_details_comment_head,
            high_fragment_goods_service_details_comment_head,
            middle_fragment_goods_service_details_comment_head,
            low_fragment_goods_service_details_comment_head;


    YWLoadingDialog ywLoadingDialog;

    SSS_Adapter sss_adapter;
    CustomRefreshLayoutCallBack3 customRefreshLayoutCallBack3;


    GoodsServiceCommentTotalModel goodsServiceCommentTotalModel = new GoodsServiceCommentTotalModel();

    int p = 1;
    String goods_id;
    String type = "1";//（type=1全部，type=2好评，type=3中评，type=4差评）
    boolean stop;

    public FragmentGoodsServiceDetailsComment() {
    }

    public FragmentGoodsServiceDetailsComment(boolean stop, String goods_id, CustomRefreshLayoutCallBack3 customRefreshLayoutCallBack3) {
        this.goods_id = goods_id;
        this.stop = stop;
        this.customRefreshLayoutCallBack3 = customRefreshLayoutCallBack3;
    }

    @Override
    public void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        view = null;
        all_fragment_goods_service_details_comment_head = null;
        high_fragment_goods_service_details_comment_head = null;
        middle_fragment_goods_service_details_comment_head = null;
        low_fragment_goods_service_details_comment_head = null;
        listviewFragmentGoodsServiceDetailsComment = null;
        linearLayoutFragmentGoodsServiceDetailsComment = null;
        parentFragmentGoodsServiceDetailsComment = null;
        customRefreshLayoutCallBack3 = null;
        goods_id = null;
        type = null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_goods_service_details_comment;
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
                                parentFragmentGoodsServiceDetailsComment.setMode(PullToRefreshBase.Mode.BOTH);
                                parentFragmentGoodsServiceDetailsComment.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                                        p = 1;
                                        goods_comment(false);
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                                        goods_comment(false);
                                    }
                                });

                                if (customRefreshLayoutCallBack3 != null) {
                                    customRefreshLayoutCallBack3.onAdd(parentFragmentGoodsServiceDetailsComment.getRefreshableView());
                                }
                                init();
                                goods_comment(false);
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

    void init() {
        sss_adapter = new SSS_Adapter<GoodsServiceCommentModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_goods_service_details_comment) {

            @Override
            protected void setView(SSS_HolderHelper helper, int position, final GoodsServiceCommentModel bean, SSS_Adapter instance) {
                ((com.azhong.ratingbar.RatingBar) helper.getView(R.id.star_item_fragment_goods_service_details_comment)).setClickable(false);
                ((com.azhong.ratingbar.RatingBar) helper.getView(R.id.star_item_fragment_goods_service_details_comment)).setStarCount(5);
                ((com.azhong.ratingbar.RatingBar) helper.getView(R.id.star_item_fragment_goods_service_details_comment)).setStar(bean.grade);
                helper.setText(R.id.name_item_fragment_goods_service_details_comment, bean.username);
                helper.setText(R.id.content_item_fragment_goods_service_details_comment, bean.contents);
                helper.setText(R.id.car_item_fragment_goods_service_details_comment, bean.vehicle_name);
                helper.setText(R.id.name_item_fragment_goods_service_details_comment, bean.username);
                helper.setText(R.id.shop_item_fragment_goods_service_details_comment, bean.create_time);

                ((HorizontalListView) helper.getView(R.id.photo_item_fragment_goods_service_details_comment)).setAdapter(new NineAdapter2(200, 200, bean.picture, getBaseFragmentActivityContext(), new LoadImageCallBack() {
                    @Override
                    public void onLoad(ImageView imageView) {
                        addImageViewList(imageView);
                    }
                }, null));
                if (!stop) {
                    ((HorizontalListView) helper.getView(R.id.photo_item_fragment_goods_service_details_comment)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (getBaseFragmentActivityContext() != null) {
                                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                        .putStringArrayListExtra("data", (ArrayList<String>) bean.picture)
                                        .putExtra("current", position));
                            }
                        }
                    });
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };


        listviewFragmentGoodsServiceDetailsComment.setAdapter(sss_adapter);


        addHead();
    }

    void addHead() {
        if (view == null) {
            view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.fragment_goods_service_details_comment_head, null);
            all_fragment_goods_service_details_comment_head = $.f(view, R.id.all_fragment_goods_service_details_comment_head);
            high_fragment_goods_service_details_comment_head = $.f(view, R.id.high_fragment_goods_service_details_comment_head);
            middle_fragment_goods_service_details_comment_head = $.f(view, R.id.middle_fragment_goods_service_details_comment_head);
            low_fragment_goods_service_details_comment_head = $.f(view, R.id.low_fragment_goods_service_details_comment_head);
            listviewFragmentGoodsServiceDetailsComment.addHeaderView(view);
            all_fragment_goods_service_details_comment_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!stop) {
                        clickEvent(1);
                        type = "1";
                        p = 1;
                        goods_comment(true);
                    }
                }
            });
            high_fragment_goods_service_details_comment_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!stop) {
                        clickEvent(2);
                        type = "2";
                        p = 1;
                        goods_comment(true);
                    }
                }
            });
            middle_fragment_goods_service_details_comment_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!stop) {
                        clickEvent(3);
                        type = "3";
                        p = 1;
                        goods_comment(true);
                    }
                }
            });
            low_fragment_goods_service_details_comment_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!stop) {
                        clickEvent(4);
                        type = "4";
                        p = 1;
                        goods_comment(true);
                    }
                }
            });
        }
    }

    void clickEvent(int what) {
        switch (what) {
            case 1:
                all_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.white));
                high_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                middle_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                low_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                APPOftenUtils.setBackgroundOfVersion(all_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra));
                APPOftenUtils.setBackgroundOfVersion(high_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                APPOftenUtils.setBackgroundOfVersion(middle_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                APPOftenUtils.setBackgroundOfVersion(low_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                break;
            case 2:
                all_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                high_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.white));
                middle_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                low_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                APPOftenUtils.setBackgroundOfVersion(all_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                APPOftenUtils.setBackgroundOfVersion(high_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra));
                APPOftenUtils.setBackgroundOfVersion(middle_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                APPOftenUtils.setBackgroundOfVersion(low_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                break;
            case 3:
                all_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                high_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                middle_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.white));
                low_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                APPOftenUtils.setBackgroundOfVersion(all_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                APPOftenUtils.setBackgroundOfVersion(high_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                APPOftenUtils.setBackgroundOfVersion(middle_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra));
                APPOftenUtils.setBackgroundOfVersion(low_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                break;
            case 4:
                all_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                high_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                middle_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                APPOftenUtils.setBackgroundOfVersion(all_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                APPOftenUtils.setBackgroundOfVersion(high_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                APPOftenUtils.setBackgroundOfVersion(middle_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra_gray));
                APPOftenUtils.setBackgroundOfVersion(low_fragment_goods_service_details_comment_head, getResources().getDrawable(R.drawable.bg_button_small_ra));
                low_fragment_goods_service_details_comment_head.setTextColor(getResources().getColor(R.color.white));
                break;
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
        unbinder = null;
    }


    /**
     * 获取评论
     */
    public void goods_comment(final boolean isClear) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_comment(
                    new JSONObject()
                            .put("p", p)
                            .put("goods_id", goods_id)
                            .put("type", type)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (parentFragmentGoodsServiceDetailsComment != null) {
                                parentFragmentGoodsServiceDetailsComment.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (parentFragmentGoodsServiceDetailsComment != null) {
                                parentFragmentGoodsServiceDetailsComment.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if (p == 1) {
                                        goodsServiceCommentTotalModel.list.clear();

                                    }
                                    p++;
                                    if (isClear) {
                                        APPOftenUtils.clearListView(listviewFragmentGoodsServiceDetailsComment, sss_adapter, goodsServiceCommentTotalModel.list);
                                    }
                                    goodsServiceCommentTotalModel.all = String.valueOf(jsonObject.getJSONObject("data").getInt("all"));
                                    goodsServiceCommentTotalModel.goods = String.valueOf(jsonObject.getJSONObject("data").getInt("goods"));
                                    goodsServiceCommentTotalModel.centre = String.valueOf(jsonObject.getJSONObject("data").getInt("centre"));
                                    goodsServiceCommentTotalModel.negative = String.valueOf(jsonObject.getJSONObject("data").getInt("negative"));
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            GoodsServiceCommentModel goodsServiceCommentModel = new GoodsServiceCommentModel();
                                            goodsServiceCommentModel.comment_id = jsonArray.getJSONObject(i).getString("comment_id");
                                            goodsServiceCommentModel.contents = jsonArray.getJSONObject(i).getString("contents");
                                            goodsServiceCommentModel.reply = jsonArray.getJSONObject(i).getString("reply");
                                            goodsServiceCommentModel.grade = jsonArray.getJSONObject(i).getInt("grade");
                                            goodsServiceCommentModel.comment_pid = jsonArray.getJSONObject(i).getString("comment_pid");
                                            goodsServiceCommentModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            goodsServiceCommentModel.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
                                            goodsServiceCommentModel.vehicle_name = jsonArray.getJSONObject(i).getString("vehicle_name");
                                            goodsServiceCommentModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            goodsServiceCommentModel.username = jsonArray.getJSONObject(i).getString("username");

                                            List<String> picture = new ArrayList<>();
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("picture");
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                picture.add(jsonArray1.getString(j));
                                                goodsServiceCommentModel.picture = picture;
                                            }
                                            goodsServiceCommentTotalModel.list.add(goodsServiceCommentModel);

                                        }
                                        sss_adapter.setList(goodsServiceCommentTotalModel.list);
                                        all_fragment_goods_service_details_comment_head.setText("    全部    ");
                                        high_fragment_goods_service_details_comment_head.setText("    好评(" + goodsServiceCommentTotalModel.goods + ")    ");
                                        middle_fragment_goods_service_details_comment_head.setText("    中评(" + goodsServiceCommentTotalModel.centre + ")    ");
                                        low_fragment_goods_service_details_comment_head.setText("    差评(" + goodsServiceCommentTotalModel.negative + ")    ");

                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:shop-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:shop-0");
            e.printStackTrace();
        }
    }
}
