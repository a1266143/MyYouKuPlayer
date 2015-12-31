package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	
	private Context context;
	private ScrollBottomListener listener;
	//private int time;

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}


	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if(t + getHeight() >=  computeVerticalScrollRange()){  
           // time++;
            //如果是第一次滑动到底部，就直接回调接口通知加载分页数据
          //  if(time == 1){
            	Log.e("底部", "滑动到了底部");
            	if(listener!=null)
            		listener.reachBottom();
          //  }
        }  
		
	}
	
	
	
	
	
	public void setScrollBottomListener(ScrollBottomListener listener){
		this.listener = listener;
	}
	
	//回调接口
	interface ScrollBottomListener{
		public void reachBottom();
	}
	
	

}
