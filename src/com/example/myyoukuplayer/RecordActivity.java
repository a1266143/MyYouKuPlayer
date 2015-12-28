package com.example.myyoukuplayer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class RecordActivity extends Activity {

	private ImageButton record_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		record_back = (ImageButton) findViewById(R.id.record_back);
		RecordOnClickListener rocl = new RecordOnClickListener();
		record_back.setOnClickListener(rocl);
	}
	
	class RecordOnClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.record_back:
				finish();
				break;
			}
		}
		
	}

}
