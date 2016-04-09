package com.bestsonic.domain;

public class WebPage implements Comparable<WebPage>{

	private Integer id;

	private String headers;

	private String text;

	private Integer status;

	private Long batchId;

	private Double score;

	private String baseUrl;

	private String content;

	private String title;

	private String parentUrl;

	private String inlinks;

	private String outlinks;

	private Long fetchTime;

	private Double rank;

	public Double getRank() {
		return rank;
	}

	public void setRank(Double rank) {
		this.rank = rank;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public String getInlinks() {
		return inlinks;
	}

	public void setInlinks(String inlinks) {
		this.inlinks = inlinks;
	}

	public String getOutlinks() {
		return outlinks;
	}

	public void setOutlinks(String outlinks) {
		this.outlinks = outlinks;
	}

	public Long getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(Long fetchTime) {
		this.fetchTime = fetchTime;
	}

	@Override
	public String toString() {
		return "WebPage [id=" + id + ", headers=" + headers + ", text=" + text + ", status=" + status + ", batchId="
				+ batchId + ", score=" + score + ", baseUrl=" + baseUrl + ", content=" + content + ", title=" + title
				+ ", parentUrl=" + parentUrl + ", inlinks=" + inlinks + ", outlinks=" + outlinks + ", fetchTime="
				+ fetchTime + "]";
	}

	public WebPage(Integer id, String headers, String text, Integer status, Long batchId, Double score, String baseUrl,
			String content, String title, String parentUrl, String inlinks, String outlinks, Long fetchTime) {
		super();
		this.id = id;
		this.headers = headers;
		this.text = text;
		this.status = status;
		this.batchId = batchId;
		this.score = score;
		this.baseUrl = baseUrl;
		this.content = content;
		this.title = title;
		this.parentUrl = parentUrl;
		this.inlinks = inlinks;
		this.outlinks = outlinks;
		this.fetchTime = fetchTime;
	}

	public WebPage() {
		super();
	}

	@Override
	public int compareTo(WebPage o) {
		return this.rank.compareTo(o.getRank());
	}

}
