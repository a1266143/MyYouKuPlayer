package com.example.myyoukuplayer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SetActivity extends Activity {

	private ImageButton set_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		set_back = (ImageButton) findViewById(R.id.set_back);
		SetOnClickListener socl = new SetOnClickListener();
		set_back.setOnClickListener(socl);
		
	}
	
	/**
	 * 按钮监听器
	 * @author 李晓军
	 *
	 */
	class SetOnClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.set_back:
				finish();
				break;
			}
		}
	}

	
	

}
