package com.blankj.utilcode.customwidget.GalleryHorizontalListView.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.R;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by leilei on 2018/1/3.
 */

public class ItemPic extends RelativeLayout {
    private SimpleDraweeView pic;
    private SimpleDraweeView close;
    private String url;
    private int position;
    private OnItemPicClick onItemPicClick;

    public ItemPic setOnItemPicClick(OnItemPicClick onItemPicClick) {
        this.onItemPicClick = onItemPicClick;
        return this;
    }

    public ItemPic setUrl(String url) {
        this.url = url;
        return this;
    }

    public ItemPic setPosition(int position) {
        this.position = position;
        return this;

    }

    public ItemPic(Context context) {
        super(context);
        init(context);
    }

    public ItemPic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemPic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        LogUtils.e(changed + "---" + l + "---" + t + "---" + r + "---" + b);
        pic.layout(l, t, r, b);
        close.layout(r - 50, 10, r - 10, 50);
    }

    private void init(Context context) {
        pic = new SimpleDraweeView(context);
        close = new SimpleDraweeView(context);
        FrescoUtils.showImage(false, 120, 120, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.drawable.close), close, 0f);
        pic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemPicClick != null) {
                    onItemPicClick.onClickItemPic(url, pic, position);
                }

            }
        });
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemPicClick != null) {
                    onItemPicClick.onClose(url, position);
                }
            }
        });
        this.addView(pic);
        this.addView(close);
        RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(SizeUtils.dp2px(context,40),SizeUtils.dp2px(context,120));
        layoutParams.width=60;
        layoutParams.height=60;
        close.setLayoutParams(layoutParams);
        this.setPadding(2,2,2,2);
    }

    public SimpleDraweeView getPic() {
        return pic;
    }

    public ItemPic hideClose(boolean isHide) {
        if (isHide) {
            close.setVisibility(INVISIBLE);
        }
        return this;
    }


    public interface OnItemPicClick {
        void onClickItemPic(String url, SimpleDraweeView pic, int position);

        void onClose(String url, int position);
    }
}
