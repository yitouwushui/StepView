package com.yitouwushui.stepview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @author ding
 * @date 2017/11/15
 */

public class StepView extends FrameLayout {

    public static final String TAG = "StepView";
    /**
     * 步骤显示器
     */
    private StepBar mStepBar;
    /**
     * 用来存放显示步骤名称的布局
     */
    private FrameLayout mTitleGroup;
    /**
     * 所有步骤的标题
     */
    private List<String> mStepTitles;

    public StepView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StepView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context mContext, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(mContext).inflate(R.layout.step_view, this, true);
        mStepBar = this.findViewById(R.id.step_bar);
        mTitleGroup = this.findViewById(R.id.step_title);
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.StepView, defStyleAttr, 0);

        mStepBar.setLineHeight(array.getDimensionPixelOffset(R.styleable.StepView_lineHeight, StepBar.DEFAULT_LINE_HEIGHT));
        mStepBar.setSmallRadius(array.getDimensionPixelOffset(R.styleable.StepView_smallRadius, StepBar.DEFAULT_SMALL_CIRCLE_RADIUS));
        mStepBar.setLargeRadius(array.getDimensionPixelOffset(R.styleable.StepView_largeRadius, StepBar.DEFAULT_LARGE_CIRCLE_RADIUS));
        mStepBar.setUnDoneColor(array.getColor(R.styleable.StepView_undoneColor, StepBar.COLOR_BAR_UNDONE));
        mStepBar.setDoneColor(array.getColor(R.styleable.StepView_undoneColor, StepBar.COLOR_BAR_DONE));
        mStepBar.setTotalStep(array.getInteger(R.styleable.StepView_totalStep, 0));
        mStepBar.setCompleteStep(array.getInteger(R.styleable.StepView_completeStep, 0));

        //在StepBar布局完成之后开始添加title
        mStepBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                initStepTitle();
                mStepBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        array.recycle();
    }

    private void initStepTitle() {
        if (mStepTitles == null) {
            return;
        }
        mTitleGroup.removeAllViews();
        if (mStepTitles.size() != mStepBar.getTotalStep()) {
            throw new IllegalArgumentException("标题和步数不一致");
        }
        int stepNum = mStepBar.getTotalStep();
        for (int i = 1; i <= stepNum; i++) {
            final float stepPos = mStepBar.getPositionByStep(i) + mStepBar.getDistance() / 2;
            final TextView title = new TextView(this.getContext());
            title.setText(mStepTitles.get(i - 1));
            title.setSingleLine();
            title.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    float x = stepPos - title.getMeasuredWidth() / 2;
                    title.setTranslationX(x);
                    title.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mTitleGroup.addView(title, lp);
        }
    }

    public void setStepTitles(List<String> stepTitles) {
        this.mStepTitles = stepTitles;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    /**
     * 设置总的步骤数
     *
     * @param mTotalStep
     */
    public void setTotalStep(int mTotalStep) {
        mStepBar.setTotalStep(mTotalStep);
    }

    /**
     * 获取步骤总数
     *
     * @return
     */
    public int getTotalStep() {
        return mStepBar.getTotalStep();
    }

    /**
     * 进入下一个步骤
     */
    public void nextStep() {
        mStepBar.nextStep();
    }

    /**
     * 重置步骤
     */
    public void reset() {
        mStepBar.reset();
    }

}
