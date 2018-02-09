package com.sss.car.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.QRCodeDataListener;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 我的二维码
 * Created by leilei on 2017/12/9.
 */

public class ActivityQR extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    ImageView rightButtonTop;
    @BindView(R.id.pic_qr)
    ImageView picQr;
    @BindView(R.id.name_qr)
    TextView nameQr;
    @BindView(R.id.id_qr)
    TextView idQr;
    @BindView(R.id.qr_pic_qr)
    SimpleDraweeView qrPicQr;
    @BindView(R.id.activity_qr)
    LinearLayout activityQr;
    YWLoadingDialog ywLoadingDialog;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showLongToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        customInit(activityQr, false, true, false);
        nameQr.setText(getIntent().getExtras().getString("username"));
        idQr.setText("用户ID:" + getIntent().getExtras().getString("id"));
        picQr.setTag(R.id.glide_tag, Config.url+getIntent().getExtras().getString("pic"));
        addImageViewList(GlidUtils.downLoader(false,picQr,getBaseActivityContext()));

        addImageViewList(FrescoUtils.showImage(false,300,300, Uri.parse(Config.url+getIntent().getExtras().getString("qr")),qrPicQr,0f));

//        qrPicQr.setTag(R.id.glide_tag, Config.url+getIntent().getExtras().getString("qr"));
//        addImageViewList(GlidUtils.downLoader(false,qrPicQr,getBaseActivityContext()));
//        qr_code();
    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                createPhotoChooseDialog();
                break;
        }
    }

    private void createPhotoChooseDialog() {
        String[] stringItems = {"保存图片", "扫一扫"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getBaseActivityContext(), stringItems, null)
                .isTitleShow(false)
                .itemTextColor(Color.parseColor("#e83e41"))
                .setmCancelBgColor(Color.parseColor("#e83e41"))
                .cancelText(Color.WHITE);
        dialog.title("选择您的方式");
        dialog.titleTextSize_SP(14.5f).show();
        stringItems = null;
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dialog.dismiss();
                        if (APPOftenUtils.saveImageToGallery(getBaseActivityContext(), BitmapUtils.getBitmapFromImageView(qrPicQr))){
                            ToastUtils.showShortToast(getBaseActivityContext(),"保存成功");
                        }else {
                            ToastUtils.showShortToast(getBaseActivityContext(),"保存失败");
                        }
                        break;
                    case 1:
                        dialog.dismiss();
                        APPOftenUtils.startQRScanView(getBaseActivityContext(), new QRCodeDataListener() {
                            @Override
                            public void onQRCodeDataChange(String data, Context baseContext) {
                                LogUtils.e(data);
                                ToastUtils.showLongToast(getBaseActivityContext(),data+"");
                            }
                        });
                        break;
                }
            }
        });

    }


    /**
     * 获取二维码
     */
    public void qr_code() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.qr_code(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }
}
