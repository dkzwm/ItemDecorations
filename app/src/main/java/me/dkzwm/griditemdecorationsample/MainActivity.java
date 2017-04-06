package me.dkzwm.griditemdecorationsample;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;

import me.dkzwm.griditemdecoration.GridItemDecoration;

public class MainActivity extends AppCompatActivity {
    private GridLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private GridItemDecoration mDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_main);
        mAdapter = new NumberedAdapter(getLayoutInflater(), 100);
        mDecoration = new GridItemDecoration(recyclerView, mAdapter,
                new GridItemDecoration.DrawableProvider() {
                    @Override
                    public Drawable createVertical(RecyclerView parent, int row) {
                        if (row % 3 == 0)
                            return ContextCompat.getDrawable(MainActivity
                                    .this, R.drawable.shape_divider_blue);
                        else if (row % 3 == 1)
                            return ContextCompat.getDrawable(MainActivity
                                    .this, R.drawable.shape_divider_green);
                        else
                            return ContextCompat.getDrawable(MainActivity
                                    .this, R.drawable.shape_divider_red);
                    }

                    @Override
                    public Drawable createHorizontal(RecyclerView parent, int row, int column) {
                        if (column % 3 == 0)
                            return ContextCompat.getDrawable(MainActivity
                                    .this, R.drawable.shape_divider_blue);
                        else if (column % 3 == 1)
                            return ContextCompat.getDrawable(MainActivity
                                    .this, R.drawable.shape_divider_green);
                        else
                            return ContextCompat.getDrawable(MainActivity
                                    .this, R.drawable.shape_divider_red);
                    }
                });
        recyclerView.addItemDecoration(mDecoration);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 4);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position + 1) % 5 == 0 ? mLayoutManager.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.change_span_size);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == Menu.FIRST) {
            Random random = new Random();
            int randomInt = random.nextInt();
            final int randomSpan = randomInt % 8 == 0 ? 1 : randomInt % 8;
            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (position + 1) % randomSpan == 0 ? mLayoutManager.getSpanCount() : 1;
                }
            });
            mAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDecoration.detach(mAdapter);
    }
}
