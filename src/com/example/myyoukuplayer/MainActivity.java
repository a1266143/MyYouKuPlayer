package com.example.myyoukuplayer;

import java.util.ArrayList;
import java.util.List;

import com.baidu.ops.appunion.sdk.AppUnionSDK;
import com.baidu.ops.appunion.sdk.banner.BaiduBanner;
import com.baidu.ops.appunion.sdk.banner.BannerType;
import com.example.adapter.MainPagerAdapter;
import com.example.fragment.AnimeFragment;
import com.example.fragment.MovieFragment;
import com.example.fragment.TelePlayFragment;
import com.example.fragment.VarietyFragment;
import com.example.utils.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class MainActivity extends FragmentActivity {

	private ViewPager viewPager;
	private Button titlebar_movie, titlebar_anime, titlebar_teleplay,
			titlebar_variety;
	private ImageButton titlebar_search,titlebar_menu,titlebar_record;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化
		init();
	}

	/**
	 * 初始化方法
	 */
	public void init() {
		viewPager = (ViewPager) findViewById(R.id.manactivity_viewpager);
		titlebar_movie = (Button) findViewById(R.id.titlebar_movie);
		titlebar_anime = (Button) findViewById(R.id.titlebar_anime);
		titlebar_teleplay = (Button) findViewById(R.id.titlebar_teleplay);
		titlebar_variety = (Button) findViewById(R.id.titlebar_variety);
		titlebar_search = (ImageButton) findViewById(R.id.titlebar_search);
		titlebar_menu = (ImageButton) findViewById(R.id.titlebar_menu);
		titlebar_record = (ImageButton) findViewById(R.id.titlebar_record);
		MyOnClickListener mocl = new MyOnClickListener();
		titlebar_movie.setOnClickListener(mocl);
		titlebar_anime.setOnClickListener(mocl);
		titlebar_teleplay.setOnClickListener(mocl);
		titlebar_variety.setOnClickListener(mocl);
		titlebar_search.setOnClickListener(mocl);
		titlebar_menu.setOnClickListener(mocl);
		titlebar_record.setOnClickListener(mocl);
		List<Fragment> list = new ArrayList<Fragment>();
		Fragment movie = new MovieFragment();
		Fragment anime = new AnimeFragment();
		Fragment telePlay = new TelePlayFragment();
		Fragment variety = new VarietyFragment();
		list.add(movie);
		list.add(anime);
		list.add(telePlay);
		list.add(variety);
		// 设置viewPager预加载数目，以免fragment被销毁
		viewPager.setOffscreenPageLimit(3);
		MainPagerAdapter mpa = new MainPagerAdapter(
				getSupportFragmentManager(), list);
		viewPager.setAdapter(mpa);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * viewpager的onpagechangelistener类，主要负责titlebar的颜色和字体随着页面的改变而改变
	 * 
	 * @author 李晓军
	 * 
	 */
	class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int position) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			changeColor(position);
		}

		// 改变颜色的方法
		public void changeColor(int position) {
			switch (position) {
			// 电影
			case 0:
				titlebar_movie.setBackgroundColor(Color.parseColor("#a4c2f4"));
				titlebar_movie.setTextColor(Color.parseColor("#ffffff"));
				titlebar_anime.setBackgroundColor(Color.parseColor("#ffffff"));
				titlebar_anime.setTextColor(Color.parseColor("#000000"));
				titlebar_teleplay.setBackgroundColor(Color
						.parseColor("#ffffff"));
				titlebar_teleplay.setTextColor(Color.parseColor("#000000"));
				titlebar_variety
						.setBackgroundColor(Color.parseColor("#ffffff"));
				titlebar_variety.setTextColor(Color.parseColor("#000000"));
				break;
			// 动漫
			case 1:
				titlebar_movie.setBackgroundColor(Color.parseColor("#ffffff"));
				titlebar_movie.setTextColor(Color.parseColor("#000000"));
				titlebar_anime.setBackgroundColor(Color.parseColor("#a4c2f4"));
				titlebar_anime.setTextColor(Color.parseColor("#ffffff"));
				titlebar_teleplay.setBackgroundColor(Color
						.parseColor("#ffffff"));
				titlebar_teleplay.setTextColor(Color.parseColor("#000000"));
				titlebar_variety
						.setBackgroundColor(Color.parseColor("#ffffff"));
				titlebar_variety.setTextColor(Color.parseColor("#000000"));
				break;
			// 电视剧
			case 2:
				titlebar_movie.setBackgroundColor(Color.parseColor("#ffffff"));
				titlebar_movie.setTextColor(Color.parseColor("#000000"));
				titlebar_anime.setBackgroundColor(Color.parseColor("#ffffff"));
				titlebar_anime.setTextColor(Color.parseColor("#000000"));
				titlebar_teleplay.setBackgroundColor(Color
						.parseColor("#a4c2f4"));
				titlebar_teleplay.setTextColor(Color.parseColor("#ffffff"));
				titlebar_variety
						.setBackgroundColor(Color.parseColor("#ffffff"));
				titlebar_variety.setTextColor(Color.parseColor("#000000"));
				break;
			// 综艺
			case 3:
				titlebar_movie.setBackgroundColor(Color.parseColor("#ffffff"));
				titlebar_movie.setTextColor(Color.parseColor("#000000"));
				titlebar_anime.setBackgroundColor(Color.parseColor("#ffffff"));
				titlebar_anime.setTextColor(Color.parseColor("#000000"));
				titlebar_teleplay.setBackgroundColor(Color
						.parseColor("#ffffff"));
				titlebar_teleplay.setTextColor(Color.parseColor("#000000"));
				titlebar_variety
						.setBackgroundColor(Color.parseColor("#a4c2f4"));
				titlebar_variety.setTextColor(Color.parseColor("#ffffff"));
				break;
			}
		}

	}

	/**
	 * titlebar的按钮监听事件
	 * 
	 * @author 李晓军
	 * 
	 */
	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.titlebar_movie:
				viewPager.setCurrentItem(0);
				break;
			case R.id.titlebar_anime:
				viewPager.setCurrentItem(1);
				break;
			case R.id.titlebar_teleplay:
				viewPager.setCurrentItem(2);
				break;
			case R.id.titlebar_variety:
				viewPager.setCurrentItem(3);
				break;
			case R.id.titlebar_search:
				Intent intent = new Intent(MainActivity.this,SearchActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.out);
				break;
			case R.id.titlebar_menu:
				setPopupMenu();
				break;
			case R.id.titlebar_record:
				Intent intent2 = new Intent(MainActivity.this,RecordActivity.class);
				startActivity(intent2);
				break;
			}
		}
		
		/**
		 * 设置弹出菜单
		 */
		public void setPopupMenu(){
			PopupMenu p = new PopupMenu(MainActivity.this, titlebar_variety);
			getMenuInflater().inflate(R.menu.main, p.getMenu());
			p.show();
			p.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()) {
					case R.id.settings:
						Intent intent = new Intent(MainActivity.this,SetActivity.class);
						startActivity(intent);
						break;
					}
					return false;
				}
			});
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Utils.getInstance().showQuitDialog(this);
			break;
		}
		return true;
	}
	
	

}
