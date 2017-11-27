package com.bj.zhaoyun.ui;


import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.zhaoyun.BaseActivity;
import com.bj.zhaoyun.R;
import com.bj.zhaoyun.util.ToastUtil;
import com.bj.zhaoyun.view.chart.PieChart;
import com.bj.zhaoyun.view.chart.PieChartTouchView;

import butterknife.BindView;

/**
 * 展示图标界面
 */
public class PieChartActivity extends BaseActivity {
    @BindView(R.id.pc_pie_chat)
    PieChart pc_pie_chat;
    @BindView(R.id.ll_container)
    LinearLayout ll_container;
    @BindView(R.id.pct_chart)
    PieChartTouchView pct_chart;
    int maxLength = 5;
    double num[] = new double[maxLength];
    int color[] = new int[maxLength];

    @Override
    public int getLayoutId() {
        return R.layout.activity_chart;
    }

    @Override
    public void initData() {
        initPieChart();
    }

    private void initPieChart() {
        for (int i = 0; i < maxLength; i++) {
            color[i] = Color.parseColor("#" + String.valueOf((int) ((Math.random() + 1) * 500000)));
        }
        TextView tv1 = setTextView(0, "食品10 %");
        ll_container.addView(tv1);
        TextView tv2 = setTextView(1, "住房30 %");
        ll_container.addView(tv2);
        TextView tv3 = setTextView(2, "旅游10 %");
        ll_container.addView(tv3);
        TextView tv4 = setTextView(3, "娱乐10 %");
        ll_container.addView(tv4);
        TextView tv5 = setTextView(4, "存储40 %");
        ll_container.addView(tv5);
        num[0] = 0.1;
        num[1] = 0.1;
        num[2] = 0.3;
        num[3] = 0.1;
        num[4] = 0.4;
        pc_pie_chat.addData(num, color, -60f);
        pct_chart.setData(num, color, -60);

        pct_chart.setOnPieChartChildClickListener(new PieChartTouchView.OnPieChartChildClickListener() {
            @Override
            public void onClick(final int position) {
                ToastUtil.showToast(mContext, "选中了-->" + position, Toast.LENGTH_SHORT);
            }
        });
    }

    private TextView setTextView(int x, String text) {
        TextView textView = new TextView(mContext);
        textView.setPadding(0, 30, 30, 0);
        textView.setText(text);
        textView.setBackgroundColor(color[x]);
        return textView;
    }
}
