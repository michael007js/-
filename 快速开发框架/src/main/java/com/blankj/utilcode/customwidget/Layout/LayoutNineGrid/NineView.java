package com.blankj.utilcode.customwidget.Layout.LayoutNineGrid;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;


/*

    List<String> imageList = new ArrayList<>();

    @Override
    public void onDisplayOneImage(RatioImageView imageView, String url, int parentWidth, Context context) {
        if ("default".equals(url)) {
            addImageViewList(GlidUtils.glideLoad(false, imageView, getBaseActivityContext(), R.mipmap.add_photo));
        } else {
            imageView.setTag(R.id.glide_tag, url);
            addImageViewList(GlidUtils.downLoader(false, imageView, getBaseActivityContext()));
        }
    }

    @Override
    public void onDisplayImage(RatioImageView imageView, RatioImageView closeButton, String url, int parentWidth, Context context) {
        if ("default".equals(url)) {
            addImageViewList(GlidUtils.glideLoad(false, imageView, getBaseActivityContext(), R.mipmap.add_photo));
            if (closeButton != null) {
                closeButton.setVisibility(View.GONE);
            }
        } else {
            imageView.setTag(R.id.glide_tag, url);
            addImageViewList(GlidUtils.downLoader(false, imageView, getBaseActivityContext()));
            if (closeButton != null) {
                closeButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClickImage(int position, String url, List<String> urlList, Context context) {
        if ("default".equals(url)) {
            int count = 9;
            if (imageList.size() > 1) {
                count = 9 - imageList.size() + 1;
            }
            APPOftenUtils.createPhotoChooseDialog(0, count, getBaseActivityContext(), MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    if (resultList == null || resultList.size() == 0) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "照片选择错误");
                        return;
                    }

                    for (int i = 0; i < imageList.size(); i++) {
                        if ("default".equals(imageList.get(i))) {
                            imageList.remove(i);
                        }
                    }
                    for (int i = 0; i < resultList.size(); i++) {
                        imageList.add(resultList.get(i).getPhotoPath());
                    }
                    if (imageList.size() < 9) {
                        imageList.add("default");
                    }
                    nine.urlList(imageList);

                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    ToastUtils.showShortToast(getBaseActivityContext(), errorMsg);
                }
            });
        } else {
            List<String> temp = new ArrayList<>();
            int count = 0;
            for (int i = 0; i < imageList.size(); i++) {
                if (!"default".equals(imageList.get(i))) {
                    temp.add(imageList.get(i));
                } else {
                    count++;
                }
            }
            startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                    .putExtra("current", position));
        }

    }

    @Override
    public void onClickImageColse(int position, String url, List<String> urlList, Context context) {
        imageList.remove(position);
        int count = 0;
        for (int i = 0; i < imageList.size(); i++) {
            if ("default".equals(imageList.get(i))) {
                count++;
            }
        }
        if (count == 0) {
            imageList.add("default");
        }

        nine.urlList(imageList);
    }

    @Override
    public void onSamePhotos(List<String> mRejectUrlList) {

    }
* */

/**
 * 自定义九宫格
 * 基于<一个仿微信朋友圈和QQ空间的九宫格图片展示自定义控件http://blog.csdn.net/hmyang314/article/details/51415396>完善
 * Created by leilei on 2017/7/26.
 */

@SuppressWarnings("ALL")
public class NineView extends NineGridLayout {
    NineViewShowCallBack nineViewShowCallBack;

    public NineView(Context context) {
        super(context);
    }

    public interface NineViewShowCallBack {
        /*如果只有一张图片的话会调用,用来设置图片*/
        void onDisplayOneImage(RatioImageView imageView, String url, int parentWidth, Context context);

        /*如果有多张图片时会调用,用来设置图片*/
        void onDisplayImage(RatioImageView imageView, RatioImageView closeButton,String url,int parentWidth, Context context);

        /*图片框被点击时调用*/
        void onClickImage(int position, String url, List<String> urlList, Context context);

        /*右上角XX被点击时调用*/
        void onClickImageColse(int position, String url, List<String> urlList, Context context);

