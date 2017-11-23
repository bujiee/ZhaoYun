package com.bj.zhaoyun.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Description：Toast 工具类
 * Created by Buuu on 2017/11/23.
 */

public class ToastUtil {
    private static Toast mToast;

    /**
     * @param mContext     context
     * @param charSequence charSequence
     */
    @SuppressLint("ShowToast")
    public static void showToastShort(Context mContext, CharSequence charSequence,int timeType) {
        if (mToast != null) {
            mToast.setText(charSequence);
        } else {
            mToast = Toast.makeText(mContext, charSequence, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
