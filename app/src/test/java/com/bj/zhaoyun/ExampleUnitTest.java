package com.bj.zhaoyun;

import android.graphics.Color;
import android.media.Image;

import com.bj.zhaoyun.util.MathUtil;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void hashMapTest() {
        HashMap<String, String> map = new HashMap<>();
        map.put("object1", "result");
//        Class c = null;
//        try {
//            c = Class.forName("sun.misc.Hashing");
//            Method m = c.getDeclaredMethod("singleWordWangJenkinsHash", Object.class);
//            System.out.println(m.invoke("object1"));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        System.out.println(indexFor(singleWordWangJenkinsHash("object1"), 11));
        System.out.println(indexFor(singleWordWangJenkinsHash("ss"), 11));
        System.out.println(indexFor(singleWordWangJenkinsHash("jfladk"), 11));
        System.out.println(indexFor(singleWordWangJenkinsHash("daf"), 11));
        System.out.println(indexFor(singleWordWangJenkinsHash("fd"), 11));
        System.out.println(indexFor(singleWordWangJenkinsHash("s"), 11));
        System.out.println(indexFor(singleWordWangJenkinsHash("b"), 11));
        System.out.println(indexFor(singleWordWangJenkinsHash("c"), 11));

    }

    public static int singleWordWangJenkinsHash(Object k) {
        int h = k.hashCode();

        h += (h << 15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h << 3);
        h ^= (h >>> 6);
        h += (h << 2) + (h << 14);
        return h ^ (h >>> 16);

    }

    static int indexFor(int h, int length) {
        // assert Integer.bitCount(length) == 1 : "length must be a non-zero power of 2";
        return h & (length - 1);
    }

    /**
     * 选择
     * 冒泡
     */
    @Test
    public void testFor() {
        int[] arr = {2, 0, 9, 1, 3, 2, 45, 65, 33, 12, 10, 9, 9, 3, 4, 6, 9, 11, 20, 4, 5, 6, 7, 8};
        int[] arr2 = {2, 0, 9, 1, 3, 2, 45, 65, 33, 12, 10, 9, 9, 3, 4, 6, 9, 11, 20, 4, 5, 6, 7, 8};
        int[] arre = {};
//        selectSort(arr);
        System.out.println("----- -------------------------");
////        bubbleSort(arr2);
//        bubbleSort2(arr2);
//        insertSort(arre);
        System.out.println(determineArray(arr2));
        System.out.println(determineArray(arre));

    }

    //无序数组有没有重复元素
    private boolean determineArray(int[] args) {
        boolean flag = false;
        for (int x = 0; x < args.length - 1; x++) {
            for (int y = x + 1; y < args.length; y++) {
                if (args[x] == args[y]) {
                    flag = true;
                    break;
                }
                if (flag)
                    break;
            }
        }
        return flag;
    }

    private void insertSort(int[] args) {
        for (int x = 1; x < args.length - 1; x++) {
            for (int y = x - 1; y >= 0; y--) {
                if (args[x] > args[y]) {
                    // 交換
                    int tmp = args[x] ^ args[y];
                    args[x] = args[x] ^ tmp;
                    args[y] = args[y] ^ tmp;
                }
            }
        }

    }


    private void bubbleSort2(int[] args) {
        boolean flag;
        for (int x = args.length - 1; x > 0; x--) {
            flag = false;
            for (int y = 0; y < x; y++) {
                if (args[y] < args[y + 1]) {
                    //交換
                    int temp = args[y] ^ args[y + 1];
                    args[y] = args[y] ^ temp;
                    args[y + 1] = args[y + 1] ^ temp;
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
            System.out.println(Arrays.toString(args));
        }
    }

    private void bubbleSort(int[] args) {
        for (int x = args.length - 1; x > 0; x--) {
            for (int y = 0; y < x; y++) {
                if (args[y] > args[y + 1]) {
                    //交換
                    int temp = args[y] ^ args[y + 1];
                    args[y] = args[y] ^ temp;
                    args[y + 1] = args[y + 1] ^ temp;
//                    int temp = args[y + 1];
//                    args[y + 1] = args[y];
//                    args[y] = temp;
                }
            }
            System.out.println(Arrays.toString(args));
        }

//        //第一层循环从数组的最后往前遍历
//        for (int i = args.length - 1; i > 0 ; --i) {
//            //这里循环的上界是 i - 1，在这里体现出 “将每一趟排序选出来的最大的数从sorted中移除”
//            for (int j = 0; j < i; j++) {
//                //保证在相邻的两个数中比较选出最大的并且进行交换(冒泡过程)
//                if (args[j] > args[j+1]) {
//                    int temp = args[j];
//                    args[j] = args[j+1];
//                    args[j+1] = temp;
//                }
//            }
//            System.out.println(Arrays.toString(args));
//        }
    }

    private void selectSort2(int[] arr) {
        for (int x = 0; x < arr.length; x++) {
            for (int y = x + 1; y < arr.length; y++) {
                if (arr[x] > arr[y]) {
                    int tmp = arr[x];
                    arr[x] = arr[y];
                    arr[y] = tmp;
                }
            }
            System.out.println(Arrays.toString(arr));
        }

    }

    private void selectSort(int[] arr) {

        for (int x = 0; x < arr.length; x++) {
            int z = x;
            for (int y = z + 1; y < arr.length; y++) {
                if (arr[y] < arr[z]) {
                    z = y;
                }
            }
            //互换
            if (z != x) {
                int tmp = arr[x];
                arr[x] = arr[z];
                arr[z] = tmp;
            }
            System.out.println(Arrays.toString(arr));
        }

    }

    @Test
    public void testColor() {
        int x = 0;
        while (x < 100) {//{0,1)
            System.out.println("#" + String.valueOf((int) ((Math.random()+1) * 500000)));
            x++;
        }
    }
    @Test
    public void testDoubleDiv() {
        int x = 0;
        while (x < 100) {//{0,1)
//            System.out.println(MathUtil.doubleDiv(1, 6));
            System.out.println((double)1/6);
            x++;
        }
    }
}