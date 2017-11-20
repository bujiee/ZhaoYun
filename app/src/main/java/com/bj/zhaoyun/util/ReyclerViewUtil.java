package com.bj.zhaoyun.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Description：
 * Created by Buuu on 2017/11/14.
 */

public class ReyclerViewUtil {
    public static void setLinearLayoutManager(RecyclerView recyclerView, Context context, int orientation) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(orientation);
        recyclerView.setLayoutManager(layoutManager);
    }
}
