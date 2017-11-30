package com.bj.zhaoyun;

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
//        String src = "10+(11+(2-10)*30)+4";
        List<String> list = new ArrayList<>();
//        list.add("10");
//        list.add("+");
//        list.add("(");
//        list.add("11");
//        list.add("+");
//        list.add("(");
//        list.add("2");
//        list.add("-");
//        list.add("10");
//        list.add(")");
//        list.add("*");
//        list.add("30");
//        list.add(")");
//        list.add("+");
//        list.add("4");
        String src = "10*(1+11)-20/2";
        list.add("10");
        list.add("*");
        list.add("(");
        list.add("1");
        list.add("+");
        list.add("11");
        list.add(")");
        list.add("-");
        list.add("20");
        list.add("/");
        list.add("2");
//
        System.out.println(middleToEnd(list));

    }

    private String middleToEnd(List<String> src) {
        StringBuffer sb = new StringBuffer();
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < src.size(); i++) {
            String tmpResult = src.get(i);
            switch (tmpResult) {
                case add:
                case subtraction:
                    while (true) {
                        if (!stack.isEmpty() && (division.equals(stack.peek()) || subtraction.equals(stack.peek()))) {
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
                        for (String s : stack) {
                            sb.append(s + " ");
                        }
                    }
                    break;
            }
        }
        return sb.toString();
    }

}
