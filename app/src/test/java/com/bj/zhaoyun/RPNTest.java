package com.bj.zhaoyun;

import android.text.TextUtils;

import com.bj.zhaoyun.util.MathUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Description：
 * Created by Buuu on 2017/11/30.
 */

public class RPNTest {

    private final String add = "+";//1
    private final String subtraction = "-";//1
    private final String multiplication = "*";//2
    private final String division = "/";//2
    private final String left_parenthesis = "(";//3
    private final String right_parenthesis = ")";//0

    @Test
    public void testRPN() {
        String src = "10+(11+(10-2)*30)+4"; //10 11 2 10 - 30 * + 4 + +
        List<String> list = new ArrayList<>();
        // 3-1-9-9
//        list.add("3");
//        list.add("-");
//        list.add("1");
//        list.add("-");
//        list.add("9");
//        list.add("-");
//        list.add("9");
        //3*9*2/3
        list.add("3.5");
        list.add("*");
        list.add("9");
        list.add("*");
        list.add("2");
        list.add("/");
        list.add("3");
        System.out.println(MathUtil.middleToEnd(list));

//        list.add("10");
//        list.add("+");
//        list.add("(");
//        list.add("11");
//        list.add("+");
//        list.add("(");
//        list.add("10");
//        list.add("-");
//        list.add("2");
//        list.add(")");
//        list.add("*");
//        list.add("30");
//        list.add(")");
//        list.add("+");
//        list.add("4");
////        String src = "10*(1+11)-20/2";  // 10 1 11 + * 20 2 / -
////        list.add("10");
////        list.add("*");
////        list.add("(");
////        list.add("1");
////        list.add("+");
////        list.add("11");
////        list.add(")");
////        list.add("-");
////        list.add("20");
////        list.add("/");
////        list.add("2");
////        System.out.println(middleToEnd(list));
////        System.out.println(MathUtil.middleToEnd(list));
//        //测试四则运算 10 11 2 10 - 30 * + 4 + +
//        List<String> listS = new ArrayList<>();
////        listS.add("10");
////        listS.add("11");
////        listS.add("10");
////        listS.add("2");
////        listS.add("-");
////        listS.add("30");
////        listS.add("*");
////        listS.add("+");
////        listS.add("4");
////        listS.add("+");
////        listS.add("+");
//        //10 1 11 + * 20 2 / -
//        listS.add("10");
//        listS.add("1");
//        listS.add("11");
//        listS.add("+");
//        listS.add("*");
//        listS.add("20");
//        listS.add("2");
//        listS.add("/");
//        listS.add("-");
//        System.out.println(getResult(listS));
    }

    private String getResult(List<String> src) {
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


    //中缀表达式变为后缀表达式
    private String middleToEnd(List<String> src) {
        StringBuffer sb = new StringBuffer();
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < src.size(); i++) {
            String tmpResult = src.get(i);
            switch (tmpResult) {
                case add:
                case subtraction:
                    while (true) {
                        if (!stack.isEmpty() && (division.equals(stack.peek()) || multiplication.equals(stack.peek()))) {
                            String tmp = stack.pop();
                            sb.append(tmp + " ");
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
                            sb.append(tmp + " ");
                        }
                    }
                    break;
                default:
                    sb.append(tmpResult + " ");
                    if (i == src.size() - 1) {
                        while (true) {
                            if (stack.isEmpty())
                                break;
                            String s = stack.pop();
                            if (s == null) {
                                break;
                            }
                            sb.append(s + " ");
                        }

                    }
                    break;
            }
        }
        return sb.toString();
    }


}
