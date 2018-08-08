package me.dkzwm.widget.decoration.provider;

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
    private SparseArray<IDivider> mRowDividers = new SparseArray<>();
    private SparseArray<IDivider> mColumnDividers = new SparseArray<>();
    private SparseBooleanArray mRowNeedDrawFlags = new SparseBooleanArray();
    private SparseBooleanArray mColumnNeedDrawFlags = new SparseBooleanArray();
    private IDivider mAllRowDivider = new ColorDivider();
    private IDivider mAllColumnDivider = new ColorDivider();
    private IGridProvider mProvider;

    public DefaultGridProvider() {
    }

    public DefaultGridProvider(IGridProvider provider) {
        mProvider = provider;
    }

    public void setAllRowDivider(IDivider allRowDivider) {
        mAllRowDivider = allRowDivider;
    }

    public void setAllColumnDivider(IDivider allColumnDivider) {
        mAllColumnDivider = allColumnDivider;
    }

    public void setRowDivider(int row, IDivider divider) {
        mRowDividers.put(row, divider);
    }

    @Override
    public IDivider createRowDivider(int row) {
        final IDivider rowDivider = mRowDividers.get(row);
        return rowDivider == null ? mProvider == null
                ? mAllRowDivider : mProvider.createRowDivider(row) : rowDivider;
    }

    public void setColumnDivider(int row, IDivider divider) {
        mColumnDividers.put(row, divider);
    }

    @Override
    public IDivider createColumnDivider(int column) {
        final IDivider columnDivider = mColumnDividers.get(column);
        return columnDivider == null ? mProvider == null
                ? mAllColumnDivider : mProvider.createColumnDivider(column) : columnDivider;
    }

    public void setRowNeedDraw(int row, boolean need) {
        mRowNeedDrawFlags.put(row, need);
    }

    @Override
    public boolean isRowNeedDraw(int row) {
        return mRowNeedDrawFlags.get(row, true);
    }

    public void setColumnNeedDraw(int column, boolean need) {
        mColumnNeedDrawFlags.put(column, need);
    }

    @Override
    public boolean isColumnNeedDraw(int column) {
        return mColumnNeedDrawFlags.get(column, true);
    }

    @Override
    public void release() {
        mRowDividers.clear();
        mColumnDividers.clear();
        mRowNeedDrawFlags.clear();
        mColumnNeedDrawFlags.clear();
        mProvider = null;
        mAllColumnDivider = null;
        mAllRowDivider = null;
    }
}
