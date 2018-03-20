package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Tab.CustomTab.HorizontalPageAdapter;
import com.blankj.utilcode.customwidget.Tab.CustomTab.ViewHolder;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.ItemTabAdapter;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.custom.ItemTab;
import com.sss.car.custom.TAB;
import com.sss.car.dao.CustomRefreshLayoutCallBack;
import com.sss.car.dao.ItemTabAdapterOperationCallBack;
import com.sss.car.model.CateModel;
import com.sss.car.view.ActivityPublishPost;
import com.sss.car.view.ActivitySharePostHot;
import com.sss.car.view.ActivitySharePostMy;
import com.sss.car.view.ActivitySharePostOther;
import com.sss.car.view.ActivitySharePostsInterest;

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
 * 分享==社区fragment>
 * Created by leilei on 2017/9/4.
 */
@SuppressLint("ValidFragment")
public class FragmentShareCommunity extends BaseFragment implements LoadImageCallBack {

    @BindView(R.id.parent_fragment_share_community)
    FrameLayout parentFragmentShareCommunity;
    @BindView(R.id.top_fragment_share_community)
    ImageView topFragmentShareCommunity;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    List<CateModel> cateList = new ArrayList<>();
    public String classify_id;
    BannerVariation bannerVariation;
    /**
     * 帖子
     */
    public FragmentCommunity_Userinfo_Posts fragmentCommunity_userinfo_posts;

    public FragmentShareCommunity(String classify_id) {
        this.classify_id = classify_id;
    }

    TAB tab;
    TextView postedFragmentShareCommunityHead;
    View view;


    @Override
    protected int setContentView() {
        return R.layout.fragment_share_community;
    }

    @Override
    public void onDestroy() {
        view = null;
        postedFragmentShareCommunityHead = null;
        tab = null;
        parentFragmentShareCommunity = null;
        topFragmentShareCommunity = null;

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();

    }

    @Override
    protected void lazyLoad() {

      if (!isLoad){
          new Thread() {
              @Override
              public void run() {
                  super.run();
                  try {
                      sleep(200);
                      getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              init();
                          }
                      });
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          }.start();
      }

    }

    public FragmentShareCommunity() {
    }

    @Override
    protected void stopLoad() {

    }

    void init() {
        view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.fragment_share_communtiy_head, null);
        postedFragmentShareCommunityHead = $.f(view, R.id.posted_fragment_share_communtiy_head);
        tab = $.f(view, R.id.tab_fragment_share_communtiy_head);
        bannerVariation=$.f(view,R.id.viewpager);
        bannerVariation.setDelayTime(Config.flash);
        initAdv("10",classify_id,bannerVariation);
        //这里显示热门帖子
        fragmentCommunity_userinfo_posts = new FragmentCommunity_Userinfo_Posts(false,true, "0", "","",

        new CustomRefreshLayoutCallBack() {
            @Override
            public void onAdd(ListView listview) {
                listview.addHeaderView(view);
                listview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (firstVisibleItem + visibleItemCount > 5) {
                            topFragmentShareCommunity.setVisibility(View.GONE);
                        } else {
                            topFragmentShareCommunity.setVisibility(View.GONE);
                        }
                    }
                });


            }

        }
        ); FragmentUtils.addFragment(getChildFragmentManager(), fragmentCommunity_userinfo_posts, R.id.parent_fragment_share_community);

        postedFragmentShareCommunityHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityPublishPost.class));
                }
            }
        });


        postsCate();
    }


    void initAdv(String site_id, String classify_id , final BannerVariation bannerVariation){
        AdvertisementManager.advertisement(site_id,classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                bannerVariation
                        .setImages(list)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setDelayTime(Config.flash)
                        .setImageLoader(new ImageLoaderInterface() {
                            @Override
                            public void displayImage(Context context, Object path, View imageView) {
                                imageView.setTag(R.id.glide_tag,((AdvertisementModel) path).picture);
                                addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                            }

                            @Override
                            public View createImageView(Context context) {
                                return null;
                            }
                        });
                bannerVariation.start();
                bannerVariation.startAutoPlay();
            }
        });
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

    @OnClick({R.id.top_fragment_share_community})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.top_fragment_share_community:
                topFragmentShareCommunity.setVisibility(View.GONE);
                if (android.os.Build.VERSION.SDK_INT >= 8) {
                    fragmentCommunity_userinfo_posts.getCanContentView().smoothScrollToPosition(0);
                } else {
                    fragmentCommunity_userinfo_posts.getCanContentView().setSelection(0);
                }

                break;
        }
    }

    /**
     * 获取社区分类
     */
    public void postsCate() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.postsCate(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    cateList.add(new CateModel(Integer.toString(Integer.MAX_VALUE-1), "热门帖子", Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_green)));
                                    cateList.add(new CateModel(Integer.toString(Integer.MAX_VALUE-2), "我的帖子", Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_red)));
                                    cateList.add(new CateModel(Integer.toString(Integer.MAX_VALUE-3), "兴趣社区", Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_red)));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        CateModel cateModel = new CateModel();
                                        cateModel.cate_id = jsonArray.getJSONObject(i).getString("cate_id");
                                        cateModel.cate_name = jsonArray.getJSONObject(i).getString("cate_name");
                                        cateModel.logo = Uri.parse(Config.url + jsonArray.getJSONObject(i).getString("logo"));
                                        cateList.add(cateModel);
                                    }

                                    tab.setAdapter(new HorizontalPageAdapter(getBaseFragmentActivityContext(), cateList, R.layout.item_tab) {
                                        @Override
                                        public void bindViews(ViewHolder viewHolder, Object o, final int i) {
                                            ((TextView) viewHolder.getView(R.id.text_item_tab)).setText(((CateModel) o).cate_name);

                                           addImageViewList( FrescoUtils.showImage(false,120,120, cateList.get(i).logo,((SimpleDraweeView) viewHolder.getView(R.id.pic_item_tab)),0f));

                                            ((LinearLayout) viewHolder.getView(R.id.click_item_tab)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    switch (i) {

                                                        case 0://我的
                                                            if (getBaseFragmentActivityContext() != null) {
                                                                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostHot.class)
                                                                        .putExtra("cate_id",cateList.get(i).cate_id));
                                                            }
                                                            break;
                                                        case 1://热门
                                                            if (getBaseFragmentActivityContext() != null) {
                                                                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostMy.class));
                                                            }
                                                            break;
                                                        case 2://兴趣社区
                                                            if (getBaseFragmentActivityContext() != null) {
                                                                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostsInterest.class)
                                                                        .putExtra("cate_id",cateList.get(i).cate_id));
                                                            }
                                                            break;
                                                        default:
                                                            if (getBaseFragmentActivityContext() != null) {
                                                                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostOther.class)
                                                                        .putExtra("cate_id", cateList.get(i).cate_id)
                                                                        .putExtra("cate_name", cateList.get(i).cate_name));
                                                            }
                                                            break;
                                                    }
                                                }
                                            });
                                        }
                                    },cateList.size(),getActivity());
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Cate-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Cate-0");
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }


}
