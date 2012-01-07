package com.apidump.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.eclipse.egit.github.core.Commit;

@Entity
public class Commits {

	@Id
	private String sha;

	private String authorName;
	
	private String authorEmail;

	@Lob
	private String message;

	private String url;
	
	public Commits(Commit commit) {
		sha = commit.getSha();

		authorName = commit.getAuthor().getName();
		authorEmail = commit.getAuthor().getEmail();
		message = commit.getMessage();
		url = commit.getUrl();
	}
	
	public Commits() {
		
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
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * @return the authorEmail
	 */
	public String getAuthorEmail() {
		return authorEmail;
	}

	/**
	 * @param authorEmail the authorEmail to set
	 */
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
