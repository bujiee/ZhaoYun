package com.bj.zhaoyun.view.calc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bj.zhaoyun.R;
import com.bj.zhaoyun.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：計算器，首先将所有的四则运算的中缀表达式变为后缀表达式
 * 然后通过后缀表达式求值
 * Created by Buuu on 2017/11/29.
 */

public class CalculatorView extends LinearLayout implements AdapterView.OnItemClickListener {
    private EditText tv_display;
    private GridView gv_control;
    private List<String> mList = new ArrayList<>();

    public CalculatorView(Context context) {
        this(context, null);
    }

    public CalculatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalculatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private StringBuffer sbTmp = new StringBuffer();

    private void initData(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calc_layout, null);
        tv_display = view.findViewById(R.id.tv_display);
        gv_control = view.findViewById(R.id.gv_control);
        gv_control.setAdapter(new MyAdapter(context));
        ViewGroup.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(view, layoutParams);
        gv_control.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String s = (String) gv_control.getAdapter().getItem(position);
        if ("AC".equals(s)) {
            mList.clear();
            tv_display.setText(" ");
        } else if ("<-".equals(s)) {//移除最后一个
            if (!mList.isEmpty())
                mList.remove(mList.size() - 1);
        } else {
            if ("+".equals(s) || "-".equals(s) || "*".equals(s) || "/".equals(s)) {
                if (mList.size() > 1 && ("+".equals(mList.get(mList.size() - 1)) || "-".equals(mList.get(mList.size() - 1)) || "*".equals(mList.get(mList.size() - 1)) || "/".equals(mList.get(mList.size() - 1)))) {
                    //empty
                    Log.i("s", "  ");
                } else {
                    mList.add(s);
                }
            } else {
                mList.add(s);
            }
        }
        String tmpResult = "";

        if (!mList.isEmpty() && "=".equals(mList.get(mList.size() - 1))) {
            List<String> list = new ArrayList<>();
            list.addAll(mList);
            list.remove(mList.size() - 1);
            String result = MathUtil.getResult(MathUtil.middleToEnd(list));
            mList.add(result);
        }
        for (String s1 : mList) {
            tmpResult += s1;
        }
        tv_display.setText(tmpResult);
    }


    public static class MyAdapter extends BaseAdapter {
        private Context mContext;
        private String[] data = {
                "AC", "<-", ".", "+",
                "1", "2", "3", "-",
                "4", "5", "6", "*",
                "7", "8", "9", "/",
                "(", ")", "0", "="
        };

        MyAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_calc_child, null);
            TextView tv = v.findViewById(R.id.tv_num);
            tv.setText(data[position]);
            return v;
        }
    }

}
