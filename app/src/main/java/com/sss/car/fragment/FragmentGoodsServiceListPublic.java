package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.model.GoodsModel;
import com.sss.car.view.ActivityGoodsServiceDetails;

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

import static com.sss.car.R.id.viewpager_up_fragment_goods_head;

/**
 * Created by leilei on 2018/1/6.
 */
@SuppressWarnings("ALL")
@SuppressLint("ValidFragment")
public class FragmentGoodsServiceListPublic extends BaseFragment {


    TextView textOne;
    LinearLayout clickOne;
    TextView textTwo;
    SimpleDraweeView imageTwo;
    LinearLayout clickTwo;
    TextView textThree;
    SimpleDraweeView imageThree;
    LinearLayout clickThree;
    TextView textFour;
    SimpleDraweeView imageFour;
    LinearLayout clickFour;
    TextView textFive;
    SimpleDraweeView imageFive;
    LinearLayout clickFive;
    TextView textSix;
    SimpleDraweeView imageSix;
    LinearLayout clickSix;


    @BindView(R.id.listview)
    PullToRefreshListView listview;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    int p = 1;
    String classify_id;
    String type;
    String shop_id;/*如果传shop_id，返回的数据则是该店铺下的商品*/
    String looks;/*人气：looks asc（升序）desc（降序）*/
    String price;/*价格：price asc（升序）desc（降序）*/
    String collect;/*收藏：collect asc（升序）desc（降序）*/
    String distance;/*距离：distance asc（升序）desc（降序）*/
    String credit;/*信誉：credit asc（升序）desc（降序）*/

    List<GoodsModel> goodsModelList = new ArrayList<>();
    SSS_Adapter sss_adapter;
    int[] hides;
    boolean showHead;
    BannerVariation bannerVariation;

    public FragmentGoodsServiceListPublic() {
    }


    public FragmentGoodsServiceListPublic(String type, boolean showHead, String classify_id, String shop_id, int[] hides) {
        this.type = type;
        this.showHead = showHead;
        this.classify_id = classify_id;
        this.shop_id = shop_id;
        this.hides = hides;
    }

