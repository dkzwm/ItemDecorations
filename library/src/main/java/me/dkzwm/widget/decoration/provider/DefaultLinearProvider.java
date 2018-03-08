package me.dkzwm.widget.decoration.provider;

import me.dkzwm.widget.decoration.divider.ColorDivider;
import me.dkzwm.widget.decoration.divider.IDivider;

/**
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */
public final class DefaultLinearProvider implements ILinearProvider {
    private IDivider mDivider = new ColorDivider();

    @Override
    public IDivider createDivider(int position) {
        return mDivider;
    }

    @Override
    public boolean isDividerNeedDraw(int position) {
        return true;
    }

    @Override
    public int marginStart(int position) {
        return 0;
    }

    @Override
    public int marginEnd(int position) {
        return 0;
    }

    @Override
    public void release() {
        mDivider = null;
    }
}
