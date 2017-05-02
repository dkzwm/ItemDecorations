package me.dkzwm.itemdecorations.divider;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */

public class DrawableDivider implements IDivider {
    private Drawable mDrawable;
    private boolean mUseWidth;

    public DrawableDivider(@NonNull Drawable drawable, boolean useWidth) {
        mDrawable = drawable;
        mUseWidth = useWidth;
    }

    @Override
    public void draw(Canvas canvas, float left, float top, float right, float bottom) {
        mDrawable.setBounds(Math.round(left), Math.round(top), Math.round(right), Math.round(bottom));
        mDrawable.draw(canvas);
    }

    @Override
    public int getType() {
        return TYPE_DRAWABLE;
    }

    @Override
    public int getDividerSize() {
        return mUseWidth ? mDrawable.getIntrinsicWidth() : mDrawable.getIntrinsicHeight();
    }
}