    @Override
    public void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        super.onDestroy();

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_goods_service_list_public;
    }


    public void hideButton(int... hides) {
        for (int i = 0; i < hides.length; i++) {
            if (hides[i] == 0) {
                clickOne.setVisibility(View.GONE);/*综合*/
            } else if (hides[i] == 1) {
                clickTwo.setVisibility(View.GONE);/*人气*/
            } else if (hides[i] == 2) {
                clickThree.setVisibility(View.GONE);/*价格*/
            } else if (hides[i] == 3) {
                clickFour.setVisibility(View.GONE);/*收藏*/
            } else if (hides[i] == 4) {
                clickFive.setVisibility(View.GONE);/*距离*/
            } else if (hides[i] == 5) {
                clickSix.setVisibility(View.GONE);/*信誉*/
            }
        }
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
                                initHeadView();
                                initAdapter();
                                goods_list();
                                listview.setMode(PullToRefreshBase.Mode.BOTH);
                                hideButton(hides);
                                listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        p = 1;
                                        goods_list();
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        goods_list();
                                    }
                                });
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

    void initHeadView() {
        View view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.fragment_goods_service_list_public_head, null);
        bannerVariation = $.f(view, R.id.banner);
        if (showHead) {
            String site_id = null;
            if ("1".equals(type)) {
                site_id = "11";
            } else {
                site_id = "12";
            }
            AdvertisementManager.advertisement(site_id, classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
                @Override
                public void onSuccessCallBack(List<AdvertisementModel> list) {
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


            });
        } else {
            bannerVariation.setVisibility(View.GONE);
        }
        textOne = $.f(view, R.id.text_one);
        clickOne = $.f(view, R.id.click_one);
        textTwo = $.f(view, R.id.text_two);
        imageTwo = $.f(view, R.id.image_two);
        clickTwo = $.f(view, R.id.click_two);
        textThree = $.f(view, R.id.text_three);
        imageThree = $.f(view, R.id.image_three);
        clickThree = $.f(view, R.id.click_three);
        textFour = $.f(view, R.id.text_four);
        imageFour = $.f(view, R.id.image_four);
        clickFour = $.f(view, R.id.click_four);
        textFive = $.f(view, R.id.text_five);
        imageFive = $.f(view, R.id.image_five);
        clickFive = $.f(view, R.id.click_five);
        textSix = $.f(view, R.id.text_six);
        imageSix = $.f(view, R.id.image_six);
        clickSix = $.f(view, R.id.click_six);
        clickOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(0);
            }
        });
        clickTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(1);
            }
        });
        clickThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(2);
            }
        });
        clickFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(3);
            }
        });
        clickFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(4);
            }
        });
        clickSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(5);
            }
        });
        listview.getRefreshableView().addHeaderView(view);

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

    void click(int what) {
        p = 1;
        switch (what) {
            case 0:
                textOne.setTextColor(getResources().getColor(R.color.mainColor));
                textTwo.setTextColor(getResources().getColor(R.color.black));
                textThree.setTextColor(getResources().getColor(R.color.black));
                textFour.setTextColor(getResources().getColor(R.color.black));
                textFive.setTextColor(getResources().getColor(R.color.black));
                textSix.setTextColor(getResources().getColor(R.color.black));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageTwo, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageThree, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFour, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFive, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageSix, 0f));
                looks = null;
                price = null;
                collect = null;
                distance = null;
                credit = null;
                break;
            case 1:
                textOne.setTextColor(getResources().getColor(R.color.black));
                textTwo.setTextColor(getResources().getColor(R.color.mainColor));
                textThree.setTextColor(getResources().getColor(R.color.black));
                textFour.setTextColor(getResources().getColor(R.color.black));
                textFive.setTextColor(getResources().getColor(R.color.black));
                textSix.setTextColor(getResources().getColor(R.color.black));
                if (StringUtils.isEmpty(looks)) {
                    addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageTwo, 0f));
                    looks = "asc";
                } else {
                    if ("asc".equals(looks)) {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_yes), imageTwo, 0f));
                        looks = "desc";
                    } else {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageTwo, 0f));
                        looks = "asc";
                    }
                }
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageThree, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFour, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFive, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageSix, 0f));


                price = null;
                collect = null;
                distance = null;
                credit = null;
                break;
            case 2:
                textOne.setTextColor(getResources().getColor(R.color.black));
                textTwo.setTextColor(getResources().getColor(R.color.black));
                textThree.setTextColor(getResources().getColor(R.color.mainColor));
                textFour.setTextColor(getResources().getColor(R.color.black));
                textFive.setTextColor(getResources().getColor(R.color.black));
                textSix.setTextColor(getResources().getColor(R.color.black));

                if (StringUtils.isEmpty(price)) {
                    addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageThree, 0f));
                    price = "asc";
                } else {
                    if ("asc".equals(price)) {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_yes), imageThree, 0f));
                        price = "desc";
                    } else {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageThree, 0f));
                        price = "asc";
                    }
                }
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageTwo, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFour, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFive, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageSix, 0f));


                looks = null;
                collect = null;
                distance = null;
                credit = null;
                break;
            case 3:
                textOne.setTextColor(getResources().getColor(R.color.black));
                textTwo.setTextColor(getResources().getColor(R.color.black));
                textThree.setTextColor(getResources().getColor(R.color.black));
                textFour.setTextColor(getResources().getColor(R.color.mainColor));
                textFive.setTextColor(getResources().getColor(R.color.black));
                textSix.setTextColor(getResources().getColor(R.color.black));

                if (StringUtils.isEmpty(collect)) {
                    addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageFour, 0f));
                    collect = "asc";
                } else {
                    if ("asc".equals(collect)) {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_yes), imageFour, 0f));
                        collect = "desc";
                    } else {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageFour, 0f));
                        collect = "asc";
                    }
                }
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageTwo, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageThree, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFive, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageSix, 0f));


                looks = null;
                price = null;
                distance = null;
                credit = null;
                break;
            case 4:
                textOne.setTextColor(getResources().getColor(R.color.black));
                textTwo.setTextColor(getResources().getColor(R.color.black));
                textThree.setTextColor(getResources().getColor(R.color.black));
                textFour.setTextColor(getResources().getColor(R.color.black));
                textFive.setTextColor(getResources().getColor(R.color.mainColor));
                textSix.setTextColor(getResources().getColor(R.color.black));

                if (StringUtils.isEmpty(distance)) {
                    addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageFive, 0f));
                    distance = "asc";
                } else {
                    if ("asc".equals(distance)) {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_yes), imageFive, 0f));
                        distance = "desc";
                    } else {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageFive, 0f));
                        distance = "asc";
                    }
                }
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageTwo, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageThree, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFour, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageSix, 0f));


                looks = null;
                price = null;
                collect = null;
                credit = null;
                break;
            case 5:
                textOne.setTextColor(getResources().getColor(R.color.black));
                textTwo.setTextColor(getResources().getColor(R.color.black));
                textThree.setTextColor(getResources().getColor(R.color.black));
                textFour.setTextColor(getResources().getColor(R.color.black));
                textFive.setTextColor(getResources().getColor(R.color.black));
                textSix.setTextColor(getResources().getColor(R.color.mainColor));

                if (StringUtils.isEmpty(credit)) {
                    addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageSix, 0f));
                    credit = "asc";
                } else {
                    if ("asc".equals(credit)) {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_yes), imageSix, 0f));
                        credit = "desc";
                    } else {
                        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_up_yes), imageSix, 0f));
                        credit = "asc";
                    }
                }
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageTwo, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageThree, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFour, 0f));
                addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.arrows_down_no), imageFive, 0f));


                looks = null;
                price = null;
                collect = null;
                distance = null;
                break;
        }
        goods_list();

    }


    void initAdapter() {
        sss_adapter = new SSS_Adapter<GoodsModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_order_goods_service_list_adapter, goodsModelList) {

            @Override
            protected void setView(SSS_HolderHelper helper, int position, GoodsModel bean, SSS_Adapter instance) {
                helper.setItemChildClickListener(R.id.click_item_goods_service_list_adapter);
                addImageViewList(FrescoUtils.showImage(false, 180, 180, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_goods_service_list_adapter)), 0f));
                helper.setText(R.id.title_item_goods_service_list_adapter, bean.title);
                helper.setText(R.id.slogan_item_goods_service_list_adapter, bean.slogan);
                helper.setText(R.id.price_item_goods_service_list_adapter, "¥" + bean.price);
                helper.setText(R.id.distance_item_goods_service_list_adapter, bean.distance);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
                            .putExtra("goods_id", goodsModelList.get(position).goods_id)
                            .putExtra("type", goodsModelList.get(position).type)
                    );
                }

            }
        });

        listview.setAdapter(sss_adapter);
    }

    /**
     * 获取商品列表
     */
    public void goods_list() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_list(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("looks", looks)
                            .put("price", price)
                            .put("collect", collect)
                            .put("distance", distance)
                            .put("credit", credit)
                            .put("shop_id", shop_id)
                            .put("p", p)
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            listview.onRefreshComplete();
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            listview.onRefreshComplete();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if (p == 1) {
                                            goodsModelList.clear();
                                        }
                                        p++;

                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            GoodsModel goodsModel = new GoodsModel();
                                            goodsModel.goods_id = jsonArray.getJSONObject(j).getString("goods_id");
                                            goodsModel.title = jsonArray.getJSONObject(j).getString("title");
                                            goodsModel.slogan = jsonArray.getJSONObject(j).getString("slogan");
                                            goodsModel.master_map = jsonArray.getJSONObject(j).getString("master_map");
                                            goodsModel.cost_price = jsonArray.getJSONObject(j).getString("cost_price");
                                            goodsModel.price = jsonArray.getJSONObject(j).getString("price");
                                            goodsModel.sell = jsonArray.getJSONObject(j).getString("sell");
                                            goodsModel.type = jsonArray.getJSONObject(j).getString("type");
                                            goodsModel.member_id = jsonArray.getJSONObject(j).getString("member_id");
                                            goodsModel.distance = jsonArray.getJSONObject(j).getString("distance");
                                            goodsModelList.add(goodsModel);
                                        }

                                        sss_adapter.setList(goodsModelList);
                                    }

                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }

}
