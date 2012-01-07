package com.apidump.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.WatcherService;

import com.apidump.generator.PullRequestsGenerator;
import com.apidump.generator.RepositoriesGenerator;
import com.apidump.generator.UsersGenerator;

@Entity
public class Repositories {

	@Id
	private long id;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_ID")
	private Users owner;

	private boolean fork;

	private boolean hasDownloads;

	private boolean hasIssues;

	private boolean hasWiki;

	@Column(name = "private")
	private boolean isPrivate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date pushedAt;

	private int forks;

	private int openIssues;

	private int size;

	private int watchers;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	private Repositories parent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOURCE_ID")
	private Repositories source;

	private String cloneUrl;

	@Lob
	private String description;

	private String homepage;

	private String gitUrl;

	private String htmlUrl;

	private String language;

	private String masterBranch;

	private String mirrorUrl;

	private String sshUrl;

	private String svnUrl;

	private String url;
	
	@ElementCollection
	private List<Integer> watcherList;
	
	@ElementCollection
	private List<Integer> collaboratorList;
	
	@ElementCollection
	private List<Long> forkList;

	// Added pull requests to this model
	@OneToMany
	private List<PullRequests> pullRequests;
	
	// Added issues to this model
	@OneToMany
	private List<Issues> issues;
	
	public Repositories(Repository r) throws IOException, RequestException {
		id = r.getId();

		name = r.getName();
		fork = r.isFork();
		hasDownloads = r.isHasDownloads();
		hasIssues = r.isHasIssues();
		hasWiki = r.isHasWiki();
		isPrivate = r.isPrivate();
		createdAt = r.getCreatedAt();
		pushedAt = r.getPushedAt();
		forks = r.getForks();
		openIssues = r.getOpenIssues();
		size = r.getSize();
		watchers = r.getWatchers();
		cloneUrl = r.getCloneUrl();
		description = r.getDescription();
		homepage = r.getHomepage();
		gitUrl = r.getGitUrl();
		htmlUrl = r.getHtmlUrl();
		language = r.getLanguage();
		masterBranch = r.getMasterBranch();
		mirrorUrl = r.getMirrorUrl();
		sshUrl = r.getSshUrl();
		svnUrl = r.getSvnUrl();
		url = r.getUrl();

		//System.out.println("repos: " + url);
		
		if (r.getOwner() != null)
			owner = UsersGenerator.getInstance().getUsers(r.getOwner().getLogin());

		Repository pRepo = r.getParent();
		if (pRepo != null && pRepo.getOwner() != null)
			parent = RepositoriesGenerator.getInstance().getRepositories(pRepo.generateId());

		Repository sRepo = r.getSource();
		if (sRepo != null && sRepo.getOwner() != null)
			source = RepositoriesGenerator.getInstance().getRepositories(sRepo.generateId());
		
		RepositoryService rs = new RepositoryService();
		CollaboratorService cs = new CollaboratorService();
		WatcherService ws = new WatcherService();
		PullRequestService prs = new PullRequestService();
		IssueService is = new IssueService();
		
		IRepositoryIdProvider repoId = RepositoryId.createFromId(r.generateId());
		
		List<User> watchers = ws.getWatchers(repoId);
		if (watchers != null) {
			watcherList = new ArrayList<Integer>();
			for (User u : watchers) {
				watcherList.add(u.getId());
			}
		}
		
		List<User> collab = cs.getCollaborators(repoId);
		if (collab != null) {
			collaboratorList = new ArrayList<Integer>();
			for (User u : collab) {
				collaboratorList.add(u.getId());
			}
		}
		
		List<Repository> forkRepos = rs.getForks(repoId);
		if (forkRepos != null) {
			forkList = new ArrayList<Long>();
			for (Repository fr : forkRepos) {
				forkList.add(fr.getId());
			}
		}
		
		List<PullRequest> pullReqs = new ArrayList<PullRequest>();
		List<PullRequest> openPulls = prs.getPullRequests(repoId, "open");
		List<PullRequest> closedPulls = prs.getPullRequests(repoId, "closed");
		if (openPulls != null)
			pullReqs.addAll(openPulls);
		if (closedPulls != null)
			pullReqs.addAll(closedPulls);
		if (!pullReqs.isEmpty()) {
			pullRequests = new ArrayList<PullRequests>();
			for (PullRequest pull : pullReqs) {
				pullRequests.add(PullRequestsGenerator.getInstance().getPullRequests(repoId, pull.getNumber()));
			}
		}
		
		List<Issue> issueList = new ArrayList<Issue>();
		Map<String, String> filter = new HashMap<String, String>();
		filter.put("state", "open");
		List<Issue> openIssues = is.getIssues(repoId, filter);
		filter.put("state", "closed");
		List<Issue> closedIssues = is.getIssues(repoId, filter);
		if (openIssues != null)
			issueList.addAll(openIssues);
		if (closedIssues != null)
			issueList.addAll(closedIssues);
		if (!issueList.isEmpty()) {
			issues = new ArrayList<Issues>();
			for (Issue issue : issueList) {
				issues.add(new Issues(issue));
			}
		}
	}
	
