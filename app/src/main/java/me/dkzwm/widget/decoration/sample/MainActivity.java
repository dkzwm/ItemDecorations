package me.dkzwm.widget.decoration.sample;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;

import me.dkzwm.widget.decoration.GridItemDecoration;
import me.dkzwm.widget.decoration.LinearItemDecoration;
import me.dkzwm.widget.decoration.divider.DrawableDivider;
import me.dkzwm.widget.decoration.divider.IDivider;
import me.dkzwm.widget.decoration.divider.PaintDivider;
import me.dkzwm.widget.decoration.provider.GridProvider;
import me.dkzwm.widget.decoration.provider.LinearProvider;

public class MainActivity extends AppCompatActivity {
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private GridItemDecoration mGridItemDecoration;
    private LinearItemDecoration mLinearItemDecoration;
    private Random mRandom = new Random();
    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.RED);
        mRecyclerView = findViewById(R.id.recyclerView_main);
        mAdapter = new NumberedAdapter(getLayoutInflater(), 100);
        mGridItemDecoration = new GridItemDecoration.Builder(this).provider(new GridProvider() {
            private IDivider mDividerOne = new PaintDivider(mPaint);

            @Override
            public IDivider createRowDivider(int row) {
                return mDividerOne;
            }

            @Override
            public IDivider createColumnDivider(int column) {
                return mDividerOne;
            }

            @Override
            public void release() {
                mDividerOne = null;
            }
        }).build();
        mLinearItemDecoration = new LinearItemDecoration.Builder(this).provider(new LinearProvider() {
            private IDivider mDividerOne = new DrawableDivider(ContextCompat.getDrawable
                    (MainActivity.this, R.drawable.shape_divider_green), true);
            private IDivider mDividerTwo = new DrawableDivider(ContextCompat.getDrawable
                    (MainActivity.this, R.drawable.shape_divider_blue), true);
            private IDivider mDividerThird = new DrawableDivider(ContextCompat.getDrawable
                    (MainActivity.this, R.drawable.shape_divider_red), true);

            @Override
            public IDivider createDivider(int position) {
                switch ((position + 3) % 3) {
                    case 0:
                        return mDividerOne;
                    case 1:
                        return mDividerTwo;
                    default:
                        return mDividerThird;
                }
            }

            @Override
            public boolean isDividerNeedDraw(int position) {
                return position != mAdapter.getItemCount() - 1;
            }

            @Override
            public int marginStart(int position) {
                if ((position + 2) % 2 == 0)
                    return 32;
                else if ((position + 2) % 3 == 0) {
                    return 62;
                }
                return 0;
            }

            @Override
            public void release() {
                mDividerOne = null;
                mDividerTwo = null;
                mDividerThird = null;
            }
        }).drawInsideEachOfItem(true).build();
        mGridLayoutManager = new GridLayoutManager(this, 4);
        mGridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position + 1) % 5 == 0 ? mGridLayoutManager.getSpanCount() : 1;
            }
        });
        mGridLayoutManager.setReverseLayout(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mLinearLayoutManager.setReverseLayout(false);
        mRecyclerView.addItemDecoration(mGridItemDecoration);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.test_grid_layout);
        menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, R.string.test_linear_layout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int randomInt = mRandom.nextInt();
        if (item.getItemId() == Menu.FIRST) {
            final int randomSpan = randomInt % 8 == 0 ? 1 : randomInt % 8;
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (position + 1) % randomSpan == 0 ? mGridLayoutManager.getSpanCount() : 1;
                }
            });
            if (randomInt % 3 == 0) {
                mLinearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
            } else {
                mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            }
            if (randomSpan % 2 == 0)
                mGridLayoutManager.setReverseLayout(true);
            else
                mGridLayoutManager.setReverseLayout(false);
            mRecyclerView.removeItemDecoration(mLinearItemDecoration);
            mRecyclerView.removeItemDecoration(mGridItemDecoration);
            mRecyclerView.addItemDecoration(mGridItemDecoration);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            return true;
        } else if (item.getItemId() == Menu.FIRST + 1) {
            if (randomInt % 4 == 0) {
                mLinearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
            } else {
                mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            }
            randomInt = mRandom.nextInt();
            if (randomInt % 2 == 0) {
                mLinearLayoutManager.setReverseLayout(false);
            } else {
                mLinearLayoutManager.setReverseLayout(true);
            }
            mRecyclerView.removeItemDecoration(mGridItemDecoration);
            mRecyclerView.removeItemDecoration(mLinearItemDecoration);
            mRecyclerView.addItemDecoration(mLinearItemDecoration);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGridItemDecoration.release();
        mLinearItemDecoration.release();
    }
}
