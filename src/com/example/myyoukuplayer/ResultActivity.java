package com.example.myyoukuplayer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class ResultActivity extends Activity {

	private String keyword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		//获取关键词
		keyword = getIntent().getStringExtra("keyword");
		Toast.makeText(this, keyword, Toast.LENGTH_SHORT).show();
	}

}
