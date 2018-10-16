package com.example.downrefresh;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.downrefresh.RefreshListView.OnRefreshListener;
import com.tuacy.nestedscrolldemo.R;
import com.tuacy.nestedscrolldemo.new_type_interface.OnHeaderRefreshListener;

public class MainActivity extends Activity {
	private RefreshListView listview;
	private MyAdapter myAdapter;
	private List<String> listDatas;
	private int datapage;
	private MyUltimateRefreshView mUltimateRefreshView;

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_main);
		listview = (RefreshListView) findViewById(R.id.listview);
		listview.setRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

			}

			@Override
			public void onLoadMore() {
				new Thread(){
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						listDatas.add("我是加载更多出来的数据!1");
						listDatas.add("我是加载更多出来的数据!2");
						listDatas.add("我是加载更多出来的数据!3");

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if(datapage==2){
									listview.setData_load_finish(true);
								}else{
									listview.setData_load_finish(false);
									datapage++;
								}
								myAdapter.notifyDataSetChanged();
								listview.onRefreshComplete();
							}
						});
					};

				}.start();
			}

		});
		listDatas = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			listDatas.add("这是一个大妹子：" + i);
		}
		myAdapter = new MyAdapter();
		listview.setAdapter(myAdapter);

		View headview = LayoutInflater.from(this).inflate(R.layout.list_headview_layout,
				null, false);
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

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listDatas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView = new TextView(getApplicationContext());
			textView.setTextSize(18f);
			textView.setTextColor(Color.BLACK);
			textView.setText(listDatas.get(position));
			return textView;
		}

	}
}
