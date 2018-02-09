package com.blankj.utilcode.customwidget.Progress.ProgressBarCircle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.blankj.utilcode.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

public class CircleProgressBar extends ProgressBar {
    private static final int LINE = 0;
    private static final int SOLID = 1;
    private static final int SOLID_LINE = 2;

    private static final int LINEAR = 0;
    private static final int RADIAL = 1;
    private static final int SWEEP = 2;

    private static final float DEFAULT_START_DEGREE = -90.0f;

    private static final int DEFAULT_LINE_COUNT = 45;

    private static final float DEFAULT_LINE_WIDTH = 4.0f;
    private static final float DEFAULT_PROGRESS_TEXT_SIZE = 11.0f;
    private static final float DEFAULT_PROGRESS_STROKE_WIDTH = 1.0f;

    private static final String COLOR_FFF2A670 = "#fff2a670";
    private static final String COLOR_FFD3D3D5 = "#ffe3e3e5";
    private static final String DEFAULT_PATTERN = "%d%%";

    private final RectF mProgressRectF = new RectF();
    private final Rect mProgressTextRect = new Rect();

    private final Paint mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mProgressBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Paint mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mRadius;
    private float mCenterX;
    private float mCenterY;

    //进度条的背景色
    private int mBackgroundColor;
    //只有在线型中才能运行,代表圆环中所包含的行数Only work well in the Line Style, represents the line count of the rings included
    private int mLineCount;
    //只有在线型中才能运行,进度条的高度Only work well in the Line Style, Height of the line of the progress bar
    private float mLineWidth;
    //进度条的行程宽度Stroke width of the progress of the progress bar
    private float mProgressStrokeWidth;

    //进度条的文本大小Text size of the progress of the progress bar
    private float mProgressTextSize;

    //进度条开始端的颜色Start color of the progress of the progress bar
    private int mProgressStartColor;
    //进度条末尾端的颜色End color of the progress of the progress bar
    private int mProgressEndColor;
    //进度条的进度值的颜色Color of the progress value of the progress bar
    private int mProgressTextColor;
    //进度条的背景颜色Background color of the progress of the progress bar
    private int mProgressBackgroundColor;

