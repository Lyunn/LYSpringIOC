package com.ly.pojo;

public class SessionFactory {
	private String name;
	private DateSource dateSource;
	public SessionFactory() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "SessionFactory [name=" + name + ", dateSource=" + dateSource + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DateSource getDateSource() {
		return dateSource;
	}
	public void setDateSource(DateSource dateSource) {
		this.dateSource = dateSource;
	}
	public SessionFactory(String name, DateSource dateSource) {
		super();
		this.name = name;
		this.dateSource = dateSource;
	}
}
