package com.bj.zhaoyun.ui;


import android.view.View;
import android.widget.TextView;

import com.bj.zhaoyun.BaseActivity;
import com.bj.zhaoyun.R;
import com.bj.zhaoyun.view.slideletter.SlideLetterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SlideLetterActivity extends BaseActivity {
    @BindView(R.id.slv_letter)
    SlideLetterView slv_letter;
    @BindView(R.id.tv_show)
    TextView tv_show;

    @Override
    public int getLayoutId() {
        return R.layout.activity_slide_letter;
    }

    @Override
    public void initData() {
        List<String> mDatas = new ArrayList<>();
        //65-90
        int x = 65;
        while (x <= 90) {
            mDatas.add(String.valueOf((char) x));
            x++;
        }
        mDatas.add(String.valueOf((char) 35));
        slv_letter.setDatas(mDatas);
        slv_letter.setOnLetterChangeListener(new SlideLetterView.LetterChangeListener() {
            @Override
            public void onLetterChange(String letter, int choose) {
                System.out.println("choose" + choose);
                if (choose == -1) {
                    tv_show.setVisibility(View.GONE);
                } else {
                    tv_show.setVisibility(View.VISIBLE);
                }
                tv_show.setText(letter);
            }
        });
    }
}
