package me.dkzwm.widget.decoration.provider;

/**
 * Created by dkzwm on 2017/4/13.
 *
 * @author dkzwm
 */
public abstract class GridProvider implements IGridProvider {
    @Override
    public boolean isRowNeedDraw(int row) {
        return true;
    }

    @Override
    public boolean isColumnNeedDraw(int column) {
        return true;
    }

}
