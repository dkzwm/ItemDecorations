package me.dkzwm.widget.decoration.provider;

import me.dkzwm.widget.decoration.divider.ColorDivider;
import me.dkzwm.widget.decoration.divider.IDivider;

/**
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */
public final class DefaultGridProvider implements IGridProvider {
    private IDivider mDivider = new ColorDivider();

    @Override
    public IDivider createRowDivider(int row) {
        return mDivider;
    }

    @Override
    public IDivider createColumnDivider(int column) {
        return mDivider;
    }

    @Override
    public boolean isRowNeedDraw(int row) {
        return true;
    }

    @Override
    public boolean isColumnNeedDraw(int column) {
        return true;
    }

    @Override
    public void release() {
        mDivider = null;
    }
}
