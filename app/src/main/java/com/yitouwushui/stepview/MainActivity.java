package com.yitouwushui.stepview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ding
 */
public class MainActivity extends AppCompatActivity {

    private StepView mStepView;
    private Button btReset, btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStepView = (StepView) this.findViewById(R.id.step_view);
        btNext = (Button) findViewById(R.id.bt_next);
        btReset = (Button) findViewById(R.id.bt_reset);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next(view);
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(view);
            }
        });
        List<String> titles = new ArrayList<String>();
        titles.add("下订单");
        titles.add("支付完成");
        titles.add("已经发货");
        titles.add("交易完成");
        mStepView.setStepTitles(titles);
    }


    public void next(View view) {
        mStepView.nextStep();
    }

    public void reset(View view) {
        mStepView.reset();
    }
}
