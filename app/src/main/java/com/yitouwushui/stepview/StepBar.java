package com.yitouwushui.stepview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author ding
 * @date 2017/11/15
 */

public class StepBar extends View {

    public static final String TAG = "StepBar";
    /**
     * 未完成的步骤的颜色
     */
    public static final int COLOR_BAR_UNDONE = 0XFF808080;
    /**
     * 完成步骤的颜色
     */
    public static final int COLOR_BAR_DONE = 0XFF00FF00;
    /**
     * 默认线条高度
     */
    public static final int DEFAULT_LINE_HEIGHT = 30;
    /**
     * 默认小圆的半径
     */
    public static final int DEFAULT_SMALL_CIRCLE_RADIUS = 10;
    /**
     * 默认大圆的半径
     */
    public static final int DEFAULT_LARGE_CIRCLE_RADIUS = 20;
    /**
     * 默认距离边缘的距离
     */
    public static final int DEFAULT_PADDING = 20;

    private float mCenterY = 0.0f;
    private float mLeftX = 0.0f;
    private float mLeftY = 0.0f;
    private float mRightX = 0.0f;
    private float mRightY = 0.0f;
    private float mDistance = 0.0f;

    private int mTotalStep;
    private int mCompleteStep;
    private int mDoneColor = COLOR_BAR_DONE;
    private int mUnDoneColor = COLOR_BAR_UNDONE;
    private float mLineHeight = 0f;
    private float mSmallRadius = 0f;
    private float mLargeRadius = 0f;

    public StepBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public StepBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public StepBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StepBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 用来处理自定义属性
     * 注意：此方法中最好不要涉及计算UI相关尺寸的逻辑，如果一定要在这里计算，那么务必使用{@link #post(Runnable)}
     *
     * @param mContext
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context mContext, AttributeSet attrs, int defStyleAttr) {

    }

    /**
     * 设置总步骤数
     *
     * @param mTotalStep
     */
    public void setTotalStep(int mTotalStep) {
        if (mTotalStep < 0) {
            throw new IllegalArgumentException("步骤数必须大于0");
        }
        this.mTotalStep = mTotalStep;
    }

    /**
     * 获取步骤总数
     *
     * @return
     */
    public int getTotalStep() {
        return mTotalStep;
    }

    /**
     * 获取步骤间距
     *
     * @return
     */
    public float getDistance() {
        return mDistance;
    }

    /**
     * 设置完成步数
     *
     * @param mCompleteStep
     */
    public void setCompleteStep(int mCompleteStep) {
        if (mCompleteStep < 0 || mCompleteStep > mTotalStep) {
            return;
        }
        this.mCompleteStep = mCompleteStep;
    }

    /**
     * 通过步骤数获取坐标
     *
     * @param step
     * @return
     */
    public float getPositionByStep(int step) {
        if (step < 1 || step > mTotalStep) {
            throw new IllegalArgumentException("step必须在 1~总步骤数之间!");
        }
        return mLeftX + (step - 1) * mDistance;
    }

    /**
     * 进入下一个步骤
     */
    public void nextStep() {
        if (mCompleteStep == mTotalStep) {
            return;
        }
        mCompleteStep++;
        invalidate();
    }

    /**
     * 重置步骤
     */
    public void reset() {
        mCompleteStep = 0;
        invalidate();
    }

    /**
     * 测量View的绘制的第一阶段调用
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultWidth();
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = 120;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        Log.d(TAG, "onMeasure-->width:" + width + " height:" + height);
        setMeasuredDimension(width, height);

    }

    /**
     * 在View的绘制的第二阶段(布局)中，当尺寸发生变化时调用
     * 注意：第二阶段本来是调用onLayout方法，此方法是在onLayout方法中被调用
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算位置
        mCenterY = this.getHeight() / 2;
        mLeftX = this.getLeft() + getPaddingLeft();
        mLeftY = mCenterY - mLineHeight / 2;
        mRightX = this.getRight() - getPaddingRight();
        mRightY = mCenterY + mLineHeight / 2;
        Log.d(TAG, "onSizeChanged->mLeftX:" + mLeftX);
        Log.d(TAG, "onSizeChanged->mRightX:" + mRightX);
        if (mTotalStep > 1) {
            mDistance = (mRightX - mLeftX) / (mTotalStep);
            Log.d(TAG, "onSizeChanged->mDistance:" + mDistance);
        }
    }

    /**
     * View的绘制的第三阶段调用
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTotalStep <= 0 || mCompleteStep < 0 || mCompleteStep > mTotalStep) {
            return;
        }
        Paint mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mUnDoneColor);
        float half = mLineHeight / 2;
        // 左边圆
        canvas.drawCircle(mLeftX + half, mLeftY + half, half, mCirclePaint);
        // 右边圆
        canvas.drawCircle(mRightX - half, mRightY - half, half, mCirclePaint);
        // 中间矩形
        canvas.drawRect(mLeftX + half, mLeftY, mRightX - half, mRightY, mCirclePaint);

        if (mCompleteStep > 0) {
            //画已经完成的步骤(圆形加矩形)
            float currentRightX = mCompleteStep * mDistance + getPaddingLeft();
            float currentLeftX = currentRightX - mDistance;

            mCirclePaint.setColor(mDoneColor);
            // 左边圆
            canvas.drawCircle(currentLeftX + half, mLeftY + half, half, mCirclePaint);
            // 右边圆
            canvas.drawCircle(currentRightX - half, mRightY - half, half, mCirclePaint);
            // 中间矩形
            canvas.drawRect(currentLeftX + half, mLeftY, currentRightX - half, mRightY, mCirclePaint);
        }
    }

    /**
     * 得到默认的StepBar的宽度
     *
     * @return
     */
    private int getDefaultWidth() {
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        return screenWidth - 2 * dp2px(DEFAULT_PADDING);
    }

    /**
     * dp单位转px单位
     *
     * @param dp
     * @return
     */
    public int dp2px(int dp) {
        return (int) (this.getContext().getResources().getDisplayMetrics().density * dp + 0.5);
    }

    /**
     * 设置步骤条的高度
     *
     * @param mLineHeight
     */
    public void setLineHeight(float mLineHeight) {
        this.mLineHeight = mLineHeight;
    }

    public void setSmallRadius(float mSmallRadius) {
        this.mSmallRadius = mSmallRadius;
    }

    public void setLargeRadius(float mLargeRadius) {
        this.mLargeRadius = mLargeRadius;
    }

    public void setUnDoneColor(int mUnDoneColor) {
        this.mUnDoneColor = mUnDoneColor;
    }

    public void setDoneColor(int mDoneColor) {
        this.mDoneColor = mDoneColor;
    }
}
