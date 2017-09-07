package com.github.jdsjlzx.ItemDecoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

/**
 * wxm修改自DividerDecoration,在每个item下方以及第一个item的上方添加分割线。
 * A simple divider decoration with customizable colour, height, and left and right padding.
 * Used only for LRecyclerView
 */
public class LBottomDecoration extends RecyclerView.ItemDecoration {

    private int mHeight;
    private int mLPadding;
    private int mRPadding;
    private Paint mPaint;
    private DecorationExcluder mExcluder;

    private LBottomDecoration(int height, int lPadding, int rPadding, int colour) {
        mHeight = height;
        mLPadding = lPadding;
        mRPadding = rPadding;

        if (colour != 0) {
            mPaint = new Paint();
            mPaint.setColor(colour);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (mPaint == null) {
            return;
        }

        RecyclerView.Adapter adapter = parent.getAdapter();

        LRecyclerViewAdapter lRecyclerViewAdapter;
        if (adapter instanceof LRecyclerViewAdapter) {
            lRecyclerViewAdapter = (LRecyclerViewAdapter) adapter;
        } else {
            throw new RuntimeException("the adapter must be LRecyclerViewAdapter");
        }

        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {

            final View child = parent.getChildAt(i);

            final int top = child.getBottom();
            final int bottom = top + mHeight;

            int left = child.getLeft() + mLPadding;
            int right = child.getRight() - mRPadding;

            int position = parent.getChildAdapterPosition(child);

            c.save();

            if (lRecyclerViewAdapter.isRefreshHeader(position) || lRecyclerViewAdapter.isHeader(position) || lRecyclerViewAdapter.isFooter(position)) {
                c.drawRect(0, 0, 0, 0, mPaint);
            } else {

                int dataPosition = lRecyclerViewAdapter.getAdapterPosition(true, position);
                if (mExcluder == null || !mExcluder.exclude(dataPosition)) {

                    if (dataPosition == 0) {

                        //wxm:第一个item的顶部分割线
                        if (mExcluder != null && !mExcluder.isExcludeTopDecoration()) {
                            int bottom1 = child.getTop();
                            int top1 = bottom1 - mHeight;
                            c.drawRect(left, top1, right, bottom1, mPaint);
                        }

                        c.drawRect(left, top, right, bottom, mPaint);

                    } else {

                        c.drawRect(left, top, right, bottom, mPaint);
                    }

                }

//                else {
//                    //wxm:排除
//                    c.drawRect(0, 0, 0, 0, mPaint);
//                }

            }

            c.restore();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();

        LRecyclerViewAdapter lRecyclerViewAdapter;
        if (adapter instanceof LRecyclerViewAdapter) {
            lRecyclerViewAdapter = (LRecyclerViewAdapter) adapter;
        } else {
            throw new RuntimeException("the adapter must be LRecyclerViewAdapter");
        }

        int position = parent.getChildAdapterPosition(view);

        if (lRecyclerViewAdapter.isRefreshHeader(position) || lRecyclerViewAdapter.isHeader(position) || lRecyclerViewAdapter.isFooter(position)) {
//            outRect.bottom = mHeight;
            outRect.set(0, 0, 0, 0);
        } else {

            int dataPosition = lRecyclerViewAdapter.getAdapterPosition(true, position);

            if (mExcluder == null || !mExcluder.exclude(dataPosition)) {

                if (dataPosition == 0) {

                    //wxm:第一个item的顶部分割线
                    if (mExcluder != null && !mExcluder.isExcludeTopDecoration()) {
                        outRect.set(0, mHeight, 0, mHeight);
                    } else {
                        outRect.set(0, 0, 0, mHeight);
                    }

                } else {
                    outRect.set(0, 0, 0, mHeight);
                }

            }

//            else {
//                //wxm:排除
//                outRect.set(0, 0, 0, 0);
//            }
        }

    }

    /**
     * A basic builder for divider decorations. The default builder creates a 1px thick black divider decoration.
     */
    public static class Builder {
        private Context mContext;
        private Resources mResources;
        private int mHeight;
        private int mLPadding;
        private int mRPadding;
        private int mColour;
        private DecorationExcluder mExcluder;

        public Builder(Context context) {
            mContext = context;
            mResources = context.getResources();
            mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1f, context.getResources().getDisplayMetrics());
            mLPadding = 0;
            mRPadding = 0;
//            mColour = Color.BLACK;
            mColour = 0;
        }

        public Builder setExcluder(DecorationExcluder excluder) {
            this.mExcluder = excluder;
            return this;
        }

        /**
         * Set the divider height in pixels
         *
         * @param pixels height in pixels
         * @return the current instance of the Builder
         */
        public Builder setHeight(float pixels) {
            mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels, mResources.getDisplayMetrics());

            return this;
        }

        /**
         * Set the divider height in dp
         *
         * @param resource height resource id
         * @return the current instance of the Builder
         */
        public Builder setHeight(@DimenRes int resource) {
            mHeight = mResources.getDimensionPixelSize(resource);
            return this;
        }

        /**
         * Sets both the left and right padding in pixels
         *
         * @param pixels padding in pixels
         * @return the current instance of the Builder
         */
        public Builder setPadding(float pixels) {
            setLeftPadding(pixels);
            setRightPadding(pixels);

            return this;
        }

        /**
         * Sets the left and right padding in dp
         *
         * @param resource padding resource id
         * @return the current instance of the Builder
         */
        public Builder setPadding(@DimenRes int resource) {
            setLeftPadding(resource);
            setRightPadding(resource);
            return this;
        }

        /**
         * Sets the left padding in pixels
         *
         * @param pixelPadding left padding in pixels
         * @return the current instance of the Builder
         */
        public Builder setLeftPadding(float pixelPadding) {
            mLPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixelPadding, mResources.getDisplayMetrics());

            return this;
        }

        /**
         * Sets the right padding in pixels
         *
         * @param pixelPadding right padding in pixels
         * @return the current instance of the Builder
         */
        public Builder setRightPadding(float pixelPadding) {
            mRPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixelPadding, mResources.getDisplayMetrics());

            return this;
        }

        /**
         * Sets the left padding in dp
         *
         * @param resource left padding resource id
         * @return the current instance of the Builder
         */
        public Builder setLeftPadding(@DimenRes int resource) {
            mLPadding = mResources.getDimensionPixelSize(resource);

            return this;
        }

        /**
         * Sets the right padding in dp
         *
         * @param resource right padding resource id
         * @return the current instance of the Builder
         */
        public Builder setRightPadding(@DimenRes int resource) {
            mRPadding = mResources.getDimensionPixelSize(resource);

            return this;
        }

        /**
         * Sets the divider colour
         *
         * @param resource the colour resource id
         * @return the current instance of the Builder
         */
        public Builder setColorResource(@ColorRes int resource) {
            setColor(ContextCompat.getColor(mContext, resource));

            return this;
        }

        /**
         * Sets the divider colour
         *
         * @param color the colour
         * @return the current instance of the Builder
         */
        public Builder setColor(@ColorInt int color) {
            mColour = color;

            return this;
        }

        /**
         * Instantiates a DividerDecoration with the specified parameters.
         *
         * @return a properly initialized DividerDecoration instance
         */
        public LBottomDecoration build() {
            LBottomDecoration LBottomDecoration = new LBottomDecoration(mHeight, mLPadding, mRPadding, mColour);
            LBottomDecoration.mExcluder = mExcluder;
            return LBottomDecoration;
        }
    }
}
