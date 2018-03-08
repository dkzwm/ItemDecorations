package me.dkzwm.widget.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import me.dkzwm.widget.decoration.provider.IProvider;

/**
 * The base ItemDecoration
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */
abstract class BaseItemDecoration<T extends IProvider> extends RecyclerView.ItemDecoration {
    T mProvider;
    boolean mDrawInsideEachOfItem;
    private boolean mDrawOverTop;

    BaseItemDecoration(BaseBuilder<T, ?> builder) {
        mProvider = builder.mProvider;
        mDrawOverTop = builder.mDrawOverTop;
        mDrawInsideEachOfItem = builder.mDrawInsideEachOfItem;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!mDrawOverTop) {
            draw(c, parent);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDrawOverTop) {
            draw(c, parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        int position = parent.getChildAdapterPosition(view);
        if (position < 0 || position >= adapter.getItemCount()) {
            return;
        }
        calculateItemOffsets(parent, position, outRect);
    }


    abstract void calculateItemOffsets(RecyclerView view, int position, Rect rect);


    /**
     * Draw the dividers of vertical orientation
     *
     * @param c             Need draw the Canvas
     * @param parent        Need draw The RecyclerView
     * @param layoutManager The LayoutManager of RecyclerView
     */
    abstract void drawVerticalOrientationDividers(Canvas c, RecyclerView parent,
                                                  RecyclerView.LayoutManager layoutManager);

    /**
     * Draw the dividers of horizontal orientation
     *
     * @param c             Need draw the Canvas
     * @param parent        Need draw The RecyclerView
     * @param layoutManager The LayoutManager of RecyclerView
     */
    abstract void drawHorizontalOrientationDividers(Canvas c, RecyclerView parent,
                                                    RecyclerView.LayoutManager layoutManager);

    private void draw(Canvas c, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if ((layoutManager instanceof LinearLayoutManager)) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            if (manager.getOrientation() == OrientationHelper.VERTICAL)
                drawVerticalOrientationDividers(c, parent, manager);
            else
                drawHorizontalOrientationDividers(c, parent, manager);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            Log.e(getClass().getSimpleName(), "Will soon support this feature !!");
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName() +
                    " can only be used in the RecyclerView which use GridLayoutManager" +
                    " or LinearLayoutManager or StaggeredGridLayoutManager");
        }
    }

    public void release() {
        mProvider.release();
    }

    interface IBuilder<R, S> {
        /**
         * Draw the divider over the top
         *
         * @param overTop True of false
         * @return Builder
         */
        R drawOverTop(boolean overTop);

        /**
         * Draw the divider inside each of item
         *
         * @param drawInsideEachOfItem true or false
         * @return Builder
         */
        R drawInsideEachOfItem(boolean drawInsideEachOfItem);

        /**
         * Set the divider provider
         *
         * @param provider Custom divider provider , can not be null
         * @return Builder
         */
        R provider(@NonNull S provider);

    }

    public abstract static class BaseBuilder<T, S> implements IBuilder<BaseBuilder, T> {
        Context mContext;
        T mProvider;
        boolean mDrawInsideEachOfItem = false;
        boolean mDrawOverTop = false;

        BaseBuilder(@NonNull Context context) {
            mContext = context;
        }

        @Override
        public BaseBuilder<T, S> drawOverTop(boolean drawOverTop) {
            mDrawOverTop = drawOverTop;
            return this;
        }

        @Override
        public BaseBuilder<T, S> drawInsideEachOfItem(boolean drawInsideEachOfItem) {
            mDrawInsideEachOfItem = drawInsideEachOfItem;
            return this;
        }

        @Override
        public BaseBuilder<T, S> provider(@NonNull T provider) {
            mProvider = provider;
            return this;
        }

        abstract public S build();

    }
}
