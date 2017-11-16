package com.yitouwushui.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by ding on 2017/11/15.
 */

public class StepView extends FrameLayout {
    public static final String TAG="StepView";

    public StepView(Context context) {
        super(context);
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
