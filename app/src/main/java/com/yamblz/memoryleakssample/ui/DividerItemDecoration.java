package com.yamblz.memoryleakssample.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yamblz.memoryleakssample.R;

/**
 * Created by i-sergeev on 01.07.16
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int mInsets;
    private Paint paintBorder;

    public DividerItemDecoration(Context context) {
        mInsets = context.getResources().getDimensionPixelSize(R.dimen.card_insets);
        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setColor(Color.GRAY);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(mInsets);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            c.drawRect(
                    layoutManager.getDecoratedLeft(child) + mInsets / 2,
                    layoutManager.getDecoratedTop(child) + mInsets / 2,
                    layoutManager.getDecoratedRight(child) - mInsets / 2,
                    layoutManager.getDecoratedBottom(child) - mInsets / 2,
                    paintBorder);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        //We can supply forced insets for each item view here in the Rect
        outRect.set(mInsets, mInsets, mInsets, mInsets);
    }
}
