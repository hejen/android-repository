package com.kqhelper.model;

public class SpinnerItem {

	private String id;
	
	private String name;
	
	public SpinnerItem(){
		this.id = "";
		this.name = "";
	}
	
	public SpinnerItem(String id, String value){
		this.id = id;
		this.name = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return name;
	}

	public void setValue(String value) {
		this.name = value;
	}
	
	@Override
	public String toString() {
		return name.toString();
	}
	
}
