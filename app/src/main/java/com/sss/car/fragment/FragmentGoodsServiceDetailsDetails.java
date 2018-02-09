package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.xrichtext.DataImageView;
import com.blankj.utilcode.xrichtext.RichTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.CustomRefreshLayoutCallBack3;
import com.sss.car.view.ActivityImages;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by leilei on 2017/9/15.
 */

@SuppressLint("ValidFragment")
public class FragmentGoodsServiceDetailsDetails extends BaseFragment {
    @BindView(R.id.parent_fragment_goods_service_details_details)
    public ScrollView parentFragmentGoodsServiceDetailsDetails;
    @BindView(R.id.linearLayout_fragment_goods_service_details_details)
    LinearLayout linearLayoutFragmentGoodsServiceDetailsDetails;
    Unbinder unbinder;
    CustomRefreshLayoutCallBack3 customRefreshLayoutCallBack3;

    String goods_id;
    YWLoadingDialog ywLoadingDialog;
    List<String> list = new ArrayList<>();

    boolean stop;
    @BindView(R.id.content)
    RichTextView content;
    @BindView(R.id.one)
    TextView one;
    @BindView(R.id.two)
    TextView two;
    @BindView(R.id.three)
    TextView three;
    @BindView(R.id.four)
    TextView four;

    public FragmentGoodsServiceDetailsDetails() {
    }

    public FragmentGoodsServiceDetailsDetails(boolean stop, String goods_id, CustomRefreshLayoutCallBack3 customRefreshLayoutCallBack3) {
        this.stop = stop;
        this.goods_id = goods_id;
        this.customRefreshLayoutCallBack3 = customRefreshLayoutCallBack3;

    }

    @Override
    public void onDestroy() {
        if (list != null) {
            list.clear();
        }
        list = null;
        parentFragmentGoodsServiceDetailsDetails = null;
        linearLayoutFragmentGoodsServiceDetailsDetails = null;

        customRefreshLayoutCallBack3 = null;
        goods_id = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_goods_service_details_details;
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
                                parameter();
                                if (customRefreshLayoutCallBack3 != null) {
                                    customRefreshLayoutCallBack3.onAdd(parentFragmentGoodsServiceDetailsDetails);
                                }
                                content.setOnImageClickListener(new RichTextView.OnImageClickListener() {
                                    @Override
                                    public void onClickImage(int position, List<String> img, DataImageView imageView) {
                                        if (getBaseFragmentActivityContext() != null) {
                                            List<String> temp = new ArrayList<>();
                                            for (int i = 0; i < img.size(); i++) {
                                                temp.add(Config.url + img.get(i));
                                            }
                                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                                    .putExtra("current", position));
                                        }
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
     * 获取店铺信息
     */
    public void get_photo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_photo(
                    new JSONObject()
                            .put("goods_id", goods_id)
                            .put("member_id", Config.member_id)
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
                                    content
                                            .showEditData(Config.url, jsonObject.getJSONArray("data").toString(),
                                                    content,
                                                    getBaseFragmentActivityContext(),
                                                    getActivity().getWindowManager().getDefaultDisplay().getWidth(),
                                                    getActivity().getWindowManager().getDefaultDisplay().getHeight(), 0);
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

    /**
     * 获取店铺信息
     */
    public void parameter() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.parameter(
                    new JSONObject()
                            .put("goods_id", goods_id)
                            .put("member_id", Config.member_id)
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
                                    one.setText(jsonObject.getJSONObject("data").getString("brand"));
                                    two.setText(jsonObject.getJSONObject("data").getString("vehicle_type"));
                                    three.setText(jsonObject.getJSONObject("data").getString("service"));
                                    four.setText(jsonObject.getJSONObject("data").getString("size_data"));
                                    get_photo();
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


    /**
     * 此方法为老版本，只加载图片
     */
    void showData() {

        for (int i = 0; i < list.size(); i++) {
            final View view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.item_fragment_goods_service_details_details, null);
            final ImageView image = $.f(view,
                    R.id.pic_item_fragment_goods_service_details_details);

            image.setTag(R.id.glide_tag, list.get(i));

            linearLayoutFragmentGoodsServiceDetailsDetails.addView(view);

            Glide.with(getBaseFragmentActivityContext())
                    .load(list.get(i))
                    .asBitmap()
                    .dontAnimate()
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            int imageWidth = resource.getWidth();
                            int imageHeight = resource.getHeight();
                            if (imageHeight > 4096) {
                                imageHeight = 4096;
                            }
                            double precent = 1.0 * imageWidth / imageHeight;
                            LogUtils.e(precent);

                            LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(getActivity().getWindowManager().getDefaultDisplay().getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                            para.width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                            para.height = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() / precent);
                            view.setLayoutParams(para);
                            LogUtils.e(imageHeight);
                        }

                    });

            final int finalI = i;
            if (!stop) {
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                    .putStringArrayListExtra("data", (ArrayList<String>) list)
                                    .putExtra("current", finalI));
                        }
                    }
                });
            }

            addImageViewList(GlidUtils.downLoader(false, image, getBaseFragmentActivityContext()));

        }
    }
}