	public Repositories() {
		
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

	/**
	 * @return the owner
	 */
	public Users getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Users owner) {
		this.owner = owner;
	}

	/**
	 * @return the fork
	 */
	public boolean isFork() {
		return fork;
	}

	/**
	 * @param fork the fork to set
	 */
	public void setFork(boolean fork) {
		this.fork = fork;
	}

	/**
	 * @return the hasDownloads
	 */
	public boolean isHasDownloads() {
		return hasDownloads;
	}

	/**
	 * @param hasDownloads the hasDownloads to set
	 */
	public void setHasDownloads(boolean hasDownloads) {
		this.hasDownloads = hasDownloads;
	}

	/**
	 * @return the hasIssues
	 */
	public boolean isHasIssues() {
		return hasIssues;
	}

	/**
	 * @param hasIssues the hasIssues to set
	 */
	public void setHasIssues(boolean hasIssues) {
		this.hasIssues = hasIssues;
	}

	/**
	 * @return the hasWiki
	 */
	public boolean isHasWiki() {
		return hasWiki;
	}

	/**
	 * @param hasWiki the hasWiki to set
	 */
	public void setHasWiki(boolean hasWiki) {
		this.hasWiki = hasWiki;
	}

	/**
	 * @return the isPrivate
	 */
	public boolean isPrivate() {
		return isPrivate;
	}

	/**
	 * @param isPrivate the isPrivate to set
	 */
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
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
	 * @return the pushedAt
	 */
	public Date getPushedAt() {
		return pushedAt;
	}

	/**
	 * @param pushedAt the pushedAt to set
	 */
	public void setPushedAt(Date pushedAt) {
		this.pushedAt = pushedAt;
	}

	/**
	 * @return the forks
	 */
	public int getForks() {
		return forks;
	}

	/**
	 * @param forks the forks to set
	 */
	public void setForks(int forks) {
		this.forks = forks;
	}

	/**
	 * @return the openIssues
	 */
	public int getOpenIssues() {
		return openIssues;
	}

	/**
	 * @param openIssues the openIssues to set
	 */
	public void setOpenIssues(int openIssues) {
		this.openIssues = openIssues;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the watchers
	 */
	public int getWatchers() {
		return watchers;
	}

	/**
	 * @param watchers the watchers to set
	 */
	public void setWatchers(int watchers) {
		this.watchers = watchers;
	}

	/**
	 * @return the parent
	 */
	public Repositories getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Repositories parent) {
		this.parent = parent;
	}

	/**
	 * @return the source
	 */
	public Repositories getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Repositories source) {
		this.source = source;
	}

	/**
	 * @return the cloneUrl
	 */
	public String getCloneUrl() {
		return cloneUrl;
	}

	/**
	 * @param cloneUrl the cloneUrl to set
	 */
	public void setCloneUrl(String cloneUrl) {
		this.cloneUrl = cloneUrl;
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
	 * @return the homepage
	 */
	public String getHomepage() {
		return homepage;
	}

	/**
	 * @param homepage the homepage to set
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	/**
	 * @return the gitUrl
	 */
	public String getGitUrl() {
		return gitUrl;
	}

	/**
	 * @param gitUrl the gitUrl to set
	 */
	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
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
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the masterBranch
	 */
	public String getMasterBranch() {
		return masterBranch;
	}

	/**
	 * @param masterBranch the masterBranch to set
	 */
	public void setMasterBranch(String masterBranch) {
		this.masterBranch = masterBranch;
	}

	/**
	 * @return the mirrorUrl
	 */
	public String getMirrorUrl() {
		return mirrorUrl;
	}

	/**
	 * @param mirrorUrl the mirrorUrl to set
	 */
	public void setMirrorUrl(String mirrorUrl) {
		this.mirrorUrl = mirrorUrl;
	}

	/**
	 * @return the sshUrl
	 */
	public String getSshUrl() {
		return sshUrl;
	}

	/**
	 * @param sshUrl the sshUrl to set
	 */
	public void setSshUrl(String sshUrl) {
		this.sshUrl = sshUrl;
	}

	/**
	 * @return the svnUrl
	 */
	public String getSvnUrl() {
		return svnUrl;
	}

	/**
	 * @param svnUrl the svnUrl to set
	 */
	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
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
