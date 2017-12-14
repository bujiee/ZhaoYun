package com.bj.zhaoyun.ui;

import android.view.View;

import com.bj.zhaoyun.BaseActivity;
import com.bj.zhaoyun.R;
import com.bj.zhaoyun.view.camera.QRCodeView;

import butterknife.BindView;


/**
 * 手机二维码扫描
 */
public class ZXingActivity extends BaseActivity {
    @BindView(R.id.qcv_QRCode)
    QRCodeView qcv_QRCode;

    @Override
    public int getLayoutId() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        return R.layout.activity_zxing;
    }

    @Override
    public void initData() {
        qcv_QRCode.start(new QRCodeView.OnCameraStatusListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        qcv_QRCode.release();
    }
}
