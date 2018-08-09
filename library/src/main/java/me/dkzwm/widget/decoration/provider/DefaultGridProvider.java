package me.dkzwm.widget.decoration.provider;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import me.dkzwm.widget.decoration.divider.ColorDivider;
import me.dkzwm.widget.decoration.divider.IDivider;

/**
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */
public final class DefaultGridProvider implements IGridProvider {
    private SparseArray<IDivider> mRowDividers;
    private SparseArray<IDivider> mColumnDividers;
    private SparseBooleanArray mRowNeedDrawFlags;
    private SparseBooleanArray mColumnNeedDrawFlags;
    private IDivider mDefaultDivider = new ColorDivider();
    private IDivider mAllRowDivider;
    private IDivider mAllColumnDivider;
    private IGridProvider mProvider;

    public DefaultGridProvider() {
    }

    public DefaultGridProvider(@NonNull IGridProvider provider) {
        mProvider = provider;
    }

    public void setAllRowDivider(@NonNull IDivider allRowDivider) {
        mAllRowDivider = allRowDivider;
    }

    public void setAllColumnDivider(@NonNull IDivider allColumnDivider) {
        mAllColumnDivider = allColumnDivider;
    }

    public void setRowDivider(int row, @NonNull IDivider divider) {
        if (mRowDividers == null)
            mRowDividers = new SparseArray<>();
        mRowDividers.put(row, divider);
    }

    @Override
    public IDivider createRowDivider(int row) {
        final IDivider rowDivider = mRowDividers == null ? null : mRowDividers.get(row);
        return rowDivider == null ? mAllRowDivider == null ? mProvider == null ?
                mDefaultDivider : mProvider.createRowDivider(row) : mAllRowDivider : rowDivider;
    }

    public void setColumnDivider(int row, IDivider divider) {
        if (mColumnDividers == null)
            mColumnDividers = new SparseArray<>();
        mColumnDividers.put(row, divider);
    }

    @Override
    public IDivider createColumnDivider(int column) {
        final IDivider columnDivider = mColumnDividers == null ? null : mColumnDividers.get(column);
        return columnDivider == null ? mAllColumnDivider == null ? mProvider == null ?
                mDefaultDivider : mProvider.createColumnDivider(column) : mAllColumnDivider :
                columnDivider;
    }

    public void setRowNeedDraw(int row, boolean need) {
        if (mRowNeedDrawFlags == null)
            mRowNeedDrawFlags = new SparseBooleanArray();
        mRowNeedDrawFlags.put(row, need);
    }

    @Override
    public boolean isRowNeedDraw(int row) {
        return mRowNeedDrawFlags == null ? mProvider == null || mProvider.isRowNeedDraw(row) :
                mRowNeedDrawFlags.get(row, true);
    }

    public void setColumnNeedDraw(int column, boolean need) {
        if (mColumnNeedDrawFlags == null)
            mColumnNeedDrawFlags = new SparseBooleanArray();
        mColumnNeedDrawFlags.put(column, need);
    }

    @Override
    public boolean isColumnNeedDraw(int column) {
        return mColumnNeedDrawFlags == null ? mProvider == null || mProvider.isColumnNeedDraw(column) :
                mColumnNeedDrawFlags.get(column, true);
    }

    @Override
    public void release() {
        if (mRowDividers != null)
            mRowDividers.clear();
        if (mColumnDividers != null)
            mColumnDividers.clear();
        if (mRowNeedDrawFlags != null)
            mRowNeedDrawFlags.clear();
        if (mColumnNeedDrawFlags != null)
            mColumnNeedDrawFlags.clear();
        mProvider = null;
        mAllColumnDivider = null;
        mAllRowDivider = null;
    }
}
