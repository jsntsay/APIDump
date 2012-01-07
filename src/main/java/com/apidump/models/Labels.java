package com.apidump.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.egit.github.core.Label;

@Entity
public class Labels {

	@Id
	private String url;
	
	private String color;

	private String name;

	public Labels(Label l) {
		url = l.getUrl();
		color = l.getColor();
		name = l.getName();
	}
	
	public Labels() {
		
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
