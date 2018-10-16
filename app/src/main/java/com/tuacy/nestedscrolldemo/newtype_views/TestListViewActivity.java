package com.tuacy.nestedscrolldemo.newtype_views;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.downrefresh.MeiTuanHeaderAdapter;
import com.example.downrefresh.MyUltimateRefreshView;
import com.sak.ultilviewlib.UltimateRefreshView;

import com.tuacy.nestedscrolldemo.R;
import com.tuacy.nestedscrolldemo.new_type_interface.OnHeaderRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class TestListViewActivity extends AppCompatActivity implements RefreshListView.IRefreshListener {
    private static final String TAG = "TestListViewActivity";

    private ListView listview01;

    private List<SaveData> data_list = new ArrayList<SaveData>();
    private TestListAdapter adapter;
    private MyUltimateRefreshView mUltimateRefreshView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list_view);
        initTestData();
        adapter = new TestListAdapter(this);
        adapter.addMoreItems(data_list,true);
        initView();
        View headview = LayoutInflater.from(this).inflate(R.layout.list_headview_layout,
                null, false);
        listview01.setAdapter(adapter);
        //listview01.addHeaderView(headview);
        mUltimateRefreshView = (MyUltimateRefreshView) findViewById(R.id.refreshView);
        mUltimateRefreshView.setBaseHeaderAdapter(new MeiTuanHeaderAdapter(this));
        mUltimateRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(MyUltimateRefreshView view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mUltimateRefreshView.onHeaderRefreshComplete();
                    }
                },2000);
            }
        });





    }

    public void initTestData(){
        for(int i=0;i<10;i++){
            SaveData data = new SaveData();
            data.setId(i);
            data.setColor_value("item_"+i);
            data_list.add(data);
        }


    }

    Handler handle = new Handler();

    /**
     * 下拉刷新接口
     */
    @Override
    public void onRefresh() {
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                //listview01.stopFresh();
            }
        },2000);

    }

    private void initView() {
        listview01 = (ListView)findViewById(R.id.listview01);
    }
}
