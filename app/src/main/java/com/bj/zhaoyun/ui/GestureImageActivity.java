package com.bj.zhaoyun.ui;

import com.bj.zhaoyun.BaseActivity;
import com.bj.zhaoyun.R;
import com.bj.zhaoyun.view.picture.GestureImageView;

import butterknife.BindView;

/**
 * 手势识别
 */
public class GestureImageActivity extends BaseActivity {
    @BindView(R.id.imageview)
    GestureImageView gestureImageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_image;
    }

    @Override
    public void initData() {
    }
}
