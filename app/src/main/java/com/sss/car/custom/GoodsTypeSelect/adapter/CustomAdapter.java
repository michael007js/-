package com.sss.car.custom.GoodsTypeSelect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.custom.GoodsTypeSelect.View.DelTextView;
import com.sss.car.custom.GoodsTypeSelect.View.FocusEditText;
import com.sss.car.custom.GoodsTypeSelect.View.ListGoodsTypeSelect;
import com.sss.car.custom.GoodsTypeSelect.model.CustomDataModel;

import java.util.List;

/**
 * 自定义属性适配器
 * Created by leilei on 2017/10/31.
 */

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<CustomDataModel> customList;
    private final int TOTAL_COUNT = 2;
    private final int EDIT = -1;
    private final int DEL_TEXT = -2;
    private OnCustomAdapterCallBack onCustomAdapterCallBack;

    public CustomAdapter(Context context, List<CustomDataModel> customList, OnCustomAdapterCallBack onCustomAdapterCallBack) {
        this.context = context;
        this.customList = customList;
        this.onCustomAdapterCallBack = onCustomAdapterCallBack;
    }

    public void refresh(List<CustomDataModel> customList) {
        this.customList = customList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return customList == null ? 0 : customList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;

    }

    @Override
    public int getViewTypeCount() {
        return TOTAL_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (customList.get(position).type.equals(ListGoodsTypeSelect.DEFAULT_HOLDER_DEL_CONTENT)) {
            return DEL_TEXT;
        } else {
            return EDIT;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomAdapter_DelTextViewHolder customAdapter_delTextViewHolder;
        if (DEL_TEXT == getItemViewType(position)) {
            if (convertView == null) {
                customAdapter_delTextViewHolder = new CustomAdapter_DelTextViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_custom_adapter_del, null);
                customAdapter_delTextViewHolder.delTextView = $.f(convertView, R.id.text_item_custom_adapter_del);
                convertView.setTag(customAdapter_delTextViewHolder);
            } else {
                customAdapter_delTextViewHolder = (CustomAdapter_DelTextViewHolder) convertView.getTag();
            }
            customAdapter_delTextViewHolder.delTextView
                    .setText(customList.get(position).content)
                    .setOnDelTextTextCallBack(new DelTextView.OnDelTextTextCallBack() {
                        @Override
                        public void onDelete(String content) {
                            if (onCustomAdapterCallBack != null) {
                                onCustomAdapterCallBack.onDelete(content, position, CustomAdapter.this);
                            }
                        }
                    });
        } else {
            if (convertView == null) {
                customAdapter_delTextViewHolder = new CustomAdapter_DelTextViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_custom_adapter_focus, null);
                customAdapter_delTextViewHolder.focusEditText = $.f(convertView, R.id.et_item_custom_adapter_focus);
                convertView.setTag(customAdapter_delTextViewHolder);
            } else {
                customAdapter_delTextViewHolder = (CustomAdapter_DelTextViewHolder) convertView.getTag();
            }
            customAdapter_delTextViewHolder.focusEditText
                    .setHint("自定义" + customList.get(position).title)
                    .setOnFocusEditTextCallBack(new FocusEditText.OnFocusEditTextCallBack() {
                        @Override
                        public void onLostFocus(String content) {
                            if (onCustomAdapterCallBack != null) {
                                onCustomAdapterCallBack.onLostFocus(content,position, CustomAdapter.this);
                            }
                        }
                    });

        }
        return convertView;
    }


    public interface OnCustomAdapterCallBack {
        void onDelete(String Content, int position, CustomAdapter customAdapter);

        void onLostFocus(String content, int position, CustomAdapter customAdapter);
    }

}

class CustomAdapter_DelTextViewHolder {
    DelTextView delTextView;
    FocusEditText focusEditText;
}



