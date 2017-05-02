package me.dkzwm.itemdecorationsample;

import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NumberedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mLabels;
    private LayoutInflater mInflater;
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() != null && v.getTag() instanceof String) {
                Toast.makeText(v.getContext(), (String) v.getTag(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    public NumberedAdapter(LayoutInflater inflater, int count) {
        mLabels = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            mLabels.add(String.valueOf(i));
        }
        mInflater = inflater;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResId = R.layout.item_one;
        RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            if (manager.getOrientation() == OrientationHelper.HORIZONTAL) {
                layoutResId = R.layout.item_two;
            }
        }
        View view = mInflater.inflate(layoutResId, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).setData(mLabels.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mLabels.size();
    }

    private class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;


        private TextViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView_item);
        }

        private void setData(String label) {
            mTextView.setText(label);
            mTextView.setTag(label);
            mTextView.setOnClickListener(mClickListener);
        }
    }
}