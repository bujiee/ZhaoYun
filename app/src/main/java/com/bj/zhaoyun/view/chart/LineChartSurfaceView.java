package com.bj.zhaoyun.view.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import java.util.List;

@SuppressLint("ClickableViewAccessibility")
public class LineChartSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    /**
     * surface持有者
     */
    private SurfaceHolder mHolder;
    /**
     * 当前画布
     */
    private Canvas mCanvas;
    /**
     * 是否开始绘画
     */
    private boolean mIsDrawing;

    /**
     * 坐标轴举例view边框的距离
     */
    private static final int DEFAULT_PADDING = 10;
    /**
     * 默认宽度设置为400dp
     */
    private static final int DEFAULT_WIDTH = 300;
    /**
     * 默认高度设置为400dp
     */
    private static final int DEFAULT_HEIGHT = 400;
    /**
     * 默认折线的颜色
     */
    private static final int DEFAULT_LINE_COLOR = 0xff000000;
    /**
     * 默认x轴的颜色
     */
    private static final int DEFAULT_X_LINE_COLOR = 0xff000000;
    /**
     * 默认Y轴的颜色
     */
    private static final int DEFAULT_Y_LINE_COLOR = 0xff000000;
    /**
     * 默认折线的宽度
     */
    private static final int DEFAULT_LINE_WIDTH = 1;
    /**
     * 默认x轴的量程
     */
    private static final int DEFAULT_X_RANGE = 1000;
    /**
     * 默认y轴的量程
     */
    private static final int DEFAULT_Y_RANGE = 1000;
    /**
     * 默认x轴每个刻度代表的单位
     */
    private static final int DEFAULT_X_UNIT = 50;
    /**
     * 默认y轴每个刻度代表的单位
     */
    private static final int DEFAULT_Y_UNIT = 50;
    /**
     * 每个刻度的高度
     */
    private static final int DEFAULT_SCALE_HEIGHT = 4;
    /**
     * 默认x轴尽头箭头的长度
     */
    private static final int DEFAULT_X_BLANK = 20;
    /**
     * 默认y轴尽头箭头的长度
     */
    private static final int DEFAULT_Y_BLANK = 20;
    /**
     * x轴的单位
     */
    private int mXUnit;
    /**
     * 每个y轴的单位的高度
     */
    private int mYUnit;
    /**
     * 折线的颜色
     */
    private int mLineColor;
    /**
     * x轴的颜色
     */
    private int mXLineColor;
    /**
     * y轴的颜色
     */
    private int mYLineColor;
    /**
     * 折线的宽度
     */
    private int mLineWidth;
    /**
     * x轴的量程
     */
    private int mXRange;
    /**
     * y轴的量程
     */
    private int mYRange;
    /**
     * 绘制折线的画笔
     */
    private Paint mLinePaint;
    /**
     * 绘制坐标的画笔
     */
    private Paint mCoordinatePaint;
    /**
     * view的宽度
     */
    private int mWidth;
    /**
     * View的高度
     */
    private int mHeight;
    /**
     * 坐标轴距离view边距的间距
     */
    private int mPaddint;
    /**
     * 刻度的高度
     */
    private int mScaleHeight;
    /**
     * y轴的实际高度
     */
    private int mAxisHeight;
    /**
     * x轴的实际高度
     */
    private int mAxisWidth;
    /**
     * X轴最后一个竖线与箭头的空格
     */
    private int mXBlank;
    /**
     * Y轴最后一个竖线与箭头的空格
     */
    private int mYBlank;
    /**
     * 每个x轴刻度的宽度
     */
    private int mXScaleWidth;
    /**
     * 每个y轴刻度的宽度
     */
    private int mYScaleWidth;

    /**
     * 外部的list，用来存放折线上的值
     */
    private List<Integer> mLines;
    /**
     * 最后一次点击的x坐标
     */
    private int lastX;
    /**
     * 偏移量，用来实现平滑移动
     */
    private int mOffset = 0;
    /**
     * 移动速度，用来实现速度递减
     */
    private int mSpeed = 0;
    /**
     * 是否触摸屏幕
     */
    private boolean mIsTouch = false;
    /**
     * 时间计数器，用来快速滚动时候减速
     */
    private int time = 0;
    /**
     * 移动时候X方向上的速度
     */
    private double xVelocity = 0;
    /**
     * 是否可以滚动，当不在范围时候不可以滚动
     */
    private boolean isScroll = true;
    /**
     * x轴上的格子数量
     */
    private int mXScaleNum;
    /**
     * y轴上的格子数量
     */
    private int mYScaleNum;

    public LineChartSurfaceView(Context context) {
        this(context, null);
    }

    public LineChartSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LineChartSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 设置一些变量的尺寸
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = dp2px(getContext(), DEFAULT_WIDTH);
        } else {
            mWidth = Math.max(widthSize, dp2px(getContext(), DEFAULT_WIDTH));
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = dp2px(getContext(), DEFAULT_HEIGHT);
        } else {
            mHeight = Math.max(heightSize, dp2px(getContext(), DEFAULT_HEIGHT));
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 初始化surfaceView操作
     */
    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        // 设置可以获取焦点
        setFocusable(true);
        // 进入触摸输入模式后,该控件是否还有获得焦点的能力
        setFocusableInTouchMode(true);
        // 是否保持屏幕常亮
        this.setKeepScreenOn(true);
        // 为部分成员变量赋初值，可以写成自定义属性
        mLineColor = DEFAULT_LINE_COLOR;
        mLineWidth = dp2px(getContext(), DEFAULT_LINE_WIDTH);
        mXLineColor = DEFAULT_X_LINE_COLOR;
        mYLineColor = DEFAULT_Y_LINE_COLOR;
        mXRange = DEFAULT_X_RANGE;
        mYRange = DEFAULT_Y_RANGE;
        mXUnit = DEFAULT_X_UNIT;
        mYUnit = DEFAULT_Y_UNIT;
        mPaddint = dp2px(getContext(), DEFAULT_PADDING);
        mScaleHeight = dp2px(getContext(), DEFAULT_SCALE_HEIGHT);

        mLinePaint = new Paint();
        mCoordinatePaint = new Paint();
    }

    /**
     * 子线程，循环绘制折线
     */
    @Override
    public void run() {
        // 如果处于绘画状态，那么就开始绘制
        while (mIsDrawing) {
            // 判断是否处于边缘状态
            setSpeedCut();
            // 绘制方法
            draw();
        }
    }

    /**
     * 具体的绘制方法
     */
    private void draw() {
        try {
            long start = System.currentTimeMillis();
            // 获取并锁定画布
            mCanvas = mHolder.lockCanvas();
            // 设置画布背景为白色
            mCanvas.drawColor(0xffffffff);
            // 绘制坐标轴
            drawAxis();
            // 绘制曲线
            drawLine();
            long end = System.currentTimeMillis();
            if (end - start < 50) {
                Thread.sleep(50 - (end - start));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                // 保证每次都将绘制的内容提交到服务器
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     * 绘制坐标系
     */
    private void drawAxis() {
        mCanvas.save();
        // 移动坐标原点到左下角
        mCanvas.translate(mPaddint, mHeight - mPaddint);
        // 画线
        drawXYLine();
        // 绘制坐标轴上的刻度
        // 绘制xy轴末端的箭头
        drawXYScale();
        mCanvas.restore();
    }

    /**
     * 绘制折线
     */
    private void drawLine() {
        mCanvas.save();
        mCanvas.translate(mPaddint, mHeight - mPaddint);
        // 设置画笔属性
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Style.STROKE);
        // 如果折线集合不为空
        if (mLines != null && mLines.size() > 0) {
            // 循环绘制所有线条
            for (int i = 1; i < mLines.size(); i++) {
                // 上一个点的xy坐标
                int previousY = (int) (mLines.get(i - 1) * 1.0 / mYRange * (mAxisHeight - mYBlank));
                int previousX = (i - 1) * mXScaleWidth + mOffset;
                // 当前点的xy坐标
                int thisY = (int) (mLines.get(i) * 1.0 / mYRange * (mAxisHeight - mYBlank));
                int thisX = i * mXScaleWidth + mOffset;
                // 保证只绘制坐标轴范围内的部分
                if (previousX > 0 && previousX < mAxisWidth - mXBlank && thisX > 0 && thisX < mAxisWidth - mXBlank)
                    // 两个坐标连线
                    mCanvas.drawLine(previousX, -previousY, thisX, -thisY, mLinePaint);
            }
        }
        mCanvas.restore();
    }

    /**
     * 设置快速滚动时，末尾的减速
     */
    private void setSpeedCut() {
        if (!mIsTouch && isScroll) {
            // 通过当前速度计算所对应的偏移量
            mOffset = mOffset + mSpeed;
            setOffsetRange();
        }
        // 每次偏移量的计算
        if (mSpeed != 0) {
            time++;
            mSpeed = (int) (xVelocity + time * time * (xVelocity / 1600.0) - (xVelocity / 20.0) * time);
        } else {
            time = 0;
            mSpeed = 0;
        }

    }

    /**
     * 绘制x、y轴
     */
    private void drawXYLine() {
        mAxisWidth = mWidth - 2 * mPaddint;
        mAxisHeight = mHeight - 2 * mPaddint;
        mCoordinatePaint.setStrokeWidth(mLineWidth);
        mCoordinatePaint.setAntiAlias(true);
        // 绘制x轴
        mCoordinatePaint.setColor(mXLineColor);
        mCanvas.drawLine(0, 0, mAxisWidth, 0, mCoordinatePaint);
        // 绘制y轴
        mCoordinatePaint.setColor(mYLineColor);
        mCanvas.drawLine(0, 0, 0, -mAxisHeight, mCoordinatePaint);
    }

    /**
     * 绘制刻度线和箭头
     */
    private void drawXYScale() {
        // 画x轴的刻度
        // x轴的分割线数量
        mXScaleNum = mXRange / mXUnit;
        // x轴尽头的空隙
        mXBlank = dp2px(getContext(), DEFAULT_X_BLANK);
        // 每个刻度的宽度
        mXScaleWidth = (int) ((mAxisWidth - mXBlank) * 1.0 / mXScaleNum);
        for (int i = 0; i < mXScaleNum; i++) {
            mCanvas.drawLine(mXScaleWidth * (i + 1), 0, mXScaleWidth * (i + 1), -mScaleHeight, mCoordinatePaint);
        }
        // 画y轴的刻度
        mYScaleNum = mYRange / mYUnit;
        // y轴尽头的空间
        mYBlank = dp2px(getContext(), DEFAULT_Y_BLANK);
        mYScaleWidth = (int) ((mAxisHeight - mYBlank) * 1.0 / mYScaleNum);
        for (int i = 0; i < mYScaleNum; i++) {
            mCanvas.drawLine(0, -mYScaleWidth * (i + 1), mScaleHeight, -mYScaleWidth * (i + 1), mCoordinatePaint);
        }

        // 画X轴的箭头
        mCanvas.drawLine(mAxisWidth, 0, mAxisWidth - mScaleHeight * 2, -mScaleHeight, mCoordinatePaint);
        mCanvas.drawLine(mAxisWidth, 0, mAxisWidth - mScaleHeight * 2, mScaleHeight, mCoordinatePaint);
        // 画Y轴的箭头
        mCanvas.drawLine(0, -mAxisHeight, mScaleHeight, -mAxisHeight + 2 * mScaleHeight, mCoordinatePaint);
        mCanvas.drawLine(0, -mAxisHeight, -mScaleHeight, -mAxisHeight + 2 * mScaleHeight, mCoordinatePaint);
    }

    /**
     * 对偏移量进行边界值判定
     */
    private void setOffsetRange() {
        int offsetMax = -mXScaleWidth * (mLines.size()) + (mXScaleWidth * mXScaleNum + 2);
        int offsetMin = 2 * mXScaleWidth;
        if (mOffset > offsetMin) {
            isScroll = false;
            mOffset = offsetMin;
        } else if (mOffset < offsetMax) {// 如果划出最大值范围
            isScroll = false;
            mOffset = offsetMax;
        } else {
            isScroll = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) (event.getRawX());
        // 计算当前速度
        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        // 计算速度的单位时间
        velocityTracker.computeCurrentVelocity(50);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸点坐标
                lastX = rawX;
                mIsTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算便宜量
                int offsetX = rawX - lastX;
                // 在当前偏移量的基础上增加偏移量
                mOffset = mOffset + offsetX;
                setOffsetRange();
                // 偏移量修改后下次重绘会有变化
                lastX = rawX;
                // 获取X方向上的速度
                xVelocity = velocityTracker.getXVelocity();
                mSpeed = (int) xVelocity;
                break;
            case MotionEvent.ACTION_UP:
                mIsTouch = false;
                break;
        }
        // 计算完成后回收内存
        velocityTracker.clear();
        velocityTracker.recycle();
        return true;
    }

    /**
     * 画布创建时候执行的方法
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 开始绘画
        mIsDrawing = true;
        // 启动绘画线程
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * 画布销毁时候执行的方法
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 停止绘画
        mIsDrawing = false;
    }

    /**
     * 外部用来设置折线数据的方法
     */
    public void setBloodPressure(List<Integer> lines) {
        this.mLines = lines;
        invalidate();
    }

    /**
     * dp转化为px工具
     */
    private int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
