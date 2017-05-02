package me.dkzwm.itemdecorations.divider;

import android.graphics.Canvas;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */

public interface IDivider {

    int TYPE_COLOR = 0;
    int TYPE_PAINT = 1;
    int TYPE_DRAWABLE = 2;

    void draw(Canvas canvas, float left, float top, float right, float bottom);

    @DividerType
    int getType();

    int getDividerSize();

    @IntDef({TYPE_COLOR, TYPE_PAINT, TYPE_DRAWABLE})
    @Retention(RetentionPolicy.SOURCE)
    @interface DividerType {
    }
}
