package com.example.myyoukuplayer;

import java.util.ArrayList;

import com.example.adapter.ResultShowsAdapter;
import com.example.adapter.ResultVideosAdapter;
import com.example.bean.ShowBean;
import com.example.bean.VideoBean;
import com.example.utils.NetForJsonUtils;
import com.example.utils.StaticCode;
import com.example.view.MyGridView;
import com.example.view.MyListView;
import com.example.view.MyScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.youku.player.ApiManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ResultActivity extends Activity {

	private String keyword;
	private PullToRefreshScrollView scrollView;
	private MyListView listView;
	private MyGridView gridView;
	private TextView result_title;
	private ImageButton result_back;
	private ArrayList<ShowBean> showList;
	private ArrayList<VideoBean> videoAddList;
	private ArrayList<VideoBean> videoList;
	private int page = 1;
	private boolean showsFinish,videosFinish;
	ResultVideosAdapter vadapter;
	
	private ImageView result_animationImage;
	public Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			//如果是网络错误
			if (msg.what == StaticCode.MISTAKE_NET) {
				Toast.makeText(ResultActivity.this, "网络连接错误",
						Toast.LENGTH_SHORT).show();
				result_animationImage.setVisibility(View.GONE);
				if(page!=1)
					page--;
				return;
			}
			//如果是查找的节目
			else if(msg.what == StaticCode.TIP_SEARCHSHOWS) {
				//如果是第一次加载
					showList = (ArrayList<ShowBean>) msg.getData().getSerializable("ArrayList");
					// 如果showList的容量为0，也就说明没搜到数据
					if (showList.size() == 0) {
						showsFinish = true;
						// TODO 不将listView显示出来
						return;
					} else {
						// 1.将listview显示出来，2.设置adapter
						ResultShowsAdapter adapter = new ResultShowsAdapter(
								ResultActivity.this, showList);
						listView.setAdapter(adapter);
						listView.setVisibility(View.VISIBLE);
						showsFinish = true;
						if(videosFinish)
							result_animationImage.setVisibility(View.GONE);
					}
			}
			//如果查找的是视频
			else if(msg.what == StaticCode.TIP_SEARCHVIDEOS){
				//如果是第一次加载
				if(msg.arg1 == StaticCode.FIRST_LOAD){
					videoList = (ArrayList<VideoBean>) msg.getData().getSerializable("ArrayList");
					if(videoList.size() == 0){
						videosFinish = true;
						return;
					}else{
						vadapter= new ResultVideosAdapter(ResultActivity.this, videoList);
						gridView.setAdapter(vadapter);
						gridView.setVisibility(View.VISIBLE);
						videosFinish = true;
						if(showsFinish )
							result_animationImage.setVisibility(View.GONE);
					}
				}
				//否则如果是分页加载
				else{
					videoAddList = (ArrayList<VideoBean>) msg.getData().getSerializable("ArrayList");
					videoList.addAll(videoAddList);
					vadapter.notifyDataSetChanged();
				}
				result_animationImage.setVisibility(View.GONE);
				scrollView.onRefreshComplete();
			}
			
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		// 获取关键词
		keyword = getIntent().getStringExtra("keyword");
		// 初始化
		init();
		//播放loading动画
		AnimationDrawable anim = (AnimationDrawable) result_animationImage.getBackground();
		anim.start();
		// 根据关键词获取节目列表
		NetForJsonUtils.getInstance().searchShows(keyword, handler);
		//根据关键词获取视频列表
		NetForJsonUtils.getInstance().searchVideos(keyword, handler,-1);
	}

	public void init() {
		scrollView = (PullToRefreshScrollView) findViewById(R.id.result_scrollview);
		listView = (MyListView) findViewById(R.id.result_listview);
		gridView = (MyGridView) findViewById(R.id.result_gridview);
		result_title = (TextView) findViewById(R.id.result_title);
		result_back = (ImageButton) findViewById(R.id.result_back);
		result_animationImage = (ImageView) findViewById(R.id.result_animationImage);
		result_title.setText(keyword);
		ResultClickListener listener = new ResultClickListener();
		result_back.setOnClickListener(listener);
		//scrollView.setOnTouchListener(new TouchListenerImpl());
		ResultItemClickListener listener1 = new ResultItemClickListener(StaticCode.TYPE_SHOW);
		listView.setOnItemClickListener(listener1);
		ResultItemClickListener listener2 = new ResultItemClickListener(StaticCode.TYPE_VIDEO);
		gridView.setOnItemClickListener(listener2);
		//设置scrollview为上拉加载
		scrollView.setMode(Mode.PULL_UP_TO_REFRESH);
		scrollView.setOnRefreshListener(new ResultPullToRefreshListener());
	}
	
	class ResultPullToRefreshListener implements OnRefreshListener2<ScrollView>{

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			
		}

		//执行分页加载节目视频
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			result_animationImage.setVisibility(View.VISIBLE);
			page++;
			NetForJsonUtils.getInstance().searchVideos(keyword, handler, page);
		}
		
	}

	/**
	 * 搜索界面中listview和gridview的事件监听器
	 * @author 李晓军
	 *
	 */
	class ResultItemClickListener implements OnItemClickListener{
		
		private int TYPE;
		public ResultItemClickListener(int tYPE) {
			super();
			TYPE = tYPE;
		}


		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(ResultActivity.this,PlayActivity.class);
			if(TYPE == StaticCode.TYPE_SHOW){
				intent.putExtra("TYPE", StaticCode.TYPE_SHOW);
				intent.putExtra("id", showList.get(arg2).getId());
			}
			else{
				intent.putExtra("TYPE", StaticCode.TYPE_VIDEO);
				intent.putExtra("id", videoList.get(arg2).getId());
			}
			startActivity(intent);
		}
		
	}
	
	
	//本activity中的按钮事件监听器
	class ResultClickListener implements OnClickListener {

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
