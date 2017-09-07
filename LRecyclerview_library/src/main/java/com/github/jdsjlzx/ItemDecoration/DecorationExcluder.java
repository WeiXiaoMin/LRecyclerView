package com.github.jdsjlzx.ItemDecoration;

/**
 * LRecyclerView 数据列表视图分割线排除回调
 * Created by sjsd on 2017/3/16.
 */

public interface DecorationExcluder {
    boolean exclude(int position);
    boolean isExcludeTopDecoration();
}
