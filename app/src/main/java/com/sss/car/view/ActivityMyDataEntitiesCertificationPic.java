package com.sss.car.view;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.EntitiesCertificationPicListAdapter;
import com.sss.car.dao.TouchItemTouchHelper;
import com.sss.car.dao.TouchItemTouchHelperCallBack;
import com.sss.car.model.DataEntitiesCertificationPicModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;

import static com.sss.car.Config.member_id;


/**
 * Created by leilei on 2017/8/21.
 */

public class ActivityMyDataEntitiesCertificationPic extends BaseActivity implements LoadImageCallBack, TouchItemTouchHelperCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.delete_activity_my_data_entities_certification_pic)
    LinearLayout deleteActivityMyDataEntitiesCertificationPic;
    @BindView(R.id.activity_my_data_entities_certification_pic)
    FrameLayout activityMyDataEntitiesCertificationPic;
    ChangeInfoModel changeUserInfoModel;
    List<DataEntitiesCertificationPicModel> list = new ArrayList<>();
    YWLoadingDialog ywLoadingDialog;
    JSONArray jSONArray = new JSONArray();

    @BindView(R.id.pic_list_list_activity_my_data_entities_certification_pic)
    RecyclerView picListListActivityMyDataEntitiesCertificationPic;
    EntitiesCertificationPicListAdapter entitiesCertificationPicListAdapter;
    ItemTouchHelper itemTouchHelper;
    TouchItemTouchHelper touchItemTouchHelper;
    /*删除DIALOG是否已经创建*/
    boolean isCreate = false;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        jSONArray = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        changeUserInfoModel = null;
        deleteActivityMyDataEntitiesCertificationPic = null;
        activityMyDataEntitiesCertificationPic = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (entitiesCertificationPicListAdapter!=null){
            entitiesCertificationPicListAdapter.clear();
        }
        entitiesCertificationPicListAdapter=null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_entities_certification_pic);
        ButterKnife.bind(this);
        customInit(activityMyDataEntitiesCertificationPic, false, true, false);
        rightButtonTop.setText("上传");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        titleTop.setText("店铺图片");
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误error-1");
            finish();
        }
        if (StringUtils.isEmpty(getIntent().getExtras().getString("auth_id"))){
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误error-2");
            finish();
        }


        entitiesCertificationPicListAdapter = new EntitiesCertificationPicListAdapter(getBaseActivityContext(),
                getWindowManager().getDefaultDisplay().getWidth() / 2,
                list,
                this);
        picListListActivityMyDataEntitiesCertificationPic.setAdapter(entitiesCertificationPicListAdapter);
        picListListActivityMyDataEntitiesCertificationPic.setHasFixedSize(true);
        touchItemTouchHelper = new TouchItemTouchHelper(this);
        itemTouchHelper = new ItemTouchHelper(touchItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(picListListActivityMyDataEntitiesCertificationPic);
        picListListActivityMyDataEntitiesCertificationPic.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        getCompanyPic();

    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                createPhotoDialog();
                break;
        }
    }


    /**
     * 加载图片回调
     *
     * @param imageView
     */
    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }


    /**
     * 创建照片选择框
     */
    void createPhotoDialog() {
        APPOftenUtils.createPhotoChooseDialog(8, 9, weakReference, MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList == null || resultList.size() == 0) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "照片选择失败");
                } else {
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    ywLoadingDialog = null;
                    if (getBaseActivityContext() != null) {
                        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                    }
                    ywLoadingDialog.show();
                    for (int i = 0; i < resultList.size(); i++) {
                        try {
                            jSONArray.put(i,
                                    ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(resultList.get(i).getPhotoPath(), 480, 960)))
                            ;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    setcompany(jSONArray);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                ToastUtils.showShortToast(getBaseActivityContext(), errorMsg);
            }
        });
    }

    /**
     * 获取图片信息
     */
    void getCompanyPic() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        }
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getCompanyPic(
                    new JSONObject()
                            .put("member_id", member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        list.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            list.add(new DataEntitiesCertificationPicModel(jsonArray.getJSONObject(i).getString("id"),
                                                    jsonArray.getJSONObject(i).getString("path"),
                                                    jsonArray.getJSONObject(i).getString("type"),
                                                    jsonArray.getJSONObject(i).getString("picture_id")
                                            ));
                                        }
                                        entitiesCertificationPicListAdapter.refresh(list);
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay_pic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay_pic-0");
            }
            e.printStackTrace();
        }
    }

    /**
     * 获取图片信息
     * @param pos
     */
    void delCompanyPic(int pos) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        }
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.delCompanyPic(
                    new JSONObject()
                            .put("member_id", member_id)
                            .put("auth_id",getIntent().getExtras().getString("auth_id"))
                            .put("picture_id",list.get(pos).id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        getCompanyPic();
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay_pic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay_pic-0");
            }
            e.printStackTrace();
        }
    }

    /**
     * 上传
     */
    void setcompany(final JSONArray send) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        }
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.uploadCompanyPic(
                    new JSONObject()
                            .put("shop_picture", send)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        getCompanyPic();
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay-0");
            }
            e.printStackTrace();
        }
    }


    /**
     * RecyclerView调用onDraw时调用，如果想自定义item对用户互动的响应,可以重写该方法
     **/
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        LogUtils.e(dY + "onChildDraw" + (recyclerView.getHeight() - viewHolder.itemView.getBottom() + deleteActivityMyDataEntitiesCertificationPic.getHeight()));
        if (dY > 100 && dY >= (recyclerView.getHeight() - viewHolder.itemView.getBottom() + deleteActivityMyDataEntitiesCertificationPic.getHeight()))//item底部距离recyclerView顶部高度
        {
            if (isCreate == false) {
                isCreate = true;
                createDeleteDialog(viewHolder.getAdapterPosition());
            }

        }
    }

    /**
     * 设置手指离开后ViewHolder的动画时间，在用户手指离开后调用
     */
    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType,float animateDx, float animateDy) {
        LogUtils.e("getAnimationDuration");
        return 100;
    }


    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        deleteActivityMyDataEntitiesCertificationPic.setVisibility(View.VISIBLE);
        LogUtils.e("onSelectedChanged");

    }


    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        deleteActivityMyDataEntitiesCertificationPic.setVisibility(View.GONE);
        LogUtils.e("clearView");

    }


    /**
     * 创建删除对话框
     */

    void createDeleteDialog(final int pos) {
        if (getBaseActivityContext() == null) {
            return;
        }
        String[] stringItems = {"删除"};
        if (getBaseActivityContext() != null) {
            final ActionSheetDialog dialog = new ActionSheetDialog(getBaseActivityContext(), stringItems, null)
                    .isTitleShow(true)
                    .itemTextColor(Color.parseColor("#e83e41"))
                    .setmCancelBgColor(Color.parseColor("#e83e41"))
                    .cancelText(Color.WHITE);
            dialog.title("是否要删除您的爱车");
            dialog.titleTextSize_SP(14.5f).show();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    isCreate = false;
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isCreate=false;
                }
            });
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            delCompanyPic(pos);
                            dialog.dismiss();
                            break;
                    }
                }
            });
        }
    }

}
