package com.example.myyoukuplayer;

import java.util.ArrayList;

import com.example.utils.NetForJsonUtils;
import com.example.utils.SqlUtils;
import com.example.utils.StaticCode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
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
	private Button close_btn,keyword_connect_close;
	private EditText search_edittext;
	private LinearLayout lin_history, lin_hotwords,lin_keyword;
	private boolean focused;
	private GridView gridView;
	private ListView listView,keyword_connect_listview;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter ad;

	// 历史纪录集合,并且指定容量为4
	private ArrayList<String> arr_history = new ArrayList<String>(4);
	// 热词记录集合
	private ArrayList<String> arr_hotwords;
	//关键词联想集合
	private ArrayList<String> arr_keywords;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == StaticCode.MISTAKE_SQL) {
				Toast.makeText(SearchActivity.this, "数据库操作错误，请联系程序猿",
						Toast.LENGTH_SHORT).show();
			}
			//如果是关键词联想
			if(msg.what == StaticCode.TIP_KEYWORDCONNECT){
				arr_keywords = (ArrayList<String>) msg.getData().getSerializable("ArrayList");
				if(arr_keywords.size() != 0){
					@SuppressWarnings("rawtypes")
					ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, arr_keywords);
					keyword_connect_listview.setAdapter(adapter);
					//为关键词联想设置adapter
					KeyWordConnectListener listener = new KeyWordConnectListener();
					keyword_connect_listview.setOnItemClickListener(listener);
					lin_keyword.setVisibility(View.VISIBLE);
				}
			}
		}

	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		search_back = (ImageButton) findViewById(R.id.search_back);
		search_edittext = (EditText) findViewById(R.id.search_edittext);
		listView = (ListView) findViewById(R.id.search_history);
		gridView = (GridView) findViewById(R.id.search_hotwords);
		close_btn = (Button) findViewById(R.id.search_close_history);
		keyword_connect_close = (Button) findViewById(R.id.keyword_connect_close);
		lin_history = (LinearLayout) findViewById(R.id.search_history_linear);
		lin_hotwords = (LinearLayout) findViewById(R.id.search_hotwords_linear);
		lin_keyword = (LinearLayout) findViewById(R.id.keyword_connect_linear);
		keyword_connect_listview = (ListView) findViewById(R.id.keyword_connect_listView);
		MyOnClickListener mocl = new MyOnClickListener();
		// edittext的焦点监听器
		search_edittext
				.setOnFocusChangeListener(new SearchOnFocusChangeListener());
		// 设置软键盘相关监听器
		search_edittext
				.setOnEditorActionListener(new SearchOnEditorActionListener());
		// 设置edittext输入文字的文字监听器
		search_edittext.addTextChangedListener(new SearchTextWatcher());
		search_back.setOnClickListener(mocl);
		close_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				lin_history.setVisibility(View.GONE);
				SqlUtils.getInstance().deleteHistory(SearchActivity.this, handler);
			}
		});
		keyword_connect_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				lin_keyword.setVisibility(View.GONE);
			}
		});
		// 进行数据库查询，将热门关键词查询出来
		if ((arr_hotwords = SqlUtils.getInstance().queryHotWords(handler, this)) != null) {
			// 将热词显示出来
			lin_hotwords.setVisibility(View.VISIBLE);
			gridView.setAdapter(new ArrayAdapter(this,
					android.R.layout.simple_list_item_1, arr_hotwords));
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Intent intent = new Intent(SearchActivity.this,
							ResultActivity.class);
					intent.putExtra("keyword", arr_hotwords.get(position));
					startActivity(intent);
				}
			});
		}
		// 进行数据库查询，将搜索历史查询出来
		if ((arr_history = SqlUtils.getInstance().queryHistory(this, handler)) != null) {
			if(ad==null){
				ad = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
						arr_history);
				listView.setAdapter(ad);
			}else{
				ad.notifyDataSetChanged();
			}
			
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Intent intent = new Intent(SearchActivity.this,
							ResultActivity.class);
					intent.putExtra("keyword", arr_history.get(position));
					startActivity(intent);
				}
			});
		}
	}
	
	class KeyWordConnectListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			go2Activity(arr_keywords.get(arg2));
		}
		
	}

	//将历史纪录存入数据库，并且进入resultactivity内容查询结果页面
	public void go2Activity(String s) {
		// 如果输入了内容，则存入数据库
		if (!s.equals("")) {
			SqlUtils.getInstance().saveHistory2db(SearchActivity.this, s,
					handler);
			Intent intent = new Intent(SearchActivity.this,
					ResultActivity.class);
			intent.putExtra("keyword", s);
			startActivity(intent);
		}
	}

	class SearchTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			// 获得每次输入一个字，然后整个字符串的String
			String text = arg0.toString();
			if(text.equals("")){
				lin_keyword.setVisibility(View.GONE);
			}
			if(!text.equals(""))
				//关键词联想
				NetForJsonUtils.getInstance().keywordConnect(text, handler);
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

	class SearchOnEditorActionListener implements OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
			// 点击了软键盘上的搜索按钮
			if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
				go2Activity(search_edittext.getText().toString());
			}
			return false;
		}

	}

	// EditText焦点改变监听器
	class SearchOnFocusChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View view, boolean focused) {
			SearchActivity.this.focused = focused;
			if (focused) {
				if (arr_history!=null&&arr_history.size() != 0) {
					lin_history.setVisibility(View.VISIBLE);
				}
			}

			if (focused == false) {
				lin_history.setVisibility(View.GONE);
			}
		}

	}

	// 按钮监听器
	class MyOnClickListener implements OnClickListener {

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

	// 重写finish 添加动画
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
