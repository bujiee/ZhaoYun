package com.bj.zhaoyun.util;

import java.math.BigDecimal;

/**
 * Description：
 * Created by Buuu on 2017/11/16.
 */

public class MathUtil {
    public static double doubleDiv(double val1, double val2) {
        BigDecimal bd1 = new BigDecimal(val1);
        BigDecimal bd2 = new BigDecimal(val2);
        return bd1.divide(bd2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取最大数
     *
     * @param floats
     */
    public static float getMaxData(float[] floats) {
        if (floats==null||floats.length<1){
            return -1;
        }
        float tmp=floats[0];
        for (int i = 0; i < floats.length-1; i++) {
            tmp=Math.max(floats[i],floats[i+1]);
        }
        return  tmp;
    }
}
