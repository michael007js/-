package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.customwidget.Tab.CustomTab.HorizontalPageAdapter;
import com.blankj.utilcode.customwidget.Tab.CustomTab.ViewHolder;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.custom.ListViewVariation;
import com.sss.car.custom.TAB;
import com.sss.car.dao.CustomRefreshLayoutCallBack2;
import com.sss.car.model.CateModel;
import com.sss.car.model.ShareDynamicModel;
import com.sss.car.view.ActivityPublishDymaic;
import com.sss.car.view.ActivityShareCollect;
import com.sss.car.view.ActivityShareDymaicPublic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.sss.car.R.id.viewpager_up_fragment_goods_head;


/**
 * 分享==>动态fragment
 * Created by leilei on 2017/8/24.
 */

@SuppressLint("ValidFragment")
public class FragmentShareDynamic extends BaseFragment {
    Unbinder unbinder;
    public int p = 1;
    @BindView(R.id.top_fragment_share_dynamic)
    ImageView topFragmentShareDynamic;
    BannerVariation viewpagerHeadFragmentShareDymaic;
    TAB tab;
    TextView shareNowFragmentShareDynamic;
    LinearLayout publishHeadFragmentShareDymaic;
    View view;

    List<ShareDynamicModel> list = new ArrayList<>();
    List<CateModel> cateList = new ArrayList<>();
    public Fragment_Dynamic_Friend_Attention_community_Near fragment_dynamic_friend_attention_community_near;
    @BindView(R.id.parent_fragment_share_dynamic)
    FrameLayout parentFragmentShareDynamic;
    public String classify_id;
    List<AdvertisementModel> advList=new ArrayList<>();


    public FragmentShareDynamic(String classify_id) {
        this.classify_id = classify_id;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_share_dynamic;
    }

    public FragmentShareDynamic() {
    }

    @Override
    protected void lazyLoad() {
        if (isLoad&&advList.size()>0&&viewpagerHeadFragmentShareDymaic!=null){
            loadAdv(advList,viewpagerHeadFragmentShareDymaic);
        }
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



    @Override
    protected void stopLoad() {
        clearRequestCall();
        clearImageViewListCache();

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isLoad && viewpagerHeadFragmentShareDymaic != null) {
            if (isVisible) {
                if (viewpagerHeadFragmentShareDymaic != null) {
                    viewpagerHeadFragmentShareDymaic.startAutoPlay();
                }
            } else {
                if (viewpagerHeadFragmentShareDymaic != null) {
                    viewpagerHeadFragmentShareDymaic.stopAutoPlay();
                }
            }
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

    @OnClick(R.id.top_fragment_share_dynamic)
    public void onViewClicked() {
        topFragmentShareDynamic.setVisibility(View.GONE);
        fragment_dynamic_friend_attention_community_near.getListFragmentDynamicFriendAttentionCommunityNear().getRefreshableView().smoothScrollTo(0,0);
    }

    @SuppressWarnings("unchecked")
    void init() {
        view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.head_fragment_share_dymaic, null);
        tab = $.f(view, R.id.tab_head_fragment_share_dymaic);
        cateList.add(new CateModel("-1", "周边动态", Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_red)));
        cateList.add(new CateModel("-1", "好友动态", Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_blue)));
        cateList.add(new CateModel("-1", "关注动态", Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_green)));
//        cateList.add(new CateModel("-1", "社区动态", Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_yellow)));
        cateList.add(new CateModel("-1", "我的动态", Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_prink)));
        cateList.add(new CateModel("-1", "我的收藏", Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_red)));


        tab.setAdapter(new HorizontalPageAdapter(getBaseFragmentActivityContext(), cateList, R.layout.item_tab) {
            @Override
            public void bindViews(ViewHolder viewHolder, Object o, final int i) {
                ((TextView) viewHolder.getView(R.id.text_item_tab)).setText(((CateModel) o).cate_name);

                addImageViewList(FrescoUtils.showImage(false, 120, 120, cateList.get(i).logo, ((SimpleDraweeView) viewHolder.getView(R.id.pic_item_tab)), 0f));
                ((LinearLayout) viewHolder.getView(R.id.click_item_tab)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (i) {
                            case 0:
                                if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareDymaicPublic.class)
                                            .putExtra("type", "6"));
                                }

                                break;
                            case 1:
                                if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareDymaicPublic.class)
                                            .putExtra("type", "3"));
                                }

                                break;
                            case 2:
                                if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareDymaicPublic.class)
                                            .putExtra("type", "4"));
                                }

                                break;
                            case 3:
                                if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareDymaicPublic.class)
                                            .putExtra("type", "2"));
                                }
                                break;
                            case 4:
                                if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareCollect.class));
                                }

                                break;
                        }
                    }
                });
            }
        },cateList.size(),getActivity());
        viewpagerHeadFragmentShareDymaic = $.f(view, R.id.viewpager_head_fragment_share_dymaic);
        shareNowFragmentShareDynamic = $.f(view, R.id.share_now_fragment_share_dynamic);
        publishHeadFragmentShareDymaic = $.f(view, R.id.publish_head_fragment_share_dymaic);
        shareNowFragmentShareDynamic.setText("立即分享");
        viewpagerHeadFragmentShareDymaic.setDelayTime(Config.flash);
        shareNowFragmentShareDynamic.setTextColor(getResources().getColor(R.color.mainColor));
        shareNowFragmentShareDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityPublishDymaic.class));
            }
        });


        //（type == 1所有，2我的，3好友，4关注，5社区，6周边）
        fragment_dynamic_friend_attention_community_near = new Fragment_Dynamic_Friend_Attention_community_Near(false,"1",true, new CustomRefreshLayoutCallBack2() {
            @Override
            public void onAdd(ListViewVariation listViewVariation) {
                listViewVariation.addHeadView(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    listViewVariation.setScrollChangeListener(new View.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (scrollY > 500) {
                                topFragmentShareDynamic.setVisibility(View.VISIBLE);
                            } else {
                                topFragmentShareDynamic.setVisibility(View.GONE);
                            }
                        }
                    });
                }


            }

        });


        FragmentUtils.addFragment(getChildFragmentManager(),fragment_dynamic_friend_attention_community_near,R.id.parent_fragment_share_dynamic);
        initAdv("10",classify_id,viewpagerHeadFragmentShareDymaic);
    }

    void initAdv(String site_id, String classify_id , final BannerVariation bannerVariation){
        AdvertisementManager.advertisement(site_id,classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                advList=list;
                loadAdv(list,bannerVariation);

            }
        });
    }

    void loadAdv(List<AdvertisementModel> list,BannerVariation bannerVariation){
        bannerVariation
                .setImages(list)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setDelayTime(Config.flash)
                .setImageLoader(new ImageLoaderInterface() {
                    @Override
                    public void displayImage(Context context, Object path, View imageView) {
                        imageView.setTag(R.id.glide_tag,((AdvertisementModel) path).picture);
                        addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
                        AdvertisementManager.jump(((AdvertisementModel) path),getBaseFragmentActivityContext());

                    }

                    @Override
                    public View createImageView(Context context) {
                        return null;
                    }
                });
        bannerVariation.setOffscreenPageLimit(list.size());
        bannerVariation.start();
        bannerVariation.startAutoPlay();
    }


}
