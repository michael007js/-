package com.sss.car.custom.GoodsTypeSelect.View;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.ListUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.custom.GoodsTypeSelect.adapter.CustomAdapter;
import com.sss.car.custom.GoodsTypeSelect.model.CustomDataModel;
import com.sss.car.custom.GoodsTypeSelect.model.TotalModel;
import com.sss.car.custom.GoodsTypeSelect.utils.ArrayListTurnsUtils;
import com.sss.car.model.CommoditySizeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 属性选择器
 * Created by leilei on 2017/10/28.
 */

public class ListGoodsTypeSelect extends LinearLayout {
    public static final String DEFAULT_HOLDER_DEL_CONTENT = "あい";
    public static final String DEFAULT_HOLDER_TEXT_CONTENT = "いあ";
    private List<CommoditySizeModel> list = new ArrayList<>();//外部数据
    private List<List<String>> selectList = new ArrayList<>();//选中的对象
    private List<TotalModel> formatList = new ArrayList<>();//筛选后的价格/库存数据
    private View view;//下方筛选后的价格/库存视图
    private SSS_Adapter totalAdapter;//下方筛选后的价格/库存适配器

    public ListGoodsTypeSelect(Context context) {
        super(context);
    }

    public ListGoodsTypeSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListGoodsTypeSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setData(Context context, List<CommoditySizeModel> list) {
        this.list = list;
        this.removeAllViews();
        showData(context);
    }


