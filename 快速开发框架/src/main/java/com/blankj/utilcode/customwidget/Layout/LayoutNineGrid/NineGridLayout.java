package com.blankj.utilcode.customwidget.Layout.LayoutNineGrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.R;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿朋友圈九宫格
 * http://blog.csdn.net/hmyang314/article/details/51415396
 * https://github.com/HMY314/NineGridLayout
 * Created by leilei on 2017/6/3.
 */

public abstract class NineGridLayout extends ViewGroup {

    private static final float DEFUALT_SPACING = 3f;


    protected Context mContext;
    private float mSpacing = DEFUALT_SPACING;
    private int mColumns;
    private int mRows;
    private int mTotalWidth;
    private int mSingleWidth;
    private static int mMaxCount = 9;
    private boolean mIsShowAll = true;
    private boolean mIsFirst = true;
    private boolean mIsRemoveSame = false;
    private int mDefaultCloseSize = 50;

    private String DistinguishTenPic="http://";//如果在发布动态时采用本九宫格,则用来区分图片最后一张的"十"字图片

    private boolean isShowCloseButton = false;
    private List<String> mUrlList = new ArrayList<>();
    private List<String> mRejectUrlList = new ArrayList<>();


    public NineGridLayout(Context context) {
        super(context);
        init(context);
    }

    public void setDistinguishTenPic(String DistinguishTenPic) {
        this.DistinguishTenPic = DistinguishTenPic;
    }

    public void setmIsRemoveSame(boolean mIsRemoveSame) {
        this.mIsRemoveSame = mIsRemoveSame;
    }


    public void defaultCloseSize(int mDefaultCloseSize) {
        this.mDefaultCloseSize = mDefaultCloseSize;
    }

    public void setMaxCount(int maxCount) {
        this.mMaxCount = maxCount;
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);

        mSpacing = typedArray.getDimension(R.styleable.NineGridLayout_sapcing, DEFUALT_SPACING);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        if (getListSize(mUrlList) == 0) {
            setVisibility(GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mTotalWidth = right - left;
        mSingleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
        if (mIsFirst) {
            notifyDataSetChanged();
            mIsFirst = false;
        }
    }

    /**
     * 设置间隔
     *
     * @param spacing
     */
    public void setSpacing(float spacing) {
        mSpacing = spacing;
    }

    /**
     * 设置是否显示所有图片（超过最大数时）
     *
     * @param isShowAll
     */
    public void setIsShowAll(boolean isShowAll) {
        mIsShowAll = isShowAll;
    }

    public void setIsShowCloseButton(boolean isShowCloseButton) {
        this.isShowCloseButton = isShowCloseButton;
    }

    public void setUrlList(List<String> urlList) {
        if (getListSize(urlList) == 0) {
            setVisibility(GONE);
            return;
        }


        if (mIsRemoveSame) {
            mRejectUrlList.clear();
            for (int i = 0; i < mUrlList.size() - 1; i++) {
                for (int j = i + 1; j < urlList.size(); j++) {
                    if (urlList.get(j).equals(mUrlList.get(i))) {
                        mRejectUrlList.add(urlList.get(j));
                        urlList.remove(j);
                    }
                }
            }
            onSamePhotos(mRejectUrlList);
        }


        setVisibility(VISIBLE);
        mUrlList.clear();
        mUrlList.addAll(urlList);

        if (!mIsFirst) {
            notifyDataSetChanged();
        }

    }

    public void notifyDataSetChanged() {
        removeAllViews();
        int size = getListSize(mUrlList);
        if (size > 0) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }

        if (size == 1) {
            String url = mUrlList.get(0);
            RatioImageView imageView = createImageView(0, url);

            //避免在ListView中一张图未加载成功时，布局高度受其他item影响
            LayoutParams params = getLayoutParams();
            params.height = mSingleWidth;
            setLayoutParams(params);
            imageView.layout(0, 0, mSingleWidth, mSingleWidth);

            boolean isShowDefualt = displayOneImage(imageView, url, mTotalWidth, mContext);
            if (isShowDefualt) {
                layoutImageView(imageView, 0, url, false);
            } else {
                addView(imageView);
            }
            return;
        }

        generateChildrenLayout(size);
        layoutParams();

        for (int i = 0; i < size; i++) {
            String url = mUrlList.get(i);
            RatioImageView imageView;
            if (!mIsShowAll) {
                if (i < mMaxCount - 1) {
                    imageView = createImageView(i, url);
                    layoutImageView(imageView, i, url, false);
                } else { //第9张时
                    if (size <= mMaxCount) {//刚好第9张
                        imageView = createImageView(i, url);
                        layoutImageView(imageView, i, url, false);
                    } else {//超过9张
                        imageView = createImageView(i, url);
                        layoutImageView(imageView, i, url, true);
                        break;
                    }
                }
            } else {
                imageView = createImageView(i, url);
                layoutImageView(imageView, i, url, false);
            }
        }
    }

