package com.bj.zhaoyun.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        if (floats == null || floats.length < 1) {
            return -1;
        }
        float tmp = floats[0];
        for (int i = 0; i < floats.length - 1; i++) {
            tmp = Math.max(floats[i], floats[i + 1]);
        }
        return tmp;
    }

    /**
     * 中缀表达式变为后缀表达式
     */
    public static List<String> middleToEnd(List<String> src) {
        final String add = "+";
        final String subtraction = "-";
        final String multiplication = "*";
        final String division = "/";
        final String left_parenthesis = "(";
        final String right_parenthesis = ")";
        //*************************** 华丽分割符号*********************************//
        List<String> list = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < src.size(); i++) {
            String tmpResult = src.get(i);
            switch (tmpResult) {
                case add:
                case subtraction:
                    while (true) {
                        if (!stack.isEmpty() && (division.equals(stack.peek()) || multiplication.equals(stack.peek()))) {
                            String tmp = stack.pop();
                            list.add(tmp);
                        } else {
                            stack.push(tmpResult);
                            break;
                        }
                    }
                    break;
                case multiplication:
                case division:
                case left_parenthesis:
                    stack.push(tmpResult);
                    break;
                case right_parenthesis://如果是右括号,将下面的第一个左括号上面元素出栈
                    while (true) {
                        String tmp = stack.pop();
                        if (left_parenthesis.equals(tmp)) {
                            break;
                        } else {
                            list.add(tmp);
                        }
                    }
                    break;
                default:
                    list.add(tmpResult);
                    if (i == src.size() - 1) {
                        while (true) {
                            if (stack.isEmpty())
                                break;
                            String s = stack.pop();
                            if (s == null) {
                                break;
                            }
                            list.add(s);
                        }

                    }
                    break;
            }
        }
        return list;
    }
}