    public JSONArray getSizeName() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < selectList.size(); i++) {
            if (selectList.get(i).size() == 0) {
                return jsonArray;
            }
        }
        for (int i = 0; i < selectList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", list.get(i).name);
            JSONArray jsonArray1 = new JSONArray();
            for (int j = 0; j < selectList.get(i).size(); j++) {
                jsonArray1.put(new JSONObject().put("name", selectList.get(i).get(j)));
            }
            jsonObject.put("data", jsonArray1);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }


    public JSONArray getSizeData() throws JSONException {
        JSONArray jsonArray = new JSONArray();

        if (formatList.size() == 0) {
            return jsonArray;
        }
        for (int i = 0; i < formatList.size(); i++) {
            if ("".equals(formatList.get(i).number) || "".equals(formatList.get(i).price)) {
                return jsonArray;
            }
        }
        for (int i = 0; i < formatList.size(); i++) {
            jsonArray.put(new JSONObject().put("name", formatList.get(i).title)
                    .put("price", formatList.get(i).price)
                    .put("number", formatList.get(i).number));
        }
        return jsonArray;
    }


    /**
     * 显示数据
     *
     * @param context
     */
    private void showData(final Context context) {
        for (int i = 0; i < list.size(); i++) {
            goodsType(i, context);
        }
        goodsTotal(context);
    }


    /**
     * 初始化商品规格类型
     *
     * @param i
     * @param context
     */
    private void goodsType(int i, Context context) {
        selectList.add(new ArrayList<String>());
        final int finalI = i;
        View view = LayoutInflater.from(context).inflate(R.layout.list_goods_type_select, null);
        TextView title_list_goods_type_select = $.f(view, R.id.title_list_goods_type_select);
        final MoreTextView more_list_goods_type_select = $.f(view, R.id.more_list_goods_type_select);
        InnerGridView custom_list_goods_type_select = $.f(view, R.id.custom_list_goods_type_select);
        InnerGridView type_list_goods_type_select = $.f(view, R.id.type_list_goods_type_select);
        title_list_goods_type_select.setText(list.get(finalI).name);
        /**************************************************上方自定义数据适配器↓************************************************************/
        final List<CustomDataModel> customList = new ArrayList<>();
        customList.add(new CustomDataModel(list.get(finalI).name, "", DEFAULT_HOLDER_TEXT_CONTENT));
        CustomAdapter customAdapter = new CustomAdapter(context, customList, new CustomAdapter.OnCustomAdapterCallBack() {
            @Override
            public void onDelete(String Content, int position, CustomAdapter customAdapter) {
                addToListAndRemoveFromList(Content, finalI, false);
                customList.remove(position);
                customAdapter.refresh(customList);
                LogUtils.e("onDelete");
            }

            @Override
            public void onLostFocus(String content, int position, CustomAdapter customAdapter) {
                addToListAndRemoveFromList(content, finalI, true);
                customList.add(new CustomDataModel(list.get(finalI).name, content, DEFAULT_HOLDER_DEL_CONTENT));
                customAdapter.refresh(customList);
                LogUtils.e("onLostFocus");
            }

        });
        custom_list_goods_type_select.setAdapter(customAdapter);
        /**************************************************上方自定义数据适配器↑************************************************************/
        /**************************************************下方服务器获取的数据适配器↓************************************************************/
        SSS_Adapter sss_adapter_from_server = new SSS_Adapter<String>(context, R.layout.item_list_goods_type_select) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean, final SSS_Adapter instance) {
                ((SelectTextView) helper.getView(R.id.text_item_list_goods_type_select))
                        .setText(bean)
                        .setOnSelectTextViewCallBack(new SelectTextView.OnSelectTextViewCallBack() {
                            @Override
                            public void onSelect(boolean isSelect, String content) {
                                addToListAndRemoveFromList(content, finalI, isSelect);
                            }
                        });
                more_list_goods_type_select
                        .setOnMoreTextViewCallBack(new MoreTextView.OnMoreTextViewCallBack() {
                            @Override
                            public void onMoreTextViewClick(boolean isModeMode) {
                                reShowDataServer(isModeMode, instance, finalI);
                            }
                        });

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        type_list_goods_type_select.setAdapter(sss_adapter_from_server);
        if (list.get(finalI).list.size() > 6) {
            reShowDataServer(false, sss_adapter_from_server, finalI);
            more_list_goods_type_select.setVisibility(VISIBLE);
        } else {
            reShowDataServer(true, sss_adapter_from_server, finalI);
            more_list_goods_type_select.setVisibility(GONE);
        }

        /**************************************************下方服务器获取的数据适配器↑************************************************************/
        this.addView(view);
    }

    /**
     * 添加到选中列表/从选中的列表中移除
     *
     * @param content
     */
    private void addToListAndRemoveFromList(String content, int i, boolean isSelect) {
        if (selectList.get(i).size() < 1) {
            selectList.get(i).add(content);
        } else {
            if (isSelect) {
                selectList.get(i).add(content);
            } else {
                for (int j = 0; j < selectList.get(i).size(); j++) {
                    if (selectList.get(i).get(j).equals(content)) {
                        selectList.get(i).remove(j);
                        break;
                    }
                }
            }
        }
        List<String> temp = ArrayListTurnsUtils.turns(selectList);
        ListUtils.distinctList(temp);
        LogUtils.e(temp.toString());
        if (temp.size() > 0) {
            formatList.clear();
            for (int j = 0; j < temp.size(); j++) {
                formatList.add(new TotalModel(temp.get(j), "", ""));
            }
            totalAdapter.setList(formatList);
            view.setVisibility(VISIBLE);
        } else {
            formatList.clear();
            view.setVisibility(GONE);
        }
    }


    /**
     * 下方服务器获取的数据适配器加载(单个)
     *
     * @param isModeMode
     * @param sss_adapter
     * @param i
     */
    private void reShowDataServer(boolean isModeMode, SSS_Adapter sss_adapter, int i) {
        if (isModeMode) {
            sss_adapter.setList(list.get(i).list);
        } else {
            List<String> temp = new ArrayList<>();
            for (int k = 0; k < 6; k++) {
                temp.add(list.get(i).list.get(k));
            }
            sss_adapter.setList(temp);
        }
    }


    /**
     * 初始化下方统计视图
     *
     * @param context
     */
    private void goodsTotal(final Context context) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_goods_total, null);
            InnerListview listview_list_goods_total = $.f(view, R.id.listview_list_goods_total);
            TextView set_list_goods_total = $.f(view, R.id.set_list_goods_total);
            set_list_goods_total.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(context);
                }
            });
            totalAdapter = new SSS_Adapter<TotalModel>(context, R.layout.item_list_goods_total, formatList) {
                @Override
                protected void setView(SSS_HolderHelper helper, final int position, TotalModel bean, SSS_Adapter instance) {
                    helper.setText(R.id.title_item_list_goods_total, bean.title);
                    ((EditText) helper.getView(R.id.price_item_list_goods_total)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!"".equals(s.toString())) {
                                formatList.get(position).price =s.toString();
                            }
                        }
                    });

                    ((EditText) helper.getView(R.id.num_item_list_goods_total)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!"".equals(s.toString())) {
                                formatList.get(position).number =s.toString();
                            }

                        }
                    });
                    if (!"".equals(bean.price)) {
                        ((EditText) helper.getView(R.id.price_item_list_goods_total)).setText(bean.price);
                    }
                    if (!"".equals(bean.number)) {
                        ((EditText) helper.getView(R.id.num_item_list_goods_total)).setText(bean.number);
                    }

                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {

                }
            };
            listview_list_goods_total.setAdapter(totalAdapter);

            view.setVisibility(GONE);
            this.addView(view);
        }
    }


    /**
     * 批量设置对话框
     *
     * @param context
     */
    private void createDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.RcDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_set_goods_type_size_info, null);
        final EditText price_dialog_set_goods_type_size_info = $.f(view, R.id.price_dialog_set_goods_type_size_info);
        final EditText number_dialog_set_goods_type_size_info = $.f(view, R.id.number_dialog_set_goods_type_size_info);
        TextView yes_dialog_set_goods_type_size_info = $.f(view, R.id.yes_dialog_set_goods_type_size_info);
        TextView cancel_dialog_set_goods_type_size_info = $.f(view, R.id.cancel_dialog_set_goods_type_size_info);
        yes_dialog_set_goods_type_size_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(price_dialog_set_goods_type_size_info.getText().toString().trim()) && !"".equals(number_dialog_set_goods_type_size_info.getText().toString().trim())) {
                    for (int i = 0; i < formatList.size(); i++) {
                        formatList.get(i).number = number_dialog_set_goods_type_size_info.getText().toString().trim();
                        formatList.get(i).price = price_dialog_set_goods_type_size_info.getText().toString().trim();
                        totalAdapter.setList(formatList);
                    }
                    dialog.dismiss();
                } else {
                    ToastUtils.showShortToast(context, "请填写价格/库存信息");
                }

            }
        });
        cancel_dialog_set_goods_type_size_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}

