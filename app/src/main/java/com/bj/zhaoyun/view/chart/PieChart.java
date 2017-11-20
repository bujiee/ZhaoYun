package com.bj.zhaoyun.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description：饼状图
 * Created by Buuu on 2017/11/16.
 */

public class PieChart extends View {
    private Paint mPaint;
    private double[] mNum;
    private int[] mColor;
    private RectF mRectF;
    private Rect tmpRectF;
    private float mStartAngle = 0f;

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRectF = new RectF();
        tmpRectF = new Rect();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mNum == null)
            return;
        float startAngle = mStartAngle;
        //画图
        for (int x = 0; x < mNum.length; x++) {
            mPaint.setColor(mColor[x]);
            canvas.save();
            canvas.rotate(startAngle, mRectF.centerX(), mRectF.centerY());
            canvas.drawArc(mRectF, 0, (float) (360 * mNum[x]), true, mPaint);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(80);
            startAngle += 360 * mNum[x];
            mPaint.reset();
            canvas.restore();

        }
        startAngle = mStartAngle;
        //画字
        canvas.translate(mRectF.centerX(), mRectF.centerY());
        for (int x = 0; x < mNum.length; x++) {
            mPaint.setColor(mColor[x]);
            mPaint.setTextAlign(Paint.Align.CENTER);
            double r = mRectF.width() / 2;
            String result = String.valueOf(((int) (mNum[x] * 100))) + "%";
            mPaint.getTextBounds(result, 0, result.length(), tmpRectF);
            float textX = (float) (Math.cos(Math.toRadians(startAngle + (mNum[x] * 360) / 2)) * r) / 2;
            float textY = (float) (Math.sin(Math.toRadians(startAngle + (mNum[x] * 360) / 2)) * r) / 2 + tmpRectF.height() / 2;
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(40);
            canvas.drawText(result, textX, textY, mPaint);
            startAngle += 360 * mNum[x];
            mPaint.reset();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0, height = 0;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
            default:
                break;
        }
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
            default:
                break;
        }
        int mWH = Math.max(width, height);
        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = (float) mWH;
        mRectF.bottom = (float) mWH;
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(mWH, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mWH, MeasureSpec.EXACTLY));
    }

    /**
     * @param num   添加的百分数
     * @param color 对应的颜色
     */
    public void addData(double[] num, int[] color, float startAngle) {
        this.mNum = num;
        this.mColor = color;
        this.mStartAngle = startAngle;
        if (mNum.length == color.length) {
            invalidate();
        }
    }
}
