package com.bj.zhaoyun.view.picture;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Description：根据手势放大缩小图片
 * 1.双击放大/缩小
 * 2.根据手势放大/缩小/移动
 * 3.截取指定位置图片
 * Created by Buuu on 2017/11/27.
 */

public class GestureImageView extends android.support.v7.widget.AppCompatImageView implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    // ScaleGestureDetector.OnScaleGestureListener//
//    GestureDetector.SimpleOnGestureListener

    //                ScaleGestureDetector
//    Matrix.Max
    private float scale = 1f;
    private int maxScale = 4;
    private Matrix mImgMatrix;
    private ScaleGestureDetector scaleGestureDetector;
    private Context mContext;

    public GestureImageView(Context context) {
        this(context, null);
    }

    public GestureImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initData();
    }

    private boolean done = false;

    private void initData() {
        setScaleType(ScaleType.MATRIX);
        mImgMatrix = new Matrix();
        setOnTouchListener(this);
        scaleGestureDetector = new ScaleGestureDetector(mContext, this);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //设置矩阵的中心点,View第一次加载的时候会调用
                Drawable drawable = getDrawable();
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                int width = getWidth();
                int height = getHeight();
                if (intrinsicWidth <= width && intrinsicHeight <= height) {
                    scale = 1.0f;
                } else if (intrinsicWidth >= width && intrinsicHeight <= height) {
                    scale = width * 1.0f / intrinsicWidth;//防止强转出错
                } else if (intrinsicWidth <= width && intrinsicHeight >= height) {
                    scale = height * 1.0f / intrinsicHeight;//防止强转出错
                } else if (intrinsicWidth >= width && intrinsicHeight >= height) {
                    scale = Math.max(width * 1.0f / intrinsicWidth, height * 1.0f / intrinsicHeight);
                }
                mImgMatrix.postTranslate((width - intrinsicWidth) / 2, (height - intrinsicHeight) / 2);
                mImgMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
                setImageMatrix(mImgMatrix);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float iScale = detector.getScaleFactor();
//        System.out.println("iScale" + iScale);
        if (getDrawable() == null) {
            return true;
        }
        float iiScale = getScale();
        //设置范围值
        if (iScale >= 1.0f && iiScale <= maxScale || iScale <= 1.0F && iiScale >= scale) {
            if (iiScale * iScale > maxScale) {
                iScale = maxScale / iiScale;
            }
            if (iiScale * iScale < scale) {
                iScale = scale / iiScale;
            }
//            mImgMatrix.postScale(iScale, iScale, getWidth() / 2, getHeight() / 2);
            //前乘后乘
            mImgMatrix.postScale(iScale, iScale, detector.getFocusX(), detector.getFocusY());
            scopeControl();
            setImageMatrix(mImgMatrix);
        }
        return true;
    }


    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return scaleGestureDetector.onTouchEvent(event);
    }

    /*获取Scale比值*/
    float[] values = new float[9];

    private float getScale() {
        mImgMatrix.getValues(values);
        return values[Matrix.MSCALE_Y];
    }

    /**
     * 范围控制
     */
    private void scopeControl() {
        float x = 0;
        float y = 0;
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        RectF rectF = new RectF();
        //需要设置getIntrinsicWidth(),getIntrinsicHeight(),因为图片有宽高,需要在起始点上相加
        rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        mImgMatrix.mapRect(rectF);

        if (rectF.width() > getWidth()) {
            if (rectF.left > 0) {
                x = -rectF.left;
            }
            if (rectF.right < getWidth()) {
                x = getWidth() - rectF.right;
            }
        }
        if (rectF.height() > getHeight()) {
            if (rectF.top > 0) {
                y = -rectF.top;
            }
            if (rectF.bottom < getHeight()) {
                y = getHeight() - rectF.bottom;
            }
        }
        //在范围内
        if (rectF.width() < getWidth()) {
            x = getWidth() / 2 - rectF.right + rectF.width() / 2;
        }
        if (rectF.height() < getHeight()) {
            y = getHeight() / 2 - rectF.bottom + rectF.height() / 2;
        }
        mImgMatrix.postTranslate(x, y);
    }
}
