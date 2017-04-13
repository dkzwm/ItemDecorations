package me.dkzwm.itemdecorations.provider;

import me.dkzwm.itemdecorations.divider.IDivider;

/**
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */
public interface IGridProvider extends IProvider {
    IDivider createRowDivider(int row);

    IDivider createColumnDivider(int column);

    boolean isRowNeedDraw(int row);

    boolean isColumnNeedDraw(int column);
}
