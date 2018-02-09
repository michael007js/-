package com.blankj.utilcode.customwidget.EditText;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.R;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.CountDownTimerUtils;
import com.blankj.utilcode.util.DigitalUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;

import static android.R.attr.maxLength;


/**
 * Created by leilei on 2017/8/11.
 */

public class NumberSelectEdit extends LinearLayout {
    /*操作回调*/
    NumberSelectEditOperationCakkBack mNumberSelectEditOperationCakkBack;
    /*加减控件*/
    TextView mAdd, mSubtract;
    /*编辑框控件*/
    EditText mEditText;
    /*当前计数*/
    int mCurrentNumber = 0;
    /*允许负数*/
    boolean mIsNegativeNumber = false;
    /*用来区分长按时加或减*/
    boolean mIsAdd = true;
    /*长按计时事件*/
    CountDownTimerUtils mCountDownTimerUtils;

    int minNumber = 0;

    int mEditPadding = 5;
    int mAddPadding = 5;
    int mSubtractPadding = 5;
    int mThisPadding = 5;

    int mWidth = 100;


    float mTextSize = 13f;

    /*允许长按*/
    boolean mEnableLongClick = false;

    boolean mIsEditMode = false;

    Context mContext;
    View mView;


    public NumberSelectEdit(Context context) {
        super(context);
    }

    public NumberSelectEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberSelectEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     *
     * @param context
     */
    public NumberSelectEdit init(Context context, boolean isEditMode) {
        mContext = context;
        mView= LayoutInflater.from(context).inflate(R.layout.number_select_edit,null);
        mIsEditMode = isEditMode;
        mSubtract = $.f(mView,R.id.subtract_item_listview_order_service_goods_list_adapter);
        mEditText  = $.f(mView,R.id.edit_item_listview_order_service_goods_list_adapter);
        mAdd = $.f(mView,R.id.add_item_listview_order_service_goods_list_adapter);
        initViews();
        initShortListener();
        return this;
    }

    /**
     * 初始化视图
     */
    void initViews() {
        this.setGravity(Gravity.CENTER);
        mEditText.setTextSize(mTextSize);
        mEditText.setGravity(Gravity.CENTER);
        mEditText.setText(mCurrentNumber + "");
        mEditText.setMaxWidth(mWidth);
        mEditText.setEnabled(mIsEditMode);
        mEditText.setFocusable(mIsEditMode);
        mEditText.setTextColor(Color.parseColor("#000000"));
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        APPOftenUtils.setBackgroundOfVersion(mEditText, null);
        mEditText.setBackgroundDrawable(null);
        mEditText.setPadding(mEditPadding, mEditPadding, mEditPadding, mEditPadding);
        mSubtract.setPadding(mSubtractPadding, mSubtractPadding, mSubtractPadding, mSubtractPadding);
        mAdd.setPadding(mAddPadding, mAddPadding, mAddPadding, mAddPadding);



        addView(mView);
    }

