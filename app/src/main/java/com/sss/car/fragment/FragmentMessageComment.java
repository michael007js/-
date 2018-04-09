package com.sss.car.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
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
import com.sss.car.view.ActivityMessageCommentDymaicPostsPublic;

import java.util.List;


/**
 * 消息==>评价
 * Created by leilei on 2017/10/18.
 */

@SuppressWarnings("ALL")
public class FragmentMessageComment extends BaseFragment {
   public FragmentMessageCommentDymaicPostsPublic fragmentMessageCommentDymaicPostsPubli;

    @Override
    protected int setContentView() {
        return R.layout.fragment_message_comment;
    }

    String is_read;//	未读消息参数为1
    boolean isHideHead = false;
    public String classify_id;

    public FragmentMessageComment(String is_read, boolean isHideHead,String classify_id) {
        this.is_read = is_read;
        this.isHideHead = isHideHead;
        this.classify_id = classify_id;
    }

    public FragmentMessageComment() {
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
                                if (fragmentMessageCommentDymaicPostsPubli == null) {
                                    fragmentMessageCommentDymaicPostsPubli = new FragmentMessageCommentDymaicPostsPublic(is_read, "", new OnListViewCallBack() {
                                        @Override
                                        public void onListViewCallBack(InnerListview listView) {
                                        }

                                        @Override
                                        public void onListViewCallBack(ListView listView) {
                                            if (isHideHead) {
                                                initHead(listView);
                                            }  else {
                                                listView.setEmptyView(LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.empty_view,null));
                                            }

                                        }
                                    });
                                    FragmentUtils.addFragment(getChildFragmentManager(), fragmentMessageCommentDymaicPostsPubli, R.id.parent_fragment_message_comment);
                                }
                                FragmentUtils.hideAllShowFragment(fragmentMessageCommentDymaicPostsPubli);

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
    public void onDestroy() {
        if (fragmentMessageCommentDymaicPostsPubli != null) {
            fragmentMessageCommentDymaicPostsPubli.onDestroy();
        }
        fragmentMessageCommentDymaicPostsPubli = null;
        is_read = null;
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    void initHead(ListView innerListview) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message_comment_head, null);
        BannerVariation viewpager_fragment_message_comment = $.f(view, R.id.viewpager_fragment_message_comment);
        LinearLayout click_dymaic_item_tab = $.f(view, R.id.click_dymaic_item_tab);
        LinearLayout click_posts_item_tab = $.f(view, R.id.click_posts_item_tab);
        LinearLayout click_deal_item_tab = $.f(view, R.id.click_deal_item_tab);
        viewpager_fragment_message_comment.setDelayTime(Config.flash);
        click_dymaic_item_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageCommentDymaicPostsPublic.class)
                            .putExtra("type", "trends")
                            .putExtra("title", "动态评价"));// trends动态，community帖子，order交易信息
                }
            }
        });
        click_posts_item_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageCommentDymaicPostsPublic.class)
                            .putExtra("type", "community")
                            .putExtra("title", "帖子评价"));// trends动态，community帖子，order交易信息
                }
            }
        });
        click_deal_item_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageCommentDymaicPostsPublic.class)
                            .putExtra("type", "order")
                            .putExtra("title", "交易评价"));// trends动态，community帖子，order交易信息
                }
            }
        });
        innerListview.addHeaderView(view);
        initAdv("8",classify_id,viewpager_fragment_message_comment);
    }



    void initAdv(String site_id, String classify_id , final BannerVariation bannerVariation){
        AdvertisementManager.advertisement(site_id,classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                if (bannerVariation != null) {
                    bannerVariation
                            .setImages(list)
                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                            .setDelayTime(Config.flash)
                            .setImageLoader(new ImageLoaderInterface() {
                                @Override
                                public void displayImage(Context context, final Object path, View imageView) {
                                    imageView.setTag(R.id.glide_tag, ((AdvertisementModel) path).picture);
                                    addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AdvertisementManager.jump(((AdvertisementModel) path),getBaseFragmentActivityContext());
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

}
