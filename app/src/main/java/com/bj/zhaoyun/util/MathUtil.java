package com.bj.zhaoyun.util;

import android.text.TextUtils;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Description： Math
 * Created by Buuu on 2017/11/16.
 */

public class MathUtil {
    private static final String add = "+";//1
    private static final String subtraction = "-";//1
    private static final String multiplication = "*";//2
    private static final String division = "/";//2
    private static final String left_parenthesis = "(";//3
    private static final String right_parenthesis = ")";//0
    private static final String point = ".";

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
     * 1.遇到操作数：直接输出（添加到后缀表达式中）
     * 2.栈为空时，遇到运算符，直接入栈
     * 3.遇到左括号：将其入栈
     * 4.遇到右括号：执行出栈操作，并将出栈的元素输出，直到弹出栈的是左括号，左括号不输出。
     * 5.遇到其他运算符：加减乘除：弹出所有优先级大于或者等于该运算符的栈顶元素，然后将该运算符入栈
     * 6.最终将栈中的元素依次出栈，输出。
     */
    public static List<String> middleToEnd(List<String> srcList) {
        for (int i = 0; i < srcList.size(); i++) {
            if (!(isOperator(srcList.get(i)) || isParenthesis(srcList.get(i)))) {
                //如果是数字或小数点
                int y = i;
                StringBuilder tmp = new StringBuilder();
                while (true) {
                    if (TextUtils.isEmpty(srcList.get(y))) {
                        break;
                    }
                    if (isOperator(srcList.get(y))) {
                        break;
                    } else if (isParenthesis(srcList.get(y))) {
                        break;
                    } else if (y == srcList.size() - 1) {
                        tmp.append(srcList.get(y));
                        srcList.set(y, "");
                        break;
                    } else {
                        tmp.append(srcList.get(y));
                        srcList.set(y, "");
                    }
                    y++;
                }
                srcList.set(i, tmp.toString());
            }
        }
        //数据处理
        List<String> src = new ArrayList<>();
        //如果左面有数字,但是没有符号,自动添加*
        for (int i = 0; i < srcList.size(); i++) {
            if (left_parenthesis.equals(srcList.get(i)) && i != 0 && !isOperator(srcList.get(i - 1))) {
                src.add(multiplication);
            }
            if (!TextUtils.isEmpty(srcList.get(i))) {
                src.add(srcList.get(i));
            }
        }
        //*************************** 华丽分割符号*********************************//
        List<String> list = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < src.size(); i++) {
            String tmpResult = src.get(i);
            switch (tmpResult) {
                case add:
                case subtraction:
                    while (true) {
                        if (!stack.isEmpty() && (add.equals(stack.peek()) || subtraction.equals(stack.peek()) || division.equals(stack.peek()) || multiplication.equals(stack.peek()))) {
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

    /**
     * 获取计算结果
     *
     * @param src eg:3*10-(9-8)
     * @return result
     */
    public static String getResult2(List<String> src) {
        double value = 0;
        value = 38 * 10 * (-1);
        return "";
    }


    /**
     * 根据后缀表达式返回,结果
     *
     * @param src 后缀表达式集合,如 3 1 + => [3,1,+]
     * @return
     */
    public static String getResult(List<String> src) {
        String result = "ERROR";
        if (src == null || src.size() < 3) {
            return result;
        }
        try {
            for (int i = 0; i < src.size(); i++) {
                switch (src.get(i)) {
                    case add:
                        src.set(i - 2, String.valueOf(Double.valueOf(src.get(i - 2)) + Double.valueOf(src.get(i - 1))));
                        src.remove(i - 1);
                        src.remove(i - 1);
                        i -= 2;
                        break;
                    case subtraction:
                        src.set(i - 2, String.valueOf(Double.valueOf(src.get(i - 2)) - Double.valueOf(src.get(i - 1))));
                        src.remove(i - 1);
                        src.remove(i - 1);
                        i -= 2;
                        break;
                    case multiplication:
                        src.set(i - 2, String.valueOf(Double.valueOf(src.get(i - 2)) * Double.valueOf(src.get(i - 1))));
                        src.remove(i - 1);
                        src.remove(i - 1);
                        i -= 2;
                        break;
                    case division:
                        src.set(i - 2, String.valueOf(Double.valueOf(src.get(i - 2)) / Double.valueOf(src.get(i - 1))));
                        src.remove(i - 1);
                        src.remove(i - 1);
                        i -= 2;
                        break;
                    default:
                        break;
                }

            }
            result = src.get(0);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    /*不是加减乘除运算符*/
    private static boolean isOperator(String operator) {
        return add.equals(operator) || subtraction.equals(operator) || multiplication.equals(operator) || division.equals(operator);
    }

    private static boolean isParenthesis(String parenthesis) {
        return left_parenthesis.equals(parenthesis) || right_parenthesis.equals(parenthesis);
    }
}
