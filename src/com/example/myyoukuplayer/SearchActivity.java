package com.example.myyoukuplayer;

import java.util.ArrayList;

import com.example.utils.SqlUtils;
import com.example.utils.StaticCode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends Activity {

	private ImageButton search_back;
	private Button close_btn;
	private EditText search_edittext;
	private LinearLayout lin_history,lin_hotwords;
	private boolean focused;
	private GridView gridView;
	private ListView listView;
	private ArrayAdapter ad;
	
	//历史纪录集合
	private ArrayList<String> arr_history;
	//热词记录集合
	private ArrayList<String> arr_hotwords;
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == StaticCode.MISTAKE_SQL){
				Toast.makeText(SearchActivity.this, "数据库操作错误，请联系程序猿", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		search_back = (ImageButton) findViewById(R.id.search_back);
		search_edittext = (EditText) findViewById(R.id.search_edittext);
		listView = (ListView) findViewById(R.id.search_history);
		gridView = (GridView) findViewById(R.id.search_hotwords);
		close_btn = (Button) findViewById(R.id.search_close_history);
		lin_history = (LinearLayout) findViewById(R.id.search_history_linear);
		lin_hotwords = (LinearLayout) findViewById(R.id.search_hotwords_linear);
		MyOnClickListener mocl = new MyOnClickListener();
		//edittext的焦点监听器
		search_edittext.setOnFocusChangeListener(new SearchOnFocusChangeListener());
		//设置软键盘相关监听器
		search_edittext.setOnEditorActionListener(new SearchOnEditorActionListener());
		//设置edittext输入文字的文字监听器
		search_edittext.addTextChangedListener(new SearchTextWatcher());
		search_back.setOnClickListener(mocl);
		close_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				lin_history.setVisibility(View.GONE);
			}
		});
		//进行数据库查询，将热门关键词查询出来
		if((arr_hotwords = SqlUtils.getInstance().queryHotWords(handler, this))!=null){
			//将热词显示出来
			lin_hotwords.setVisibility(View.VISIBLE);
			gridView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, arr_hotwords));
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Intent intent = new Intent(SearchActivity.this,ResultActivity.class);
					intent.putExtra("keyword", arr_hotwords.get(position));
					startActivity(intent);
				}
			});
		}
		//进行数据库查询，将搜索历史查询出来
		if((arr_history = SqlUtils.getInstance().queryHistory(this, handler))!=null){
			ad = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arr_history);
			listView.setAdapter(ad);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Intent intent = new Intent(SearchActivity.this,ResultActivity.class);
					intent.putExtra("keyword", arr_history.get(position));
					startActivity(intent);
				}
			});
		}
	}
	
	public void go2Activity(){
		//获取edittext中的文本
		String s = search_edittext.getText().toString();
		//如果输入了内容，则存入数据库
		if(!s.equals("")){
			SqlUtils.getInstance().saveHistory2db(SearchActivity.this, s, handler);
			Intent intent = new Intent(SearchActivity.this,ResultActivity.class);
			intent.putExtra("keyword", s);
			startActivity(intent);
		}
	}

	
	class SearchTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable arg0) {
			//获得每次输入一个字，然后整个字符串的String
			String text = arg0.toString();
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int start, int before,
				int count) {
			
		}
		
	}
	
	class SearchOnEditorActionListener implements OnEditorActionListener{

		@Override
		public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
			//点击了软键盘上的搜索按钮
			if(arg1 == EditorInfo.IME_ACTION_SEARCH){
				Toast.makeText(SearchActivity.this, "你点击了软键盘上的搜索按钮", Toast.LENGTH_SHORT).show();
				go2Activity();
			}
			return false;
		}
		
	}
	
	//EditText焦点改变监听器
	class SearchOnFocusChangeListener implements OnFocusChangeListener{

		@Override
		public void onFocusChange(View view, boolean focused) {
			SearchActivity.this.focused = focused;
			if(focused)
				lin_history.setVisibility(View.VISIBLE);
			if(focused == false){
				lin_history.setVisibility(View.GONE);
			}
		}
		
	}
	
	//按钮监听器
	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.search_back:
				finish();
				break;
			}
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
				finish();
			break;
		}
		return true;
	}

	//重写finish 添加动画
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.enter1, R.anim.out1);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
}
