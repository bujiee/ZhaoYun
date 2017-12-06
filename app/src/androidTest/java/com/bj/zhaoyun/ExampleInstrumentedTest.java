package com.bj.zhaoyun;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.bj.zhaoyun.util.MathUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.bj.zhaoyun", appContext.getPackageName());
    }

    @Test
    public void testMiddle2End() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("3.5");
        list.add("*");
        list.add("9");
        list.add("*");
        list.add("2");
        list.add("/");
        list.add("3");
//        System.out.println(MathUtil.middleToEnd(list));
        Log.d("testMiddle2End",MathUtil.middleToEnd(list).toString());
    }
}
