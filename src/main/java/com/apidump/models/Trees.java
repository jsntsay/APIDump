package com.apidump.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.egit.github.core.Tree;

@Entity
public class Trees {

	@Id
	private String sha;

	private String url;
	
	//Ignoring TreeEntry list since Commits don't seem to use it.
	
	public Trees(Tree t) {
		sha = t.getSha();
		url = t.getUrl();
	}
	
	public Trees() {
		
	}

	/**
	 * @return the sha
	 */
	public String getSha() {
		return sha;
	}

	/**
	 * @param sha the sha to set
	 */
	public void setSha(String sha) {
		this.sha = sha;
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
}
