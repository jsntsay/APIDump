package com.apidump.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.egit.github.core.Commit;

import com.apidump.util.UrlUtil;

@Entity
public class Commits {

	@Id
	private String sha;

	@Lob
	private String message;

	private String url;
	
	@OneToOne
	private Trees tree;
	
	// Fold in author and committer here.
	private String authorName;
	
	private String authorEmail;

	@Temporal(TemporalType.TIMESTAMP)
	private Date authorDate;
	
	private String committerName;
	
	private String committerEmail;

	@Temporal(TemporalType.TIMESTAMP)
	private Date committerDate;
	
	// Parents left out of model, doesn't seem to be returned in API calls used.

	public Commits(Commit commit) {
		// In RepositoryCommit.commit, doesn't seem to have a sha, so provide
		// one using the given url.
		if (commit.getSha() == null)
			sha = UrlUtil.parseUrl(commit.getUrl(), 2, 3, 6).get(2);
		else
			sha = commit.getSha();
		
		message = commit.getMessage();
		url = commit.getUrl();

		if (commit.getAuthor() != null) {
			authorName = commit.getAuthor().getName();
			authorEmail = commit.getAuthor().getEmail();
			authorDate = commit.getAuthor().getDate();
		}
		if (commit.getCommitter() != null) {
			committerName = commit.getCommitter().getName();
			committerEmail = commit.getCommitter().getEmail();
			committerDate = commit.getCommitter().getDate();
		}
		if (commit.getTree() != null) {
			tree = new Trees(commit.getTree());
		}
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

	/**
	 * @return the tree
	 */
	public Trees getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(Trees tree) {
		this.tree = tree;
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
	 * @return the authorDate
	 */
	public Date getAuthorDate() {
		return authorDate;
	}

	/**
	 * @param authorDate the authorDate to set
	 */
	public void setAuthorDate(Date authorDate) {
		this.authorDate = authorDate;
	}

	/**
	 * @return the committerName
	 */
	public String getCommitterName() {
		return committerName;
	}

	/**
	 * @param committerName the committerName to set
	 */
	public void setCommitterName(String committerName) {
		this.committerName = committerName;
	}

	/**
	 * @return the committerEmail
	 */
	public String getCommitterEmail() {
		return committerEmail;
	}

	/**
	 * @param committerEmail the committerEmail to set
	 */
	public void setCommitterEmail(String committerEmail) {
		this.committerEmail = committerEmail;
	}

	/**
	 * @return the committerDate
	 */
	public Date getCommitterDate() {
		return committerDate;
	}

	/**
	 * @param committerDate the committerDate to set
	 */
	public void setCommitterDate(Date committerDate) {
		this.committerDate = committerDate;
	}
}
