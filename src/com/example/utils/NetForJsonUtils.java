package com.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.bean.ShowBean;
import com.example.myyoukuplayer.ResultActivity;

import android.content.Context;
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
				case StaticCode.TYPE_ANIME:
					get = new HttpGet(StaticCode.URL_ANIME + page);
					break;
				case StaticCode.TYPE_TELEPLAY:
					get = new HttpGet(StaticCode.URL_TELEPLAY + page);
					break;
				case StaticCode.TYPE_VARIETY:
					get = new HttpGet(StaticCode.URL_VARIETY + page);
					break;
				case StaticCode.TYPE_MOVIE:
					get = new HttpGet(StaticCode.URL_MOVIE + page);
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
							handler.sendEmptyMessage(StaticCode.MISTAKE_JSON);
							return;
						}

						// 否则将json数据解析后的arraylist发送给handler处理
						else {
							Message m = new Message();
							// 将消息设置为电视剧类型
							m.what = StaticCode.TYPE_TELEPLAY;
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
					handler.sendEmptyMessage(StaticCode.MISTAKE_NET);
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
				String json = getConnect(StaticCode.URL_TELESET + id, handler,
						page);
				Log.e("json", json);
				// 如果所有都正常，解析json字符串并将arraylist传回给handler
				if (json != null) {
					Message m = new Message();
					Bundle b = new Bundle();
					ArrayList<String> arr = new ArrayList<String>();
					// 如果解析json错误，函数会返回null，所以如果返回null，直接利用handler发送给主线程消息通知
					if ((arr = AnalysisJson.getInstance().getTeleSetArr(json)) == null) {
						
						handler.sendEmptyMessage(StaticCode.MISTAKE_JSON);
						return;
					}
					b.putSerializable("ArrayList", arr);
					m.setData(b);
					Log.e("json", arr.get(0));
					handler.sendMessage(m);
					
				}
			}
		}).start();

	}

	/**
	 * 获取热门搜索词
	 * 
	 * @param handler
	 */
	public void getHotWords(final Context context, final Handler handler) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String s = null;
				// 如果网络连接没有错误的话，就解析
				if ((s = getConnect(StaticCode.URL_HOTWORDS, handler, -1)) != null) {
					// 解析json数据并返回arraylist
					ArrayList<String> arr = AnalysisJson.getInstance()
							.getHotWordsArr(handler, s);
					if (arr != null) {
						boolean success = SqlUtils.getInstance()
								.saveHotWords2db(context, handler, arr);
						if (success)
							handler.sendEmptyMessage(StaticCode.TIP_SUCCESSSQL);
					}
				}
				// 进行数据库操作

			}
		}).start();
	}
	
	/**
	 * 根据关键词搜索节目
	 */
	public void searchShows(final String keyword,final Handler handler){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String url = keyword.replaceAll(" ", "%20");
				String json = getConnect(StaticCode.URL_SHOWS+url, handler, -1);
				if(json!=null){
					ArrayList<ShowBean> arr = AnalysisJson.getInstance().AnaShows(json, handler);
						Message m = new Message();
						Bundle b = new Bundle();
						b.putSerializable("ArrayList", arr);
						m.setData(b);
						handler.sendMessage(m);
				}
			}
		}).start();
	}

	/**
	 * 连接网络操作
	 * 
	 * @param url
	 * @param handler
	 * @param page
	 * @return 返回获得的json数据
	 */
	public String getConnect(String url, Handler handler, int page) {
		String json = null;
		HttpGet get = null;
		HttpClient client = new DefaultHttpClient();
		if (page >= 0)
			get = new HttpGet(url + "&page=" + page);
		else
			get = new HttpGet(url);
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
				handler.sendEmptyMessage(StaticCode.TIP_GETCOMPLETE);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(StaticCode.MISTAKE_NET);
			return null;
		}
		return json;
	}
}
