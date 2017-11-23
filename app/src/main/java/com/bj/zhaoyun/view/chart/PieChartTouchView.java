package com.bj.zhaoyun.view.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：饼状图展示,有接口回调
 * Created by Buuu on 2017/11/23.
 */

public class PieChartTouchView extends View {
    private double[] mDescription;
    private int[] mColor;
    private List<Region> mRegionList = new ArrayList<>();
    private List<Path> mPathList = new ArrayList<>();
    private Paint mPaint;
    private Matrix mMatrix;
    private List<Point> mPoints = new ArrayList<>();

    public PieChartTouchView(Context context) {
        this(context, null);
    }

    public PieChartTouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(40);
        mMatrix = new Matrix();
    }

    private int flagIndex = -1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] point = new float[2];
        point[0] = event.getRawX();
        point[1] = event.getRawY();
        mMatrix.mapPoints(point);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int x = 0; x < mRegionList.size(); x++) {
                    if (mRegionList.get(x).contains((int) point[0], (int) point[1])) {
                        flagIndex = x;
                    }
                }
                if (mOnPieChartChildClickListener != null) {
                    mOnPieChartChildClickListener.onClick(flagIndex);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                flagIndex = -1;
                invalidate();
                break;
            default:
                flagIndex = -1;
                invalidate();
                break;

        }

        return true;
    }

    private float startAngle = 0f;
    private int mWh;
    private Rect mTmpRect = new Rect();

    //初始化时会被调用一次  onMeasure -> onSizeChanged -> onDraw
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float startAngleChild = startAngle;
        mMatrix.reset();
        Region unitRegion = new Region(-w, -h, w, h);
        mWh = Math.min(w, h);
        RectF mRectFMax = new RectF(-mWh / 2, -mWh / 2, mWh / 2, mWh / 2);
        if (mDescription != null) {
            for (double aMDescription : mDescription) {
                Region region = new Region();
                Path path = new Path();
                path.addArc(mRectFMax, startAngleChild, (float) (360 * aMDescription));
                path.arcTo(-0, -0, 0, 0, startAngleChild + (float) (360 * aMDescription), -(float) (360 * aMDescription), false);
                path.close();
                region.setPath(path, unitRegion);
                mPathList.add(path);
                mRegionList.add(region);
                String tmpString = ((int) ((aMDescription * 100))) / 100 + "%";
                mPaint.getTextBounds(tmpString, 0, tmpString.length(), mTmpRect);
                Point point = new Point((int) (mWh / 2 * Math.cos(Math.toRadians(startAngleChild + (float) (360 * aMDescription) / 2))) / 2,
                        (int) (mWh / 2 * Math.sin(Math.toRadians(startAngleChild + (float) (360 * aMDescription) / 2))) / 2 + mTmpRect.width() / 2);

                mPoints.add(point);
                startAngleChild += 360 * aMDescription;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWh / 2, mWh / 2);
        if (mMatrix.isIdentity()) {
            canvas.getMatrix().invert(mMatrix);
        }
        for (int i = 0; i < mColor.length; i++) {
            if (flagIndex == i) {
                mPaint.setColor(Color.parseColor("#2333ff00"));
            } else {
                mPaint.setColor(mColor[i]);
            }
            canvas.drawPath(mPathList.get(i), mPaint);
            mPaint.setColor(Color.parseColor("#000000"));
            String tmpString = ((int) ((mDescription[i] * 10000))) / 100 + "%";
            canvas.drawText(tmpString, mPoints.get(i).x, mPoints.get(i).y, mPaint);
        }
    }

    /**
     * 设置颜色,以及对应的描述
     *
     * @param mDescription 百分比
     * @param mColor       颜色
     */
    public void setData(double[] mDescription, int[] mColor, float startAngle) {
        this.mDescription = mDescription;
        this.mColor = mColor;
        this.startAngle = startAngle;
        invalidate();
    }

    public interface OnPieChartChildClickListener {
        void onClick(int position);
    }

    public void setOnPieChartChildClickListener(OnPieChartChildClickListener mOnPieChartChildClickListener) {
        this.mOnPieChartChildClickListener = mOnPieChartChildClickListener;
    }

    private OnPieChartChildClickListener mOnPieChartChildClickListener;
}
