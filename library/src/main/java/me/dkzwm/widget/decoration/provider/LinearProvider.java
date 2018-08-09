package me.dkzwm.widget.decoration.provider;

/**
 * Created by dkzwm on 2017/4/13.
 *
 * @author dkzwm
 */
public abstract class LinearProvider implements ILinearProvider {
    @Override
    public boolean isDividerNeedDraw(int position) {
        return true;
    }

    @Override
    public int marginEnd(int position) {
        return 0;
    }

    @Override
    public int marginStart(int position) {
        return 0;
    }

}
