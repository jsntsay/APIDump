package com.apidump.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.IssueService;

import com.apidump.generator.LabelsGenerator;
import com.apidump.generator.PullRequestsGenerator;
import com.apidump.generator.UsersGenerator;
import com.apidump.util.UrlUtil;

@Entity
public class Issues {

	@Id
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date closedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	private int comments;

	private int number;

	@OneToMany
	private List<Labels> labels;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MILESTONE_URL")
	private Milestones milestone;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PULL_REQUEST_ID")
	private PullRequests pullRequest;

	@Lob
	private String body;

	private String bodyHtml;

	@Lob
	private String bodyText;

	private String htmlUrl;

	private String state;

	private String title;

	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSIGNEE_ID")
	private Users assignee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private Users user;
	
	// Added comments to this model
	@OneToMany
	private List<Comments> commentsList;
	
	public Issues(Issue i) throws RequestException, IOException {
		id = i.getId();
		
		closedAt = i.getClosedAt();
		createdAt = i.getCreatedAt();
		updatedAt = i.getUpdatedAt();
		
		comments = i.getComments();
		number = i.getNumber();
		
		body = i.getBody();
		bodyHtml = i.getBodyHtml();
		bodyText = i.getBodyText();
		htmlUrl = i.getHtmlUrl();
		state = i.getState();
		title = i.getTitle();
		url = i.getUrl();
		
		if (i.getPullRequest() != null) {
			pullRequest = PullRequestsGenerator.getInstance().
					getPullRequests(i.getPullRequest().getHtmlUrl());
		}
		
		if (i.getAssignee() != null) {
			assignee = UsersGenerator.getInstance().getUsers(i.getAssignee().getLogin());
		}
		
		if (i.getUser() != null) {
			user = UsersGenerator.getInstance().getUsers(i.getUser().getLogin());
		}
		
		if (i.getLabels() != null) {
			labels = new ArrayList<Labels>();
			for (Label l : i.getLabels()) {
				labels.add(LabelsGenerator.getInstance().getLabels(l));
			}
		}
		
		if (i.getMilestone() != null) {
			milestone = new Milestones(i.getMilestone());
		}
		
		
		List<Comment> cList = getCommentList(i.getHtmlUrl());
		if (cList != null) {
			commentsList = new ArrayList<Comments>();
			for (Comment c : cList) {
				commentsList.add(new Comments(c));
			}
		}
	}
	
	public Issues() {
		
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
	 * @return the labels
	 */
	public List<Labels> getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(List<Labels> labels) {
		this.labels = labels;
	}

	/**
	 * @return the milestone
	 */
	public Milestones getMilestone() {
		return milestone;
	}

	/**
	 * @param milestone the milestone to set
	 */
	public void setMilestone(Milestones milestone) {
		this.milestone = milestone;
	}

	/**
	 * @return the pullRequest
	 */
	public PullRequests getPullRequest() {
		return pullRequest;
	}

	/**
	 * @param pullRequest the pullRequest to set
	 */
	public void setPullRequest(PullRequests pullRequest) {
		this.pullRequest = pullRequest;
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
	 * @return the bodyHtml
	 */
	public String getBodyHtml() {
		return bodyHtml;
	}

	/**
	 * @param bodyHtml the bodyHtml to set
	 */
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	/**
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * @param bodyText the bodyText to set
	 */
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
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
	 * @return the assignee
	 */
	public Users getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(Users assignee) {
		this.assignee = assignee;
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
	
	// Meant for html_url field in Issue
	private List<Comment> getCommentList(String url) throws IOException {
		List<String> fields = UrlUtil.parseUrl(url);
		
		if (fields == null)
			return null;
		
		IssueService is = new IssueService();
		return is.getComments(fields.get(0), fields.get(1), fields.get(2));
	}
}
