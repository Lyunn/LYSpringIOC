package com.ly.pojo;

public class DateSource {
	private String driver;
	private String url;
	private int maxCon;
	public DateSource() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "DateSource [driver=" + driver + ", url=" + url + ", maxCon=" + maxCon + "]";
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getMaxCon() {
		return maxCon;
	}
	public void setMaxCon(int maxCon) {
		this.maxCon = maxCon;
	}
	public DateSource(String driver, String url, int maxCon) {
		super();
		this.driver = driver;
		this.url = url;
		this.maxCon = maxCon;
	}
}
