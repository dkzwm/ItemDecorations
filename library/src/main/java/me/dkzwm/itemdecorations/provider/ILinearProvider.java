package me.dkzwm.itemdecorations.provider;

import me.dkzwm.itemdecorations.divider.IDivider;

/**
 * Created by dkzwm on 2017/4/12.
 *
 * @author dkzwm
 */
public interface ILinearProvider extends IProvider {
    IDivider createDivider(int position);

    boolean isDividerNeedDraw(int position);

    int marginStart(int position);

    int marginEnd(int position);

}
