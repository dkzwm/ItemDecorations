package me.dkzwm.widget.decoration.provider;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

import me.dkzwm.widget.decoration.divider.ColorDivider;
import me.dkzwm.widget.decoration.divider.IDivider;

/**
 * Created by dkzwm on 2017/4/11.
 *
 * @author dkzwm
 */
public final class DefaultLinearProvider implements ILinearProvider {
    private IDivider mDivider = new ColorDivider();
    private SparseArray<IDivider> mDividers = new SparseArray<>();
    private SparseIntArray mMarginsOfStart = new SparseIntArray();
    private SparseIntArray mMarginsOfEnd = new SparseIntArray();
    private SparseBooleanArray mNeedDrawFlags = new SparseBooleanArray();
    private ILinearProvider mProvider;
    private int mAllMarginOfStart = -1;
    private int mAllMarginOfEnd = -1;

    public DefaultLinearProvider() {
    }

    public DefaultLinearProvider(ILinearProvider provider) {
        mProvider = provider;
    }

    public void setDivider(int position, IDivider divider) {
        mDividers.put(position, divider);
    }

    @Override
    public IDivider createDivider(int position) {
        final IDivider divider = mDividers.get(position);
        return divider == null ? mProvider == null
                ? mDivider : mProvider.createDivider(position) : divider;
    }

    public void setDividerNeedDraw(int position, boolean need) {
        mNeedDrawFlags.put(position, need);
    }

    @Override
    public boolean isDividerNeedDraw(int position) {
        return mNeedDrawFlags.get(position, true);
    }

    public void setMarginStart(int position, int margin) {
        mMarginsOfStart.put(position, margin);
    }

    public void setAllMarginStart(int margin) {
        mAllMarginOfStart = margin;
    }

    @Override
    public int marginStart(int position) {
        final int margin = mMarginsOfStart.get(position, -1);
        return margin == -1 ? mAllMarginOfStart == -1 ? 0 : mAllMarginOfStart : margin;
    }

    public void setMarginEnd(int position, int margin) {
        mMarginsOfEnd.put(position, margin);
    }

    public void setAllMarginEnd(int margin) {
        mAllMarginOfEnd = margin;
    }

    @Override
    public int marginEnd(int position) {
        final int margin = mMarginsOfEnd.get(position, -1);
        return margin == -1 ? mAllMarginOfEnd == -1 ? 0 : mAllMarginOfEnd : margin;
    }

    @Override
    public void release() {
        mDividers.clear();
        mNeedDrawFlags.clear();
        mMarginsOfStart.clear();
        mMarginsOfEnd.clear();
        mDivider = null;
    }
}
