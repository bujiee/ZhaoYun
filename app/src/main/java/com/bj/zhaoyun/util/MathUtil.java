package com.bj.zhaoyun.util;

import java.math.BigDecimal;

/**
 * Description：
 * Created by Buuu on 2017/11/16.
 */

public class MathUtil {
    public  static  double doubleDiv(double val1, double val2) {
        BigDecimal bd1 = new BigDecimal(val1);
        BigDecimal bd2 = new BigDecimal(val2);
        return bd1.divide(bd2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
