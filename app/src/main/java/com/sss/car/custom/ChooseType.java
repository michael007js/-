package com.sss.car.custom;

import android.content.Context;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.R;
import com.sss.car.model.GoodsChooseSizeName;
import com.sss.car.model.GoodsChooseSizeName;
import com.sss.car.model.GoodsChooseSizeName_Model;

import java.util.List;


/**
 * Created by leilei on 2017/9/25.
 */

public class ChooseType extends LinearLayout {

    ChooseTypeCallBack chooseTypeCallBack;

    public ChooseType(Context context) {
        super(context);
    }

    public ChooseType(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChooseType(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChooseType setChooseTypeCallBack(ChooseTypeCallBack chooseTypeCallBack) {
        this.chooseTypeCallBack = chooseTypeCallBack;
        return this;
    }

    public void create(Context context, int numColumns, final List<GoodsChooseSizeName> list) {
        this.setOrientation(OrientationHelper.VERTICAL);
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            TextView textView = new TextView(context);
            textView.setText(list.get(i).title);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setTextSize(15f);
            this.addView(textView);
            final InnerGridView innerGridView = new InnerGridView(context);
            this.addView(innerGridView);
            innerGridView.setNumColumns(numColumns);
            final SSS_Adapter sss_adapter = new SSS_Adapter<GoodsChooseSizeName_Model>(context, R.layout.item_choosetype_adapter) {
                @Override
                protected void setView(SSS_HolderHelper helper, final int position, GoodsChooseSizeName_Model bean,SSS_Adapter instance) {
                    helper.setText(R.id.text_item_choosetype_adapter, bean.name);
                    if (bean.isChoose) {
                        if (chooseTypeCallBack != null) {
                            chooseTypeCallBack.onChooseType(list,list.get(finalI), list.get(finalI).data.get(position));
                        }
                        helper.setTextColorRes(R.id.text_item_choosetype_adapter, R.color.white);
                        APPOftenUtils.setBackgroundOfVersion(((TextView) helper.getView(R.id.text_item_choosetype_adapter)), getResources().getDrawable(R.drawable.bg_button_small_ra));
                    } else {
                        helper.setTextColorRes(R.id.text_item_choosetype_adapter, R.color.textColor);
                        APPOftenUtils.setBackgroundOfVersion(((TextView) helper.getView(R.id.text_item_choosetype_adapter)), getResources().getDrawable(R.drawable.bg_button_small_ra1));
                    }


                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(R.id.text_item_choosetype_adapter);
                }
            };

            sss_adapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, final int position, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.text_item_choosetype_adapter:
                            for (int j = 0; j < list.get(finalI).data.size(); j++) {
                                list.get(finalI).data.get(j).isChoose=false;
                            }
                            list.get(finalI).data.get(position).isChoose = true;

                            sss_adapter.setList(list.get(finalI).data);
                            if (chooseTypeCallBack != null) {
                                chooseTypeCallBack.onChooseType(list,list.get(finalI), list.get(finalI).data.get(position));
                            }
                            break;
                    }
                }
            });


            innerGridView.setAdapter(sss_adapter);
            sss_adapter.setList(list.get(i).data);

        }
    }



    public interface ChooseTypeCallBack {
        void onChooseType(List<GoodsChooseSizeName> list, GoodsChooseSizeName GoodsChooseSizeName, GoodsChooseSizeName_Model GoodsChooseSizeName_DataModel);
    }

}
