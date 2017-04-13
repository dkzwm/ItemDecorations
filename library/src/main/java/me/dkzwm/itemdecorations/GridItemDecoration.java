package me.dkzwm.itemdecorations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import me.dkzwm.itemdecorations.divider.IDivider;
import me.dkzwm.itemdecorations.provider.DefaultGridProvider;
import me.dkzwm.itemdecorations.provider.GridProvider;
import me.dkzwm.itemdecorations.provider.IGridProvider;


/**
 * Created by dkzwm on 2017/4/5.
 *
 * @author dkzwm
 */
public class GridItemDecoration extends BaseItemDecoration<IGridProvider> {
    private SparseArray<GridItemRelationship> mRelationships;

    private GridItemDecoration(Builder builder) {
        super(builder);
        mRelationships = new SparseArray<>();
    }

    @Override
    void calculateItemOffsets(RecyclerView parent, int position, Rect rect) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof GridLayoutManager)) {
            throw new UnsupportedOperationException("GridItemDecoration can only be used in " +
                    "the RecyclerView which use GridLayoutManager");
        }
        GridLayoutManager manager = (GridLayoutManager) layoutManager;
        int spanCount = manager.getSpanCount();
        GridLayoutManager.SpanSizeLookup lookup = manager.getSpanSizeLookup();
        GridItemRelationship relationship = new GridItemRelationship();
        int columnSize = 0, rowSize = 0;
        if (manager.getOrientation() == OrientationHelper.VERTICAL) {
            relationship.row = lookup.getSpanGroupIndex(position, spanCount);
            relationship.column = lookup.getSpanIndex(position, spanCount);
            relationship.position = position;
            relationship.totalSpanSize = getTotalSpanSizeByPosition(manager, position);
            if (!mDrawInsideEachOfItem) {
                boolean drawColumn;
                boolean drawRow = isRowNeedDraw(relationship, true, manager.getReverseLayout(),
                        spanCount);
                if (drawRow) {
                    IDivider rowDivider = createRowDivider(relationship, true,
                            manager.getReverseLayout());
                    rowSize = rowDivider.getDividerSize();
                }
                if (manager.getReverseLayout()) {
                    drawColumn = isColumnNeedDraw(relationship, true, true, spanCount);
                    if (drawColumn) {
                        IDivider columnDivider = createColumnDivider(relationship, true, true);
                        columnSize = columnDivider.getDividerSize();
                    }
                    rect.set(0, 0, columnSize, rowSize);
                } else {
                    drawColumn = isColumnNeedDraw(relationship, true, false, spanCount);
                    if (drawColumn) {
                        IDivider columnDivider = createColumnDivider(relationship, true, false);
                        columnSize = columnDivider.getDividerSize();
                    }
                    rect.set(columnSize, rowSize, 0, 0);
                }
            }
        } else {
            relationship.row = lookup.getSpanIndex(position, spanCount);
            relationship.column = lookup.getSpanGroupIndex(position, spanCount);
            relationship.position = position;
            relationship.totalSpanSize = getTotalSpanSizeByPosition(manager, position);
            if (!mDrawInsideEachOfItem) {
                boolean drawColumn = isColumnNeedDraw(relationship, false, manager.getReverseLayout(),
                        spanCount);
                boolean drawRow;
                if (drawColumn) {
                    IDivider columnDivider = createColumnDivider(relationship, false,
                            manager.getReverseLayout());
                    columnSize = columnDivider.getDividerSize();
                }
                if (manager.getReverseLayout()) {
                    drawRow = isRowNeedDraw(relationship, false, true, spanCount);
                    if (drawRow) {
                        IDivider rowDivider = createRowDivider(relationship, false, true);
                        rowSize = rowDivider.getDividerSize();
                    }
                    rect.set(0, 0, columnSize, rowSize);
                } else {
                    drawRow = isRowNeedDraw(relationship, false, false, spanCount);
                    if (drawRow) {
                        IDivider rowDivider = createRowDivider(relationship, false, false);
                        rowSize = rowDivider.getDividerSize();
                    }
                    rect.set(columnSize, rowSize, 0, 0);
                }
            }
        }
        if (mDrawInsideEachOfItem)
            rect.set(0, 0, 0, 0);
        mRelationships.put(position, relationship);
    }

    @Override
    void drawVerticalOrientationDividers(Canvas c,
                                         RecyclerView parent,
                                         RecyclerView.LayoutManager layoutManager) {
        GridLayoutManager manager = (GridLayoutManager) layoutManager;
        int spanCount = manager.getSpanCount();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int position = lp.getViewAdapterPosition();
            if (position < 0)
                continue;
            GridItemRelationship relationship = mRelationships.get(position);
            if (relationship == null)
                continue;
            int transitionX = (int) ViewCompat.getTranslationX(view);
            int transitionY = (int) ViewCompat.getTranslationY(view);
            boolean drawColumn = isColumnNeedDraw(relationship, true, manager.getReverseLayout(),
                    spanCount);
            boolean drawRow = isRowNeedDraw(relationship, true, manager.getReverseLayout(),
                    spanCount);
            int left, top, right, bottom;
            if (drawColumn) {
                top = view.getTop() - lp.topMargin + transitionY;
                bottom = view.getBottom() + lp.bottomMargin + transitionY;
                if (manager.getReverseLayout()) {
                    IDivider columnDivider = createColumnDivider(relationship, true, true);
                    if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        left = view.getRight() + lp.rightMargin + transitionX;
                        right = left + columnDivider.getDividerSize();
                    } else {
                        left = view.getRight() + lp.rightMargin + transitionX
                                + Math.round(columnDivider.getDividerSize() / 2f);
                        right = left;
                    }
                    if (mDrawInsideEachOfItem) {
                        left -= columnDivider.getDividerSize();
                        right -= columnDivider.getDividerSize();
                    }
                    columnDivider.draw(c, left, top, right, bottom);
                    boolean needDrawLastRow = !mDrawInsideEachOfItem
                            && relationship.totalSpanSize != spanCount
                            && relationship.row > 1
                            && mProvider.isRowNeedDraw(relationship.row - 1);
                    if (needDrawLastRow) {
                        left = view.getRight() + lp.rightMargin + transitionX;
                        right = left + columnDivider.getDividerSize();
                        IDivider rowDivider = mProvider.createRowDivider(relationship.row - 1);
                        if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                            top = bottom;
                            bottom = top + rowDivider.getDividerSize();
                        } else {
                            top = bottom + Math.round(rowDivider.getDividerSize() / 2f);
                            bottom = top;
                        }
                        rowDivider.draw(c, left, top, right, bottom);
                    }
                } else {
                    IDivider columnDivider = createColumnDivider(relationship, true, false);
                    if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        right = view.getLeft() - lp.leftMargin + transitionX;
                        left = right - columnDivider.getDividerSize();
                    } else {
                        left = view.getLeft() - lp.leftMargin + transitionX
                                - Math.round(columnDivider.getDividerSize() / 2f);
                        right = left;
                    }
                    if (mDrawInsideEachOfItem) {
                        left += columnDivider.getDividerSize();
                        right += columnDivider.getDividerSize();
                    }
                    columnDivider.draw(c, left, top, right, bottom);
                    boolean needDrawLastRow = !mDrawInsideEachOfItem
                            && relationship.column > 0
                            && relationship.row > 1
                            && mProvider.isRowNeedDraw(relationship.row - 1);
                    if (needDrawLastRow) {
                        right = view.getLeft() - lp.leftMargin + transitionX;
                        left = right - columnDivider.getDividerSize();
                        IDivider rowDivider = mProvider.createRowDivider(relationship.row - 1);
                        if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                            bottom = top;
                            top = top - rowDivider.getDividerSize();
                        } else {
                            top = top - Math.round(rowDivider.getDividerSize() / 2f);
                            bottom = top;
                        }
                        rowDivider.draw(c, left, top, right, bottom);
                    }
                }
            }
            if (drawRow) {
                IDivider rowDivider = createRowDivider(relationship, true, manager.getReverseLayout());
                left = view.getLeft() - lp.leftMargin + transitionX;
                right = view.getRight() + lp.rightMargin + transitionX;
                if (manager.getReverseLayout()) {
                    if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        top = view.getBottom() + lp.bottomMargin + transitionY;
                        bottom = top + rowDivider.getDividerSize();
                    } else {
                        bottom = view.getBottom() + lp.bottomMargin + transitionY
                                + Math.round(rowDivider.getDividerSize() / 2f);
                        top = bottom;
                    }
                    if (mDrawInsideEachOfItem) {
                        top -= rowDivider.getDividerSize();
                        bottom -= rowDivider.getDividerSize();
                    }
                } else {
                    if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        bottom = view.getTop() - lp.topMargin + transitionY;
                        top = bottom - rowDivider.getDividerSize();
                    } else {
                        bottom = view.getTop() - lp.topMargin + transitionY
                                - Math.round(rowDivider.getDividerSize() / 2f);
                        top = bottom;
                    }
                    if (mDrawInsideEachOfItem) {
                        top += rowDivider.getDividerSize();
                        bottom += rowDivider.getDividerSize();
                    }
                }
                rowDivider.draw(c, left, top, right, bottom);
            }
        }
    }

    @Override
    void drawHorizontalOrientationDividers(Canvas c,
                                           RecyclerView parent,
                                           RecyclerView.LayoutManager layoutManager) {
        GridLayoutManager manager = (GridLayoutManager) layoutManager;
        int spanCount = manager.getSpanCount();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int position = lp.getViewAdapterPosition();
            if (position < 0)
                continue;
            GridItemRelationship relationship = mRelationships.get(position);
            if (relationship == null)
                continue;
            int transitionX = (int) ViewCompat.getTranslationX(view);
            int transitionY = (int) ViewCompat.getTranslationY(view);
            boolean drawColumn = isColumnNeedDraw(relationship, false, manager.getReverseLayout(),
                    spanCount);
            boolean drawRow = isRowNeedDraw(relationship, false, manager.getReverseLayout(),
                    spanCount);
            int left, top, right, bottom;
            if (drawColumn) {
                IDivider columnDivider = createColumnDivider(relationship, false,
                        manager.getReverseLayout());
                top = view.getTop() - lp.topMargin + transitionY;
                bottom = view.getBottom() + lp.bottomMargin + transitionY;
                if (manager.getReverseLayout()) {
                    if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        left = view.getRight() + lp.rightMargin + transitionX;
                        right = left + columnDivider.getDividerSize();
                    } else {
                        left = view.getRight() + lp.rightMargin + transitionX
                                + Math.round(columnDivider.getDividerSize() / 2f);
                        right = left;
                    }
                    if (mDrawInsideEachOfItem) {
                        left -= columnDivider.getDividerSize();
                        right -= columnDivider.getDividerSize();
                    }
                } else {
                    if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        left = view.getLeft() - lp.leftMargin + transitionX;
                        right = left - columnDivider.getDividerSize();
                    } else {
                        left = view.getLeft() - lp.leftMargin + transitionX
                                - Math.round(columnDivider.getDividerSize() / 2f);
                        right = left;
                    }
                    if (mDrawInsideEachOfItem) {
                        left += columnDivider.getDividerSize();
                        right += columnDivider.getDividerSize();
                    }
                }
                columnDivider.draw(c, left, top, right, bottom);
            }
            if (drawRow) {
                IDivider rowDivider = createRowDivider(relationship, false, manager
                        .getReverseLayout());
                left = view.getLeft() - lp.leftMargin + transitionX;
                right = view.getRight() + lp.rightMargin + transitionX;
                if (manager.getReverseLayout()) {
                    if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        top = view.getBottom() + lp.bottomMargin + transitionY;
                        bottom = top + rowDivider.getDividerSize();
                    } else {
                        bottom = view.getBottom() + lp.bottomMargin + transitionY
                                + Math.round(rowDivider.getDividerSize() / 2f);
                        top = bottom;
                    }
                    if (mDrawInsideEachOfItem) {
                        top -= rowDivider.getDividerSize();
                        bottom -= rowDivider.getDividerSize();
                    }
                    rowDivider.draw(c, left, top, right, bottom);
                    boolean needDrawLastColumn = !mDrawInsideEachOfItem
                            && relationship.column > 1
                            && relationship.totalSpanSize != spanCount
                            && mProvider.isColumnNeedDraw(relationship.column - 1);
                    if (needDrawLastColumn) {
                        top = view.getBottom() + lp.bottomMargin + transitionY;
                        bottom = top + rowDivider.getDividerSize();
                        IDivider columnDivider = mProvider.createColumnDivider(relationship.column - 1);
                        if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                            left = right;
                            right = left + columnDivider.getDividerSize();
                        } else {
                            left = right + Math.round(columnDivider.getDividerSize() / 2f);
                            right = left;
                        }
                        columnDivider.draw(c, left, top, right, bottom);
                    }
                } else {
                    if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        top = view.getTop() - lp.topMargin + transitionY;
                        bottom = top - rowDivider.getDividerSize();
                    } else {
                        bottom = view.getTop() - lp.topMargin + transitionY
                                - Math.round(rowDivider.getDividerSize() / 2f);
                        top = bottom;
                    }
                    if (mDrawInsideEachOfItem) {
                        top += rowDivider.getDividerSize();
                        bottom += rowDivider.getDividerSize();
                    }
                    rowDivider.draw(c, left, top, right, bottom);
                    boolean needDrawLastColumn = !mDrawInsideEachOfItem
                            && relationship.column > 0
                            && relationship.row != spanCount
                            && mProvider.isColumnNeedDraw(relationship.column - 1);
                    if (needDrawLastColumn) {
                        top = view.getTop() - lp.topMargin + transitionY;
                        bottom = top - rowDivider.getDividerSize();
                        IDivider columnDivider = mProvider.createColumnDivider(relationship.column - 1);
                        if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                            right = left;
                            left = left - columnDivider.getDividerSize();
                            columnDivider.draw(c, left, top, right, bottom);
                        } else {
                            left = left - Math.round(columnDivider.getDividerSize() / 2f);
                            right = left;
                            columnDivider.draw(c, left, top, right, bottom);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void release() {
        super.release();
        mRelationships.clear();
    }

    private IDivider createColumnDivider(GridItemRelationship relationship,
                                         boolean vertical,
                                         boolean reverseLayout) {
        if (vertical) {
            if (reverseLayout)
                return mProvider.createColumnDivider(relationship.column);
            else
                return mProvider.createColumnDivider(relationship.column - 1);
        } else {
            return mProvider.createColumnDivider(relationship.column - 1);
        }
    }

    private IDivider createRowDivider(GridItemRelationship relationship,
                                      boolean vertical,
                                      boolean reverseLayout) {
        if (vertical) {
            return mProvider.createRowDivider(relationship.row - 1);
        } else {
            if (reverseLayout) {
                return mProvider.createRowDivider(relationship.row);
            } else {
                return mProvider.createRowDivider(relationship.row - 1);
            }
        }
    }

    private boolean isColumnNeedDraw(GridItemRelationship relationship,
                                     boolean vertical,
                                     boolean reverseLayout,
                                     int spanCount) {
        if (vertical) {
            if (reverseLayout)
                return relationship.totalSpanSize != spanCount
                        && mProvider.isColumnNeedDraw(relationship.column);
            else
                return relationship.column > 0
                        && mProvider.isColumnNeedDraw(relationship.column - 1);
        } else {
            return relationship.column > 0
                    && mProvider.isColumnNeedDraw(relationship.column - 1);
        }
    }

    private boolean isRowNeedDraw(GridItemRelationship relationship,
                                  boolean vertical,
                                  boolean reverseLayout,
                                  int spanCount) {
        if (vertical) {
            return relationship.row > 0 && mProvider.isRowNeedDraw(relationship.row - 1);
        } else {
            if (reverseLayout) {
                return relationship.totalSpanSize != spanCount
                        && mProvider.isRowNeedDraw(relationship.row);
            } else {
                return relationship.row > 0 && mProvider.isRowNeedDraw(relationship.row - 1);
            }
        }
    }


    /**
     * Gets the total number of spans by the position
     *
     * @param manager  The GridLayoutManager
     * @param position The position of Item
     * @return
     */
    private int getTotalSpanSizeByPosition(GridLayoutManager manager, int position) {
        int spanTotalSize = 0;
        GridLayoutManager.SpanSizeLookup lookup = manager.getSpanSizeLookup();
        int spanCount = manager.getSpanCount();
        for (int i = 0; i <= position; i++) {
            int spanLookup = lookup.getSpanSize(i);
            spanTotalSize = spanTotalSize + spanLookup;
            if (spanTotalSize >= spanCount) {
                if (spanTotalSize % spanCount != 0) {
                    spanTotalSize = spanLookup;
                } else {
                    if (spanTotalSize > spanCount) {
                        if (spanTotalSize % spanCount != 0)
                            spanTotalSize = spanTotalSize % spanCount;
                        else
                            spanTotalSize = spanCount;
                    }
                }
            }
        }
        return spanTotalSize;
    }

    private static class GridItemRelationship {
        int position;
        int row;
        int column;
        int totalSpanSize;
    }

    public static class Builder extends BaseBuilder<IGridProvider>
            implements IDecorationBuilder<Builder, GridProvider, GridItemDecoration> {

        public Builder(@NonNull Context context) {
            super(context);
        }

        @Override
        public Builder drawInsideEachOfItem(boolean drawInsideEachOfItem) {
            mDrawInsideEachOfItem = drawInsideEachOfItem;
            return this;
        }

        @Override
        public Builder provider(GridProvider provider) {
            mProvider = provider;
            return this;
        }

        @Override
        public GridItemDecoration build() {
            if (mProvider == null) {
                mProvider = new DefaultGridProvider();
            }
            return new GridItemDecoration(this);
        }
    }
}
