package com.lzx.demo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.jdsjlzx.ItemDecoration.LLDecoration;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.MRecyclerView;
import com.github.jdsjlzx.recyclerview.MRecyclerViewAdapter;
import com.lzx.demo.R;
import com.lzx.demo.adapter.DataAdapter;
import com.lzx.demo.bean.ItemModel;
import com.lzx.demo.util.TLog;

import java.util.ArrayList;

public class MRecyclerViewActivity extends AppCompatActivity {

    private MRecyclerView mRecyclerView;
    private DataAdapter mDataAdapter = null;
    private LLDecoration mDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrecycler_view);

        // wxm:设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // wxm:设置RecyclerView
        mRecyclerView = (MRecyclerView) findViewById(R.id.mrv_demo);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, null);
        mRecyclerView.setEmptyView(emptyView);

        ArrayList<ItemModel> dataList = new ArrayList<>();
        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setDataList(dataList);

        MRecyclerViewAdapter adapter = new MRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLoadMoreEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_mrecyclerview, menu);
        return true;
    }


    private void removeDecorations() {
        if (mDecoration != null) {
            mRecyclerView.removeItemDecoration(mDecoration);
        }
    }

    private void addDecorations() {

        if (mDecoration == null) {
            mDecoration = new LLDecoration()
                    .setHeight(16)
                    .setLPadding(16)
                    .setRPadding(16)
                    .setColor(Color.parseColor("#66ccff"));
        }
        mRecyclerView.addItemDecoration(mDecoration);
    }

    // wxm:模拟请求数据
    private void requestData() {

        TLog.error("refresh data");
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.title = "item" + i;
            dataList.add(itemModel);
        }
        mDataAdapter.setDataList(dataList);
        mRecyclerView.refreshComplete(8);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();

        } else if (itemId == R.id.addOrMoveDecoration) {
            if ("添加分割线".equals(item.getTitle().toString())) {
                item.setTitle("移除分割线");
                addDecorations();
            } else if ("移除分割线".equals(item.getTitle().toString())) {
                item.setTitle("添加分割线");
                removeDecorations();
            }
        }
        return true;
    }
}
