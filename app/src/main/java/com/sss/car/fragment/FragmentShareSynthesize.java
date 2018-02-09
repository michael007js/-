package com.sss.car.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.NineGridLayout;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.NineView;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.RatioImageView;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.model.ShareSynthesizeModel;
import com.sss.car.view.ActivityDymaicDetails;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivitySharePostDetails;

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
 * Created by leilei on 2017/12/11.
 */

@SuppressWarnings("ALL")
public class FragmentShareSynthesize extends BaseFragment {
    @BindView(R.id.listview)
    PullToRefreshListView listView;
    Unbinder unbinder;
    BannerVariation viewpagerUpFragmentGoodsHead;
    SSS_Adapter sss_adapter;
    YWLoadingDialog ywLoadingDialog;
    List<ShareSynthesizeModel> list=new ArrayList<>();
    int p=1;
    public String classify_id;

    public FragmentShareSynthesize(String classify_id) {
        this.classify_id = classify_id;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_share_synthesize;
    }

    public FragmentShareSynthesize() {
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad){
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(100);
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               init();
                               shareSynthesize();
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
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isLoad && viewpagerUpFragmentGoodsHead != null) {
            if (isVisible) {
                if (viewpagerUpFragmentGoodsHead != null) {
                    viewpagerUpFragmentGoodsHead.startAutoPlay();
                }
                if (viewpagerUpFragmentGoodsHead != null) {
                    viewpagerUpFragmentGoodsHead.startAutoPlay();
                }
            } else {
                if (viewpagerUpFragmentGoodsHead != null) {
                    viewpagerUpFragmentGoodsHead.stopAutoPlay();
                }
                if (viewpagerUpFragmentGoodsHead != null) {
                    viewpagerUpFragmentGoodsHead.stopAutoPlay();
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
    void initAdv(String site_id, String classify_id , final BannerVariation bannerVariation){
        AdvertisementManager.advertisement(site_id,classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                bannerVariation
                        .setImages(list)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setDelayTime(5000)
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
    private void init() {
        View view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.fragment_share_synthesize_head, null);
        viewpagerUpFragmentGoodsHead = $.f(view, R.id.viewpager_fragment_share_synthesize_head);
//
//        List<String> list1 = new ArrayList<>();
//        list1.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
//        list1.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
//        list1.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
//        viewpagerUpFragmentGoodsHead
//                .setImages(list1)
//                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
//                .setDelayTime(3000)
//                .setImageLoader(new ImageLoaderInterface() {
//                    @Override
//                    public void displayImage(Context context, Object path, View imageView) {
//                        imageView.setTag(R.id.glide_tag, ((String) path));
//                        addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
//                        imageView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public View createImageView(Context context) {
//                        return null;
//                    }
//                });
//        viewpagerUpFragmentGoodsHead.start();
//        viewpagerUpFragmentGoodsHead.startAutoPlay();
        initAdv("10",classify_id,viewpagerUpFragmentGoodsHead);
        sss_adapter=new SSS_Adapter<ShareSynthesizeModel>(getBaseFragmentActivityContext(),R.layout.item_message_share_synthesize) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final ShareSynthesizeModel bean, SSS_Adapter instance) {
                helper.setText(R.id.name,bean.username);
                helper.setText(R.id.date,bean.create_time);
                helper.setText(R.id.content,bean.contents);
                addImageViewList(FrescoUtils.showImage(false,40,40, Uri.parse(bean.face),((SimpleDraweeView)helper.getView(R.id.pic)),99999));
                helper.getView(R.id.click_item_message_share_synthesize).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("trends".equals(bean.type)){
                            if (getBaseFragmentActivityContext() != null) {
                                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDymaicDetails.class)
                                        .putExtra("id", bean.ids));
                            }
                        }else {
                            if (getBaseFragmentActivityContext() != null) {
                                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostDetails.class)
                                        .putExtra("community_id",bean.ids));
                            }
                        }
                    }
                });
                ((NineView)helper.getView(R.id.nine)).maxCount(9)
                        .isShowAll(false)
                        .spacing(2)
                        .maxCount(9)
                        .isShowCloseButton(false)
                        .setNineViewShowCallBack(new NineView.NineViewShowCallBack() {
                            @Override
                            public void onDisplayOneImage(RatioImageView imageView, String url, int parentWidth, Context context) {
                                imageView.setTag(R.id.glide_tag,url);
                                addImageViewList(GlidUtils.downLoader(false,imageView,getBaseFragmentActivityContext()));
//                                addImageViewList(FrescoUtils.showImage(false,100,100,Uri.parse(url),imageView,0f));
                            }

                            @Override
                            public void onDisplayImage(RatioImageView imageView,RatioImageView closeButton, String url,int parentWidth,Context context) {
                                imageView.setTag(R.id.glide_tag,url);
                                addImageViewList(GlidUtils.downLoader(false,imageView,getBaseFragmentActivityContext()));
                            }

                            @Override
                            public void onClickImage(int position, String url, List<String> urlList, Context context) {
                                if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                            .putStringArrayListExtra("data", (ArrayList<String>) urlList)
                                            .putExtra("current", position));
                                }
                            }

                            @Override
                            public void onClickImageColse(int position, String url, List<String> urlList, Context context) {

                            }

                            @Override
                            public void onSamePhotos(List<String> mRejectUrlList) {

                            }
                        });
                ((NineView)helper.getView(R.id.nine)).urlList( bean.picture);


            }


            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

        listView.setAdapter(sss_adapter);
        listView.getRefreshableView().addHeaderView(view);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                p=1;
                shareSynthesize();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                shareSynthesize();
            }
        });

    }

    public void shareSynthesize() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.shareSynthesize(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p",p)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listView!=null){
                                listView.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listView!=null){
                                listView.onRefreshComplete();
                            }

                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                                    if (p==1){
                                        list.clear();
                                    }

                                    p++;

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ShareSynthesizeModel shareSynthesizeModel=new ShareSynthesizeModel();
                                        shareSynthesizeModel.type=jsonArray.getJSONObject(i).getString("type");
                                        shareSynthesizeModel. ids=jsonArray.getJSONObject(i).getString("ids");
                                        shareSynthesizeModel. contents=jsonArray.getJSONObject(i).getString("contents");
                                        shareSynthesizeModel. create_time=jsonArray.getJSONObject(i).getString("create_time");
                                        shareSynthesizeModel. member_id=jsonArray.getJSONObject(i).getString("member_id");
                                        shareSynthesizeModel.username=jsonArray.getJSONObject(i).getString("username");
                                        shareSynthesizeModel. face=Config.url+jsonArray.getJSONObject(i).getString("face");
                                        List<String> picture=new ArrayList<>();
                                        JSONArray jsonArray1=jsonArray.getJSONObject(i).getJSONArray("picture");
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            picture.add(Config.url+jsonArray1.getString(j));
                                        }
                                        shareSynthesizeModel.picture=picture;
                                        list.add(shareSynthesizeModel);
                                    }
                                    sss_adapter.setList(list);

                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-2");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }
}
