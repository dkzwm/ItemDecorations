package me.dkzwm.itemdecorations.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */

public class ColorDivider implements IDivider {
    private static final int DEFAULT_COLOR = Color.GRAY;
    private static final int DEFAULT_SIZE = 2;
    private int mColor;
    private Paint mPaint;
    private int mSize;

    public ColorDivider() {
        this(DEFAULT_COLOR, DEFAULT_SIZE);
    }

    public ColorDivider(int color) {
        this(color, DEFAULT_SIZE);
    }

    public ColorDivider(int color, int size) {
        mColor = color;
        if (size % 2 == 0)
            mSize = size;
        else {
            Log.w(getClass().getSimpleName(), "divider size should be multiple of 2，Otherwise " +
                    "there may be gaps");
            mSize = size + 1;
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mSize);
        mPaint.setColor(mColor);
    }

    @Override
    public void draw(Canvas canvas, int left, int top, int right, int bottom) {
        canvas.drawLine(left, top, right, bottom, mPaint);
    }

    public int getColor() {
        return mColor;
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
