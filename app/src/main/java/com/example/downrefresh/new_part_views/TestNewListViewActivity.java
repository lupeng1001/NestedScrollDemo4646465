package com.example.downrefresh.new_part_views;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.tuacy.nestedscrolldemo.R;
import com.tuacy.nestedscrolldemo.newtype_views.SaveData;
import com.tuacy.nestedscrolldemo.newtype_views.TestListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TestNewListViewActivity extends AppCompatActivity {
    private static final String TAG = "TestNewListViewActivity";


    //新版本下拉刷新控件
    private PullToRefreshLayout activity_main;
    private ListView listview01;
    private List<SaveData> data_list = new ArrayList<SaveData>();
    private TestListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_new_list_view2);
        listview01 = (ListView)findViewById(R.id.listview01);
        initTestData();
        adapter = new TestListAdapter(this);
        adapter.addMoreItems(data_list,true);
        listview01.setAdapter(adapter);
        activity_main = (PullToRefreshLayout)findViewById(R.id.activity_main);
        activity_main.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束刷新
                        activity_main.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        activity_main.finishLoadMore();
                    }
                }, 2000);
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

}
