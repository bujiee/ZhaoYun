package com.bj.zhaoyun.view.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bj.zhaoyun.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：QRCode 中间的扫描View
 * Created by Buuu on 2017/12/7.
 */

public class QRCodeCenterRectangleView extends View {
    //TODO 处理生命周期
    private int cornerWidth = 20;//四个角的线条宽度
    private int cornerLength = 75;//四个角的线条长度
    private int cornerColor = Color.parseColor("#62de11");//四个角的线条宽度颜色
    private String description = "将二维码/条码放入框内,即可自动扫描";
    private int defaultCornerWidthHeight;
    private Paint mPaint;
    private List<Path> mCornerPath;
    private int mDescriptionTextMargin;//描述文字上边距
    private int mDescriptionTextSize;//描述文字大小
    private int mCenterStrokeWidth;//中间扫框线条宽度
    private int mDescriptionTextColor = Color.parseColor("#88000000");//描述文字颜色
    private int mCenterLineColor = Color.parseColor("#33000000");//中间扫框线条颜色
    private Rect maxRect = new Rect();
    private Rect centertRect = new Rect();

    public QRCodeCenterRectangleView(Context context) {
        this(context, null);
    }

    public QRCodeCenterRectangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QRCodeCenterRectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultCornerWidthHeight = DisplayUtil.dip2px(context, 200);//默认宽高200dp
        mDescriptionTextMargin = DisplayUtil.dip2px(context, 19);//
        mDescriptionTextSize = DisplayUtil.sp2px(context, 15);//
        mCenterStrokeWidth = DisplayUtil.sp2px(context, 1);//
        initData();
    }

    private void initData() {
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(cornerWidth);
        mPaint.setColor(cornerColor);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mCornerPath = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int www = defaultCornerWidthHeight / 2;
        maxRect.set(-w / 2, w / 2, w / 2, w / 2);
        centertRect.set(-www - cornerWidth / 2, -www - cornerWidth / 2, www + cornerWidth / 2, www + cornerWidth / 2);

        //left_top
        Path mPath = new Path();
        mPath.moveTo(-www, -www + cornerLength);
        mPath.lineTo(-www, -www - cornerWidth / 2);
        mPath.moveTo(-www, -www);
        mPath.lineTo(-www + cornerLength, -www);
        mCornerPath.add(mPath);
        //right_top
        Path mPath1 = new Path();
        mPath1.moveTo(www - cornerLength, -www);
        mPath1.lineTo(www, -www);
        mPath1.moveTo(www, -www - cornerWidth / 2);
        mPath1.lineTo(www, -www + cornerLength);
        mCornerPath.add(mPath1);
        //left_bottom
        Path mPath2 = new Path();
        mPath2.moveTo(-www, www - cornerLength);
        mPath2.lineTo(-www, www);
        mPath2.moveTo(-www - cornerWidth / 2, www);
        mPath2.lineTo(-www + cornerLength, www);
        mCornerPath.add(mPath2);
        //right_bottom
        Path mPath3 = new Path();
        mPath3.moveTo(www - cornerLength, www);
        mPath3.lineTo(www, www);
        mPath3.moveTo(www, www + cornerWidth / 2);
        mPath3.lineTo(www, www - cornerLength);
        mCornerPath.add(mPath3);

    }

    private Rect textRect = new Rect();

    private Paint mCenterPaint = new Paint();

    /**
     * 获取预览的PreviewRect,用于扫描二维码的时候获取指定位置的图片的图像
     */
    public Rect getPreviewRect() {
        Rect rect = new Rect();
        rect.set(0, 0, centertRect.width(), centertRect.height());
        return rect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        //画四周阴影
        mCenterPaint.setColor(Color.BLACK);
        mCenterPaint.setStyle(Paint.Style.FILL);
        mCenterPaint.setAlpha(88);
        int halfWidth = getWidth() / 2;
        int halfHeight = getHeight() / 2;
        canvas.drawRect(-halfWidth, -halfHeight, -centertRect.width() / 2, halfHeight, mCenterPaint);
        canvas.drawRect(-centertRect.width() / 2, -halfHeight, centertRect.width() / 2, -centertRect.width() / 2, mCenterPaint);
        canvas.drawRect(centertRect.width() / 2, -halfHeight, halfWidth, halfHeight, mCenterPaint);
        canvas.drawRect(-centertRect.width() / 2, centertRect.height() / 2, centertRect.width() / 2, halfHeight, mCenterPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAlpha(0);
        mPaint.setColor(mCenterLineColor);
        mPaint.setStrokeWidth(mCenterStrokeWidth);
        canvas.drawRect(centertRect, mPaint);

        for (Path path : mCornerPath) {
            mPaint.setAlpha(255);
            mPaint.setColor(cornerColor);
            mPaint.setStrokeWidth(cornerWidth);
            canvas.drawPath(path, mPaint);
        }

        //画文字
        mPaint.setTextSize(mDescriptionTextSize);
        mPaint.setColor(mDescriptionTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.getTextBounds(description, 0, description.length(), textRect);
        canvas.drawText(description, 0, defaultCornerWidthHeight / 2 + textRect.height() + mDescriptionTextMargin, mPaint);
    }
}