    /**
     * 初始化点击监听
     */
    void initShortListener() {
        if (mIsEditMode) {
            mSubtract.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    subtract();
                }
            });
            mAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    add();
                }
            });
        }

        initLongListener();
    }

    /**
     * 初始化长按监听
     */
    void initLongListener() {
        if (mIsEditMode) {
            if (mEnableLongClick) {
                mSubtract.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mIsAdd = false;
                        longClickEvent();
                        return true;
                    }
                });

                mAdd.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mIsAdd = true;
                        longClickEvent();
                        return true;
                    }
                });

                mSubtract.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP:
                                if (mCountDownTimerUtils != null) {
                                    mCountDownTimerUtils.cancel();
                                }
                                break;
                        }
                        return false;
                    }
                });

                mAdd.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP:
                                if (mCountDownTimerUtils != null) {
                                    mCountDownTimerUtils.cancel();
                                }
                                break;
                        }
                        return false;
                    }
                });
            }
            mEditText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIsEditMode) {
                        mEditText.setEnabled(mIsEditMode);
                        mEditText.setFocusable(mIsEditMode);
                        KeyboardUtils.showSoftInput(mContext, mEditText);
                    }
                }
            });
        }
        if (mEditText!=null){
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (StringUtils.isEmpty(mEditText.getText().toString())){
                        mEditText.setText("0");
                    }
                    if (mNumberSelectEditOperationCakkBack != null) {
                        mCurrentNumber = Integer.valueOf(mEditText.getText().toString());
                        mNumberSelectEditOperationCakkBack.onEditChanged(NumberSelectEdit.this, mCurrentNumber);
                    }

                }
            });
        }


    }


    /**
     * 初始值
     *
     * @param number
     */
    public NumberSelectEdit defaultNumber(int number) {
        this.mCurrentNumber = number;
        if (mEditText!=null){
            mEditText.setText(this.mCurrentNumber + "");
        }
        return this;
    }

    /**
     * 最小值
     *
     * @param number
     */
    public NumberSelectEdit minNumber(int number) {
        this.minNumber = number;
        return this;
    }

    /**
     * 最大宽度
     *
     * @param Width
     */
    public NumberSelectEdit maxWidth(int Width) {
        this.mWidth = Width;
        return this;
    }

    /**
     * 允许负数
     *
     * @param isNegativeNumber
     * @return
     */
    public NumberSelectEdit isNegativeNumber(boolean isNegativeNumber) {
        this.mIsNegativeNumber = isNegativeNumber;
        return this;
    }

    /**
     * 允许长按事件
     *
     * @param enableLongClick
     * @return
     */
    public NumberSelectEdit isLongClick(boolean enableLongClick) {
        this.mEnableLongClick = enableLongClick;
        initLongListener();
        return this;
    }

    /**
     * 设置当前数量
     *
     * @param mCurrentNumber
     */
    public NumberSelectEdit setCurrentNumber(int mCurrentNumber) {
        this.mCurrentNumber = mCurrentNumber;
        if (mEditText != null) {
            mEditText.setText(this.mCurrentNumber + "");
        }
        return this;
    }

    /**
     * 获取当前数量
     *
     * @return
     */
    public int getCurrentNumber() {
        return mCurrentNumber;
    }




    /**
     * 本控件的padding值
     *
     * @param thisPadding
     * @return
     */
    public NumberSelectEdit parentPadding(int thisPadding) {
        if (thisPadding < 0) {
            this.mThisPadding = 5;
        } else {
            this.mThisPadding = thisPadding;
        }
        setPadding(mThisPadding, mThisPadding, mThisPadding, mThisPadding);
        return this;
    }

    /**
     * 减号的padding值
     *
     * @param subtractPadding
     * @return
     */
    public NumberSelectEdit subtractPadding(int subtractPadding) {
        if (subtractPadding < 0) {
            this.mSubtractPadding = 5;
        } else {
            this.mSubtractPadding = subtractPadding;
        }
        mSubtract.setPadding(mSubtractPadding, mSubtractPadding, mSubtractPadding, mSubtractPadding);
        return this;
    }

    /**
     * 加号的padding值
     *
     * @param addPadding
     * @return
     */
    public NumberSelectEdit addPadding(int addPadding) {
        this.mAddPadding = addPadding;
        if (addPadding < 0) {
            this.mAddPadding = 5;
        } else {
            this.mAddPadding = addPadding;
        }
        mAdd.setPadding(mAddPadding, mAddPadding, mAddPadding, mAddPadding);
        return this;
    }

    /**
     * 编辑框的padding值
     *
     * @param editPadding
     * @return
     */
    public NumberSelectEdit editPadding(int editPadding) {
        this.mEditPadding = editPadding;
        if (editPadding < 0) {
            this.mEditPadding = 5;
        } else {
            this.mEditPadding = editPadding;
        }
        mEditText.setPadding(mEditPadding, mEditPadding, mEditPadding, mEditPadding);
        return this;
    }

    /**
     * 加
     */
    void add() {
        mCurrentNumber++;
        mEditText.setText(String.valueOf(mCurrentNumber));
        if (mNumberSelectEditOperationCakkBack != null) {
            mNumberSelectEditOperationCakkBack.onAdd(NumberSelectEdit.this, mCurrentNumber);
        }
    }

    /**
     * 减
     */
    void subtract() {
        if (mCurrentNumber > minNumber) {
            mCurrentNumber--;
            if (mIsNegativeNumber) {
                mCurrentNumber = minNumber;
                if (mNumberSelectEditOperationCakkBack != null) {
                    mNumberSelectEditOperationCakkBack.onZero(NumberSelectEdit.this);
                }
            }
            mEditText.setText(String.valueOf(mCurrentNumber));
            if (mNumberSelectEditOperationCakkBack != null) {
                mNumberSelectEditOperationCakkBack.onSubtract(NumberSelectEdit.this, mCurrentNumber);
            }
        }
    }


    /**
     * 长按事件
     */
    void longClickEvent() {
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.onFinish();
            mCountDownTimerUtils.cancel();
        }
        mCountDownTimerUtils = null;
        mCountDownTimerUtils = new CountDownTimerUtils(Long.MAX_VALUE, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mIsAdd) {
                    add();
                } else {
                    subtract();
                }
            }

            @Override
            public void onFinish() {

            }
        };
        mCountDownTimerUtils.start();

    }

    /**
     * 清理各种内存占用
     */
    public void clear() {
        mNumberSelectEditOperationCakkBack = null;
        mEditText = null;
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.cancel();
        }
        mCountDownTimerUtils = null;
        this.removeAllViews();
    }

    /**
     * 操作回调
     *
     * @param numberSelectEditOperationCakkBack
     * @return
     */
    public NumberSelectEdit setNumberSelectEditOperationCakkBack(NumberSelectEditOperationCakkBack numberSelectEditOperationCakkBack) {
        this.mNumberSelectEditOperationCakkBack = numberSelectEditOperationCakkBack;
        return this;
    }



    public interface NumberSelectEditOperationCakkBack {

        void onAdd(NumberSelectEdit numberSelectEdit, int currentNumber);

        void onSubtract(NumberSelectEdit numberSelectEdit, int currentNumber);

        void onZero(NumberSelectEdit numberSelectEdit);

        void onEditChanged(NumberSelectEdit numberSelectEdit, int currentNumber);

    }
}
