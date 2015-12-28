package com.example.myyoukuplayer;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends Activity {

	private ImageButton search_back;
	private EditText search_edittext;
	private boolean focused;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		search_back = (ImageButton) findViewById(R.id.search_back);
		search_edittext = (EditText) findViewById(R.id.search_edittext);
		MyOnClickListener mocl = new MyOnClickListener();
		//edittext的焦点监听器
		search_edittext.setOnFocusChangeListener(new SearchOnFocusChangeListener());
		//设置软键盘相关监听器
		search_edittext.setOnEditorActionListener(new SearchOnEditorActionListener());
		//设置edittext输入文字的文字监听器
		search_edittext.addTextChangedListener(new SearchTextWatcher());
		search_back.setOnClickListener(mocl);
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
			if(arg1 == EditorInfo.IME_ACTION_SEARCH){
				Toast.makeText(SearchActivity.this, "你点击了软键盘上的搜索按钮", Toast.LENGTH_SHORT).show();
			}
			return false;
		}
		
	}
	
	//EditText焦点改变监听器
	class SearchOnFocusChangeListener implements OnFocusChangeListener{

		@Override
		public void onFocusChange(View view, boolean focused) {
			SearchActivity.this.focused = focused;
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
