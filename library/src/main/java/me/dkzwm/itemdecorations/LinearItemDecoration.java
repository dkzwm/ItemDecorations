package me.dkzwm.itemdecorations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.dkzwm.itemdecorations.divider.IDivider;
import me.dkzwm.itemdecorations.provider.DefaultLinearProvider;
import me.dkzwm.itemdecorations.provider.ILinearProvider;

/**
 * Created by dkzwm on 2017/4/13.
 *
 * @author dkzwm
 */
public class LinearItemDecoration extends BaseItemDecoration<ILinearProvider> {

    private LinearItemDecoration(Builder builder) {
        super(builder);
    }

    @Override
    void calculateItemOffsets(RecyclerView parent, int position, Rect rect) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new UnsupportedOperationException("LinearItemDecoration can only be used in " +
                    "the RecyclerView which use LinearLayoutManager");
        }
        if (mDrawInsideEachOfItem) {
            rect.set(0, 0, 0, 0);
            return;
        }
        LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
        if (manager.getOrientation() == OrientationHelper.VERTICAL) {
            if (manager.getReverseLayout()) {
                if (mProvider.isDividerNeedDraw(position)) {
                    rect.set(0, mProvider.createDivider(position).getDividerSize(), 0, 0);
                } else {
                    rect.set(0, 0, 0, 0);
                }
            } else {
                if (mProvider.isDividerNeedDraw(position)) {
                    rect.set(0, 0, 0, mProvider.createDivider(position).getDividerSize());
                } else {
                    rect.set(0, 0, 0, 0);
                }
            }
        } else {
            if (manager.getReverseLayout()) {
                if (mProvider.isDividerNeedDraw(position)) {
                    rect.set(mProvider.createDivider(position).getDividerSize(), 0, 0, 0);
                } else {
                    rect.set(0, 0, 0, 0);
                }
            } else {
                if (mProvider.isDividerNeedDraw(position)) {
                    rect.set(0, 0, mProvider.createDivider(position).getDividerSize(), 0);
                } else {
                    rect.set(0, 0, 0, 0);
                }
            }
        }
    }

    @Override
    void drawVerticalOrientationDividers(Canvas c, RecyclerView parent,
                                         RecyclerView.LayoutManager layoutManager) {
        LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
        int childCount = parent.getChildCount();
        int left, top, right, bottom;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int position = lp.getViewAdapterPosition();
            if (position < 0 || !mProvider.isDividerNeedDraw(position))
                continue;
            int transitionX = (int) ViewCompat.getTranslationX(view);
            int transitionY = (int) ViewCompat.getTranslationY(view);
            left = view.getLeft() - lp.leftMargin + transitionX + mProvider.marginStart(position);
            right = view.getRight() + lp.rightMargin + transitionX - mProvider.marginEnd(position);
            IDivider divider = mProvider.createDivider(position);
            if (manager.getReverseLayout()) {
                if (divider.getType() == IDivider.TYPE_DRAWABLE) {
                    bottom = view.getTop() - lp.topMargin + transitionY;
                    top = bottom - divider.getDividerSize();
                } else {
                    top = view.getTop() - lp.topMargin -
                            (Math.round(divider.getDividerSize() / 2f)) + transitionY;
                    bottom = top;
                }
                if (mDrawInsideEachOfItem) {
                    top += divider.getDividerSize();
                    bottom += divider.getDividerSize();
                }
            } else {
                if (divider.getType() == IDivider.TYPE_DRAWABLE) {
                    top = view.getBottom() + lp.bottomMargin + transitionY;
                    bottom = top + divider.getDividerSize();
                } else {
                    top = view.getBottom() + lp.bottomMargin +
                            (Math.round(divider.getDividerSize() / 2f)) + transitionY;
                    bottom = top;
                }
                if (mDrawInsideEachOfItem) {
                    top -= divider.getDividerSize();
                    bottom -= divider.getDividerSize();
                }
            }
            divider.draw(c, left, top, right, bottom);
        }
    }

    @Override
    void drawHorizontalOrientationDividers(Canvas c, RecyclerView parent,
                                           RecyclerView.LayoutManager layoutManager) {
        LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
        int childCount = parent.getChildCount();
        int left, top, right, bottom;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int position = lp.getViewAdapterPosition();
            if (position < 0 || !mProvider.isDividerNeedDraw(position))
                continue;
            int transitionX = (int) ViewCompat.getTranslationX(view);
            int transitionY = (int) ViewCompat.getTranslationY(view);
            top = view.getTop() - lp.topMargin + transitionY + mProvider.marginStart(position);
            bottom = view.getBottom() + lp.bottomMargin + transitionY - mProvider.marginEnd(position);
            IDivider divider = mProvider.createDivider(position);
            if (manager.getReverseLayout()) {
                if (divider.getType() == IDivider.TYPE_DRAWABLE) {
                    right = view.getLeft() - lp.leftMargin + transitionX;
                    left = right - divider.getDividerSize();
                } else {
                    left = view.getLeft() - lp.leftMargin -
                            (Math.round(divider.getDividerSize() / 2f)) + transitionY;
                    right = left;
                }
                if (mDrawInsideEachOfItem) {
                    left += divider.getDividerSize();
                    right += divider.getDividerSize();
                }
            } else {
                if (divider.getType() == IDivider.TYPE_DRAWABLE) {
                    left = view.getRight() + lp.rightMargin + transitionX;
                    right = left + divider.getDividerSize();
                } else {
                    left = view.getRight() + lp.rightMargin +
                            (Math.round(divider.getDividerSize() / 2f)) + transitionX;
                    right = left;
                }
                if (mDrawInsideEachOfItem) {
                    left -= divider.getDividerSize();
                    right -= divider.getDividerSize();
                }
            }
            divider.draw(c, left, top, right, bottom);
        }
    }


    public static class Builder extends BaseBuilder<ILinearProvider, LinearItemDecoration> {

        public Builder(@NonNull Context context) {
            super(context);
        }

        @Override
        public LinearItemDecoration build() {
            if (mProvider == null) {
                mProvider = new DefaultLinearProvider();
            }
            return new LinearItemDecoration(this);
        }
    }
}
