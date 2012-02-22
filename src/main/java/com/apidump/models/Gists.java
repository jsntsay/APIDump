package com.apidump.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.GistRevision;
import org.eclipse.egit.github.core.client.RequestException;

import com.apidump.generator.GistsGenerator;
import com.apidump.generator.UsersGenerator;

@Entity
public class Gists {
	
	@Id
	private String id;
	
	@Column(name = "PUBLIC")
	private boolean isPublic;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	private int comments;

	@OneToMany
	private List<GistsRevision> history;

	// Removed mapping of filename to GistFile
	@OneToMany
	private List<GistsFile> files;

	private String description;

	private String gitPullUrl;

	private String gitPushUrl;

	private String htmlUrl;

	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private Users user;
	
	// Added comments to this model
	@OneToMany
	private List<Comments> commentsList;
	
	public Gists(Gist g) throws RequestException, IOException {
		id = g.getId();
		isPublic = g.isPublic();
		createdAt = g.getCreatedAt();
		updatedAt = g.getUpdatedAt();
		comments = g.getComments();
		
		if (g.getHistory() != null) {
			history = new ArrayList<GistsRevision>();
			for (GistRevision gr : g.getHistory())
				history.add(new GistsRevision(gr));
		}
		
		if (g.getFiles() != null) {
			files = new ArrayList<GistsFile>();
			for (GistFile file : g.getFiles().values())
				files.add(new GistsFile(file));
		}
		
		description = g.getDescription();
		gitPullUrl = g.getGitPullUrl();
		gitPushUrl = g.getGitPushUrl();
		htmlUrl = g.getHtmlUrl();
		url = g.getUrl();

		if (g.getUser() != null) {
			user = UsersGenerator.getInstance().getUsers(g.getUser().getLogin());
		}
		
		List<Comments> cList = GistsGenerator.getInstance().getComments(g.getId());
		if (cList != null) {
			commentsList = cList;
		}
	}
	
	public Gists() {
		
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the isPublic
	 */
	public boolean isPublic() {
		return isPublic;
	}

	/**
	 * @param isPublic the isPublic to set
	 */
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the comments
	 */
	public int getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(int comments) {
		this.comments = comments;
	}

	/**
	 * @return the history
	 */
	public List<GistsRevision> getHistory() {
		return history;
	}

	/**
	 * @param history the history to set
	 */
	public void setHistory(List<GistsRevision> history) {
		this.history = history;
	}

	/**
	 * @return the files
	 */
	public List<GistsFile> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<GistsFile> files) {
		this.files = files;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the gitPullUrl
	 */
	public String getGitPullUrl() {
		return gitPullUrl;
	}

	/**
	 * @param gitPullUrl the gitPullUrl to set
	 */
	public void setGitPullUrl(String gitPullUrl) {
		this.gitPullUrl = gitPullUrl;
	}

	/**
	 * @return the gitPushUrl
	 */
	public String getGitPushUrl() {
		return gitPushUrl;
	}

	/**
	 * @param gitPushUrl the gitPushUrl to set
	 */
	public void setGitPushUrl(String gitPushUrl) {
		this.gitPushUrl = gitPushUrl;
	}

	/**
	 * @return the htmlUrl
	 */
	public String getHtmlUrl() {
		return htmlUrl;
	}

	/**
	 * @param htmlUrl the htmlUrl to set
	 */
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
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
	 * @return the user
	 */
	public Users getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(Users user) {
		this.user = user;
	}

	/**
	 * @return the commentsList
	 */
	public List<Comments> getCommentsList() {
		return commentsList;
	}

	/**
	 * @param commentsList the commentsList to set
	 */
	public void setCommentsList(List<Comments> commentsList) {
		this.commentsList = commentsList;
	}
}
