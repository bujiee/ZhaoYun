package com.bj.zhaoyun.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：
 * Created by Buuu on 2017/12/1.
 */

public class CirclePieChart extends View {
    private List<PieChartPart> allData;
    private Context mContext;
    private Paint mPaint;
    private RectF mMinRect;
    private RectF mMaxRect;
    private Matrix mMatrix;
    private List<Path> mPathsList;
    private int mWH;
    private int mMaxWH;
    private int defaultSpace = 300;
    private float startAngle = 0f;
    private float lineLength = 65;

    public CirclePieChart(Context context) {
        this(context, null);
    }

    public CirclePieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMatrix.reset();
        if (allData != null && !allData.isEmpty()) {
            mMaxWH = Math.min(w, h);
            mWH = Math.min(w, h) - defaultSpace;
            mMaxRect = new RectF(-mWH / 3, -mWH / 3, mWH / 3, mWH / 3);
            mMinRect = new RectF(-mWH / 5, -mWH / 5, mWH / 5, mWH / 5);
            //画环形图形
            for (int i = 0; i < allData.size(); i++) {
                Path path = new Path();
                path.addArc(mMaxRect, startAngle, allData.get(i).getNumerical() * 360);
                path.arcTo(mMinRect, startAngle + allData.get(i).getNumerical() * 360, -allData.get(i).getNumerical() * 360);
                path.close();
                startAngle += allData.get(i).getNumerical() * 360;
                mPathsList.add(path);
            }
        }
    }

    private void initData() {
        mPathsList = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setTextSize(40);
        mPaint.setColor(Color.parseColor("#3300f0ff"));
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(6);
        mMatrix = new Matrix();
    }

    private Rect mTmpRect = new Rect();
    private float defaultS = 10F;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mMaxWH / 2, mMaxWH / 2);
        if (mMatrix.isIdentity()) {
            canvas.getMatrix().invert(mMatrix);
        }
        for (int i = 0; i < mPathsList.size(); i++) {
            mPaint.setColor(allData.get(i).getColor());
            canvas.drawPath(mPathsList.get(i), mPaint);
        }
        float childStartAngle = startAngle;
        //画线和字
        for (int i = 0; i < allData.size(); i++) {
            double cos = Math.cos(Math.toRadians(childStartAngle + (allData.get(i).getNumerical() * 360) / 2));
            double sin = Math.sin(Math.toRadians(childStartAngle + (allData.get(i).getNumerical() * 360) / 2));

            float startX = (float) (cos * mWH) / 3;
            float endX = (float) (lineLength * cos + startX);
            float startY = (float) (sin * mWH) / 3;
            float endY = (float) (lineLength * sin + startY);

            mPaint.setColor(allData.get(i).getColor());
            canvas.drawLine(startX, startY, endX, endY, mPaint);
            //保留兩位小數 0.6537
            String tmpText = (allData.get(i).getNumerical() * 10000 / 1) / 100f + "%";
            mPaint.getTextBounds(tmpText, 0, tmpText.length(), mTmpRect);

            if (endY == 0 && endX > 0) {// x轴 正方向
                mPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(tmpText, endX + defaultS, endY + mTmpRect.height() / 2, mPaint);
            } else if (endX > 0 && endY > 0 && endX >= endY) {//x轴 正方向 0<angle<=45
                mPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(tmpText, endX + defaultS, endY + mTmpRect.height() / 2, mPaint);
            } else if (endX > 0 && endY > 0 && endX < endY) {//x轴 正方向 45<angle<90
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(tmpText, endX, endY + mTmpRect.height() + defaultS, mPaint);
            } else if (endX == 0 && endY > 0) { //y轴正方向
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(tmpText, endX, endY + mTmpRect.height() + defaultS, mPaint);
            } else if (endX < 0 && endY > 0 && Math.abs(endX) <= Math.abs(endY)) {//y轴正方向 90<a<=135
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(tmpText, endX, endY + mTmpRect.height() + defaultS, mPaint);
            } else if (endX < 0 && endY > 0 && Math.abs(endX) > Math.abs(endY)) {//y轴正方向 135<a<180
                mPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(tmpText, endX - defaultS, endY + mTmpRect.height() / 2, mPaint);
            } else if (endX < 0 && endY == 0) {//x轴负反向
                mPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(tmpText, endX - defaultS, endY + mTmpRect.height() / 2, mPaint);
            } else if (endX < 0 && endY < 0 && Math.abs(endX) >= Math.abs(endY)) {//x轴负反向 180<a<=225
                mPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(tmpText, endX - defaultS, endY + mTmpRect.height() / 2, mPaint);
            } else if (endX < 0 && endY < 0 && Math.abs(endX) < Math.abs(endY)) {//x轴负反向 225<a<270
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(tmpText, endX, endY - defaultS, mPaint);
            } else if (endX == 0 && endY < 0) {//y轴负方向
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(tmpText, endX, endY - defaultS, mPaint);
            } else if (endX > 0 && endY < 0 && Math.abs(endX) <= Math.abs(endY)) {//y轴负方向 270<a<=315
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(tmpText, endX, endY - defaultS, mPaint);
            } else if (endX > 0 && endY < 0 && Math.abs(endX) > Math.abs(endY)) {//y轴负方向 315<a<360
                mPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(tmpText, endX + defaultS, endY + mTmpRect.height() / 2, mPaint);
            } else {//其他
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(tmpText, endX + defaultS, endY + defaultS, mPaint);
            }
//            canvas.drawText(tmpText, endX, endY, mPaint);
            childStartAngle += allData.get(i).getNumerical() * 360;
        }
    }

    public void setData(List<PieChartPart> allData) {
        this.allData = allData;
        invalidate();
    }
}
