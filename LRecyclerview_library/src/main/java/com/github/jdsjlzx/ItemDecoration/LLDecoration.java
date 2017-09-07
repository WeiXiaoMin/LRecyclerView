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
 * 适合LayoutManager为LinearLayoutManager的RecyclerView
 * Created by sjsd on 2017/3/17.
 */

public class LLDecoration extends RecyclerView.ItemDecoration {

    protected int mHeight;
    protected int mLPadding;
    protected int mRPadding;
    protected Paint mPaint;

    /**
     * 间隙高度
     *
     * @param height
     * @return
     */
    public LLDecoration setHeight(int height) {
        this.mHeight = height;

        return this;
    }

    /**
     * 左侧间隙宽度
     *
     * @param lPadding
     * @return
     */
    public LLDecoration setLPadding(int lPadding) {
        this.mLPadding = lPadding;
        return this;
    }

    /**
     * 右侧间隙宽度
     *
     * @param rPadding
     * @return
     */
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
    public final void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.Adapter adapter = parent.getAdapter();
        int count = parent.getChildCount();

        if (adapter instanceof LRecyclerViewAdapter) {
            LRecyclerViewAdapter lRecyclerViewAdapter = (LRecyclerViewAdapter) adapter;
            drawL(c, parent, lRecyclerViewAdapter, count);

        } else if (adapter instanceof MRecyclerViewAdapter) {
            MRecyclerViewAdapter mRecyclerViewAdapter = (MRecyclerViewAdapter) adapter;
            drawM(c, parent, mRecyclerViewAdapter, count);

        } else {
            throw new RuntimeException("the adapter must be LRecyclerViewAdapter");
        }

    }

    private void drawM(Canvas c, RecyclerView parent, MRecyclerViewAdapter adapter, int count) {
        for (int i = 0; i < count; i++) {

            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            c.save();

            if (adapter.isRefreshHeader(position)) {
                drawRefreshHeaderDecoration(child, c, position);

            } else if (adapter.isHeader(position)) {
                drawHeaderDecoration(child, c, position);

            } else if (adapter.isFooter(position)) {
                drawFooterDecoration(child, c, position);

            } else {

                int itemPosition = adapter.getAdapterPosition(true, position);
                drawItemDecoration(child, c, itemPosition);
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
                drawRefreshHeaderDecoration(child, c, position);

            } else if (lRecyclerViewAdapter.isHeader(position)) {
                drawHeaderDecoration(child, c, position);

            } else if (lRecyclerViewAdapter.isFooter(position)) {
                drawFooterDecoration(child, c, position);

            } else {

                int itemPosition = lRecyclerViewAdapter.getAdapterPosition(true, position);
                drawItemDecoration(child, c, itemPosition);
            }

            c.restore();
        }
    }

    /**
     * wxm:绘制ItemDecoration，第一个item的上方分割线默认绘制
     *
     * @param child
     * @param c
     * @param itemPosition
     */
    protected void drawItemDecoration(View child, Canvas c, int itemPosition) {

        if (mPaint == null) {
            return;
        }

        if(mLPadding>0) {
            // wxm:item左侧
            c.drawRect( child.getLeft(),
                    child.getTop() - mHeight,
                    child.getLeft() + mLPadding,
                    child.getBottom() + mHeight,
                    mPaint);
        }

        if (itemPosition == 0 && mHeight > 0) {
            // wxm:item上方
            c.drawRect(child.getLeft() + mLPadding,
                    child.getTop() - mHeight,
                    child.getRight() - mRPadding,
                    child.getTop(),
                    mPaint);
        }

        if(mRPadding>0) {
            // wxm:item右侧
            c.drawRect(child.getRight() - mRPadding,
                    child.getTop() - mHeight,
                    child.getRight(),
                    child.getBottom() + mHeight,
                    mPaint);
        }

        if(mHeight>0) {
            // wxm:item下方
            c.drawRect(child.getLeft() + mLPadding,
                    child.getBottom(),
                    child.getRight() - mRPadding,
                    child.getBottom() + mHeight,
                    mPaint);
        }
    }


    /**
     * wxm: 绘制脚视图的Decoration，供实现
     *
     * @param child
     * @param c
     * @param position
     */
    protected void drawFooterDecoration(View child, Canvas c, int position) {

    }

    /**
     * wxm: 绘制头视图的Decoration，供实现
     *
     * @param child
     * @param c
     * @param position
     */
    protected void drawHeaderDecoration(View child, Canvas c, int position) {

    }

    /**
     * wxm: 绘制头部刷新视图的Decoration，供实现
     *
     * @param child
     * @param c
     * @param position
     */
    protected void drawRefreshHeaderDecoration(View child, Canvas c, int position) {

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.Adapter adapter = parent.getAdapter();
        int position = parent.getChildAdapterPosition(view);

        if (adapter instanceof LRecyclerViewAdapter) {
            LRecyclerViewAdapter lRecyclerViewAdapter = (LRecyclerViewAdapter) adapter;
            offL(outRect, lRecyclerViewAdapter, position);

        } else if (adapter instanceof MRecyclerViewAdapter) {
            MRecyclerViewAdapter mRecyclerViewAdapter = (MRecyclerViewAdapter) adapter;
            offM(outRect, mRecyclerViewAdapter, position);
        } else {

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

    /**
     * 让item腾出间隙，第一个item上方默认腾出间隙
     *
     * @param outRect
     * @param itemPosition
     */
    protected void offsetsItemDecoration(Rect outRect, int itemPosition) {

        if (itemPosition == 0) {
            outRect.set(0, mHeight, 0, mHeight);
        } else {
            outRect.set(0, 0, 0, mHeight);
        }
    }

    /**
     * 腾出脚视图的间隙
     *
     * @param outRect
     * @param position
     */
    protected void offsetsFooterDecoration(Rect outRect, int position) {

    }

    /**
     * 腾出头视图的间隙
     *
     * @param outRect
     * @param position
     */
    protected void offsetsHeaderDecoration(Rect outRect, int position) {

    }

    /**
     * 腾出头部刷新视图的间隙
     *
     * @param outRect
     * @param position
     */
    protected void offsetsRefreshHeaderDecoration(Rect outRect, int position) {

    }
}
