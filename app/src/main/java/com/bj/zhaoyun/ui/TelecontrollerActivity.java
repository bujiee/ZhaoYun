package com.bj.zhaoyun.ui;


import android.text.TextUtils;
import android.widget.TextView;

import com.bj.zhaoyun.BaseActivity;
import com.bj.zhaoyun.R;
import com.bj.zhaoyun.view.chart.Telecontroller;

import butterknife.BindView;

public class TelecontrollerActivity extends BaseActivity {
    @BindView(R.id.telecontroller)
    Telecontroller telecontroller;
    @BindView(R.id.tv_result)
    TextView tv_result;

    @Override
    public int getLayoutId() {
        return R.layout.activity_telecontroller;
    }

    @Override
    public void initData() {
        telecontroller.setOnTelecontrollerClickListener(new Telecontroller.OnTelecontrollerClickListener() {
            @Override
            public void onClick(String result) {
                if (!TextUtils.isEmpty(result)) {
                    tv_result.setText(result);
                }
            }
        });
    }
}
