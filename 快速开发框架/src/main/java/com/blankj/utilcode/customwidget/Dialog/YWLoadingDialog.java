package com.blankj.utilcode.customwidget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.R;


/**
 * 加载dialog
 * @author Driver
 * @version V1.0
 * @Date 2015-04-01
 */
public class YWLoadingDialog extends Dialog {
	private ImageView iv_load_result;// 加载的结果图标显示
	private TextView tv_load;// 加载的文字展示
	private ProgressBar pb_loading;// 加载中的图片
	private final int LOAD_SUCC = 0x001;
	private final int LOAD_FAIL = 0x002;
//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//				case LOAD_SUCC:
//					dismiss();
//					break;
//				case LOAD_FAIL:
//					dismiss();
//					break;
//				default:
//					break;
//			}
//		};
//	};
	public YWLoadingDialog(Context context) {
		super(context, R.style.YWLoadingDialogTheme);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commom_loading_layout);
		setCanceledOnTouchOutside(false);//防止触碰到阴影地方关闭
		iv_load_result = (ImageView) findViewById(R.id.iv_load_result);
		tv_load = (TextView) findViewById(R.id.tv_load);
		pb_loading = (ProgressBar) findViewById(R.id.pb_loading);

	}
	// 加载成功
	public void dimissSuc(String str) {
		if (pb_loading==null||tv_load==null/*||mHandler==null*/||iv_load_result==null){
			return;
		}
		pb_loading.setVisibility(View.GONE);
		iv_load_result.setVisibility(View.VISIBLE);
		tv_load.setText(str);
		iv_load_result.setImageResource(R.drawable.load_suc_icon);
		dismiss();
//		mHandler.sendEmptyMessageDelayed(LOAD_SUCC, 2000);
	}
	// 加载失败
	public void dimissFail(String str) {
		if (pb_loading==null||tv_load==null/*||mHandler==null*/||iv_load_result==null){
			return;
		}
		pb_loading.setVisibility(View.GONE);
		iv_load_result.setVisibility(View.VISIBLE);
		tv_load.setText(str);
		iv_load_result.setImageResource(R.drawable.load_fail_icon);
		dismiss();
//		mHandler.sendEmptyMessageDelayed(LOAD_FAIL, 2000);
	}

	//立即关闭
	public void disMiss(){
		dismiss();
	}

	//立即设置标题
	public void setTitle(String str){
		if (tv_load==null){
			return;
		}
		tv_load.setText(str);
	}
}

