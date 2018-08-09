package me.dkzwm.widget.decoration.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;

/**
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */

public class ColorDivider implements IDivider {
    private static final int DEFAULT_COLOR = Color.GRAY;
    private static final int DEFAULT_SIZE = 2;
    @ColorInt
    private int mColor;
    private Paint mPaint;
    private int mSize;

    public ColorDivider() {
        this(DEFAULT_COLOR, DEFAULT_SIZE);
    }

    public ColorDivider(@ColorInt int color) {
        this(color, DEFAULT_SIZE);
    }

    public ColorDivider(@ColorInt int color, int size) {
        mColor = color;
        mSize = size;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mSize);
        mPaint.setColor(mColor);
    }

    public void setSize(int size) {
        mSize = size;
    }

    @Override
    public void draw(Canvas canvas, float left, float top, float right, float bottom) {
        canvas.drawLine(left, top, right, bottom, mPaint);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(@ColorInt int color) {
        mColor = color;
    }

    @Override
    public int getType() {
        return TYPE_PAINT;
    }

    @Override
    public int getDividerSize() {
        return mSize;
    }
}
