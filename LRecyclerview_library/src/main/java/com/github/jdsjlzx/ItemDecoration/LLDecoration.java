package com.github.jdsjlzx.ItemDecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.MRecyclerViewAdapter;

/**
 * wxm 修改自LBottomDecoration，默认在每个item下方以及第一个item的上方添加分割线，可重写
 * Created by sjsd on 2017/3/17.
 */

public class LLDecoration extends RecyclerView.ItemDecoration {

    protected int mHeight;
    protected int mLPadding;
    protected int mRPadding;
    protected Paint mPaint;

    public LLDecoration setHeight(int height) {
        this.mHeight = height;

        return this;
    }

    public LLDecoration setLPadding(int lPadding) {
        this.mLPadding = lPadding;
        return this;
    }

    public LLDecoration setRPadding(int rPadding) {
        this.mRPadding = rPadding;
        return this;
    }

    public LLDecoration setColor(int color) {
        mPaint = new Paint();
        mPaint.setColor(color);
        return this;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.Adapter adapter = parent.getAdapter();
        int count = parent.getChildCount();

        if (adapter instanceof LRecyclerViewAdapter) {
            LRecyclerViewAdapter lRecyclerViewAdapter = (LRecyclerViewAdapter) adapter;
            drawL(c, parent, lRecyclerViewAdapter, count);

        } else if(adapter instanceof MRecyclerViewAdapter){
            MRecyclerViewAdapter mRecyclerViewAdapter = (MRecyclerViewAdapter) adapter;
            drawM(c, parent, mRecyclerViewAdapter, count);

        }else {
            throw new RuntimeException("the adapter must be LRecyclerViewAdapter");
        }

    }

    private void drawM(Canvas c, RecyclerView parent, MRecyclerViewAdapter adapter, int count) {
        for (int i = 0; i < count; i++) {

            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            c.save();

            if (adapter.isRefreshHeader(position)) {
                onDrawRefreshHeaderDecoration(child, c, position);

            } else if (adapter.isHeader(position)) {
                onDrawHeaderDecoration(child, c, position);

            } else if (adapter.isFooter(position)) {
                onDrawFooterDecoration(child, c, position);

            } else {

                int itemPosition = adapter.getAdapterPosition(true, position);
                onDrawItemDecoration(child, c, itemPosition);
            }

            c.restore();
        }
    }

    private void drawL(Canvas c, RecyclerView parent, LRecyclerViewAdapter lRecyclerViewAdapter, int count) {

        for (int i = 0; i < count; i++) {

            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            c.save();

            if (lRecyclerViewAdapter.isRefreshHeader(position)) {
                onDrawRefreshHeaderDecoration(child, c, position);

            } else if (lRecyclerViewAdapter.isHeader(position)) {
                onDrawHeaderDecoration(child, c, position);

            } else if (lRecyclerViewAdapter.isFooter(position)) {
                onDrawFooterDecoration(child, c, position);

            } else {

                int itemPosition = lRecyclerViewAdapter.getAdapterPosition(true, position);
                onDrawItemDecoration(child, c, itemPosition);
            }

            c.restore();
        }
    }

    protected void onDrawItemDecoration(View child, Canvas c, int itemPosition) {

        if (mPaint == null) {
            return;
        }

        final int top = child.getBottom();
        final int bottom = top + mHeight;

        int left = child.getLeft() + mLPadding;
        int right = child.getRight() - mRPadding;

        if (itemPosition == 0) {

            int bottom1 = child.getTop();
            int top1 = bottom1 - mHeight;
            c.drawRect(left, top1, right, bottom1, mPaint);
            c.drawRect(left, top, right, bottom, mPaint);

        } else {

            c.drawRect(left, top, right, bottom, mPaint);
        }

    }

    protected void onDrawFooterDecoration(View child, Canvas c, int position) {

    }

    protected void onDrawHeaderDecoration(View child, Canvas c, int position) {

    }

    protected void onDrawRefreshHeaderDecoration(View child, Canvas c, int position) {

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.Adapter adapter = parent.getAdapter();
        int position = parent.getChildAdapterPosition(view);

        if (adapter instanceof LRecyclerViewAdapter) {
            LRecyclerViewAdapter lRecyclerViewAdapter = (LRecyclerViewAdapter) adapter;
            offL(outRect, lRecyclerViewAdapter, position);

        } else if(adapter instanceof MRecyclerViewAdapter){
            MRecyclerViewAdapter mRecyclerViewAdapter = (MRecyclerViewAdapter) adapter;
            offM(outRect, mRecyclerViewAdapter, position);
        }else {

            throw new RuntimeException("the adapter must be LRecyclerViewAdapter");
        }

    }

    private void offM(Rect outRect, MRecyclerViewAdapter adapter, int position) {

        if (adapter.isRefreshHeader(position)) {
            offsetsRefreshHeaderDecoration(outRect, position);

        } else if (adapter.isHeader(position)) {
            offsetsHeaderDecoration(outRect, position);

        } else if (adapter.isFooter(position)) {
            offsetsFooterDecoration(outRect, position);

        } else {

            int itemPosition = adapter.getAdapterPosition(true, position);
            offsetsItemDecoration(outRect, itemPosition);
        }
    }

    private void offL(Rect outRect, LRecyclerViewAdapter lRecyclerViewAdapter, int position) {
        if (lRecyclerViewAdapter.isRefreshHeader(position)) {
            offsetsRefreshHeaderDecoration(outRect, position);

        } else if (lRecyclerViewAdapter.isHeader(position)) {
            offsetsHeaderDecoration(outRect, position);

        } else if (lRecyclerViewAdapter.isFooter(position)) {
            offsetsFooterDecoration(outRect, position);

        } else {

            int itemPosition = lRecyclerViewAdapter.getAdapterPosition(true, position);
            offsetsItemDecoration(outRect, itemPosition);
        }
    }

    protected void offsetsItemDecoration(Rect outRect, int itemPosition) {

        if (itemPosition == 0) {
            outRect.set(0, mHeight, 0, mHeight);
        } else {
            outRect.set(0, 0, 0, mHeight);
        }

    }

    protected void offsetsFooterDecoration(Rect outRect, int position) {

    }

    protected void offsetsHeaderDecoration(Rect outRect, int position) {

    }

    protected void offsetsRefreshHeaderDecoration(Rect outRect, int position) {

    }
}
