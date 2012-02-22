package com.apidump.models;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.RequestException;

import com.apidump.generator.PullRequestsGenerator;
import com.apidump.generator.UsersGenerator;

@Entity
public class PullRequests {

	@Id
	private long id;
	
	private boolean mergeable;

	private boolean merged;

	@Temporal(TemporalType.TIMESTAMP)
	private Date closedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date mergedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	private int additions;

	private int changedFiles;

	private int comments;

	private int commits;

	private int deletions;

	private int number;

	@OneToOne
	@JoinColumn(name = "BASE_SHA")
	private PullRequestMarkers base;

	@OneToOne
	@JoinColumn(name = "HEAD_SHA")
	private PullRequestMarkers head;

	@Lob
	private String body;

	private String diffUrl;

	private String htmlUrl;

	private String issueUrl;

	private String patchUrl;

	private String state;

	private String title;

	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MERGED_BY_ID")
	private Users mergedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private Users user;
	
	// Added CommitComments to this model
	@OneToMany
	private List<CommitComments> commentsList;
	
	public PullRequests(PullRequest p) throws RequestException, IOException {
		id = p.getId();
		mergeable = p.isMergeable();
		merged = p.isMerged();
		closedAt = p.getClosedAt();
		mergedAt = p.getMergedAt();
		updatedAt = p.getUpdatedAt();
		createdAt = p.getCreatedAt();
		additions = p.getAdditions();
		changedFiles = p.getChangedFiles();
		comments = p.getComments();
		commits = p.getCommits();
		deletions = p.getDeletions();
		number = p.getNumber();
		body = p.getBody();
		diffUrl = p.getDiffUrl();
		htmlUrl = p.getHtmlUrl();
		issueUrl = p.getIssueUrl();
		patchUrl = p.getPatchUrl();
		state = p.getState();
		title = p.getTitle();
		url = p.getUrl();
		
		if (p.getBase() != null) {
			base = new PullRequestMarkers(p.getBase());
		}
		
		if (p.getHead() != null) {
			head = new PullRequestMarkers(p.getHead());
		}
		
		if (p.getMergedBy() != null) {
			mergedBy = UsersGenerator.getInstance().getUsers(p.getMergedBy().getLogin());
		}
		
		if (p.getUser() != null) {
			user = UsersGenerator.getInstance().getUsers(p.getUser().getLogin());
		}
		
		IRepositoryIdProvider repoId = RepositoryId.createFromId(p.getBase().getRepo().generateId());
		List<CommitComments> cList = PullRequestsGenerator.getInstance().getComments(repoId, p.getNumber());
		if (cList != null) {
			commentsList = cList;
		}
	}
	
	public PullRequests() {
		
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the mergeable
	 */
	public boolean isMergeable() {
		return mergeable;
	}

	/**
	 * @param mergeable the mergeable to set
	 */
	public void setMergeable(boolean mergeable) {
		this.mergeable = mergeable;
	}

	/**
	 * @return the merged
	 */
	public boolean isMerged() {
		return merged;
	}

	/**
	 * @param merged the merged to set
	 */
	public void setMerged(boolean merged) {
		this.merged = merged;
	}

	/**
	 * @return the closedAt
	 */
	public Date getClosedAt() {
		return closedAt;
	}

	/**
	 * @param closedAt the closedAt to set
	 */
	public void setClosedAt(Date closedAt) {
		this.closedAt = closedAt;
	}

	/**
	 * @return the mergedAt
	 */
	public Date getMergedAt() {
		return mergedAt;
	}

	/**
	 * @param mergedAt the mergedAt to set
	 */
	public void setMergedAt(Date mergedAt) {
		this.mergedAt = mergedAt;
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
	 * @return the additions
	 */
	public int getAdditions() {
		return additions;
	}

	/**
	 * @param additions the additions to set
	 */
	public void setAdditions(int additions) {
		this.additions = additions;
	}

	/**
	 * @return the changedFiles
	 */
	public int getChangedFiles() {
		return changedFiles;
	}

	/**
	 * @param changedFiles the changedFiles to set
	 */
	public void setChangedFiles(int changedFiles) {
		this.changedFiles = changedFiles;
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
	 * @return the commits
	 */
	public int getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(int commits) {
		this.commits = commits;
	}

	/**
	 * @return the deletions
	 */
	public int getDeletions() {
		return deletions;
	}

	/**
	 * @param deletions the deletions to set
	 */
	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the base
	 */
	public PullRequestMarkers getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(PullRequestMarkers base) {
		this.base = base;
	}

	/**
	 * @return the head
	 */
	public PullRequestMarkers getHead() {
		return head;
	}

	/**
	 * @param head the head to set
	 */
	public void setHead(PullRequestMarkers head) {
		this.head = head;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the diffUrl
	 */
	public String getDiffUrl() {
		return diffUrl;
	}

	/**
	 * @param diffUrl the diffUrl to set
	 */
	public void setDiffUrl(String diffUrl) {
		this.diffUrl = diffUrl;
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
	 * @return the issueUrl
	 */
	public String getIssueUrl() {
		return issueUrl;
	}

	/**
	 * @param issueUrl the issueUrl to set
	 */
	public void setIssueUrl(String issueUrl) {
		this.issueUrl = issueUrl;
	}

	/**
	 * @return the patchUrl
	 */
	public String getPatchUrl() {
		return patchUrl;
	}

	/**
	 * @param patchUrl the patchUrl to set
	 */
	public void setPatchUrl(String patchUrl) {
		this.patchUrl = patchUrl;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return the mergedBy
	 */
	public Users getMergedBy() {
		return mergedBy;
	}

	/**
	 * @param mergedBy the mergedBy to set
	 */
	public void setMergedBy(Users mergedBy) {
		this.mergedBy = mergedBy;
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
	public List<CommitComments> getCommentsList() {
		return commentsList;
	}

	/**
	 * @param commentsList the commentsList to set
	 */
	public void setCommentsList(List<CommitComments> commentsList) {
		this.commentsList = commentsList;
	}
}
