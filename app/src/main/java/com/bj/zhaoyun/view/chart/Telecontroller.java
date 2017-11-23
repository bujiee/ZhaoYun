package com.bj.zhaoyun.view.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Description：仿遥控器布局小样
 * Created by Buuu on 2017/11/22.
 */

public class Telecontroller extends View {
    private Context mContext;
    private Path mLeftPath, mTopPath, mRightPath, mBottomPath, mCenterPath;
    private Region mUnitRegion;
    private Region mLeftRegion, mTopRegion, mRightRegion, mBottomRegion, mCenterRegion;
    private Paint mPaint;
    private RectF mCenterRectFMax;
    private RectF mCenterRectFMin;
    private Matrix mMatrix;

    public Telecontroller(Context context) {
        this(context, null);
    }

    public Telecontroller(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Telecontroller(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initData(attrs);
    }

    private void initData(AttributeSet attrs) {
        mLeftPath = new Path();
        mTopPath = new Path();
        mRightPath = new Path();
        mBottomPath = new Path();
        mCenterPath = new Path();

        mLeftRegion = new Region();
        mTopRegion = new Region();
        mRightRegion = new Region();
        mBottomRegion = new Region();
        mCenterRegion = new Region();

        mPaint = new Paint();
//      mPaint.setTextSize(TypedValue.applyDimension());
        mPaint.setTextSize(60);
        mPaint.setColor(Color.parseColor("#3300f0ff"));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mMatrix = new Matrix();
    }

    int mWH;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMatrix.reset();
        mUnitRegion = new Region(-w, -h, w, h);
        mWH = Math.min(w, h);
        mCenterRectFMax = new RectF(-mWH / 2, -mWH / 2, mWH / 2, mWH / 2);
        mCenterRectFMin = new RectF(-mWH / 4, -mWH / 4, mWH / 4, mWH / 4);
        //center
        mCenterPath.addCircle(0, 0, (float) (0.2 * mWH), Path.Direction.CW);
        mCenterRegion.setPath(mCenterPath, mUnitRegion);
        //left
        mLeftPath.addArc(mCenterRectFMax, 145, 70);
        mLeftPath.arcTo(mCenterRectFMin, -145, -70);
        mLeftPath.close();
        mLeftRegion.setPath(mLeftPath, mUnitRegion);
        //top
        mTopPath.addArc(mCenterRectFMax, -125, 70);
        mTopPath.arcTo(mCenterRectFMin, -55, -70);
        mTopPath.close();
        mTopRegion.setPath(mTopPath, mUnitRegion);
        //right
        mRightPath.addArc(mCenterRectFMax, -35, 70);
        mRightPath.arcTo(mCenterRectFMin, 35, -70);
        mRightPath.close();
        mRightRegion.setPath(mRightPath, mUnitRegion);
        //bottom
        mBottomPath.addArc(mCenterRectFMax, 55, 70);
        mBottomPath.arcTo(mCenterRectFMin, 125, -70);
        mBottomPath.close();
        mBottomRegion.setPath(mBottomPath, mUnitRegion);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Rect rect = new Rect();
    private boolean flag = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWH / 2, mWH / 2);
        if (mMatrix.isIdentity()) {
            canvas.getMatrix().invert(mMatrix);
        }
        mPaint.setColor(Color.parseColor("#000000"));
        canvas.drawPath(mCenterPath, mPaint);
        canvas.drawPath(mLeftPath, mPaint);
        canvas.drawPath(mTopPath, mPaint);
        canvas.drawPath(mRightPath, mPaint);
        canvas.drawPath(mBottomPath, mPaint);
        //画文字
        mPaint.setColor(Color.parseColor("#ffffff"));
        float tmpWidth = mCenterRectFMax.width() / 4 + mCenterRectFMin.width() / 4;
        mPaint.getTextBounds("OK", 0, "OK".length(), rect);
        canvas.drawText("OK", 0, rect.height() / 2, mPaint);
        //Left
        mPaint.getTextBounds("L", 0, "L".length(), rect);
        canvas.drawText("L", -tmpWidth, rect.width() / 2, mPaint);
        //TOP
        mPaint.getTextBounds("T", 0, "L".length(), rect);
        canvas.drawText("T", 0, -tmpWidth + rect.width() / 2, mPaint);
        //RIGHT
        mPaint.getTextBounds("R", 0, "L".length(), rect);
        canvas.drawText("R", tmpWidth - rect.width() / 2, rect.width() / 2, mPaint);
        //BOTTOM
        mPaint.getTextBounds("B", 0, "L".length(), rect);
        canvas.drawText("B", 0, tmpWidth + rect.width() / 2, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] point = new float[2];
        point[0] = event.getRawX();
        point[1] = event.getRawY();
        mMatrix.mapPoints(point);
        int x = (int) point[0];
        int y = (int) point[1];
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCenterRegion.contains(x, y)) {
                    if (mOnTelecontrollerClickListener != null) {
                        mOnTelecontrollerClickListener.onClick("OK");
                    }
                } else if (mLeftRegion.contains(x, y)) {
                    if (mOnTelecontrollerClickListener != null) {
                        mOnTelecontrollerClickListener.onClick("L");
                    }
                } else if (mTopRegion.contains(x, y)) {
                    if (mOnTelecontrollerClickListener != null) {
                        mOnTelecontrollerClickListener.onClick("T");
                    }
                } else if (mRightRegion.contains(x, y)) {
                    if (mOnTelecontrollerClickListener != null) {
                        mOnTelecontrollerClickListener.onClick("R");
                    }
                } else if (mBottomRegion.contains(x, y)) {
                    if (mOnTelecontrollerClickListener != null) {
                        mOnTelecontrollerClickListener.onClick("B");
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private OnTelecontrollerClickListener mOnTelecontrollerClickListener;

    public void setOnTelecontrollerClickListener(OnTelecontrollerClickListener mOnTelecontrollerClickListener) {
        this.mOnTelecontrollerClickListener = mOnTelecontrollerClickListener;
    }

    public interface OnTelecontrollerClickListener {
        void onClick(String result);
    }
}
