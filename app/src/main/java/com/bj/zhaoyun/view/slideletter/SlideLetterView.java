package com.bj.zhaoyun.view.slideletter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bj.zhaoyun.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：侧滑字符选择栏
 * 字符集合,字符大小,字符颜色(选择,非选择),滑动背景
 * Created by Buuu on 2017/11/14.
 */

public class SlideLetterView extends View {
    private int letter_default_color;
    private int letter_selected_color;
    private int letter_size;
    private int letter_selected_bg_color;
    private Context mContext;
    private List<String> mDatas = new ArrayList<>();
    private Paint mPaint;
    //
    private boolean isBackground = false;
    private int choose = -1;

    public SlideLetterView(Context context) {
        this(context, null);
    }

    public SlideLetterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLetterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initData(attrs);
    }

    public void setDatas(List<String> mDatas) {
        this.mDatas = mDatas;
        invalidate();
    }

    private void initData(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SlideLetterView);
        letter_default_color = typedArray.getColor(R.styleable.SlideLetterView_letter_default_color, Color.parseColor("#999999"));
        letter_selected_color = typedArray.getColor(R.styleable.SlideLetterView_letter_selected_color, Color.parseColor("#999999"));
        letter_size = typedArray.getDimensionPixelSize(R.styleable.SlideLetterView_letter_size, 40);
        letter_selected_bg_color = typedArray.getColor(R.styleable.SlideLetterView_letter_selected_bg_color, Color.parseColor("#3333ff00"));
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isBackground) {
            canvas.drawColor(letter_selected_bg_color);
        } else {
            canvas.drawColor(Color.TRANSPARENT);
        }
        if (mDatas == null || mDatas.isEmpty()) {
            return;
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int singleLetterHeight = height / mDatas.size();
        for (int i = 0; i < mDatas.size(); i++) {
            mPaint.setColor(letter_default_color);
            if (choose == i) {
                mPaint.setColor(letter_selected_color);
            }
            mPaint.setTextSize(letter_size);
            canvas.drawText(mDatas.get(i), (width - mPaint.measureText(mDatas.get(i))) / 2, (i + 1) * singleLetterHeight, mPaint);
            mPaint.reset();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float y = event.getY();
        choose = (int) ((y / getMeasuredHeight()) * mDatas.size());
        System.out.println("choose-->" + choose);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (letterChangeListener != null && choose >= 0&&choose<mDatas.size())
                    letterChangeListener.onLetterChange(mDatas.get(choose), choose);
                isBackground = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (letterChangeListener != null && choose >= 0&&choose<mDatas.size())
                    letterChangeListener.onLetterChange(mDatas.get(choose), choose);
                isBackground = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                choose = -1;
                if (letterChangeListener != null)
                    letterChangeListener.onLetterChange("", choose);
                isBackground = false;
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public interface LetterChangeListener {
        void onLetterChange(String letter, int choose);
    }

    public void setOnLetterChangeListener(LetterChangeListener letterChangeListener) {
        this.letterChangeListener = letterChangeListener;
    }

    private LetterChangeListener letterChangeListener;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
