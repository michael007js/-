package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.customwidget.ViewPager.AnimationViewPager;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.FragmentUtils;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.dao.OnListViewCallBack;
import com.sss.car.view.ActivityMessageSOSOrderSureCompletePublic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 消息==>订单
 * Created by leilei on 2017/10/19.
 */

@SuppressLint("ValidFragment")
public class FragmentMessageOrder extends BaseFragment {
    @BindView(R.id.parent_fragment_message_order)
    FrameLayout parentFragmentMessageOrder;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    public FragmentMessageOrderPublic fragmentMessageOrderPublic;
    String is_read;//	未读消息参数为1
    boolean isHideHead = false;
    public String classify_id;
    @Override
    public void onDestroy() {
        parentFragmentMessageOrder = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        if (fragmentMessageOrderPublic != null) {
            fragmentMessageOrderPublic.onDestroy();
        }
        fragmentMessageOrderPublic = null;
        super.onDestroy();
    }

    public FragmentMessageOrder(String is_read, boolean isHideHead,String classify_id) {
        this.is_read = is_read;
        this.isHideHead = isHideHead;
        this.classify_id=classify_id;
    }

    public FragmentMessageOrder() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_message_order;
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
                                if (fragmentMessageOrderPublic == null) {
                                    fragmentMessageOrderPublic = new FragmentMessageOrderPublic(is_read, new OnListViewCallBack() {
                                        @Override
                                        public void onListViewCallBack(InnerListview innerListview) {

                                        }

                                        @Override
                                        public void onListViewCallBack(ListView listView) {
                                            if (isHideHead) {
                                                initHeadView(listView);
                                            } else {
                                                listView.setEmptyView(LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.empty_view,null));
                                            }
                                        }
                                    }, null/*    String type;//	1实物订单，2服务订单，3SOS订单*/);
                                    FragmentUtils.addFragment(getChildFragmentManager(), fragmentMessageOrderPublic, R.id.parent_fragment_message_order);
                                }
                                FragmentUtils.hideAllShowFragment(fragmentMessageOrderPublic);

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

    void initHeadView(ListView listView) {
        View view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.fragment_message_order_head, null);
        BannerVariation viewpager_fragment_message_order_head = $.f(view, R.id.viewpager_fragment_message_order_head);
        viewpager_fragment_message_order_head.setDelayTime(Config.flash);
        LinearLayout click_sos_item_tab = $.f(view, R.id.click_sos_item_tab);
        LinearLayout click_order_item_tab = $.f(view, R.id.click_order_item_tab);
        LinearLayout click_sure_item_tab = $.f(view, R.id.click_sure_item_tab);
        click_sos_item_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageSOSOrderSureCompletePublic.class)
                            .putExtra("status", "1")//1SOS订单，2收入订单，3支出订单
                            .putExtra("title", "SOS订单"));
                }
            }
        });
        click_order_item_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageSOSOrderSureCompletePublic.class)
                            .putExtra("status", "2")//1SOS订单，2收入订单，3支出订单
                            .putExtra("title", "收入订单"));
                }
            }
        });
        click_sure_item_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageSOSOrderSureCompletePublic.class)
                            .putExtra("status", "3")//1SOS订单，2收入订单，3支出订单
                            .putExtra("title", "支出订单"));
                }
            }
        });

        listView.addHeaderView(view);
        initAdv("8",classify_id,viewpager_fragment_message_order_head);
    }


    void initAdv(String site_id, String classify_id , final BannerVariation bannerVariation){
        AdvertisementManager.advertisement(site_id,classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                if (bannerVariation != null) {
                    bannerVariation
                            .setImages(list)
                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                            .setDelayTime(5000)
                            .setImageLoader(new ImageLoaderInterface() {
                                @Override
                                public void displayImage(Context context, Object path, View imageView) {
                                    imageView.setTag(R.id.glide_tag, ((AdvertisementModel) path).picture);
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


}
