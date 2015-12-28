package com.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 这个单例模式的工具类主要是连接网络获取数据
 * 
 * @author 李晓军
 * 
 */
public class NetForJsonUtils {
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

	private static final NetForJsonUtils netutils = new NetForJsonUtils();

	private NetForJsonUtils() {
	};

	public static NetForJsonUtils getInstance() {
		return netutils;
	}

	// 获得电视剧json数据
	public void getTelePlay(final Handler handler, final int page,
			final int Type) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = null;
				switch (Type) {
				case TYPE_ANIME:
					get = new HttpGet(URL_ANIME + page);
					break;
				case TYPE_TELEPLAY:
					get = new HttpGet(URL_TELEPLAY + page);
					break;
				case TYPE_VARIETY:
					get = new HttpGet(URL_VARIETY + page);
					break;
				case TYPE_MOVIE:
					get = new HttpGet(URL_MOVIE + page);
					break;
				}
				
				try {
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(entity.getContent()));
						StringBuffer sb = new StringBuffer();
						String line = null;
						while ((line = br.readLine()) != null) {
							sb.append(line);
						}
						// Log.e("json", sb.toString());
						// 如果json数据解析错误，直接利用handler发送错误消息给主线程
						if (AnalysisJson.getInstance().getTelePlayArr(
								sb.toString()) == null) {
							handler.sendEmptyMessage(MISTAKE_JSON);
							return;
						}

						// 否则将json数据解析后的arraylist发送给handler处理
						else {
							Message m = new Message();
							// 将消息设置为电视剧类型
							m.what = TYPE_TELEPLAY;
							Bundle b = new Bundle();
							b.putSerializable("ArrayList", AnalysisJson
									.getInstance()
									.getTelePlayArr(sb.toString()));
							m.setData(b);
							// 发送消息
							handler.sendMessage(m);
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					return;
				} catch (IOException e) {
					handler.sendEmptyMessage(MISTAKE_NET);
					return;
				}
			}
		}).start();
	}

	// 根据id获得具体电视剧的信息
	public void getTeleSet(final String id, final Handler handler,
			final int page) {
		// 获取json字符串
		new Thread(new Runnable() {

			@Override
			public void run() {
				String json = getConnect(URL_TELESET + id, handler, page);
				// 如果所有都正常，解析json字符串并将arraylist传回给handler
				if (json != null) {
					Message m = new Message();
					Bundle b = new Bundle();
					//TODO
					//如果解析json错误，函数会返回null，所以如果返回null，直接利用handler发送给主线程消息通知
					if(AnalysisJson.getInstance().getTeleSetArr(json)==null){
						handler.sendEmptyMessage(MISTAKE_JSON);
						return;
					}
					b.putSerializable("ArrayList", AnalysisJson.getInstance()
							.getTeleSetArr(json));
					m.setData(b);
					handler.sendMessage(m);
				}
			}
		}).start();

	}

	/**
	 * 连接网络操作
	 * @param url
	 * @param handler
	 * @param page
	 * @return 返回获得的json数据
	 */
	public String getConnect(String url, Handler handler, int page) {
		String json = null;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url + "&page=" + page);
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						entity.getContent()));
				StringBuffer sb = new StringBuffer();
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				json = sb.toString();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(MISTAKE_NET);
			return null;
		}
		return json;
	}
}
