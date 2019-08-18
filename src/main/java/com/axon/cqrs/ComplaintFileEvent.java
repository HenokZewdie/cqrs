package com.axon.cqrs;

public class ComplaintFileEvent {
	private String id; 
	private String company;
	private String description;
	
	
	public ComplaintFileEvent(String id, String company, String description) {
		super();
		this.id = id;
		this.company = company;
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public String getCompany() {
		return company;
	}
	public String getDescription() {
		return description;
	}


}
