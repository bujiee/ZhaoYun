package com.bj.zhaoyun.ui;

import android.graphics.Point;
import android.widget.Toast;

import com.bj.zhaoyun.BaseActivity;
import com.bj.zhaoyun.R;
import com.bj.zhaoyun.util.ToastUtil;
import com.bj.zhaoyun.view.chart.LineChartSurfaceView;
import com.bj.zhaoyun.view.chart.LineChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 折綫圖
 */
public class LineChartActivity extends BaseActivity {
    @BindView(R.id.lcv_chart)
    LineChartView lcv_chart;
    @BindView(R.id.lcsv_view)
    LineChartSurfaceView lcsv_view;


    @Override
    public int getLayoutId() {
        return R.layout.activity_line_chart;
    }

    @Override
    public void initData() {
        String[] x = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
        String[] y = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        final List<Point> points = new ArrayList<>();
        for (int z = 0; z < x.length; z++) {
            Point point = new Point();
            point.y = (int) ((Math.random() + 0.1) * 10);
            point.x = z + 1;
            points.add(point);
        }
        lcv_chart.setData(x, y, points);
        lcv_chart.setOnPointClickListener(new LineChartView.OnPointClickListener() {
            @Override
            public void onPointClick(int position) {
                ToastUtil.showToast(mContext, position + "", Toast.LENGTH_SHORT);
            }
        });

        List<Integer> list=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        lcsv_view.setBloodPressure(list);

    }
}
