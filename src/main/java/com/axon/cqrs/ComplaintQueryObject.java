package com.axon.cqrs;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ComplaintQueryObject {

	@Id
	private String complaintId;
	private String company;
	private String description;
	
	public ComplaintQueryObject(String complaintId, String company, String description) {
		super();
		this.complaintId = complaintId;
		this.company = company;
		this.description = description;
	}
	
	
	
	public ComplaintQueryObject() {
	}



	public String getComplaintId() {
		return complaintId;
	}
	public String getCompany() {
		return company;
	}
	public String getDescription() {
		return description;
	}
	
	
	
}
