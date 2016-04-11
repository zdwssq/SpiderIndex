package com.bestsonic.domain;

/**
 * 
 * @author zheng
 */
public class Keyword implements Comparable<Keyword> {

	private int id;

	private String keyword;

	private String relationship;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	@Override
	public String toString() {
		return "Keyword [id=" + id + ", keyword=" + keyword + ", relationship=" + relationship + "]";
	}

	public int count() {
		int count = 0;
		for (char c : keyword.toCharArray()) {
			if (c == '1')
				count++;
		}
		return count;
	}

	@Override
	public int compareTo(Keyword o) {
		return o.count() - this.count();
	}
}
