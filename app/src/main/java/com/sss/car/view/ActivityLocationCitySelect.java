package com.sss.car.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.activity.ActivityInputKeyboard;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.bilibili_search_box.utils.KeyBoardUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentCitySelectListView;
import com.sss.car.fragment.FragmentCitySelectSearchListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 地址选择器
 * Created by leilei on 2017/11/8.
 */

public class ActivityLocationCitySelect extends BaseActivity {

    @BindView(R.id.back_top_image)
    LinearLayout backTopImage;
    @BindView(R.id.right_search_top_image)
    LinearLayout rightSearchTopImage;
    @BindView(R.id.activity_location_city_select)
    LinearLayout activityLocationCitySelect;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.editText)
    EditText editText;

    FragmentCitySelectListView fragmentCitySelectListView;

    FragmentCitySelectSearchListView fragmentCitySelectSearchListView;
    boolean normalMode = true;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        KeyBoardUtils.closeKeyboard(getBaseActivityContext(), editText);
        backTopImage = null;
        rightSearchTopImage = null;
        activityLocationCitySelect = null;
        if (KeyboardUtils.isSoftShowing(this)) {
            KeyBoardUtils.closeKeyboard(this, editText);
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_city_select);
        ButterKnife.bind(this);
        customInit(activityLocationCitySelect, false, true, false, false);
        fragmentCitySelectListView = new FragmentCitySelectListView();
        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentCitySelectListView, R.id.parent_city_select);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (StringUtils.isEmpty(editText.getText().toString().trim())) {
                        if (normalMode == false) {
                            normalMode = true;
                            FragmentUtils.hideAllShowFragment(fragmentCitySelectListView);
                        }
                    } else {
                        if (normalMode == true) {
                            normalMode = false;
                            if (fragmentCitySelectSearchListView == null) {
                                fragmentCitySelectSearchListView = new FragmentCitySelectSearchListView();
                                FragmentUtils.addFragment(getSupportFragmentManager(), fragmentCitySelectSearchListView, R.id.parent_city_select);
                            }
                            FragmentUtils.hideAllShowFragment(fragmentCitySelectSearchListView);
                            fragmentCitySelectSearchListView.search_city(editText.getText().toString().trim());
                        }

                    }
            }
        });
    }

    @OnClick({R.id.back_top_image, R.id.right_search_top_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top_image:
                finish();
                break;
            case R.id.right_search_top_image:
//                if (!StringUtils.isEmpty(editText.getText().toString().trim())) {
//                    normalMode = false;
//                    if (fragmentCitySelectSearchListView == null) {
//                        fragmentCitySelectSearchListView = new FragmentCitySelectSearchListView();
//                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentCitySelectSearchListView, R.id.parent_city_select);
//                    }
//                    FragmentUtils.hideAllShowFragment(fragmentCitySelectSearchListView);
//                    fragmentCitySelectSearchListView.search_city(editText.getText().toString().trim());
//                }

                break;
        }
    }


}
