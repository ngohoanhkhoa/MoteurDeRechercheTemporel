package SolrConnection;

public class Document {
	private Double sortedIndex;
	private String id;
	private String title;
	private String date;
	private String url;
	private Double score;
	
	public Document(Double sortedIndex, String id, String title, String date, String url, Double score) {
		this.sortedIndex = sortedIndex;
		this.id = id;
		this.title = title;
		this.date = date;
		this.url = url;
		this.score = score;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getUrl() {
		return url;
	}

	public Double getSortedIndex() {
		return sortedIndex;
	}

	public Double getScore() {
		return score;
	}

}
