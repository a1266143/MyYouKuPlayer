package com.example.utils;

public class StaticCode {
	// handler根据类型常量判断，然后处理相应的逻辑
		public static final int TYPE_TELEPLAY = 0x123;
		public static final int TYPE_ANIME = 0x124;
		public static final int TYPE_MOVIE = 0x125;
		public static final int TYPE_VARIETY = 0x126;
		

		// 分页加载
		public static final int PAGE_LOAD = 1;
		public static final int FIRST_LOAD = 2;
		
		// 如果json解析错误
		public static final int MISTAKE_JSON = 0x127;
		// 网络错误
		public static final int MISTAKE_NET = 0x128;
		
		//SplashActivity的提示语修改标记
		public static final int TIP_GETCOMPLETE = 0x129;
		//存入数据库成功
		public static final int TIP_SUCCESSSQL = 0x131;
		
		//数据库操作出现错误
		public static final int MISTAKE_SQL = 0x130;
			
			
		// 电视剧url
		public static final String URL_TELEPLAY = "https://openapi.youku.com/v2/shows/by_category.json?client_id=02b97cb39f025d2c&category=电视剧&page=";
		// 具体电视剧的url
		public static final String URL_TELESET = "https://openapi.youku.com/v2/shows/videos.json?client_id=02b97cb39f025d2c&show_id=";
		// 动漫url
		public static final String URL_ANIME = "https://openapi.youku.com/v2/shows/by_category.json?client_id=02b97cb39f025d2c&category=动漫&page=";
		// 电影url
		public static final String URL_MOVIE = "https://openapi.youku.com/v2/shows/by_category.json?client_id=02b97cb39f025d2c&category=电影&page=";
		// 综艺url
		public static final String URL_VARIETY = "https://openapi.youku.com/v2/shows/by_category.json?client_id=02b97cb39f025d2c&category=综艺&page=";
		//热门搜索词url
		public static final String URL_HOTWORDS = "https://openapi.youku.com/v2/searches/keyword/top.json?client_id=02b97cb39f025d2c";
}