    //如果mDrawProgressText是正确的，则会绘制进度条。否则，将不会绘制进度条。If mDrawProgressText is true, will draw the progress text. otherwise, will not draw the progress text.
    private boolean mDrawProgressText;
    //将当前的进度值格式化为指定的格式Format the current progress value to the specified format
    private String mProgressTextFormatPattern;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LINE, SOLID, SOLID_LINE})
    private @interface Style {
    }

    //前进色彩的风格The style of the progress color
    @Style
    private int mStyle;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LINEAR, RADIAL, SWEEP})
    private @interface ShaderMode {
    }

    //mProgressPaint的着色器The Shader of mProgressPaint
    @ShaderMode
    private int mShader;
    //The Stroke Cap of mProgressPaint and mProgressBackgroundPaint
    private Paint.Cap mCap;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        adjustIndeterminate();
        initFromAttributes(context, attrs);
        initPaint();
    }

    /**
     * 基本数据初始化
     */
    @SuppressWarnings("ResourceType")
    private void initFromAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        mBackgroundColor = a.getColor(R.styleable.CircleProgressBar_background_color, Color.TRANSPARENT);

        mDrawProgressText = a.getBoolean(R.styleable.CircleProgressBar_draw_progress_text, true);

        mLineCount = a.getInt(R.styleable.CircleProgressBar_line_count, DEFAULT_LINE_COUNT);
        mProgressTextFormatPattern = a.hasValue(R.styleable.CircleProgressBar_progress_text_format_pattern) ?
                a.getString(R.styleable.CircleProgressBar_progress_text_format_pattern) : DEFAULT_PATTERN;

        mStyle = a.getInt(R.styleable.CircleProgressBar_style, LINE);
        mShader = a.getInt(R.styleable.CircleProgressBar_progress_shader, LINEAR);
        mCap = a.hasValue(R.styleable.CircleProgressBar_progress_stroke_cap) ?
                Paint.Cap.values()[a.getInt(R.styleable.CircleProgressBar_progress_stroke_cap, 0)] : Paint.Cap.BUTT;

        mLineWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_line_width, UnitUtils.dip2px(getContext(), DEFAULT_LINE_WIDTH));
        mProgressTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_text_size, UnitUtils.dip2px(getContext(), DEFAULT_PROGRESS_TEXT_SIZE));
        mProgressStrokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_stroke_width, UnitUtils.dip2px(getContext(), DEFAULT_PROGRESS_STROKE_WIDTH));

        mProgressStartColor = a.getColor(R.styleable.CircleProgressBar_progress_start_color, Color.parseColor(COLOR_FFF2A670));
        mProgressEndColor = a.getColor(R.styleable.CircleProgressBar_progress_end_color, Color.parseColor(COLOR_FFF2A670));
        mProgressTextColor = a.getColor(R.styleable.CircleProgressBar_progress_text_color, Color.parseColor(COLOR_FFF2A670));
        mProgressBackgroundColor = a.getColor(R.styleable.CircleProgressBar_progress_background_color, Color.parseColor(COLOR_FFD3D3D5));

        a.recycle();
    }

    /**
     * Paint 初始化
     */
    private void initPaint() {
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        mProgressTextPaint.setTextSize(mProgressTextSize);

        mProgressPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressPaint.setColor(mProgressStartColor);
        mProgressPaint.setStrokeCap(mCap);

        mProgressBackgroundPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        mProgressBackgroundPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressBackgroundPaint.setColor(mProgressBackgroundColor);
        mProgressBackgroundPaint.setStrokeCap(mCap);

        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);
    }

    /**
     * 进度条的渐变颜色The progress bar color gradient,
     * 需要在此调用 need to be invoked in the {@link #onSizeChanged(int, int, int, int)}
     */
    private void updateProgressShader() {
        if (mProgressStartColor != mProgressEndColor) {
            Shader shader = null;
            switch (mShader) {
                case LINEAR:
                    shader = new LinearGradient(mProgressRectF.left, mProgressRectF.top,
                            mProgressRectF.left, mProgressRectF.bottom,
                            mProgressStartColor, mProgressEndColor, Shader.TileMode.CLAMP);
                    break;
                case RADIAL:
                    shader = new RadialGradient(mCenterX, mCenterY, mRadius,
                            mProgressStartColor, mProgressEndColor, Shader.TileMode.CLAMP);
                    break;
                case SWEEP:
                    //arc = radian * radius
                    float radian = (float) (mProgressStrokeWidth / Math.PI * 2.0f / mRadius);
                    float rotateDegrees = (float) (DEFAULT_START_DEGREE
                            - (mCap == Paint.Cap.BUTT && mStyle == SOLID_LINE ? 0 : Math.toDegrees(radian)));

                    shader = new SweepGradient(mCenterX, mCenterY, new int[] {mProgressStartColor, mProgressEndColor},
                            new float[] {0.0f, 1.0f});
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotateDegrees, mCenterX, mCenterY);
                    shader.setLocalMatrix(matrix);
                    break;
            }

            mProgressPaint.setShader(shader);
        } else {
            mProgressPaint.setShader(null);
            mProgressPaint.setColor(mProgressStartColor);
        }
    }

    /**
     * 为了能够很好地工作，需要通过反射来修改下面的一些字段In order to work well, need to modify some of the following fields through reflection.
     * 另一种可用的方法:将下列属性写入xml Another available way: write the following attributes to the xml
     * <p>
     * android:indeterminateOnly="false"
     * android:indeterminate="false"
     */
    private void adjustIndeterminate() {
        try {
            Field mOnlyIndeterminateField = ProgressBar.class.getDeclaredField("mOnlyIndeterminate");
            mOnlyIndeterminateField.setAccessible(true);
            mOnlyIndeterminateField.set(this, false);

            Field mIndeterminateField = ProgressBar.class.getDeclaredField("mIndeterminate");
            mIndeterminateField.setAccessible(true);
            mIndeterminateField.set(this, false);

            Field mCurrentDrawableField = ProgressBar.class.getDeclaredField("mCurrentDrawable");
            mCurrentDrawableField.setAccessible(true);
            mCurrentDrawableField.set(this, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawProgress(canvas);
        drawProgressText(canvas);
    }

    private void drawBackground(Canvas canvas) {
        if (mBackgroundColor != Color.TRANSPARENT) {
            canvas.drawCircle(mCenterX, mCenterX, mRadius, mBackgroundPaint);
        }
    }

    private void drawProgressText(Canvas canvas) {
        if (!mDrawProgressText) {
            return;
        }

        String progressText = String.format(mProgressTextFormatPattern, getProgress());

        mProgressTextPaint.setTextSize(mProgressTextSize);
        mProgressTextPaint.setColor(mProgressTextColor);
        mProgressTextPaint.getTextBounds(progressText, 0, progressText.length(), mProgressTextRect);
        canvas.drawText(progressText, mCenterX, mCenterY + mProgressTextRect.height() / 2, mProgressTextPaint);
    }

    private void drawProgress(Canvas canvas) {
        switch (mStyle) {
            case SOLID:
                drawSolidProgress(canvas);
                break;
            case SOLID_LINE:
                drawSolidLineProgress(canvas);
                break;
            case LINE:
            default:
                drawLineProgress(canvas);
                break;
        }
    }

    /**
     * 在绘制区域的中心作为参考点，旋转画布 In the center of the drawing area as a reference point , rotate the canvas
     */
    private void drawLineProgress(Canvas canvas) {
        float unitDegrees = (float) (2.0f * Math.PI / mLineCount);
        float outerCircleRadius = mRadius;
        float interCircleRadius = mRadius - mLineWidth;

        int progressLineCount = (int) ((float) getProgress() / (float) getMax() * mLineCount);

        for (int i = 0; i < mLineCount; i++) {
            float rotateDegrees = i * unitDegrees;

            float startX = mCenterX + (float) Math.sin(rotateDegrees) * interCircleRadius;
            float startY = mCenterX - (float) Math.cos(rotateDegrees) * interCircleRadius;

            float stopX = mCenterX + (float) Math.sin(rotateDegrees) * outerCircleRadius;
            float stopY = mCenterX - (float) Math.cos(rotateDegrees) * outerCircleRadius;

            if (i < progressLineCount) {
                canvas.drawLine(startX, startY, stopX, stopY, mProgressPaint);
            } else {
                canvas.drawLine(startX, startY, stopX, stopY, mProgressBackgroundPaint);
            }
        }
    }

    /**
     * 画弧 Just draw arc
     */
    private void drawSolidProgress(Canvas canvas) {
        canvas.drawArc(mProgressRectF, DEFAULT_START_DEGREE, 360.0f, false, mProgressBackgroundPaint);
        canvas.drawArc(mProgressRectF, DEFAULT_START_DEGREE, 360.0f * getProgress() / getMax(), true, mProgressPaint);
    }

    /**
     * 画弧 Just draw arc
     */
    private void drawSolidLineProgress(Canvas canvas) {
        canvas.drawArc(mProgressRectF, DEFAULT_START_DEGREE, 360.0f, false, mProgressBackgroundPaint);
        canvas.drawArc(mProgressRectF, DEFAULT_START_DEGREE, 360.0f * getProgress() / getMax(), false, mProgressPaint);
    }

    /**
     * 当CircleProgressBar的大小发生变化时，需要重新调整绘制区域 When the size of ProgressBarCircle changed, need to re-adjust the drawing area
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;

        mRadius = Math.min(mCenterX, mCenterY);
        mProgressRectF.top = mCenterY - mRadius;
        mProgressRectF.bottom = mCenterY + mRadius;
        mProgressRectF.left = mCenterX - mRadius;
        mProgressRectF.right = mCenterX + mRadius;

        updateProgressShader();

        //Prevent the progress from clipping
        mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2);
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        mBackgroundPaint.setColor(backgroundColor);
        invalidate();
    }

    public void setProgressTextFormatPattern(String progressTextformatPattern) {
        this.mProgressTextFormatPattern = progressTextformatPattern;
        invalidate();
    }

    public String getProgressTextFormatPattern() {
        return mProgressTextFormatPattern;
    }

    public void setProgressStrokeWidth(float progressStrokeWidth) {
        this.mProgressStrokeWidth = progressStrokeWidth;
        mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2);
        invalidate();
    }

    public float getProgressStrokeWidth() {
        return mProgressStrokeWidth;
    }

    public void setProgressTextSize(float progressTextSize) {
        this.mProgressTextSize = progressTextSize;
        invalidate();
    }

    public float getProgressTextSize() {
        return mProgressTextSize;
    }

    public void setProgressStartColor(int progressStartColor) {
        this.mProgressStartColor = progressStartColor;
        updateProgressShader();
        invalidate();
    }

    public int getProgressStartColor() {
        return mProgressStartColor;
    }

    public void setProgressEndColor(int progressEndColor) {
        this.mProgressEndColor = progressEndColor;
        updateProgressShader();
        invalidate();
    }

    public int getProgressEndColor() {
        return mProgressEndColor;
    }

    public void setProgressTextColor(int progressTextColor) {
        this.mProgressTextColor = progressTextColor;
        invalidate();
    }

    public int getProgressTextColor() {
        return mProgressTextColor;
    }

    public void setProgressBackgroundColor(int progressBackgroundColor) {
        this.mProgressBackgroundColor = progressBackgroundColor;
        mProgressBackgroundPaint.setColor(mProgressBackgroundColor);
        invalidate();
    }

    public int getProgressBackgroundColor() {
        return mProgressBackgroundColor;
    }

    public int getLineCount() {
        return mLineCount;
    }

    public void setLineCount(int lineCount) {
        this.mLineCount = lineCount;
        invalidate();
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.mLineWidth = lineWidth;
        invalidate();
    }

    public int getStyle() {
        return mStyle;
    }

    public void setStyle(@Style int style) {
        this.mStyle = style;
        mProgressPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        mProgressBackgroundPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        invalidate();
    }

    public int getShader() {
        return mShader;
    }

    public void setShader(@ShaderMode int shader) {
        mShader = shader;
        updateProgressShader();
        invalidate();
    }

    public Paint.Cap getCap() {
        return mCap;
    }

    public void setCap(Paint.Cap cap) {
        mCap = cap;
        mProgressPaint.setStrokeCap(cap);
        mProgressBackgroundPaint.setStrokeCap(cap);
        invalidate();
    }
}
