package com.example.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.bean.TelePlayBean;

/**
 * 解析json数据的工具类
 * @author 李晓军
 *
 */
public class AnalysisJson {
	private static final AnalysisJson an = new AnalysisJson();
	private AnalysisJson(){};
	public static  AnalysisJson getInstance(){
		return an;
	}
	
	/**
	 * 解析电视剧的json数据为列表
	 * @param json
	 * @return
	 */
	public ArrayList<TelePlayBean> getTelePlayArr(String json){
		ArrayList<TelePlayBean> arr = new ArrayList<TelePlayBean>();
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray shows = jo.getJSONArray("shows");
			for(int i=0;i<shows.length();i++){
				
				TelePlayBean tb = new TelePlayBean();
				//如果需要付费，直接跳过
				if(shows.getJSONObject(i).getInt("paid")==1)
					continue;
				tb.setId(shows.getJSONObject(i).getString("id"));
				tb.setName(shows.getJSONObject(i).getString("name"));
				tb.setPoster(shows.getJSONObject(i).getString("poster"));
				tb.setScore((float) shows.getJSONObject(i).getDouble("score"));
				tb.setPaid(shows.getJSONObject(i).getInt("paid"));
				tb.setEpisode_count(shows.getJSONObject(i).getInt("episode_count"));
				tb.setEpisode_updated(shows.getJSONObject(i).getInt("episode_updated"));
				arr.add(tb);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			//如果json解析错误，直接返回null
			return null;
		}
		return arr;
	}
	
	/**
	 * 解析具体电视剧集的json为arraylist
	 * @param json
	 * @return
	 */
	public ArrayList<String> getTeleSetArr(String json){
		ArrayList<String> arr = new ArrayList<String>();
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray videos = jo.getJSONArray("videos");
			for(int i=0;i<videos.length();i++){
				String id = videos.getJSONObject(i).getString("id");
				arr.add(id);
			}
			return arr;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
