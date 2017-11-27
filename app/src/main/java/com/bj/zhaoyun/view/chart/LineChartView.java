package com.bj.zhaoyun.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bj.zhaoyun.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:折线图
 * ^
 * |    -   -   -------     -           -           ---------
 * |    -   -   -           -           -           -       -
 * |    - - -   -------     -           -           -       -
 * |    -   -   -           -           -           -       -
 * |    -   -   -------     --------    --------    ---------
 * ------------------------------------------------------------->
 * Created by Buuu on 2017/11/16.
 */

public class LineChartView extends View {
    //折线颜色
    private int lineColor = Color.parseColor("#F75000");
    //坐标系颜色
    private int coordinateColor = Color.parseColor("#3C3C3C");
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    //x轴数据
    String[] xLineData;
    //y轴数据
    String[] yLineData;
    //坐标点
    List<Point> mPoints;
    //默认四周的宽高
    private int mDefaultLength = 50;
    private int lineWidth = 6;
    private int arrowWh = 20;
    //坐标刻度线长度
    private int mDefaultCoordinateHeight = 10;
    private int defaultTextSize = 30;
    private Rect tmpRect;
    float xLineL;
    float yLineL;
    private List<Region> regions;
    private List<Path> paths;
    private int pointColor = Color.parseColor("#FF2D2D");
    private Path linePath;
    private Context mContext;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
        mContext = context;
    }

    private void initData() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(defaultTextSize);
        tmpRect = new Rect();
        regions = new ArrayList<>();
        paths = new ArrayList<>();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        linePath = new Path();
        Region unitRegion = new Region(-w, -h, w, h);
        mWidth = w;
        mHeight = h;
        xLineL = mWidth - mDefaultLength - arrowWh - 5;
        yLineL = mHeight - mDefaultLength - arrowWh - 5;
        //画点
        for (int i = 0; i < mPoints.size(); i++) {
            Region region = new Region();
            Path path = new Path();
            float x = mDefaultLength + (float) mPoints.get(i).x / Integer.valueOf(xLineData[xLineData.length - 1]) * xLineL;
            float y = mHeight - mDefaultLength - (((yLineL * mPoints.get(i).y)) / Integer.valueOf(yLineData[yLineData.length - 1]));
            path.addCircle(x, y, 10, Path.Direction.CW);
            region.setPath(path, unitRegion);
            paths.add(path);
            regions.add(region);
            if (i == 0) {
                linePath.moveTo(x, y);
            } else {
                linePath.lineTo(x, y);
            }
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas, mDefaultLength, mHeight - mDefaultLength, mWidth - mDefaultLength, mHeight - mDefaultLength);
        drawCoordinateScaleLine(canvas, mDefaultLength, mHeight - mDefaultLength);
        mPaint.setColor(pointColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setDither(true);
        for (Path path : paths) {
            canvas.drawPath(path, mPaint);
        }
        mPaint.setColor(lineColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(linePath, mPaint);
    }

    /*画坐标系*/
    private void drawCoordinate(Canvas canvas, int x, int y, int xDistance, int yDistance) {
        //横线
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(coordinateColor);
        canvas.drawLine(x, y, x + xDistance, y, mPaint);
        canvas.drawLine(x + xDistance - arrowWh, y - arrowWh, x + xDistance, y, mPaint);
        canvas.drawLine(x + xDistance, y, x + xDistance - arrowWh, y + arrowWh, mPaint);
        //竖线
        canvas.drawLine(x, y, x, -(y + yDistance), mPaint);
        canvas.drawLine(x, y - yDistance, x - arrowWh, y - yDistance + arrowWh, mPaint);
        canvas.drawLine(x, y - yDistance, x + arrowWh, y - yDistance + arrowWh, mPaint);
    }

    /*画坐标系轴刻度*/
    private void drawCoordinateScaleLine(Canvas canvas, float x, float y) {
        if (xLineData == null || yLineData == null)
            return;
        //横向刻度线
        for (int i = 1; i < xLineData.length + 1; i++) {
            mPaint.getTextBounds(xLineData[i - 1], 0, xLineData[i - 1].length(), tmpRect);
            canvas.drawLine(x + xLineL * i / xLineData.length, y, x + xLineL * i / xLineData.length, y - mDefaultCoordinateHeight, mPaint);
            canvas.drawText(xLineData[i - 1], x + xLineL * i / xLineData.length, y + tmpRect.height() + 8, mPaint);
        }
        //画0点
        mPaint.getTextBounds("0", 0, "0".length(), tmpRect);
        canvas.drawText("0", x, y + tmpRect.height() + 8, mPaint);
        //垂直刻度线
        for (int i = 1; i < yLineData.length + 1; i++) {
            mPaint.getTextBounds(yLineData[i - 1], 0, yLineData[i - 1].length(), tmpRect);
            canvas.drawLine(x, y - yLineL * i / yLineData.length, x + mDefaultCoordinateHeight, y - yLineL * i / yLineData.length, mPaint);
            canvas.drawText(yLineData[i - 1], x - tmpRect.width() / 2 - 8, y - yLineL * i / yLineData.length + tmpRect.height() / 2, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < regions.size(); i++) {
                    Rect tmpRect = regions.get(i).getBounds();
                    tmpRect.top = tmpRect.top - 20;
                    tmpRect.left = tmpRect.left - 20;
                    tmpRect.right = tmpRect.right + 20;
                    tmpRect.bottom = tmpRect.bottom + 20;
                    if (tmpRect.contains(x, y)) {
                        if (onPointClickListener!=null){
                            onPointClickListener.onPointClick(i);
                        }
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setData(String[] xLineData, String[] yLineData, List<Point> mPoints) {
        this.xLineData = xLineData;
        this.yLineData = yLineData;
        this.mPoints = mPoints;
    }

    private OnPointClickListener onPointClickListener;

    public void setOnPointClickListener(OnPointClickListener onPointClickListener) {
        this.onPointClickListener=onPointClickListener;
    }

    public interface OnPointClickListener {
        void onPointClick(int position);
    }
}