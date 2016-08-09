package com.yamblz.memoryleakssample.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import com.yamblz.memoryleakssample.R;

/**
 * Created by i-sergeev on 01.07.16
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int insetPx;

    public DividerItemDecoration(Context context) {
        insetPx = context.getResources().getDimensionPixelSize(R.dimen.card_insets);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        outRect.set(insetPx, insetPx, insetPx, insetPx);
    }
}
