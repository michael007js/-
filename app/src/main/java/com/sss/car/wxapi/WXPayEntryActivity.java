package com.sss.car.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.ChangedCouponModel;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.EventBusModel.ChangedPopularizeModel;
import com.sss.car.EventBusModel.ChangedWalletModel;
import com.sss.car.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import static com.sss.car.R.id.code;


//必须实现接口，重写方法
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wxfa869f12af50587a", false);
        api.registerApp("wxfa869f12af50587a");
        api.handleIntent(getIntent(), this);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.e("onNewIntent");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
                break;
            default:
                break;
        }

    }

    @Override
    public void onResp(BaseResp baseResp) {

        int code = baseResp.errCode;

        if (code == 0) {
            LogUtils.e("onResp" + code);
            ToastUtils.showShortToast(this, "支付成功");
            EventBus.getDefault().post(new ChangedOrderModel());
            EventBus.getDefault().post(new ChangedOrderModel());
            EventBus.getDefault().post(new ChangedCouponModel());
            EventBus.getDefault().post(new ChangedPopularizeModel());
            EventBus.getDefault().post(new ChangedWalletModel());
            finish();
            //显示充值成功的页面和需要的操作
        }

        if (code == -1) {
            //错误
            ToastUtils.showShortToast(this, "支付失败");
            finish();
        }

        if (code == -2) {
            ToastUtils.showShortToast(this, "支付取消");
            finish();
            //用户取消
        }

    }


    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer();
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);

    }
}