package bob.paper3.models;

import java.io.Serializable;
import java.util.HashMap;

public class Document implements Serializable {
	private String id;
	private String title;
	private Long date;
	private String url;
	private Double score;
	private String info;

	HashMap<String, Feature> features = new HashMap<>();
	int clusterNumber;

	public Document(String id, String title, Long date, String url, Double score, String info) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.url = url;
		this.score = score;
		this.info = info;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Long getDate() {
		return date;
	}

	public String getUrl() {
		return url;
	}

	public Double getScore() {
		return score;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public HashMap<String, Feature> getFeatures() {
		return features;
	}

	public void setFeatures(HashMap<String, Feature> features) {
		this.features = features;
	}

	public int getClusterNumber() {
		return clusterNumber;
	}

	public void setClusterNumber(int clusterNumber) {
		this.clusterNumber = clusterNumber;
	}

}
