package com.bj.zhaoyun.view.picture;

import android.content.Context;
import android.graphics.ImageFormat;
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

/**
 * Description：根据手势放大缩小图片
 * 1.双击放大/缩小
 * 2.根据手势放大/缩小/移动
 * 3.截取指定位置图片
 * 知识点:
 * Matrix.postTranslate
 * Matrix.postScale
 * 前乘后乘
 * 范围控制
 * Drawable.getIntrinsicWidth()
 * Drawable.getIntrinsicHeight()
 * Created by Buuu on 2017/11/27.
 */

public class GestureImageView extends android.support.v7.widget.AppCompatImageView
        implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private float scale = 1f;
    private int maxScale = 4;
    private Matrix mImgMatrix;
    private ScaleGestureDetector scaleGestureDetector;
    private Context mContext;
    private GestureDetector mGestureDetector;
    private float originIntrinsicWidth = 0F;
    private float originIntrinsicHeight = 0F;

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

                originIntrinsicWidth = intrinsicWidth;
                originIntrinsicHeight = intrinsicHeight;
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
        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    RectF rectF = getRectF(drawable);
                    if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                        if (originIntrinsicHeight < getHeight() || originIntrinsicWidth < getWidth()) {
                            float childScale = Math.min(originIntrinsicHeight / rectF.width(), originIntrinsicWidth / rectF.height());
                            mImgMatrix.postScale(childScale, childScale, e.getX(), e.getY());
                        } else {
                            float childScale = Math.min(getWidth() / rectF.width(), getHeight() / rectF.height());
                            mImgMatrix.postScale(childScale, childScale, e.getX(), e.getY());
                        }

                    } else if (rectF.height() <= getHeight() && rectF.width() <= getWidth()) {
                        if (originIntrinsicHeight < getHeight() || originIntrinsicWidth < getWidth()) {
                            if (rectF.width() > originIntrinsicWidth || rectF.height() > originIntrinsicHeight) {
                                mImgMatrix.postScale(originIntrinsicWidth / rectF.width(), originIntrinsicHeight / rectF.height(), e.getX(), e.getY());
                            } else {
                                mImgMatrix.postScale(2, 2, e.getX(), e.getY());
                            }

                        } else {
                            mImgMatrix.postScale(2, 2, e.getX(), e.getY());
                        }
                    }
                }
                scopeControl();//todo double control
                setImageMatrix(mImgMatrix);
                return super.onDoubleTap(e);
            }
        });
    }

    private boolean flag = true;


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float iScale = detector.getScaleFactor();
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

    /*处理多指触控*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        translateControl(event);
        mGestureDetector.onTouchEvent(event);
        touchControl(event);
        return true;
    }

    /*处理华东冲突*/
    private void touchControl(MotionEvent event) {
        RectF rectF = getRectF(getDrawable());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            default:
                break;
        }
    }

    float lastX = 0f;
    float lastY = 0f;
    private boolean lastPointCount = true;

    private void translateControl(MotionEvent event) {
        float x;
        float y;
        x = event.getX();
        y = event.getY();
        //只能单指移动
        if (event.getPointerCount() > 1) {
            return;
        }
        if (lastPointCount) {
            lastX = x;
            lastY = y;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    translateScopeControl(drawable, x, y);
                    setImageMatrix(mImgMatrix);
                }
                lastX = x;
                lastY = y;
                lastPointCount = false;
                break;
            case MotionEvent.ACTION_UP:
                lastPointCount = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                lastPointCount = true;
                break;
            default:
                break;
        }
    }

    /*单指移动范围控制*/
    private void translateScopeControl(Drawable drawable, float x, float y) {
        float width = getWidth();
        float height = getHeight();
        float dx = x - lastX;
        float dy = y - lastY;
        RectF rectF = getRectF(drawable);
        if (rectF.width() <= width) {
            //禁止移动
            dx = 0;
        } else if (rectF.left + dx > 0) {
            if (rectF.left < 0) {
                dx = -rectF.left;
            } else {
                dx = 0;
            }
        } else if (rectF.right + dx < width) {
            if (rectF.right > width) {
                dx = width - rectF.right;
            } else {
                dx = 0;
            }
        }

        if (rectF.height() <= height) {
            dy = 0;
        } else if (rectF.top + dy > 0) {
            if (rectF.top < 0) {
                dy = -rectF.top;
            } else {
                dy = 0;
            }
        } else if (rectF.bottom + dy < height) {
            if (rectF.bottom > height) {
                dy = height - rectF.bottom;
            } else {
                dy = 0;
            }
        }
        mImgMatrix.postTranslate(dx, dy);

    }

    /*获取Scale比值*/
    float[] values = new float[9];

    private float getScale() {
        mImgMatrix.getValues(values);
        return values[Matrix.MSCALE_Y];
    }

    /*范围控制*/
    private void scopeControl() {
        float x = 0;
        float y = 0;
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        RectF rectF = getRectF(drawable);
        if (rectF.width() >= getWidth()) {
            if (rectF.left > 0) {
                x = -rectF.left;
            }
            if (rectF.right < getWidth()) {
                x = getWidth() - rectF.right;
            }
        }
        if (rectF.height() >= getHeight()) {
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

    private RectF getRectF(Drawable drawable) {
        RectF rectF = new RectF();
        //需要设置getIntrinsicWidth(),getIntrinsicHeight(),因为图片有宽高,需要在起始点上相加
        rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        mImgMatrix.mapRect(rectF);
        return rectF;
    }

}
