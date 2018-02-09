package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.model.DataEntitiesCertificationModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by leilei on 2017/8/19.
 */

public class ActivityMyDataEntitiesCertificationBusinessLicense extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.pic_activity_my_data_entities_certification_business_license)
    ImageView picActivityMyDataEntitiesCertificationBusinessLicense;
    @BindView(R.id.submit_activity_my_data_entities_certification_business_license)
    TextView submitActivityMyDataEntitiesCertificationBusinessLicense;
    @BindView(R.id.activity_my_data_entities_certification_business_license)
    LinearLayout activityMyDataEntitiesCertificationBusinessLicense;
    String base64;
    ChangeInfoModel changeUserInfoModel;
    YWLoadingDialog ywLoadingDialog;   DataEntitiesCertificationModel dataEntitiesCertificationModel;
    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;changeUserInfoModel=null;
        dataEntitiesCertificationModel=null;
         backTop=null;
         titleTop=null;
         rightButtonTop=null;
         picActivityMyDataEntitiesCertificationBusinessLicense=null;
         submitActivityMyDataEntitiesCertificationBusinessLicense=null;
         activityMyDataEntitiesCertificationBusinessLicense=null;
         base64=null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_entities_certification_business_license);
        ButterKnife.bind(this);
        customInit(activityMyDataEntitiesCertificationBusinessLicense,false,true,false);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传输错误");
            finish();
        }

        dataEntitiesCertificationModel= (DataEntitiesCertificationModel) getIntent().getExtras().get("extra");
        if (dataEntitiesCertificationModel==null|| StringUtils.isEmpty(dataEntitiesCertificationModel.business_license)){

        }else {
            LogUtils.e( Config.url+dataEntitiesCertificationModel.business_license+"+++");
            picActivityMyDataEntitiesCertificationBusinessLicense.setTag(R.id.glide_tag, Config.url+dataEntitiesCertificationModel.business_license);
                addImageViewList(GlidUtils.downLoader(false,picActivityMyDataEntitiesCertificationBusinessLicense,getBaseActivityContext()));
        }
        titleTop.setText("营业执照");
    }

    @OnClick({R.id.back_top, R.id.pic_activity_my_data_entities_certification_business_license, R.id.submit_activity_my_data_entities_certification_business_license})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.pic_activity_my_data_entities_certification_business_license:
                createPhotoDialog();
                break;
            case R.id.submit_activity_my_data_entities_certification_business_license:
                if (StringUtils.isEmpty(base64)){
                    ToastUtils.showShortToast(getBaseActivityContext(),"您未选中任何照片");
                }else {
                    changeUserInfoModel=new ChangeInfoModel();
                    changeUserInfoModel.type=getIntent().getExtras().getString("type");
                    changeUserInfoModel.msg=base64;
                    EventBus.getDefault().post(changeUserInfoModel);
                    finish();
                }
                break;
        }
    }


    /**
     * 创建照片选择框
     */
    void createPhotoDialog(){
        APPOftenUtils.createPhotoChooseDialog(8, 1, weakReference, MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList==null||resultList.size()==0){
                    ToastUtils.showShortToast(getBaseActivityContext(),"照片选择失败");
                }else {
                    base64=resultList.get(0).getPhotoPath();
                    picActivityMyDataEntitiesCertificationBusinessLicense.setTag(R.id.glide_tag,resultList.get(0).getPhotoPath());
                    addImageViewList(GlidUtils.downLoader(false,picActivityMyDataEntitiesCertificationBusinessLicense,getBaseActivityContext()));
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                    ToastUtils.showShortToast(getBaseActivityContext(),errorMsg);
            }
        });
    }
}
