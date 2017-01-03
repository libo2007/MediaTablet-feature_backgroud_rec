package com.jiaying.mediatablet.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * 作者：lenovo on 2016/3/27 12:19
 * 邮箱：353510746@qq.com
 * 功能：竖直progressbar
 */
public class HorizontalProgressBar extends ProgressBar {
    private String text;
    private Paint mPaint;

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public HorizontalProgressBar(Context context) {
        super(context);
        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
//        canvas.rotate(-180);
//        canvas.translate(-getHeight(), 0);
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);
    }

    @Override
    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    private void initPaint(){
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.argb(255, 33, 150, 243));
        this.mPaint.setTextSize(60);
    }

    private void setText(int progress){
        text = "血液连接你我！";
    }

}

