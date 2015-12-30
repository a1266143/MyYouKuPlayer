package com.example.bean;

import java.io.Serializable;


/**
 * 节目的javabean
 * @author 李晓军
 *
 */
public class ShowBean implements Serializable{
	private String id;
	private String name;
	private String poster;
	private String showcategory;
	private String description;
	private String score;
	private String published;
	private int completed;
	private String episode_updated;
	
	
	public int getCompleted() {
		return completed;
	}
	public void setCompleted(int completed) {
		this.completed = completed;
	}
	public String getEpisode_updated() {
		return episode_updated;
	}
	public void setEpisode_updated(String episode_updated) {
		this.episode_updated = episode_updated;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public String getShowcategory() {
		return showcategory;
	}
	public void setShowcategory(String showcategory) {
		this.showcategory = showcategory;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	
}
