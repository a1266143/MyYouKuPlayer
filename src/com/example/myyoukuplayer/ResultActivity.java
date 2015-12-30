package com.example.myyoukuplayer;

import java.util.ArrayList;

import com.example.adapter.ResultShowsAdapter;
import com.example.bean.ShowBean;
import com.example.utils.NetForJsonUtils;
import com.example.utils.StaticCode;
import com.example.view.MyGridView;
import com.example.view.MyListView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {

	private String keyword;
	private ScrollView scrollView;
	private MyListView listView;
	private MyGridView gridView;
	private TextView result_title;
	private ImageButton result_back;
	public Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==StaticCode.MISTAKE_NET){
				Toast.makeText(ResultActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
				return;
			}else{
				ArrayList<ShowBean> showList = new ArrayList<ShowBean>();
				Bundle b = msg.getData();
				showList = (ArrayList<ShowBean>) b.getSerializable("ArrayList");
				//TODO 
				//
				//
				//
				if(showList == null){
					Toast.makeText(ResultActivity.this, "showList为空", Toast.LENGTH_SHORT).show();
				}
				if(showList!=null){
					//如果showList的容量为0，也就说明没搜到数据
					if(showList.size()==0){
						//TODO 不将listView显示出来
						Toast.makeText(ResultActivity.this, "list容量为0", Toast.LENGTH_SHORT).show();
						return;
					}else{
						Toast.makeText(ResultActivity.this, "执行了", Toast.LENGTH_SHORT).show();
						//1.将listview显示出来，2.设置adapter
						ResultShowsAdapter adapter = new ResultShowsAdapter(ResultActivity.this, showList);
						listView.setAdapter(adapter);
						listView.setVisibility(View.VISIBLE);
					}
					
				}
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		//获取关键词
		keyword = getIntent().getStringExtra("keyword");
		//初始化
		init();
		//进行网络操作获取节目
		NetForJsonUtils.getInstance().searchShows(keyword,handler);
	}
	public void init(){
		scrollView = (ScrollView) findViewById(R.id.result_scrollview);
		listView = (MyListView) findViewById(R.id.result_listview);
		gridView = (MyGridView) findViewById(R.id.result_gridview);
		result_title = (TextView) findViewById(R.id.result_title);
		result_back = (ImageButton) findViewById(R.id.result_back);
		result_title.setText(keyword);
		ResultClickListener listener = new ResultClickListener();
		result_back.setOnClickListener(listener);
	}
	
	class ResultClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.result_back:
				finish();
				break;
			}
		}
		
	}

}
