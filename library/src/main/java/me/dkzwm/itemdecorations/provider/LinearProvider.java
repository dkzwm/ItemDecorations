package me.dkzwm.itemdecorations.provider;

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

    @Override
    public void release() {

    }
}
