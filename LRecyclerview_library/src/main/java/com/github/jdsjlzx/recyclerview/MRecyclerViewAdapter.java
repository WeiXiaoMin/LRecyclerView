package com.github.jdsjlzx.recyclerview;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.IRefreshHeader;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;

import java.util.List;

/**
 * RecyclerView.Adapter with Header and Footer
 */
public class MRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_REFRESH_HEADER = 10000;
    private static final int COUNT_REFRESH_HEADER = 1;
    private static final int TYPE_NORMAL = 0;
    private static final int HEADER_BASE_KEY = 17031601;
    private static final int FOOTER_BASE_KEY = 17042701;

    private IRefreshHeader mRefreshHeader;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * RecyclerView使用的，真正的Adapter
     */
    private RecyclerView.Adapter mInnerAdapter;

    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFooterViewArray = new SparseArray<>(2);

    private SpanSizeLookup mSpanSizeLookup;


    public MRecyclerViewAdapter(RecyclerView.Adapter innerAdapter) {
        this.mInnerAdapter = innerAdapter;
    }

    public void setRefreshHeader(IRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    public void addHeaderView(View view) {

        if (view == null) {
            throw new RuntimeException("header is null");
        }

        mHeaderViews.put(HEADER_BASE_KEY + mHeaderViews.size(), view);
    }

    //wxm:
    public void addHeaderView(int key, View view) {

        if (view == null) {
            throw new RuntimeException("header is null");
        }

        mHeaderViews.put(HEADER_BASE_KEY + key, view);
    }

    public void addFooterView(View view) {

        if (view == null) {
            throw new RuntimeException("footer is null");
        }

        if (mFooterViewArray.size() > 0) {
            int key = mFooterViewArray.keyAt(mFooterViewArray.size() - 1);
            mFooterViewArray.put(key + 1, view);
        } else {
            mFooterViewArray.put(FOOTER_BASE_KEY, view);
        }

    }

    public void addFooterView(int key, View view) {
        mFooterViewArray.put(FOOTER_BASE_KEY + key, view);
    }

    public int getFooterViewIndex(View view) {
        return mFooterViewArray.indexOfValue(view);
    }

    /**
     * 根据header的ViewType判断是哪个header
     *
     * @param itemType
     * @return
     */
    private View getHeaderViewByType(int itemType) {
        return mHeaderViews.get(itemType);
    }

    /**
     * 判断一个type是否为HeaderType
     *
     * @param itemViewType
     * @return
     */
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViews.get(itemViewType) != null;
    }

    private boolean isFooterType(int viewType) {
        return mFooterViewArray.get(viewType) != null;
    }

    @Nullable
    public View getFooterView(int key) {
        return mFooterViewArray.get(FOOTER_BASE_KEY + key);
    }


    /**
     * 返回第一个HeaderView
     *
     * @return
     */
    public View getHeaderView(int key) {
        return mHeaderViews.get(HEADER_BASE_KEY + key);
    }

    public SparseArray<View> getHeaderViews() {
        return mHeaderViews;
    }

    public void clearHeaderView() {
        mHeaderViews.clear();
    }

    //wxm:
    public void removeHeaderView(View headerView) {
        int index = mHeaderViews.indexOfValue(headerView);

        if (index >= 0) {
            mHeaderViews.removeAt(index);
            this.notifyDataSetChanged();
        }
    }

    //wxm:
    public void removeHeaderView(int key) {
        mHeaderViews.remove(HEADER_BASE_KEY + key);
    }

    //wxm:
    public boolean isContainsHeaderView(View headerView) {
        return mHeaderViews.indexOfValue(headerView) >= 0;
    }

    //wxm:
    public boolean isContainsHeaderView(int key) {
        return mHeaderViews.get(HEADER_BASE_KEY + key) != null;
    }

    public void clearFooterView() {
        mFooterViewArray.clear();
        this.notifyDataSetChanged();
    }

    public void removeFooterView(View view) {
        int index = mFooterViewArray.indexOfValue(view);
        if (index >= 0) {
            mFooterViewArray.removeAt(index);
            this.notifyDataSetChanged();
        }
    }

    public void removeFooterViewWithNotify(View view) {
        int index = mFooterViewArray.indexOfValue(view);
        if (index >= 0) {
            mFooterViewArray.removeAt(index);
        }
    }


    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViewArray.size();
    }

    public boolean isHeader(int position) {
        return position >= COUNT_REFRESH_HEADER && position < mHeaderViews.size() + COUNT_REFRESH_HEADER;
    }

    public boolean isRefreshHeader(int position) {
        return position < COUNT_REFRESH_HEADER;
    }

    public boolean isFooter(int position) {
        int lastPosition = getItemCount() - getFooterViewsCount();
        return getFooterViewsCount() > 0 && position >= lastPosition;
    }

    public int convertFooterPosition(int position) {

        if (getFooterViewsCount() == 0) {
            return -1;
        }

        int innerItemCount = 0;
        if (mInnerAdapter != null) {
            innerItemCount = mInnerAdapter.getItemCount();
        }

        return position - COUNT_REFRESH_HEADER - getHeaderViewsCount() - innerItemCount;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_REFRESH_HEADER) {
            return new ViewHolder(mRefreshHeader.getHeaderView());
        } else if (isHeaderType(viewType)) {
            return new ViewHolder(getHeaderViewByType(viewType));
        } else if (isFooterType(viewType)) {
            return new ViewHolder(mFooterViewArray.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position) || isRefreshHeader(position)) {
            return;
        }
        final int adjPosition = position - (getHeaderViewsCount() + COUNT_REFRESH_HEADER);
        int adapterCount;
        if (mInnerAdapter != null) {
            adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mInnerAdapter.onBindViewHolder(holder, adjPosition);

                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onItemClick(holder.itemView, adjPosition);
                        }
                    });

                }

                if (mOnItemLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            mOnItemLongClickListener.onItemLongClick(holder.itemView, adjPosition);
                            return true;
                        }
                    });
                }

            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {

            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
            final int adjPosition = position - (getHeaderViewsCount() + COUNT_REFRESH_HEADER);
            int adapterCount;
            if (mInnerAdapter != null) {
                adapterCount = mInnerAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    mInnerAdapter.onBindViewHolder(holder, adjPosition, payloads);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        if (mInnerAdapter != null) {
            return getHeaderViewsCount() + getFooterViewsCount() + mInnerAdapter.getItemCount() + COUNT_REFRESH_HEADER;
        } else {
            return getHeaderViewsCount() + getFooterViewsCount() + COUNT_REFRESH_HEADER;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int adjPosition = position - (getHeaderViewsCount() + COUNT_REFRESH_HEADER);
        if (isRefreshHeader(position)) {
            return TYPE_REFRESH_HEADER;
        }
        if (isHeader(position)) {
            position = position - COUNT_REFRESH_HEADER;
            return mHeaderViews.keyAt(position);
        }
        if (isFooter(position)) {
            int footerPosition = convertFooterPosition(position);
            if (footerPosition >= 0) {
                return mFooterViewArray.keyAt(footerPosition);
            }
        }


        int adapterCount;
        if (mInnerAdapter != null) {
            adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mInnerAdapter.getItemViewType(adjPosition);
            }
        }

        return TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        if (mInnerAdapter != null && position >= getHeaderViewsCount()) {
            int adjPosition = position - getHeaderViewsCount();
            //判断是否setHasStableIds(true);
            if (hasStableIds()) {
                adjPosition--;
            }
            int adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mInnerAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mSpanSizeLookup == null) {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    } else {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : mSpanSizeLookup.getSpanSize(gridManager, (position - (getHeaderViewsCount() + 1)));
                    }

                }
            });
        }
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (isHeader(holder.getLayoutPosition()) || isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition())) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        mInnerAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewRecycled(holder);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * @param isCallback whether position is from callback interface //wxm:true获取innerAdapter的position,false获取LRecyclerAdapter的position
     * @param position
     * @return
     */
    public int getAdapterPosition(boolean isCallback, int position) {
        if (isCallback) {
            int adjPosition = position - (getHeaderViewsCount() + 1);
            int adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adjPosition;
            }
        } else {
            return (position + getHeaderViewsCount()) + 1;
        }

        return -1;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mOnItemLongClickListener = itemLongClickListener;
    }

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    /**
     * @param spanSizeLookup only used to GridLayoutManager
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

}
