package com.sss.car.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.R;
import com.sss.car.model.CitySelectModel;
import com.sss.car.model.CitySelectModel_Browse_Top;


/**
 * Created by leilei on 2017/11/8.
 */

public class CitySelect extends LinearLayout {
    View view;
    TextView title_recently_city_select;
    TextView current_city_select;
    InnerGridView recently_city_select;
    InnerGridView hot_city_select;
    TextView title_current_city_select;

    public CitySelect(Context context) {
        super(context);
        init(context);
    }

    public CitySelect(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CitySelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.city_select, null);
        title_recently_city_select = $.f(view, R.id.title_recently_city_select);
        current_city_select = $.f(view, R.id.current_city_select);
        recently_city_select = $.f(view, R.id.recently_city_select);
        hot_city_select = $.f(view, R.id.hot_city_select);
        title_current_city_select=$.f(view,R.id.title_current_city_select);
        this.addView(view);
    }


    public void setCurrent(String city){
        if (!StringUtils.isEmpty(city)){
            current_city_select.setVisibility(VISIBLE);
            current_city_select.setText(city);
            title_current_city_select.setVisibility(VISIBLE);
        }
    }



    public void setData(String city,Context context, final CitySelectModel citySelectModel, final CitySelectCallBack citySelectCallBack) {
        title_current_city_select.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (citySelectCallBack != null) {
                    citySelectCallBack.onCurrent();
                }
            }
        });
        setCurrent(city);
        if (citySelectModel.browse.size() > 0) {
            title_recently_city_select.setVisibility(View.VISIBLE);
            recently_city_select.setVisibility(View.VISIBLE);
            SSS_Adapter sss_adapter = new SSS_Adapter<CitySelectModel_Browse_Top>(context, R.layout.item_city_select_adapter, citySelectModel.browse) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, CitySelectModel_Browse_Top bean, SSS_Adapter instance) {
                    helper.setText(R.id.text_item_city_select_adapter, bean.name);
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(R.id.text_item_city_select_adapter);

                }
            };
            sss_adapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.text_item_city_select_adapter:
                            if (citySelectCallBack != null) {
                                citySelectCallBack.onHistroy(citySelectModel.browse.get(position));
                            }
                            break;
                    }
                }
            });
            recently_city_select.setAdapter(sss_adapter);
        }

        SSS_Adapter hot = new SSS_Adapter<CitySelectModel_Browse_Top>(context, R.layout.item_city_select_adapter, citySelectModel.top) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CitySelectModel_Browse_Top bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_city_select_adapter, bean.name);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.text_item_city_select_adapter);

            }
        };
        hot.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.text_item_city_select_adapter:
                        if (citySelectCallBack != null) {
                            citySelectCallBack.onTop(citySelectModel.top.get(position));
                        }
                        break;
                }
            }
        });
        hot_city_select.setAdapter(hot);

    }


    public interface CitySelectCallBack {
        void onCurrent();

        void onHistroy(CitySelectModel_Browse_Top model);

        void onTop(CitySelectModel_Browse_Top model);
    }


}
