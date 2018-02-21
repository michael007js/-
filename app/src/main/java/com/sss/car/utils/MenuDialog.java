package com.sss.car.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.EditText.NumberSelectEdit;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.customwidget.Menu.popMenu.MenuItemEntity;
import com.blankj.utilcode.customwidget.Menu.popMenu.UIPopupMenu;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.dao.QRCodeDataListener;
import com.blankj.utilcode.dao.SharePostDetailsBottomDialogCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ChooseType;
import com.sss.car.dao.CollectBottomDialogCallaback;
import com.sss.car.dao.OnExistsShopCallBack;
import com.sss.car.dao.OnIntegralCallBack;
import com.sss.car.dao.OnOneKeyReadCallBack;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.dao.OnUserInfoMenuCallBack;
import com.sss.car.dao.ShoppingCartCallBack;
import com.sss.car.model.GoodsChooseModel;
import com.sss.car.model.GoodsChooseSizeName;
import com.sss.car.model.GoodsChooseSizeName_Model;
import com.sss.car.model.UserinfoModel;
import com.sss.car.view.ActivityCreateGroupInviteGroupSend;
import com.sss.car.view.ActivityFeedback;
import com.sss.car.view.ActivityMyDataSetPassword;
import com.sss.car.view.ActivityPublishDymaic;
import com.sss.car.view.ActivityPublishPost;
import com.sss.car.view.ActivityReport;
import com.sss.car.view.ActivitySearchAddFriend;
import com.sss.car.view.ActivitySearchGoodsShopUserListPublic;
import com.sss.car.view.ActivityShareCollect;
import com.sss.car.view.ActivitySharePostMy;
import com.sss.car.view.ActivityUserInfo;
import com.sss.car.view.Main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by leilei on 2017/8/28.
 */

@SuppressWarnings("ALL")
public class MenuDialog implements QRCodeDataListener {
    UIPopupMenu uiPopupMenu;
    YWLoadingDialog ywLoadingDialog;
    SharePostDetailsBottomDialogCallBack sharePostDetailsBottomDialogCallBack;

    List<BottomSheetDialog> dialogList = new ArrayList<>();
    List<MenuItemEntity> list;
    Activity activity;
    SPUtils spUtils;

