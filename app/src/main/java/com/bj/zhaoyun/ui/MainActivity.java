package com.bj.zhaoyun.ui;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bj.zhaoyun.BaseActivity;
import com.bj.zhaoyun.adapter.MyRecyclerAdapter;
import com.bj.zhaoyun.R;
import com.bj.zhaoyun.util.ReyclerViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.rv_container)
    RecyclerView rv_container;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        ReyclerViewUtil.setLinearLayoutManager(rv_container, mContext, LinearLayoutManager.VERTICAL);
        List<String> mDatas = new ArrayList<>();
        mDatas.add("字符侧滑栏");
        mDatas.add("图标");
        mDatas.add("遥控器小布局");
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(mDatas, mContext);
        rv_container.setAdapter(adapter);
        adapter.setItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(mContext, SlideLetterActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(mContext, ChartActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(mContext, TelecontrollerActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