    private void layoutParams() {
        int singleHeight = mSingleWidth;

        //根据子view数量确定高度
        LayoutParams params = getLayoutParams();
        params.height = (int) (singleHeight * mRows + mSpacing * (mRows - 1));
        setLayoutParams(params);
    }

    private RatioImageView createImageView(final int i, final String url) {
        RatioImageView imageView = new RatioImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage(i, url, mUrlList, mContext);
            }
        });
        return imageView;
    }

    /**
     * @param imageView
     * @param url
     * @param showNumFlag 是否在最大值的图片上显示还有未显示的图片张数
     */
    private void layoutImageView(RatioImageView imageView, final int i, final String url, boolean showNumFlag) {
        final int singleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
        int singleHeight = singleWidth;

        int[] position = findPosition(i);
        int left = (int) ((singleWidth + mSpacing) * position[1]);
        int top = (int) ((singleHeight + mSpacing) * position[0]);
        int right = left + singleWidth;
        int bottom = top + singleHeight;

        imageView.layout(left, top, right, bottom);
        addView(imageView);
        if (showNumFlag) {//添加超过最大显示数量的文本
            int overCount = getListSize(mUrlList) - mMaxCount;
            if (overCount > 0) {
                for (int j = 0; j < mUrlList.size(); j++) {
                    if (DistinguishTenPic.equals(mUrlList.get(j))) {
                        overCount--;
                    }
                }
                if (overCount > 0) {
                    float textSize = 30;
                    final TextView textView = new TextView(mContext);
                    textView.setText("+" + String.valueOf(overCount));
                    textView.setTextColor(Color.WHITE);
                    textView.setPadding(0, singleHeight / 2 - getFontHeight(textSize), 0, 0);
                    textView.setTextSize(textSize);
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackgroundColor(Color.BLACK);
                    textView.getBackground().setAlpha(120);
                    textView.layout(left, top, right, bottom);
                    addView(textView);
                }
            }
        }

        if (isShowCloseButton && !DistinguishTenPic.equals(url)) {
            RatioImageView close = new RatioImageView(mContext);
            close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickImageColse(i, url, mUrlList, mContext);
                }
            });
            close.layout(right - mDefaultCloseSize - 5, top + 5, right - 5, top + mDefaultCloseSize + 5);
            GlidUtils.glideLoad(false,close, mContext, R.drawable.close);
            addView(close);

            displayImage(imageView,close, url, mTotalWidth, mContext);
        }else {
            displayImage(imageView,null, url, mTotalWidth, mContext);
        }

    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                if ((i * mColumns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    /**
     * 根据图片个数确定行列数量
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            mRows = 1;
            mColumns = length;
        } else if (length <= 6) {
            mRows = 2;
            mColumns = 3;
            if (length == 4) {
                mColumns = 2;
            }
        } else {
            mColumns = 3;
            if (mIsShowAll) {
                mRows = length / 3;
                int b = length % 3;
                if (b > 0) {
                    mRows++;
                }
            } else {
                mRows = 3;
            }
        }

    }

    protected void setOneImageLayoutParams(RatioImageView imageView, int width, int height) {
        imageView.setLayoutParams(new LayoutParams(width, height));
        imageView.layout(0, 0, width, height);
        LayoutParams params = getLayoutParams();
//        params.width = width;
        params.height = height;
        setLayoutParams(params);
    }

    private int getListSize(List<String> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * @param imageView
     * @param url
     * @param parentWidth 父控件宽度
     * @return true 代表按照九宫格默认大小显示，false 代表按照自定义宽高显示
     */
    protected abstract boolean displayOneImage(RatioImageView imageView, String url, int parentWidth, Context context);

    protected abstract void displayImage(RatioImageView imageView,RatioImageView closeButton, String url,int parentWidth, Context context);

    protected abstract void onClickImage(int position, String url, List<String> urlList, Context context);

    protected abstract void onClickImageColse(int position, String url, List<String> urlList, Context context);

    protected abstract void onSamePhotos(List<String> mRejectUrlList);


}
