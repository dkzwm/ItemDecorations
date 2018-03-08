package me.dkzwm.widget.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.dkzwm.widget.decoration.divider.IDivider;
import me.dkzwm.widget.decoration.provider.DefaultGridProvider;
import me.dkzwm.widget.decoration.provider.IGridProvider;


/**
 * Created by dkzwm on 2017/4/5.
 *
 * @author dkzwm
 */
public class GridItemDecoration extends BaseItemDecoration<IGridProvider> {

    private GridItemDecoration(BaseBuilder<IGridProvider, ?> builder) {
        super(builder);
    }

    @Override
    void calculateItemOffsets(RecyclerView parent, int position, Rect rect) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof GridLayoutManager)) {
            throw new UnsupportedOperationException("GridItemDecoration can only be used in " +
                    "the RecyclerView which use GridLayoutManager");
        }
        if (mDrawInsideEachOfItem) {
            rect.set(0, 0, 0, 0);
            return;
        }
        GridLayoutManager manager = (GridLayoutManager) layoutManager;
        int spanCount = manager.getSpanCount();
        GridLayoutManager.SpanSizeLookup lookup = manager.getSpanSizeLookup();
        int columnSize = 0, rowSize = 0, row, column, totalSpanSize;
        if (manager.getOrientation() == OrientationHelper.VERTICAL) {
            row = lookup.getSpanGroupIndex(position, spanCount);
            column = lookup.getSpanIndex(position, spanCount);
            totalSpanSize = getTotalSpanSizeByPosition(manager, position);
            boolean drawColumn;
            boolean drawRow = isRowNeedDraw(row, totalSpanSize, true, manager.getReverseLayout(),
                    spanCount);
            if (drawRow) {
                IDivider rowDivider = createRowDivider(row, true,
                        manager.getReverseLayout());
                rowSize = rowDivider.getDividerSize();
            }
            if (manager.getReverseLayout()) {
                drawColumn = isColumnNeedDraw(column, totalSpanSize, true, true, spanCount);
                if (drawColumn) {
                    IDivider columnDivider = createColumnDivider(column, true, true);
                    columnSize = columnDivider.getDividerSize();
                }
                rect.set(0, 0, columnSize, rowSize);
            } else {
                drawColumn = isColumnNeedDraw(column, totalSpanSize, true, false, spanCount);
                if (drawColumn) {
                    IDivider columnDivider = createColumnDivider(column, true, false);
                    columnSize = columnDivider.getDividerSize();
                }
                rect.set(columnSize, rowSize, 0, 0);
            }
        } else {
            row = lookup.getSpanIndex(position, spanCount);
            column = lookup.getSpanGroupIndex(position, spanCount);
            totalSpanSize = getTotalSpanSizeByPosition(manager, position);
            boolean drawColumn = isColumnNeedDraw(column, totalSpanSize, false, manager.getReverseLayout(),
                    spanCount);
            boolean drawRow;
            if (drawColumn) {
                IDivider columnDivider = createColumnDivider(column, false,
                        manager.getReverseLayout());
                columnSize = columnDivider.getDividerSize();
            }
            if (manager.getReverseLayout()) {
                drawRow = isRowNeedDraw(row, totalSpanSize, false, true, spanCount);
                if (drawRow) {
                    IDivider rowDivider = createRowDivider(row, false, true);
                    rowSize = rowDivider.getDividerSize();
                }
                rect.set(0, 0, columnSize, rowSize);
            } else {
                drawRow = isRowNeedDraw(row, totalSpanSize, false, false, spanCount);
                if (drawRow) {
                    IDivider rowDivider = createRowDivider(row, false, false);
                    rowSize = rowDivider.getDividerSize();
                }
                rect.set(columnSize, rowSize, 0, 0);
            }
        }
    }

    @Override
    void drawVerticalOrientationDividers(Canvas c,
                                         RecyclerView parent,
                                         RecyclerView.LayoutManager layoutManager) {
        GridLayoutManager manager = (GridLayoutManager) layoutManager;
        int spanCount = manager.getSpanCount();
        int childCount = parent.getChildCount();
        GridLayoutManager.SpanSizeLookup lookup = manager.getSpanSizeLookup();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int position = lp.getViewAdapterPosition();
            if (position < 0)
                continue;
            int row = lookup.getSpanGroupIndex(position, spanCount);
            int column = lookup.getSpanIndex(position, spanCount);
            int totalSpanSize = getTotalSpanSizeByPosition(manager, position);
            float transitionX = view.getTranslationX();
            float transitionY = view.getTranslationY();
            boolean drawColumn = isColumnNeedDraw(column, totalSpanSize, true, manager.getReverseLayout(),
                    spanCount);
            boolean drawRow = isRowNeedDraw(row, totalSpanSize, true, manager.getReverseLayout(),
                    spanCount);
            float left, top, right, bottom;
            if (drawColumn) {
                top = view.getTop() - lp.topMargin + transitionY;
                bottom = view.getBottom() + lp.bottomMargin + transitionY;
                if (manager.getReverseLayout()) {
                    IDivider columnDivider = createColumnDivider(column, true, true);
                    if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        left = view.getRight() + lp.rightMargin + transitionX;
                        right = left + columnDivider.getDividerSize();
                    } else {
                        left = view.getRight() + lp.rightMargin + transitionX
                                + columnDivider.getDividerSize() / 2f;
                        right = left;
                    }
                    if (mDrawInsideEachOfItem) {
                        left -= columnDivider.getDividerSize();
                        right -= columnDivider.getDividerSize();
                    }
                    columnDivider.draw(c, left, top, right, bottom);
                    boolean needDrawLastRow = !mDrawInsideEachOfItem
                            && totalSpanSize != spanCount
                            && row > 1
                            && mProvider.isRowNeedDraw(row - 1);
                    if (needDrawLastRow) {
                        left = view.getRight() + lp.rightMargin + transitionX;
                        right = left + columnDivider.getDividerSize();
                        IDivider rowDivider = mProvider.createRowDivider(row - 1);
                        if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                            top = bottom;
                            bottom = top + rowDivider.getDividerSize();
                        } else {
                            top = bottom + rowDivider.getDividerSize() / 2f;
                            bottom = top;
                        }
                        rowDivider.draw(c, left, top, right, bottom);
                    }
                } else {
                    IDivider columnDivider = createColumnDivider(column, true, false);
                    if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        right = view.getLeft() - lp.leftMargin + transitionX;
                        left = right - columnDivider.getDividerSize();
                    } else {
                        left = view.getLeft() - lp.leftMargin + transitionX
                                - columnDivider.getDividerSize() / 2f;
                        right = left;
                    }
                    if (mDrawInsideEachOfItem) {
                        left += columnDivider.getDividerSize();
                        right += columnDivider.getDividerSize();
                    }
                    columnDivider.draw(c, left, top, right, bottom);
                    boolean needDrawLastRow = !mDrawInsideEachOfItem
                            && column > 0
                            && row > 1
                            && mProvider.isRowNeedDraw(row - 1);
                    if (needDrawLastRow) {
                        right = view.getLeft() - lp.leftMargin + transitionX;
                        left = right - columnDivider.getDividerSize();
                        IDivider rowDivider = mProvider.createRowDivider(row - 1);
                        if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                            bottom = top;
                            top = top - rowDivider.getDividerSize();
                        } else {
                            top = top - rowDivider.getDividerSize() / 2f;
                            bottom = top;
                        }
                        rowDivider.draw(c, left, top, right, bottom);
                    }
                }
            }
            if (drawRow) {
                IDivider rowDivider = createRowDivider(row, true, manager.getReverseLayout());
                left = view.getLeft() - lp.leftMargin + transitionX;
                right = view.getRight() + lp.rightMargin + transitionX;
                if (manager.getReverseLayout()) {
                    if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        top = view.getBottom() + lp.bottomMargin + transitionY;
                        bottom = top + rowDivider.getDividerSize();
                    } else {
                        bottom = view.getBottom() + lp.bottomMargin + transitionY
                                + rowDivider.getDividerSize() / 2f;
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
                                - rowDivider.getDividerSize() / 2f;
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
        GridLayoutManager.SpanSizeLookup lookup = manager.getSpanSizeLookup();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int position = lp.getViewAdapterPosition();
            if (position < 0)
                continue;
            int row = lookup.getSpanIndex(position, spanCount);
            int column = lookup.getSpanGroupIndex(position, spanCount);
            int totalSpanSize = getTotalSpanSizeByPosition(manager, position);
            float transitionX = view.getTranslationX();
            float transitionY = view.getTranslationY();
            boolean drawColumn = isColumnNeedDraw(column, totalSpanSize, false, manager.getReverseLayout(),
                    spanCount);
            boolean drawRow = isRowNeedDraw(row, totalSpanSize, false, manager.getReverseLayout(),
                    spanCount);
            float left, top, right, bottom;
            if (drawColumn) {
                IDivider columnDivider = createColumnDivider(column, false,
                        manager.getReverseLayout());
                top = view.getTop() - lp.topMargin + transitionY;
                bottom = view.getBottom() + lp.bottomMargin + transitionY;
                if (manager.getReverseLayout()) {
                    if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        left = view.getRight() + lp.rightMargin + transitionX;
                        right = left + columnDivider.getDividerSize();
                    } else {
                        left = view.getRight() + lp.rightMargin + transitionX
                                + columnDivider.getDividerSize() / 2f;
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
                                - columnDivider.getDividerSize() / 2f;
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
                IDivider rowDivider = createRowDivider(row, false, manager
                        .getReverseLayout());
                left = view.getLeft() - lp.leftMargin + transitionX;
                right = view.getRight() + lp.rightMargin + transitionX;
                if (manager.getReverseLayout()) {
                    if (rowDivider.getType() == IDivider.TYPE_DRAWABLE) {
                        top = view.getBottom() + lp.bottomMargin + transitionY;
                        bottom = top + rowDivider.getDividerSize();
                    } else {
                        bottom = view.getBottom() + lp.bottomMargin + transitionY
                                + rowDivider.getDividerSize() / 2f;
                        top = bottom;
                    }
                    if (mDrawInsideEachOfItem) {
                        top -= rowDivider.getDividerSize();
                        bottom -= rowDivider.getDividerSize();
                    }
                    rowDivider.draw(c, left, top, right, bottom);
                    boolean needDrawLastColumn = !mDrawInsideEachOfItem
                            && column > 1
                            && totalSpanSize != spanCount
                            && mProvider.isColumnNeedDraw(column - 1);
                    if (needDrawLastColumn) {
                        top = view.getBottom() + lp.bottomMargin + transitionY;
                        bottom = top + rowDivider.getDividerSize();
                        IDivider columnDivider = mProvider.createColumnDivider(column - 1);
                        if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                            left = right;
                            right = left + columnDivider.getDividerSize();
                        } else {
                            left = right + columnDivider.getDividerSize() / 2f;
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
                                - rowDivider.getDividerSize() / 2f;
                        top = bottom;
                    }
                    if (mDrawInsideEachOfItem) {
                        top += rowDivider.getDividerSize();
                        bottom += rowDivider.getDividerSize();
                    }
                    rowDivider.draw(c, left, top, right, bottom);
                    boolean needDrawLastColumn = !mDrawInsideEachOfItem
                            && column > 0
                            && row != spanCount
                            && mProvider.isColumnNeedDraw(column - 1);
                    if (needDrawLastColumn) {
                        top = view.getTop() - lp.topMargin + transitionY;
                        bottom = top - rowDivider.getDividerSize();
                        IDivider columnDivider = mProvider.createColumnDivider(column - 1);
                        if (columnDivider.getType() == IDivider.TYPE_DRAWABLE) {
                            right = left;
                            left = left - columnDivider.getDividerSize();
                            columnDivider.draw(c, left, top, right, bottom);
                        } else {
                            left = left - columnDivider.getDividerSize() / 2f;
                            right = left;
                            columnDivider.draw(c, left, top, right, bottom);
                        }
                    }
                }
            }
        }
    }


    private IDivider createColumnDivider(int column,
                                         boolean vertical,
                                         boolean reverseLayout) {
        if (vertical) {
            if (reverseLayout)
                return mProvider.createColumnDivider(column);
            else
                return mProvider.createColumnDivider(column - 1);
        } else {
            return mProvider.createColumnDivider(column - 1);
        }
    }

    private IDivider createRowDivider(int row,
                                      boolean vertical,
                                      boolean reverseLayout) {
        if (vertical) {
            return mProvider.createRowDivider(row - 1);
        } else {
            if (reverseLayout) {
                return mProvider.createRowDivider(row);
            } else {
                return mProvider.createRowDivider(row - 1);
            }
        }
    }

    private boolean isColumnNeedDraw(int column,
                                     int totalSpanSize,
                                     boolean vertical,
                                     boolean reverseLayout,
                                     int spanCount) {
        if (vertical) {
            if (reverseLayout)
                return totalSpanSize != spanCount
                        && mProvider.isColumnNeedDraw(column);
            else
                return column > 0
                        && mProvider.isColumnNeedDraw(column - 1);
        } else {
            return column > 0
                    && mProvider.isColumnNeedDraw(column - 1);
        }
    }

    private boolean isRowNeedDraw(int row,
                                  int totalSpanSize,
                                  boolean vertical,
                                  boolean reverseLayout,
                                  int spanCount) {
        if (vertical) {
            return row > 0 && mProvider.isRowNeedDraw(row - 1);
        } else {
            if (reverseLayout) {
                return totalSpanSize != spanCount
                        && mProvider.isRowNeedDraw(row);
            } else {
                return row > 0 && mProvider.isRowNeedDraw(row - 1);
            }
        }
    }


    /**
     * Gets the total number of spans by the position
     *
     * @param manager  The GridLayoutManager
     * @param position The position of Item
     * @return The total span size
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

    public static class Builder extends BaseBuilder<IGridProvider, GridItemDecoration> {

        public Builder(@NonNull Context context) {
            super(context);
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
