package com.blankj.utilcode.xrichtext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.R;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.xrichtext.util.ImageUtils;
import com.blankj.utilcode.xrichtext.util.ScreenUtils;
import com.blankj.utilcode.xrichtext.util.StringUtils;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.path;

/**
 * Created by sendtion on 2016/6/24.
 * 显示富文本
 */
public class RichTextView extends ScrollView {
    private static final int EDIT_PADDING = 10; // edittext常规padding是10dp

    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private LayoutInflater inflater;
    private int editNormalPadding = 0; //
    private List<TextView> textList = new ArrayList();
    private List<SimpleDraweeView> imageList = new ArrayList();
    private OnImageClickListener onImageClickListener;
    private OnHeightCallBack onHeightCallBack;

    public void setOnHeightCallBack(OnHeightCallBack onHeightCallBack) {
        this.onHeightCallBack = onHeightCallBack;
    }

    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        //allLayout.setBackgroundColor(Color.WHITE);//去掉背景
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        allLayout.setPadding(50, 15, 50, 15);//设置间距，防止生成图片时文字太靠边
        addView(allLayout, layoutParams);

        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //editNormalPadding = dip2px(EDIT_PADDING);
        TextView firstText = createTextView(""/*"没有内容"*/, dip2px(context, EDIT_PADDING));
        allLayout.addView(firstText, firstEditParam);
    }

    public int dip2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    /**
     * 清除所有的view
     */
    public void clearAllLayout() {
        allLayout.removeAllViews();
    }

    /**
     * 获得最后一个子view的位置
     *
     * @return
     */
    public int getLastIndex() {
        int lastEditIndex = allLayout.getChildCount();
        return lastEditIndex;
    }

    /**
     * 生成文本输入框
     */
    public TextView createTextView(String hint, int paddingTop) {
        TextView textView = (TextView) inflater.inflate(R.layout.rich_textview, null);
        textView.setTag(viewTagIndex++);
        textView.setPadding(editNormalPadding, paddingTop, editNormalPadding, paddingTop);
        textView.setHint(hint);
        textList.add(textView);
        return textView;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.edit_imageview, null);
        layout.setTag(viewTagIndex++);
        View closeView = layout.findViewById(R.id.image_close);
        closeView.setVisibility(GONE);


        return layout;
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    public void addTextViewAtIndex(final int index, CharSequence editStr, int textSize) {
        TextView textView = createTextView("", EDIT_PADDING);
        textView.setText(editStr);
        switch (textSize) {
            case -1:
                textView.setTextSize(10);
                break;
            case 0:
                textView.setTextSize(14);
                break;
            case 1:
                textView.setTextSize(18);
                break;
        }
        allLayout.addView(textView, index);
        if (onHeightCallBack!=null){
            onHeightCallBack.onHeight(allLayout.getLayoutParams().height);
        }
    }





    /**
     * 在特定位置添加ImageView
     */
    public void addImageViewAtIndex(final Context context, final int index, final String imagePath, final int w, final int h) {
        final RelativeLayout imageLayout = createImageLayout();
        final DataImageView imageView = (DataImageView) imageLayout.findViewById(R.id.edit_imageView);
        imageList.add(imageView);
        imageLayout.setTag(imagePath);
        LogUtils.e(imagePath);
        GlidUtils.getWidthAndHeight(context, imageView, imagePath, new GlidUtils.OnWidthAndHeightCallBack() {
            @Override
            public void onCallBack(String imgUrl, int width, int height) {

                int screenWidth=ScreenUtils.getScreenWidth(context);



                double percentage = new BigDecimal((float)height/width).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                int tempH = h;
                if ((int) (percentage * screenWidth) > 0) {
                    tempH = (int) (percentage * screenWidth);
                }
                LogUtils.e("percentage:"+percentage+"\nheight:"+height+"\nwidth:"+width+"\nscreenWidth:"+screenWidth+"\nw:"+w);
                FrescoUtils.showImage(false, w, tempH, Uri.parse(imgUrl), imageView, 0f);
                if (onHeightCallBack!=null){
                    onHeightCallBack.onHeight(tempH);
                }
                imageView.setAbsolutePath(imgUrl);

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        screenWidth, tempH);
                lp.bottomMargin = 5;
                imageView.setLayoutParams(lp);

                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onImageClickListener != null) {
                            List<String> temp=new ArrayList<>();
                            for (int i = img.size()-1; i >-1; i--) {
                                temp.add(img.get(i));
                            }
                            onImageClickListener.onClickImage(index,imagePath, temp, imageView);
                            return;
                        }
                    }
                });
            }
        });
        allLayout.addView(imageLayout, index);
    }


    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    public Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width
                + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    List<String> img = new ArrayList<>();

    public void showEditData(String servelUrl, String content, RichTextView richTextView, Context context, int width, int height, int textSize) throws JSONException {
        allLayout.removeAllViews();
        JSONArray jsonArray = new JSONArray(content);
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).has("img")) {
                img.add(jsonArray.getJSONObject(i).getString("img"));
                int w;
                int h;

                if (width == 0 || height == 0) {
                    w = ScreenUtils.getScreenWidth(context);
                    h = ScreenUtils.getScreenHeight(context);
                } else {
                    w = width;
                    h = height;
                }
                addImageViewAtIndex(context, richTextView.getLastIndex(), servelUrl + jsonArray.getJSONObject(i).getString("img"), w, h);
            } else if (jsonArray.getJSONObject(i).has("text")) {
                richTextView.addTextViewAtIndex(richTextView.getLastIndex(), jsonArray.getJSONObject(i).getString("text"), textSize);
            }
        }


    }


    public void changeImageSize(int width, int height) {

        for (int i = 0; i < imageList.size(); i++) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageList.get(i).getLayoutParams();
            lp.width = width;
            lp.height = height;
            imageList.get(i).setLayoutParams(lp);
        }
    }


    /**
     * @param sizeType -1  0  1
     */
    public void changeTextSize(int sizeType) {

        for (int i = 0; i < textList.size(); i++) {
            switch (sizeType) {
                case -1:
                    textList.get(i).setTextSize(10);
                    break;
                case 0:
                    textList.get(i).setTextSize(14);
                    break;
                case 1:
                    textList.get(i).setTextSize(18);
                    break;
            }

        }
    }


    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }
/* public void showEditData(String content,RichTextView richTextView,Context context,int width,int height) {
        richTextView.clearAllLayout();
        List<String> textList = StringUtils.cutStringByImgTag(content);
        for (int i = 0; i < textList.size(); i++) {
            String text = textList.get(i);
            if (text.contains("<img")) {
                String imagePath = StringUtils.getImage(text);
                LogUtils.e(imagePath);
                int w ;
                int h ;

                if (width==0||height==0){
                    w= ScreenUtils.getScreenWidth(context);
                    h= ScreenUtils.getScreenHeight(context);
                }else {
                    w=width;
                    h=height;
                }
                richTextView.measure(0,0);

//                Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);
//                if (bitmap != null){
//                    richTextView.addImageViewAtIndex(richTextView.getLastIndex(), bitmap, imagePath);
//                } else {
//                    richTextView.addTextViewAtIndex(richTextView.getLastIndex(), text);
//                }

                addImageViewAtIndex(richTextView.getLastIndex(),imagePath,w,h);
            }else {
                richTextView.addTextViewAtIndex(richTextView.getLastIndex(), text);
            }
        }
    }*/

    public interface OnHeightCallBack{
        void onHeight(int h);
    }


    public interface OnImageClickListener {

        void onClickImage(int position,String path, List<String> img, DataImageView imageView);

    }
}
