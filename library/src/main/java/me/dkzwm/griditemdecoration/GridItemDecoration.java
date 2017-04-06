package me.dkzwm.griditemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Support cross span features
 *
 * @author dkzwm
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private SafeCycleObserver mObserver;
    private DrawableProvider mProvider;

    public GridItemDecoration(RecyclerView recyclerView, RecyclerView.Adapter adapter,
                              DrawableProvider provider) {
        mObserver = new SafeCycleObserver(recyclerView);
        if (adapter == null)
            throw new IllegalArgumentException("RecyclerView.Adapter can not be null");
        adapter.registerAdapterDataObserver(mObserver);
        if (provider == null)
            throw new IllegalArgumentException("DrawableProvider can not be null");
        mProvider = provider;
    }

    @Override
    public synchronized void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }


    @Override
    public synchronized void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!mObserver.isPrepared())
            mObserver.refreshRelationShips();
        int position = parent.getChildAdapterPosition(view);
        if (position < 0)
            return;
        List<SafeCycleObserver.GridItemRelationship> relationships = mObserver.getRelationships();
        SafeCycleObserver.GridItemRelationship item = relationships.get(position);
        if (item.row < 0) {
            if (!item.firstColumnOfRow) {
                Drawable horizontalDrawable = mProvider.createHorizontal(parent, item.row + 1, item.column);
                outRect.set(horizontalDrawable.getIntrinsicWidth(), 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            Drawable verticalDrawable = mProvider.createVertical(parent, item.row);
            if (item.firstColumnOfRow) {
                outRect.set(0, verticalDrawable.getIntrinsicHeight(), 0, 0);
            } else {
                Drawable horizontalDrawable = mProvider.createHorizontal(parent, item.row + 1, item.column);
                outRect.set(horizontalDrawable.getIntrinsicWidth(), verticalDrawable.getIntrinsicHeight(), 0, 0);
            }
        }
    }

    public void detach(RecyclerView.Adapter adapter) {
        adapter.unregisterAdapterDataObserver(mObserver);
        mObserver.release();
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            if (position < 0)
                continue;
            List<SafeCycleObserver.GridItemRelationship> relationships = mObserver.getRelationships();
            SafeCycleObserver.GridItemRelationship item = relationships.get(position);
            Drawable horizontalDrawable = mProvider.createHorizontal(parent, item.row + 1, item
                    .column);
            if (item.firstColumnOfRow)
                continue;
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getLeft() + params.leftMargin - horizontalDrawable.getIntrinsicWidth();
            final int right = child.getLeft();
            horizontalDrawable.setBounds(left, top, right, bottom);
            horizontalDrawable.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        int left = 0, lastLineRight = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            if (position < 0)
                continue;
            List<SafeCycleObserver.GridItemRelationship> relationships = mObserver.getRelationships();
            SafeCycleObserver.GridItemRelationship item = relationships.get(position);
            Drawable verticalDrawable = mProvider.createVertical(parent, item.row);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            if (item.firstColumnOfRow)
                left = child.getLeft() - params.leftMargin;
            if (item.lastColumnOfRow) {
                final int top = child.getTop() - params.topMargin - verticalDrawable
                        .getIntrinsicHeight();
                final int bottom = child.getTop() - params.topMargin;
                final int right = child.getRight() + params.rightMargin;
                if (lastLineRight <= right && lastLineRight > 0) {
                    verticalDrawable.setBounds(left, top, lastLineRight, bottom);
                } else {
                    verticalDrawable.setBounds(left, top, right, bottom);
                }
                verticalDrawable.draw(c);
                lastLineRight = right;
            }
        }
    }


    public interface DrawableProvider {
        /**
         * Create vertical divider drawable
         *
         * @param parent RecyclerView
         * @param row    The number of lines in which the dividing line is located ，Starting
         *               from zero
         * @return Drawable: vertical divider drawable
         */
        Drawable createVertical(RecyclerView parent, int row);

        /**
         * Create horizontal divider drawable
         *
         * @param parent RecyclerView
         * @param row    The number of lines in which the dividing line is located ，Starting
         *               from zero
         * @param column The number of columns in which the dividing line is located , Starting
         *               from zero
         * @return Drawable: horizontal divider drawable
         */
        Drawable createHorizontal(RecyclerView parent, int row, int column);
    }

    private static class SafeCycleObserver extends RecyclerView.AdapterDataObserver {
        private WeakReference<RecyclerView> mReference;
        private List<GridItemRelationship> mRelationships = new ArrayList<>();
        private boolean mPrepared = false;

        private SafeCycleObserver(RecyclerView recyclerView) {
            mReference = new WeakReference<>(recyclerView);
        }

        private int getSpanCount(RecyclerView parent) {
            int spanCount;
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            } else {
                throw new UnsupportedOperationException("GridItemDecoration can only be used in " +
                        "the RecyclerView which use a GridLayoutManager");
            }
            return spanCount;
        }

        private int getSpanLookup(RecyclerView parent, int pos) {
            int spanLookup;
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                spanLookup = ((GridLayoutManager) layoutManager).getSpanSizeLookup().getSpanSize(pos);
            } else {
                throw new UnsupportedOperationException("GridItemDecoration can only be used in " +
                        "the RecyclerView which use a GridLayoutManager");
            }
            return spanLookup;
        }

        private List<GridItemRelationship> getRelationships() {
            return mRelationships;
        }

        private void release() {
            mRelationships.clear();
            mReference.clear();
        }

        private boolean isPrepared() {
            return mPrepared;
        }

        @Override
        public synchronized void onChanged() {
            refreshRelationShips();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            refreshRelationShips();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            refreshRelationShips();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            refreshRelationShips();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            refreshRelationShips();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            refreshRelationShips();
        }

        private synchronized void refreshRelationShips() {
            if (mReference.get() != null) {
                mPrepared = false;
                mRelationships.clear();
                RecyclerView recyclerView = mReference.get();
                int count = recyclerView.getAdapter().getItemCount();
                if (count > 0) {
                    int spanCount = getSpanCount(recyclerView);
                    int rows = -1, columnSize = 0, column = -1;
                    GridItemRelationship item, lastItem = null;
                    for (int i = 0; i < count; i++) {
                        item = new GridItemRelationship();
                        int spanLookup = getSpanLookup(recyclerView, i);
                        if ((columnSize + spanLookup) / spanCount >= 1) {
                            if ((columnSize + spanLookup) % spanCount != 0) {
                                if (lastItem != null) {
                                    lastItem.lastColumnOfRow = true;
                                }
                                item.lastColumnOfRow = false;
                                item.firstColumnOfRow = true;
                                rows++;
                                column = 0;
                                item.column = column;
                                columnSize = spanLookup;
                            } else {
                                if ((lastItem != null && lastItem.lastColumnOfRow)) {
                                    item.firstColumnOfRow = true;
                                    item.column = 0;
                                    rows++;
                                } else {
                                    item.firstColumnOfRow = i == 0;
                                    item.column = column;
                                }
                                item.lastColumnOfRow = true;
                                column++;
                                columnSize = columnSize + spanLookup;
                            }
                        } else {
                            if ((lastItem != null && lastItem.lastColumnOfRow)) {
                                item.firstColumnOfRow = true;
                                item.column = 0;
                                rows++;
                            } else {
                                item.firstColumnOfRow = i == 0;
                                item.column = column;
                            }
                            item.lastColumnOfRow = false;
                            item.column = item.firstColumnOfRow ? 0 : column;
                            column++;
                            columnSize = columnSize + spanLookup;
                        }
                        item.row = rows;
                        mRelationships.add(item);
                        lastItem = item;
                    }
                    if (lastItem != null)
                        lastItem.lastColumnOfRow = true;
                }
                mPrepared = true;
            }
        }

        private class GridItemRelationship {
            private boolean lastColumnOfRow;
            private boolean firstColumnOfRow;
            private int row;
            private int column;
        }
    }

}
