package com.blankj.utilcode.zxing.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.dao.QRCodeDataListener;
import com.blankj.utilcode.util.ActivityManagerUtils;
import com.blankj.utilcode.zxing.decode.DecodeThread;


public class ResultActivity extends Activity {
    private QRCodeDataListener mSSS_QRCodeDataListener;
    private ImageView mResultImage;
    private TextView mResultText, mResultTextYes;
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        final Bundle extras = getIntent().getExtras();
        mResultImage = (ImageView) findViewById(R.id.result_image);
        mResultText = (TextView) findViewById(R.id.result_text);
        mResultTextYes = (TextView) findViewById(R.id.result_yes);
        if (null != extras) {
            int width = extras.getInt("width");
            int height = extras.getInt("height");

            LayoutParams lps = new LayoutParams(width, height);
            lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
            lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

            mResultImage.setLayoutParams(lps);

            result = extras.getString("result");
            mResultText.setText(result);

            Bitmap barcode = null;
            byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                // Mutable copy:
                barcode = barcode.copy(Bitmap.Config.RGB_565, true);
            }

            mResultImage.setImageBitmap(barcode);
        }
        mResultTextYes.setOnClickListener(new View.OnClickListener() {

                                              @Override
                                              public void onClick(View v) {

                                                  mSSS_QRCodeDataListener.onQRCodeDataChange(result, getBaseContext());

                                              }
                                          }
        );


        ActivityManagerUtils.getActivityManager().addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagerUtils.getActivityManager().finishActivity(this);
    }

    public void setSSS_QRCodeDataListener(QRCodeDataListener listener){
        mSSS_QRCodeDataListener=listener;
    }

    public QRCodeDataListener getmSSS_QRCodeDataListener() {
        return mSSS_QRCodeDataListener;
    }
}