    public void clear() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (dialogList != null) {
            for (int i = 0; i < dialogList.size(); i++) {
                if (dialogList.get(i) != null) {
                    dialogList.get(i).dismiss();
                }
            }
        }
        dialogList = null;
        if (uiPopupMenu != null) {
            uiPopupMenu.dismiss();
        }
        uiPopupMenu = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        activity = null;
        sharePostDetailsBottomDialogCallBack = null;
        spUtils = null;
    }

    public MenuDialog(Activity activity) {
        activity.setTheme(com.blankj.utilcode.R.style.windowIsNotTranslucent);
        list = new ArrayList<>();
    }

    /**
     * 商品页菜单
     *
     * @param view
     * @param activity
     * @return
     */
    public UIPopupMenu createGoodsMenu(View view, final Activity activity, final String type, final String ids) {
        this.activity = activity;
        if (list != null) {
            list.clear();
        }
        if (uiPopupMenu != null) {
            uiPopupMenu.dismiss();
        }
        uiPopupMenu = null;
        this.uiPopupMenu = new UIPopupMenu(activity);
        list.add(new MenuItemEntity(-1, "首页"));
        list.add(new MenuItemEntity(-1, "反馈"));
        list.add(new MenuItemEntity(-1, "分享"));
        uiPopupMenu
                .setAnimationEnable(false)
                .setClickDismissible(true)
                .setAlpha(0.5f)
                .setMargin(0, 0, 0, 0)
                .setMenuItems(list)
                .setOnMenuItemClickListener(new UIPopupMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        switch (position) {
                            case 0:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, Main.class));
                                break;
                            case 1:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivityFeedback.class));
                                break;
                            case 2:
                                ShareUtils.prepareShare(ywLoadingDialog, activity, type, ids);
                                break;


                        }
                    }
                })
                .showAsDropDown(view, -50, 0);

        return uiPopupMenu;
    }


    /**
     * 车品车服菜单
     *
     * @param view
     * @param activity
     * @return
     */
    public UIPopupMenu createMainRightMenu(View view, final Activity activity, final OnExistsShopCallBack onExistsShopCallBack) {
        this.activity = activity;
        if (list != null) {
            list.clear();
        }
        if (uiPopupMenu != null) {
            uiPopupMenu.dismiss();
        }
        uiPopupMenu = null;
        this.uiPopupMenu = new UIPopupMenu(activity);
        list.add(new MenuItemEntity(-1, "分享平台"));
        list.add(new MenuItemEntity(-1, "添加朋友"));
        list.add(new MenuItemEntity(-1, "添加商品"));
        list.add(new MenuItemEntity(-1, "扫一扫"));
        list.add(new MenuItemEntity(-1, "建议反馈"));
        uiPopupMenu
                .setAnimationEnable(false)
                .setClickDismissible(true)
                .setAlpha(0.5f)
                .setMargin(0, 0, 0, 0)
                .setMenuItems(list)
                .setOnMenuItemClickListener(new UIPopupMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        switch (position) {
                            case 0:
                                ShareUtils.prepareShare(ywLoadingDialog, activity, "terrace", Config.member_id);
                                break;
                            case 1:
//                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivitySearchAddFriend.class));
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivitySearchGoodsShopUserListPublic.class)
                                        .putExtra("type", "3")
                                        .putExtra("keywords", ""));
                                break;
                            case 2:
                                if (onExistsShopCallBack != null) {
                                    onExistsShopCallBack.onExists();
                                }
                                break;
                            case 3:
                                APPOftenUtils.startQRScanView(MenuDialog.this.activity, MenuDialog.this);
                                break;
                            case 4:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivityFeedback.class));
                                break;

                        }
                    }
                })
                .showAsDropDown(view,  -50, 0);

        return uiPopupMenu;
    }

    /**
     * 消息菜单
     *
     * @param view
     * @param activity
     * @return
     */
    public UIPopupMenu createMessageMenu(View view, final Activity activity, final OnOneKeyReadCallBack onOneKeyReadCallBack) {
        this.activity = activity;
        if (list != null) {
            list.clear();
        }
        if (uiPopupMenu != null) {
            uiPopupMenu.dismiss();
        }
        uiPopupMenu = null;
        this.uiPopupMenu = new UIPopupMenu(activity);
        list.add(new MenuItemEntity(-1, "添加朋友"));
        list.add(new MenuItemEntity(-1, "扫一扫"));
        list.add(new MenuItemEntity(-1, "创建新聊天"));
        list.add(new MenuItemEntity(-1, "群发助手"));
        list.add(new MenuItemEntity(-1, "创建群组"));
        list.add(new MenuItemEntity(-1, "建议反馈"));
        list.add(new MenuItemEntity(-1, "一键已读"));
        uiPopupMenu
                .setAnimationEnable(false)
                .setClickDismissible(true)
                .setAlpha(0.5f)
                .setMargin(0, 0, 0, 0)
                .setMenuItems(list)
                .setOnMenuItemClickListener(new UIPopupMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        switch (position) {
                            case 0:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivitySearchGoodsShopUserListPublic.class)
                                        .putExtra("type", "3")
                                        .putExtra("keywords", ""));
//                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivitySearchAddFriend.class));
                                break;
                            case 1:
                                APPOftenUtils.startQRScanView(MenuDialog.this.activity, MenuDialog.this);
                                break;
                            case 2:
                                activity.startActivity(new Intent(activity, ActivityCreateGroupInviteGroupSend.class).putExtra("type", ActivityCreateGroupInviteGroupSend.create_private));
                                break;
                            case 3:
                                activity.startActivity(new Intent(activity, ActivityCreateGroupInviteGroupSend.class).putExtra("type", ActivityCreateGroupInviteGroupSend.send));
                                break;
                            case 4:
                                activity.startActivity(new Intent(activity, ActivityCreateGroupInviteGroupSend.class).putExtra("type", ActivityCreateGroupInviteGroupSend.create_group));
                                break;
                            case 5:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivityFeedback.class));
                                break;
                            case 6:
                                if (onOneKeyReadCallBack != null) {
                                    onOneKeyReadCallBack.onOneKeyRead();
                                }
                                break;
                        }
                    }
                })
                .showAsDropDown(view,  -50, 0);

        return uiPopupMenu;
    }

    /**
     * 分享菜单
     *
     * @param view
     * @param activity
     * @return
     */
    public UIPopupMenu createShare(View view, final Activity activity) {
        this.activity = activity;
        if (list != null) {
            list.clear();
        }
        if (uiPopupMenu != null) {
            uiPopupMenu.dismiss();
        }
        uiPopupMenu = null;
        this.uiPopupMenu = new UIPopupMenu(activity);
        list.add(new MenuItemEntity(-1, "立刻分享"));
        list.add(new MenuItemEntity(-1, "立刻发帖"));
        list.add(new MenuItemEntity(-1, "我的帖子"));
        list.add(new MenuItemEntity(-1, "我的收藏"));
        list.add(new MenuItemEntity(-1, "建议反馈"));
        uiPopupMenu
                .setAnimationEnable(false)
                .setClickDismissible(true)
                .setAlpha(0.5f)
                .setMargin(0, 0, 0, 0)
                .setMenuItems(list)
                .setOnMenuItemClickListener(new UIPopupMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        switch (position) {
                            case 0:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivityPublishDymaic.class));
                                break;
                            case 1:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivityPublishPost.class));
                                break;
                            case 2:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivitySharePostMy.class));
                                break;
                            case 3:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivityShareCollect.class));
                                break;
                            case 4:
                                MenuDialog.this.activity.startActivity(new Intent(MenuDialog.this.activity, ActivityFeedback.class));
                                break;
                        }
                    }
                })
                .showAsDropDown(view,  -50, 0);

        return uiPopupMenu;
    }

    /**
     * 快递公司菜单
     *
     * @param context
     */
    public BottomSheetDialog createExpressBottomDialog(final Context context, SSS_Adapter sss_adapter) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_express, null);
        TextView cancel = $.f(view, R.id.cancel);
        ListView listview = $.f(view, R.id.listview);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        listview.setAdapter(sss_adapter);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        return bottomSheetDialog;
    }


    /**
     * 推广菜单
     *
     * @param context
     */
    public BottomSheetDialog createPopularizeDialog(final Context context, SSS_Adapter sss_adapter) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_popularize, null);
        TextView cancel = $.f(view, R.id.cancel);
        ListView listview = $.f(view, R.id.listview);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        listview.setAdapter(sss_adapter);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        return bottomSheetDialog;
    }


    /**
     * 社区文章详情右上角
     *
     * @param context
     * @param member_id
     * @param username
     * @param sharePostDetailsBottomDialogCallBack
     */
    public void createSharePostDetailsBottomDialog(final Context context, final String member_id, final String username, SharePostDetailsBottomDialogCallBack sharePostDetailsBottomDialogCallBack) {
        this.sharePostDetailsBottomDialogCallBack = sharePostDetailsBottomDialogCallBack;

        if (spUtils == null) {
            spUtils = new SPUtils(context, Config.defaultFileName, Context.MODE_PRIVATE);
        }

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share_post_details_bottom, null);
        LinearLayout click_userinfo_dialog_share_post_details_bottom = $.f(view, R.id.click_userinfo_dialog_share_post_details_bottom);
        LinearLayout click_report_dialog_share_post_details_bottom = $.f(view, R.id.click_report_dialog_share_post_details_bottom);
        final Switch small_dialog_share_post_details_bottom = $.f(view, R.id.small_dialog_share_post_details_bottom);
        final Switch night_dialog_share_post_details_bottom = $.f(view, R.id.night_dialog_share_post_details_bottom);

        final TextView text_small_dialog_share_post_details_bottom = $.f(view, R.id.text_small_dialog_share_post_details_bottom);
        final TextView text_middle_dialog_share_post_details_bottom = $.f(view, R.id.text_middle_dialog_share_post_details_bottom);
        final TextView text_big_dialog_share_post_details_bottom = $.f(view, R.id.text_big_dialog_share_post_details_bottom);
        TextView cancel_big_dialog_share_post_details_bottom = $.f(view, R.id.cancel_big_dialog_share_post_details_bottom);


        small_dialog_share_post_details_bottom.setChecked(spUtils.getBoolean("small_text_image" + Config.account));
        night_dialog_share_post_details_bottom.setChecked(spUtils.getBoolean("night" + Config.account));
        switch (spUtils.getInt("text_size" + Config.account)) {

            case -1:
                text_small_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.white));
                text_middle_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_big_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_small_dialog_share_post_details_bottom.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_dialog_text));
                text_middle_dialog_share_post_details_bottom.setBackground(null);
                text_big_dialog_share_post_details_bottom.setBackground(null);
                break;
            case 0:
                text_small_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_middle_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.white));
                text_big_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_small_dialog_share_post_details_bottom.setBackground(null);
                text_middle_dialog_share_post_details_bottom.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_dialog_text));
                text_big_dialog_share_post_details_bottom.setBackground(null);
                break;
            case 1:
                text_small_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_middle_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_big_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.white));
                text_small_dialog_share_post_details_bottom.setBackground(null);
                text_middle_dialog_share_post_details_bottom.setBackground(null);
                text_big_dialog_share_post_details_bottom.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_dialog_text));
                break;
        }


        cancel_big_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });


        text_small_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                spUtils.put("text_size", -1);

                if (MenuDialog.this.sharePostDetailsBottomDialogCallBack != null) {
                    MenuDialog.this.sharePostDetailsBottomDialogCallBack.onTextSize(-1);
                }
                text_small_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.white));
                text_middle_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_big_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_small_dialog_share_post_details_bottom.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_dialog_text));
                text_middle_dialog_share_post_details_bottom.setBackground(null);
                text_big_dialog_share_post_details_bottom.setBackground(null);
            }
        });

        text_middle_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                spUtils.put("text_size", 0);
                if (MenuDialog.this.sharePostDetailsBottomDialogCallBack != null) {
                    MenuDialog.this.sharePostDetailsBottomDialogCallBack.onTextSize(0);
                    text_small_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                    text_middle_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.white));
                    text_big_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                    text_small_dialog_share_post_details_bottom.setBackground(null);
                    text_middle_dialog_share_post_details_bottom.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_dialog_text));
                    text_big_dialog_share_post_details_bottom.setBackground(null);
                }
            }
        });

        text_big_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                spUtils.put("text_size", 1);
                if (MenuDialog.this.sharePostDetailsBottomDialogCallBack != null) {
                    MenuDialog.this.sharePostDetailsBottomDialogCallBack.onTextSize(1);
                }
                text_small_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_middle_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.black));
                text_big_dialog_share_post_details_bottom.setTextColor(context.getResources().getColor(R.color.white));
                text_small_dialog_share_post_details_bottom.setBackground(null);
                text_middle_dialog_share_post_details_bottom.setBackground(null);
                text_big_dialog_share_post_details_bottom.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_dialog_text));
            }
        });

        night_dialog_share_post_details_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bottomSheetDialog.dismiss();
                spUtils.put("night", isChecked);
                if (MenuDialog.this.sharePostDetailsBottomDialogCallBack != null) {
                    MenuDialog.this.sharePostDetailsBottomDialogCallBack.onNight(isChecked);
                }
            }
        });

        small_dialog_share_post_details_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bottomSheetDialog.dismiss();
                spUtils.put("small_text_image", isChecked);
                if (MenuDialog.this.sharePostDetailsBottomDialogCallBack != null) {
                    MenuDialog.this.sharePostDetailsBottomDialogCallBack.onSmallTextAndImg(isChecked);
                }
            }
        });

        click_report_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                context.startActivity(new Intent(context, ActivityReport.class)
                        .putExtra("id", member_id)
                        .putExtra("type", "private"));
            }
        });
        click_userinfo_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                context.startActivity(new Intent(context, ActivityUserInfo.class)
                        .putExtra("id", member_id));
            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }


    /**
     * 收藏页长按列表底部弹出
     *
     * @param context
     * @param collectBottomDialogCallaback
     */
    public void createCollectBottomDialog(final Context context, final CollectBottomDialogCallaback collectBottomDialogCallaback) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_collect_bottom, null);
        LinearLayout click_transmit_userinfo_dialog_share_post_details_bottom = $.f(view, R.id.click_transmit_userinfo_dialog_share_post_details_bottom);
        LinearLayout click_edit_userinfo_dialog_share_post_details_bottom = $.f(view, R.id.click_edit_userinfo_dialog_share_post_details_bottom);
        LinearLayout click_delete_userinfo_dialog_share_post_details_bottom = $.f(view, R.id.click_delete_userinfo_dialog_share_post_details_bottom);
        LinearLayout click_more_userinfo_dialog_share_post_details_bottom = $.f(view, R.id.click_more_userinfo_dialog_share_post_details_bottom);
        click_transmit_userinfo_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (collectBottomDialogCallaback != null) {
                    collectBottomDialogCallaback.onTransmit();
                }
            }
        });
        click_edit_userinfo_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (collectBottomDialogCallaback != null) {
                    collectBottomDialogCallaback.onEdit();
                }
            }
        });
        click_delete_userinfo_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (collectBottomDialogCallaback != null) {
                    collectBottomDialogCallaback.onDetete();
                }
            }
        });
        click_more_userinfo_dialog_share_post_details_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (collectBottomDialogCallaback != null) {
                    collectBottomDialogCallaback.onMore();
                }
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }


    /**
     * 商品编辑页列表框
     *
     * @param context
     */
    public BottomSheetDialog creasteGoodsServiceEditPublic(final Context context, SSS_Adapter sss_adapter) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_goods_service_publish, null);
        ListView listView = $.f(view, R.id.listview);
        TextView cancel = $.f(view, R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        listView.setAdapter(sss_adapter);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        return bottomSheetDialog;
    }


    /**
     * 点击商品详情页与店铺页优惠券按钮从底部弹出所支持的优惠券弹窗
     *
     * @param context
     */
    public void createGoodsBottomCouponDialog(Context context, SSS_Adapter sss_adapter) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_coupons, null);
        InnerListview listview_dialog_coupons = $.f(view, R.id.listview_dialog_coupons);
        TextView cancel_dialog_coupons = $.f(view, R.id.cancel_dialog_coupons);
        listview_dialog_coupons.setAdapter(sss_adapter);
        cancel_dialog_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }


    /**
     * 点击商品详情页选择商品属性从底部弹出
     *
     * @param context
     */
    int num = 1;
    int price = 0;
    JSONArray jsonArray = new JSONArray();

    public void createGoodsBottomDialog(String where,int number, final Context context, int numColumns, final GoodsChooseModel goodsChooseModel, final ShoppingCartCallBack shoppingCartCallBack) {
        num = 1;
        price = 0;
        jsonArray = null;
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_shop_info_bottom, null);
        SimpleDraweeView pic_dialog_choose_shop_info_bottom = $.f(view, R.id.pic_dialog_choose_shop_info_bottom);
        TextView title_dialog_choose_shop_info_bottom = $.f(view, R.id.title_dialog_choose_shop_info_bottom);
        ImageView close_dialog_choose_shop_info_bottom = $.f(view, R.id.close_dialog_choose_shop_info_bottom);
        final TextView price_dialog_choose_shop_info_bottom = $.f(view, R.id.price_dialog_choose_shop_info_bottom);
        ChooseType choose_type_dialog_choose_shop_info_bottom = $.f(view, R.id.choose_type_dialog_choose_shop_info_bottom);
        final NumberSelectEdit numberSelectEdit_dialog_choose_shop_info_bottom = $.f(view, R.id.numberSelectEdit_dialog_choose_shop_info_bottom);
        TextView add_dialog_choose_shop_info_bottom = $.f(view, R.id.add_dialog_choose_shop_info_bottom);
        TextView subscribe_dialog_choose_shop_info_bottom = $.f(view, R.id.subscribe_dialog_choose_shop_info_bottom);
        final TextView m_dialog_choose_shop_info_bottom = $.f(view, R.id.m_dialog_choose_shop_info_bottom);
        TextView save_dialog_choose_shop_info_bottom = $.f(view, R.id.save_dialog_choose_shop_info_bottom);
        title_dialog_choose_shop_info_bottom.setText(goodsChooseModel.title);

        if ("cart".equals(where)) {
            add_dialog_choose_shop_info_bottom.setVisibility(View.GONE);
            subscribe_dialog_choose_shop_info_bottom.setVisibility(View.GONE);
            save_dialog_choose_shop_info_bottom.setVisibility(View.VISIBLE);
        } else if ("goods".equals(where)) {
            add_dialog_choose_shop_info_bottom.setVisibility(View.VISIBLE);
            subscribe_dialog_choose_shop_info_bottom.setVisibility(View.VISIBLE);
            save_dialog_choose_shop_info_bottom.setVisibility(View.GONE);
        }

        numberSelectEdit_dialog_choose_shop_info_bottom
                .init(context, true)
                .setCurrentNumber(1)
                .withKeyBoard(false)
                .isLongClick(true)
                .defaultNumber(number)
                .minNumber(1)
                .setNumberSelectEditOperationCakkBack(new NumberSelectEdit.NumberSelectEditOperationCakkBack() {
                    @Override
                    public void onAdd(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        num = numberSelectEdit.getCurrentNumber();
                        price_dialog_choose_shop_info_bottom.setText(String.valueOf(price * num));
                    }

                    @Override
                    public void onSubtract(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        num = numberSelectEdit.getCurrentNumber();
                        price_dialog_choose_shop_info_bottom.setText(String.valueOf(price * num));
                    }

                    @Override
                    public void onZero(NumberSelectEdit numberSelectEdit) {
                        num = numberSelectEdit.getCurrentNumber();
                        price_dialog_choose_shop_info_bottom.setText(String.valueOf(price * num));
                    }

                    @Override
                    public void onEditChanged(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        num = numberSelectEdit.getCurrentNumber();
                        price_dialog_choose_shop_info_bottom.setText(String.valueOf( num*price));
                    }
                });

        if (goodsChooseModel.picture.size() > 0) {
            FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + goodsChooseModel.picture.get(0)), pic_dialog_choose_shop_info_bottom, 0f);
        }

        choose_type_dialog_choose_shop_info_bottom.setChooseTypeCallBack(new ChooseType.ChooseTypeCallBack() {
            @Override
            public void onChooseType(List<GoodsChooseSizeName> list, GoodsChooseSizeName GoodsChooseSizeName, GoodsChooseSizeName_Model GoodsChooseSizeName_DataModel) {
                m_dialog_choose_shop_info_bottom.setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder();
                jsonArray = null;
                jsonArray = new JSONArray();
                try {
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list.get(i).data.size(); j++) {
                            if (list.get(i).data.get(j).isChoose) {
                                sb.append(list.get(i).data.get(j).name);
                                jsonArray.put(new JSONObject().put("title", list.get(i).title).put("name", list.get(i).data.get(j).name));
                                if (i < list.size() - 1) {
                                    sb.append("+");
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonArray.length() == list.size()) {
                    for (int i = 0; i < goodsChooseModel.size_dat.size(); i++) {
                        if (sb.toString().equals(goodsChooseModel.size_dat.get(i).name)) {
                            if (StringUtils.isEmpty(goodsChooseModel.size_dat.get(i).price)) {
                                price = 0;
                            } else {
                                price = Integer.parseInt(goodsChooseModel.size_dat.get(i).price);
                            }

                            price_dialog_choose_shop_info_bottom.setText(String.valueOf(price * num));
                        }
                    }
                }
                com.blankj.utilcode.util.LogUtils.e(jsonArray.toString());

            }


        }).create(context, numColumns, goodsChooseModel.size_name);

        close_dialog_choose_shop_info_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        add_dialog_choose_shop_info_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (shoppingCartCallBack != null) {
                    shoppingCartCallBack.onShoppingCartCallBack(price, num, goodsChooseModel.size_name, jsonArray, "cart");////添加购物车type=cart, 添加立即下单，type=order
                }
            }
        });
        subscribe_dialog_choose_shop_info_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (shoppingCartCallBack != null) {
                    shoppingCartCallBack.onShoppingCartCallBack(price, num, goodsChooseModel.size_name, jsonArray, "order");////添加购物车type=cart, 添加立即下单，type=order
                }
            }
        });
        save_dialog_choose_shop_info_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (shoppingCartCallBack != null) {
                    shoppingCartCallBack.onShoppingCartCallBack(price, num, goodsChooseModel.size_name, jsonArray, "save");////添加购物车type=cart, 添加立即下单，type=order
                }
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                numberSelectEdit_dialog_choose_shop_info_bottom.clear();
            }
        });
        bottomSheetDialog.show();
    }


    /**
     * 服务预购单/实物预购单点击优惠端后底部弹出的优惠券框
     */
    public void createCouponBottomDialog(Context context, SSS_Adapter sss_adapter) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_coupon, null);
        InnerListview listview_dialog_coupon = $.f(view, R.id.listview_dialog_coupon);
        TextView cancel_dialog_coupon = $.f(view, R.id.cancel_dialog_coupon);
        listview_dialog_coupon.setAdapter(sss_adapter);
        cancel_dialog_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }


    /**
     * 服务预购单/实物预购单点击违约金后底部弹出的选择框
     */
    public void createIntegrityBottomDialog(String title, Context context, SSS_Adapter sss_adapter) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_coupon, null);
        InnerListview listview_dialog_coupon = $.f(view, R.id.listview_dialog_coupon);
        TextView title_dialog_coupon = $.f(view, R.id.title_dialog_coupon);
        TextView cancel_dialog_coupon = $.f(view, R.id.cancel_dialog_coupon);
        title_dialog_coupon.setText(title);
        listview_dialog_coupon.setAdapter(sss_adapter);
        cancel_dialog_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }

    /**
     * 弹出支付选择框
     *
     * @param title
     * @param activity
     */
    public void createPaymentDialog(final YWLoadingDialog ywLoadingDialog, final String title, final Activity activity, final OnPayPasswordVerificationCallBack onPayPasswordVerificationCallBack) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_payment_bottom, null);
        TextView close_dialog_payment_bottom = $.f(view, R.id.close_dialog_payment_bottom);
        TextView money_dialog_payment_bottom = $.f(view, R.id.money_dialog_payment_bottom);
        TextView click_dialog_payment_bottom = $.f(view, R.id.click_dialog_payment_bottom);
        TextView score_dialog_payment_bottom_score = $.f(view, R.id.score_dialog_payment_bottom_score);
        TextView cancel_dialog_payment_bottom_score = $.f(view, R.id.cancel_dialog_payment_bottom_score);
        TextView click_next_dialog_payment_bottom = $.f(view, R.id.click_next_dialog_payment_bottom);
        final CheckBox cb_balance_dialog_payment_bottom = $.f(view, R.id.cb_balance_dialog_payment_bottom);
        final CheckBox cb_wx_dialog_payment_bottom = $.f(view, R.id.cb_wx_dialog_payment_bottom);
        final CheckBox cb_zfb_dialog_payment_bottom = $.f(view, R.id.cb_zfb_dialog_payment_bottom);
        CheckBox cb_score_dialog_payment_bottom_score = $.f(view, R.id.cb_score_dialog_payment_bottom_score);
        final LinearLayout parent_dialog_payment_bottom_score = $.f(view, R.id.parent_dialog_payment_bottom_score);

        click_dialog_payment_bottom.setText("使用积分抵扣");
        close_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        click_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_dialog_payment_bottom_score.setVisibility(View.VISIBLE);
            }
        });
        cancel_dialog_payment_bottom_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_dialog_payment_bottom_score.setVisibility(View.GONE);
            }
        });
        click_next_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (cb_balance_dialog_payment_bottom.isChecked()) {
                    P.e(ywLoadingDialog, Config.member_id, activity, new P.p() {
                        @Override
                        public void exist() {
                            createPasswordInputDialog(title, activity, onPayPasswordVerificationCallBack);
                        }

                        @Override
                        public void nonexistence() {
                            if (activity != null) {
                                activity.startActivity(new Intent(activity, ActivityMyDataSetPassword.class));
                            }
                        }
                    });

                } else if (cb_wx_dialog_payment_bottom.isChecked()) {
                    bottomSheetDialog.dismiss();
                } else if (cb_zfb_dialog_payment_bottom.isChecked()) {
                    bottomSheetDialog.dismiss();
                }

            }
        });

        cb_balance_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_wx_dialog_payment_bottom.setChecked(false);
                    cb_zfb_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_wx_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_balance_dialog_payment_bottom.setChecked(false);
                    cb_zfb_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_zfb_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_balance_dialog_payment_bottom.setChecked(false);
                    cb_wx_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_score_dialog_payment_bottom_score.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                parent_dialog_payment_bottom_score.setVisibility(View.GONE);
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }


    /**
     * 弹出支付选择框
     *
     * @param title
     * @param activity
     */
    String integral = "0";
    String mode = "balance";//支付方式：balance余额，ali_pay支付，we_chat_pay微信支付

    public void createPaymentOrderDialog(final YWLoadingDialog ywLoadingDialog, final String title, String price, final Activity activity, final OnIntegralCallBack onIntegralCallBack, final OnPayPasswordVerificationCallBack onPayPasswordVerificationCallBack) {
        integral = "0";
        mode = "balance";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_payment_bottom, null);
        TextView close_dialog_payment_bottom = $.f(view, R.id.close_dialog_payment_bottom);
        TextView money_dialog_payment_bottom = $.f(view, R.id.money_dialog_payment_bottom);
        TextView title_dialog_payment_bottom = $.f(view, R.id.title_dialog_payment_bottom);
        TextView click_dialog_payment_bottom = $.f(view, R.id.click_dialog_payment_bottom);
        TextView score_dialog_payment_bottom_score = $.f(view, R.id.score_dialog_payment_bottom_score);
        TextView click_next_dialog_payment_bottom = $.f(view, R.id.click_next_dialog_payment_bottom);
        final CheckBox cb_balance_dialog_payment_bottom = $.f(view, R.id.cb_balance_dialog_payment_bottom);
        final CheckBox cb_wx_dialog_payment_bottom = $.f(view, R.id.cb_wx_dialog_payment_bottom);
        final CheckBox cb_zfb_dialog_payment_bottom = $.f(view, R.id.cb_zfb_dialog_payment_bottom);
        title_dialog_payment_bottom.setText(title);
        click_dialog_payment_bottom.setVisibility(View.GONE);
        money_dialog_payment_bottom.setText(price);
        close_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        click_next_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onIntegralCallBack != null) {
                    onIntegralCallBack.onIntegralCallBack(integral, mode);
                }
                bottomSheetDialog.dismiss();
                if (cb_balance_dialog_payment_bottom.isChecked()) {
                    P.e(ywLoadingDialog, Config.member_id, activity, new P.p() {
                        @Override
                        public void exist() {
                            createPasswordInputDialog(title, activity, onPayPasswordVerificationCallBack);
                        }

                        @Override
                        public void nonexistence() {
                            if (activity != null) {
                                activity.startActivity(new Intent(activity, ActivityMyDataSetPassword.class));
                            }
                        }
                    });

                } else if (cb_wx_dialog_payment_bottom.isChecked()) {
                    bottomSheetDialog.dismiss();
                } else if (cb_zfb_dialog_payment_bottom.isChecked()) {
                    bottomSheetDialog.dismiss();
                }

            }
        });

        cb_balance_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_wx_dialog_payment_bottom.setChecked(false);
                    cb_zfb_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_wx_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_balance_dialog_payment_bottom.setChecked(false);
                    cb_zfb_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_zfb_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_balance_dialog_payment_bottom.setChecked(false);
                    cb_wx_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }


    public void createPaymentTOrderDialog(YWLoadingDialog ywLoadingDialog, final String title, final String price, final Activity activity, final OnIntegralCallBack onIntegralCallBack, final OnPayPasswordVerificationCallBack onPayPasswordVerificationCallBack) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(activity);
        ywLoadingDialog.show();

        try {
            final YWLoadingDialog finalYwLoadingDialog = ywLoadingDialog;
            final YWLoadingDialog finalYwLoadingDialog1 = ywLoadingDialog;
            new RequestModel(System.currentTimeMillis() + "", RequestWeb.can_integral(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (finalYwLoadingDialog != null) {
                                finalYwLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (finalYwLoadingDialog != null) {
                                finalYwLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    final String a = jsonObject.getJSONObject("data").getString("deduction");
                                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
                                    if (dialogList != null) {
                                        dialogList.add(bottomSheetDialog);
                                    }
                                    View view = LayoutInflater.from(activity).inflate(R.layout.dialog_payment_bottom, null);
                                    TextView close_dialog_payment_bottom = $.f(view, R.id.close_dialog_payment_bottom);
                                    TextView money_dialog_payment_bottom = $.f(view, R.id.money_dialog_payment_bottom);
                                    TextView title_dialog_payment_bottom = $.f(view, R.id.title_dialog_payment_bottom);
                                    TextView click_dialog_payment_bottom = $.f(view, R.id.click_dialog_payment_bottom);
                                    TextView cancel_dialog_payment_bottom_score = $.f(view, R.id.cancel_dialog_payment_bottom_score);
                                    final CheckBox cb_score_dialog_payment_bottom_score = $.f(view, R.id.cb_score_dialog_payment_bottom_score);
                                    final LinearLayout parent_dialog_payment_bottom_score = $.f(view, R.id.parent_dialog_payment_bottom_score);
                                    TextView score_dialog_payment_bottom_score = $.f(view, R.id.score_dialog_payment_bottom_score);
                                    TextView click_next_dialog_payment_bottom = $.f(view, R.id.click_next_dialog_payment_bottom);
                                    final CheckBox cb_balance_dialog_payment_bottom = $.f(view, R.id.cb_balance_dialog_payment_bottom);
                                    final CheckBox cb_wx_dialog_payment_bottom = $.f(view, R.id.cb_wx_dialog_payment_bottom);
                                    final CheckBox cb_zfb_dialog_payment_bottom = $.f(view, R.id.cb_zfb_dialog_payment_bottom);
                                    title_dialog_payment_bottom.setText(title);
                                    money_dialog_payment_bottom.setText(price);
                                    click_dialog_payment_bottom.setText("使用积分抵扣");
                                    score_dialog_payment_bottom_score.setText(a + "");
                                    click_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (View.VISIBLE == parent_dialog_payment_bottom_score.getVisibility()) {
                                                parent_dialog_payment_bottom_score.setVisibility(View.GONE);
                                            } else {
                                                parent_dialog_payment_bottom_score.setVisibility(View.VISIBLE);
                                            }

                                        }
                                    });
                                    cancel_dialog_payment_bottom_score.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            parent_dialog_payment_bottom_score.setVisibility(View.GONE);
                                            integral = "0";
                                            cb_score_dialog_payment_bottom_score.setChecked(false);
                                        }
                                    });
                                    cb_score_dialog_payment_bottom_score.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                integral = a;
                                                parent_dialog_payment_bottom_score.setVisibility(View.GONE);
                                            } else {
                                                integral = "0";
                                            }
                                        }
                                    });
                                    close_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            bottomSheetDialog.dismiss();
                                        }
                                    });
                                    cb_balance_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                cb_wx_dialog_payment_bottom.setChecked(false);
                                                cb_zfb_dialog_payment_bottom.setChecked(false);
                                                mode = "balance";
                                            }
                                        }
                                    });
                                    cb_wx_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                cb_balance_dialog_payment_bottom.setChecked(false);
                                                cb_zfb_dialog_payment_bottom.setChecked(false);
                                                mode = "we_chat_pay";
                                            }
                                        }
                                    });
                                    cb_zfb_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                cb_balance_dialog_payment_bottom.setChecked(false);
                                                cb_wx_dialog_payment_bottom.setChecked(false);
                                                mode = "ali_pay";
                                            }
                                        }
                                    });

                                    click_next_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (cb_balance_dialog_payment_bottom.isChecked()) {
                                                P.e(finalYwLoadingDialog1, Config.member_id, activity, new P.p() {
                                                    @Override
                                                    public void exist() {
                                                        if (onIntegralCallBack != null) {
                                                            onIntegralCallBack.onIntegralCallBack(integral, mode);
                                                        }
                                                        bottomSheetDialog.dismiss();
                                                        createPasswordInputDialog(title, activity, onPayPasswordVerificationCallBack);
                                                    }

                                                    @Override
                                                    public void nonexistence() {
                                                        if (activity != null) {
                                                            activity.startActivity(new Intent(activity, ActivityMyDataSetPassword.class));
                                                        }
                                                    }
                                                });

                                            } else if (cb_wx_dialog_payment_bottom.isChecked()) {
                                                if (onIntegralCallBack != null) {
                                                    onIntegralCallBack.onIntegralCallBack(integral, mode);
                                                }
                                                bottomSheetDialog.dismiss();
                                            } else if (cb_zfb_dialog_payment_bottom.isChecked()) {
                                                if (onIntegralCallBack != null) {
                                                    onIntegralCallBack.onIntegralCallBack(integral, mode);
                                                }
                                                bottomSheetDialog.dismiss();
                                            }

                                        }
                                    });

                                    bottomSheetDialog.setContentView(view);
                                    bottomSheetDialog.setCanceledOnTouchOutside(false);
                                    bottomSheetDialog.show();

                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    }));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }


    public interface OnPaymentCallBack {
        void onVerificationPassword(String password, PassWordKeyboard passWordKeyboard, String payMode, String is_integral, BottomSheetDialog bottomSheetDialog);
    }

    /**
     * 弹出支付密码输入框
     *
     * @param title
     * @param activity
     */
    String payMode;
    String is_integral = "0";

    public void createPaymentDialog(final YWLoadingDialog ywLoadingDialog, final String title, final int money, int score, final Activity activity, final OnPaymentCallBack onPaymentCallBack) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_payment_bottom, null);
        TextView close_dialog_payment_bottom = $.f(view, R.id.close_dialog_payment_bottom);
        TextView money_dialog_payment_bottom = $.f(view, R.id.money_dialog_payment_bottom);
        TextView click_dialog_payment_bottom = $.f(view, R.id.click_dialog_payment_bottom);
        TextView title_dialog_payment_bottom = $.f(view, R.id.title_dialog_payment_bottom);
        TextView score_dialog_payment_bottom_score = $.f(view, R.id.score_dialog_payment_bottom_score);
        TextView cancel_dialog_payment_bottom_score = $.f(view, R.id.cancel_dialog_payment_bottom_score);
        TextView click_next_dialog_payment_bottom = $.f(view, R.id.click_next_dialog_payment_bottom);
        final CheckBox cb_balance_dialog_payment_bottom = $.f(view, R.id.cb_balance_dialog_payment_bottom);
        final CheckBox cb_wx_dialog_payment_bottom = $.f(view, R.id.cb_wx_dialog_payment_bottom);
        final CheckBox cb_zfb_dialog_payment_bottom = $.f(view, R.id.cb_zfb_dialog_payment_bottom);
        CheckBox cb_score_dialog_payment_bottom_score = $.f(view, R.id.cb_score_dialog_payment_bottom_score);
        final LinearLayout parent_dialog_payment_bottom_score = $.f(view, R.id.parent_dialog_payment_bottom_score);
        score_dialog_payment_bottom_score.setText(score + "");
        money_dialog_payment_bottom.setText("¥" + money + ".00");
        if (StringUtils.isEmpty(title)) {
            title_dialog_payment_bottom.setText(title);
        }
        click_dialog_payment_bottom.setText("使用积分抵扣");
        close_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        click_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_dialog_payment_bottom_score.setVisibility(View.VISIBLE);
            }
        });
        cancel_dialog_payment_bottom_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_dialog_payment_bottom_score.setVisibility(View.GONE);
            }
        });
        click_next_dialog_payment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (cb_balance_dialog_payment_bottom.isChecked()) {
                    P.e(ywLoadingDialog, Config.member_id, activity, new P.p() {
                        @Override
                        public void exist() {
                            createPasswordInputDialog("请输入您的支付密码", activity, new OnPayPasswordVerificationCallBack() {
                                @Override
                                public void onVerificationPassword(String password, PassWordKeyboard passWordKeyboard, BottomSheetDialog bottomSheetDialog) {
                                    if (onPaymentCallBack != null) {
                                        onPaymentCallBack.onVerificationPassword(password, passWordKeyboard, payMode, is_integral, bottomSheetDialog);
                                    }
                                }

                            });
                        }

                        @Override
                        public void nonexistence() {
                            if (activity != null) {
                                activity.startActivity(new Intent(activity, ActivityMyDataSetPassword.class));
                            }
                        }
                    });

                } else if (cb_wx_dialog_payment_bottom.isChecked()) {
                    bottomSheetDialog.dismiss();
                } else if (cb_zfb_dialog_payment_bottom.isChecked()) {
                    bottomSheetDialog.dismiss();
                }

            }
        });

        cb_balance_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payMode = "balance";
                    cb_wx_dialog_payment_bottom.setChecked(false);
                    cb_zfb_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_wx_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payMode = "we_chat_pay";
                    cb_balance_dialog_payment_bottom.setChecked(false);
                    cb_zfb_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_zfb_dialog_payment_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payMode = "ali_pay";
                    cb_balance_dialog_payment_bottom.setChecked(false);
                    cb_wx_dialog_payment_bottom.setChecked(false);
                }
            }
        });
        cb_score_dialog_payment_bottom_score.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_integral = "1";
                } else {
                    is_integral = "0";
                }
                parent_dialog_payment_bottom_score.setVisibility(View.GONE);
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }


    /**
     * 弹出密码输入框
     *
     * @param title
     * @param activity
     * @param onPayPasswordVerificationCallBack
     */
    public void createPasswordInputDialog(String title, final Activity activity, final OnPayPasswordVerificationCallBack onPayPasswordVerificationCallBack) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        if (dialogList != null) {
            dialogList.add(bottomSheetDialog);
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_password_input, null);
        final PassWordKeyboard PassWordKeyboard = $.f(view, R.id.PassWordKeyboard);
        PassWordKeyboard
                .title(title)
                .titleColor(activity.getResources().getColor(R.color.mainColor))
                .setColor(activity.getResources().getColor(R.color.mainColor))
                .setLoadingDraw(activity, R.mipmap.logo_loading)
                .overridePendingTransition(activity)
                .customFunction("")
                .setOnPassWordKeyboardCallBack(new PassWordKeyboard.OnPassWordKeyboardCallBack() {
                    @Override
                    public void onPassword(final String pasword) {
                        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (onPayPasswordVerificationCallBack != null) {
                                    onPayPasswordVerificationCallBack.onVerificationPassword(pasword, PassWordKeyboard, bottomSheetDialog);
                                }
                            }
                        });
                        bottomSheetDialog.dismiss();

                    }

                    @Override
                    public void onFinish() {
                        bottomSheetDialog.dismiss();
                    }

                    @Override
                    public void onCustomFunction() {
                    }
                });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }


    public void createUserRightMenu(UserinfoModel userinfoModel, Context context, final OnUserInfoMenuCallBack onUserInfoMenuCallBack) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_userinfo, null);
        TextView remark = $.f(view, R.id.remark);
        TextView line_remark = $.f(view, R.id.line_remark);
        TextView specialAttention = $.f(view, R.id.special_attention);
        TextView share = $.f(view, R.id.share);
        TextView disturb = $.f(view, R.id.disturb);
        TextView privacy = $.f(view, R.id.privacy);
        TextView report = $.f(view, R.id.report);
        TextView black = $.f(view, R.id.black);
        TextView delete = $.f(view, R.id.delete);
        TextView line_delete = $.f(view, R.id.line_delete);
        if ("1".equals(userinfoModel.is_special)) {
            specialAttention.setText("取消特别关注");
        } else {
            specialAttention.setText("添加特别关注");
        }

        if ("0".equals(userinfoModel.status)) {//0什么关系也不是
            remark.setVisibility(View.GONE);
            line_remark.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            line_delete.setVisibility(View.GONE);
        } else {
            if ("3".equals(userinfoModel.status)) {
                black.setText("取消拉黑TA");
            } else {
                black.setText("拉黑TA");
            }
        }


        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (onUserInfoMenuCallBack != null) {
                    onUserInfoMenuCallBack.remark();
                }
            }
        });
        specialAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (onUserInfoMenuCallBack != null) {
                    onUserInfoMenuCallBack.specialAttention();
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (onUserInfoMenuCallBack != null) {
                    onUserInfoMenuCallBack.share();
                }
            }
        });
        disturb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (onUserInfoMenuCallBack != null) {
                    onUserInfoMenuCallBack.disturb();
                }
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (onUserInfoMenuCallBack != null) {
                    onUserInfoMenuCallBack.privacy();
                }
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (onUserInfoMenuCallBack != null) {
                    onUserInfoMenuCallBack.report();
                }
            }
        });
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (onUserInfoMenuCallBack != null) {
                    onUserInfoMenuCallBack.black();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (onUserInfoMenuCallBack != null) {
                    onUserInfoMenuCallBack.delete();
                }
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }


    @Override
    public void onQRCodeDataChange(String data, Context baseContext) {
        QRUtils.start(data,activity,ywLoadingDialog);
    }
}