        /*如果有重复如图片后会调用*/
        void onSamePhotos(List<String> mRejectUrlList);
    }

    public NineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 是否移除相同的图片
     *
     * @param mIsRemoveSame
     */
    public NineView isRemoveSame(boolean mIsRemoveSame) {
        super.setmIsRemoveSame(mIsRemoveSame);
        return this;
    }

    /**
     * 默认右上角XX按钮的尺寸
     *
     * @param mDefaultColoseSize
     * @return
     */
    public NineView CloseSize(int mDefaultColoseSize) {
        super.defaultCloseSize(mDefaultColoseSize);
        return this;
    }

    /**
     * 如果在发布动态时采用本九宫格,则用来区分图片最后一张的"十"字图片
     * @param picUrl
     * @return
     */
    public NineView distinguishTenPic(String picUrl) {
        super.setDistinguishTenPic(picUrl);
        return this;
    }

    /**
     * 是否显示右上角XX
     *
     * @param isShowCloseButton
     */
    public NineView isShowCloseButton(boolean isShowCloseButton) {
        super.setIsShowCloseButton(isShowCloseButton);
        return this;
    }

    /**
     * 如果图片数量大于9张,则设置是否要显示全部图片
     *
     * @param isShowAll
     */
    public NineView isShowAll(boolean isShowAll) {
        super.setIsShowAll(isShowAll);
        return this;
    }

    /**
     * 设置要显示的图片集合
     *
     * @param urlList
     * @param nineViewShowCallBack
     * @return
     */
    public NineView urlList(List<String> urlList, NineViewShowCallBack nineViewShowCallBack) {
        this.nineViewShowCallBack = nineViewShowCallBack;
        super.setUrlList(urlList);
        return this;
    }

    /**
     * 设置操作回调
     *
     * @param nineViewShowCallBack
     * @return
     */
    public NineView setNineViewShowCallBack(NineViewShowCallBack nineViewShowCallBack) {
        this.nineViewShowCallBack = nineViewShowCallBack;
        return this;
    }

    /**
     * 设置要显示的图片集合
     *
     * @param urlList
     * @return
     */
    public NineView urlList(List<String> urlList) {
        super.setUrlList(urlList);
        return this;
    }

    /**
     * 设置可以显示最大数量的图片(多余的图片会在最后一张图片上用+表示)
     *
     * @param maxCount
     */
    public NineView maxCount(int maxCount) {
        super.setMaxCount(maxCount);
        return this;
    }

    /**
     * 设置间距
     *
     * @param spacing
     */
    public NineView spacing(float spacing) {
        super.setSpacing(spacing);
        return this;
    }

    @Override
    /*如果只有一张图片的话会调用,用来设置图片*/
    protected boolean displayOneImage(RatioImageView imageView, String url, int parentWidth, Context context) {
        if (nineViewShowCallBack != null) {
            nineViewShowCallBack.onDisplayOneImage(imageView, url, parentWidth, context);
        }
        return false;
    }

    @Override
     /*如果有多张图片时会调用,用来设置图片*/
    protected void displayImage(RatioImageView imageView, RatioImageView closeButton,String url,int parentWidth, Context context) {
        if (nineViewShowCallBack != null) {
            nineViewShowCallBack.onDisplayImage(imageView,closeButton, url, parentWidth,context);
        }
    }

    @Override
     /*图片框被点击时调用*/
    protected void onClickImage(int position, String url, List<String> urlList, Context context) {
        if (nineViewShowCallBack != null) {
            nineViewShowCallBack.onClickImage(position, url, urlList, context);
        }
    }

    @Override
     /*右上角XX被点击时调用*/
    protected void onClickImageColse(int position, String url, List<String> urlList, Context context) {
        if (nineViewShowCallBack != null) {
            nineViewShowCallBack.onClickImageColse(position, url, urlList, context);
        }
    }

    @Override
    /*如果有重复如图片后会调用*/
    protected void onSamePhotos(List<String> mRejectUrlList) {
        if (nineViewShowCallBack != null) {
            nineViewShowCallBack.onSamePhotos(mRejectUrlList);
        }
    }

}
