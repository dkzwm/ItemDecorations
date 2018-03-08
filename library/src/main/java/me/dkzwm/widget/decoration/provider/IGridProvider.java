package me.dkzwm.widget.decoration.provider;

import me.dkzwm.widget.decoration.divider.IDivider;

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
