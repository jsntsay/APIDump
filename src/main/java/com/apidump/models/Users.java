package com.apidump.models;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.service.WatcherService;

import com.apidump.generator.UsersGenerator;

@Entity
public class Users {
	
	private static String TYPE_ORGANIZATION = "Organization";
	
	@Id
	private int id;
	
	private boolean hireable;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	private int collaborators;

	private int diskUsage;

	private int followers;

	private int following;

	private int ownedPrivateRepos;

	private int privateGists;

	private int publicGists;

	private int publicRepos;

	private int totalPrivateRepos;

	private String avatarUrl;

	private String blog;

	private String company;

	private String email;

	private String htmlUrl;

	private String location;

	private String login;

	private String name;

	private String type;

	private String url;
	
	//TODO: change this to Users and Repositories (or do I even want to do that? accesses will explode)
	@ElementCollection
	private List<Integer> followersList;
	
	@ElementCollection
	private List<Integer> followingList;
	
	@ElementCollection
	private List<Long> watchList;
	
	@ElementCollection
	private List<Long> repoList;
	
	@ManyToMany
	@JoinTable(name = "ORG_MEMBERS", joinColumns = { @JoinColumn(name="ORG_ID") }, inverseJoinColumns = { @JoinColumn(name="USER_ID") })
	private List<Users> memberList;
	
	public Users(User user) throws RequestException, IOException {
		id = user.getId();

		hireable = user.isHireable();
		createdAt = user.getCreatedAt();
		collaborators = user.getCollaborators();
		diskUsage = user.getDiskUsage();
		followers = user.getFollowers();
		following = user.getFollowing();
		ownedPrivateRepos = user.getOwnedPrivateRepos();
		privateGists = user.getPrivateGists();
		publicGists = user.getPublicGists();
		publicRepos = user.getPublicRepos();
		totalPrivateRepos = user.getTotalPrivateRepos();
		avatarUrl = user.getAvatarUrl();
		blog = user.getBlog();
		company = user.getCompany();
		email = user.getEmail();
		htmlUrl = user.getHtmlUrl();
		location = user.getLocation();
		login = user.getLogin();
		name = user.getName();
		type = user.getType();
		url = user.getUrl();
		
		//System.out.println("users: " + login);
		
		List<Integer> fList = UsersGenerator.getInstance().getFollowers(login);
		if (fList != null)
			followersList = fList;
		
		List<Integer> fList2 = UsersGenerator.getInstance().getFollowing(login);
		if (fList2 != null)
			followingList = fList2;
		
		List<Long> wList = UsersGenerator.getInstance().getWatching(login);
		if (wList != null)
			watchList = wList;
		
		List<Long> rList = UsersGenerator.getInstance().getRepositories(login);
		if (wList != null)
			repoList = rList;
		
		if (type.equals(TYPE_ORGANIZATION)) {
			List<Users> members = UsersGenerator.getInstance().getOrgMembers(login);
			if (members != null)
				memberList = members;
		}
	}
	
	public Users() {
		
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the hireable
	 */
	public boolean isHireable() {
		return hireable;
	}

	/**
	 * @param hireable the hireable to set
	 */
	public void setHireable(boolean hireable) {
		this.hireable = hireable;
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
	 * @return the collaborators
	 */
	public int getCollaborators() {
		return collaborators;
	}

	/**
	 * @param collaborators the collaborators to set
	 */
	public void setCollaborators(int collaborators) {
		this.collaborators = collaborators;
	}

	/**
	 * @return the diskUsage
	 */
	public int getDiskUsage() {
		return diskUsage;
	}

	/**
	 * @param diskUsage the diskUsage to set
	 */
	public void setDiskUsage(int diskUsage) {
		this.diskUsage = diskUsage;
	}

	/**
	 * @return the followers
	 */
	public int getFollowers() {
		return followers;
	}

	/**
	 * @param followers the followers to set
	 */
	public void setFollowers(int followers) {
		this.followers = followers;
	}

	/**
	 * @return the following
	 */
	public int getFollowing() {
		return following;
	}

	/**
	 * @param following the following to set
	 */
	public void setFollowing(int following) {
		this.following = following;
	}

	/**
	 * @return the ownedPrivateRepos
	 */
	public int getOwnedPrivateRepos() {
		return ownedPrivateRepos;
	}

	/**
	 * @param ownedPrivateRepos the ownedPrivateRepos to set
	 */
	public void setOwnedPrivateRepos(int ownedPrivateRepos) {
		this.ownedPrivateRepos = ownedPrivateRepos;
	}

	/**
	 * @return the privateGists
	 */
	public int getPrivateGists() {
		return privateGists;
	}

	/**
	 * @param privateGists the privateGists to set
	 */
	public void setPrivateGists(int privateGists) {
		this.privateGists = privateGists;
	}

	/**
	 * @return the publicGists
	 */
	public int getPublicGists() {
		return publicGists;
	}

	/**
	 * @param publicGists the publicGists to set
	 */
	public void setPublicGists(int publicGists) {
		this.publicGists = publicGists;
	}

	/**
	 * @return the publicRepos
	 */
	public int getPublicRepos() {
		return publicRepos;
	}

	/**
	 * @param publicRepos the publicRepos to set
	 */
	public void setPublicRepos(int publicRepos) {
		this.publicRepos = publicRepos;
	}

	/**
	 * @return the totalPrivateRepos
	 */
	public int getTotalPrivateRepos() {
		return totalPrivateRepos;
	}

	/**
	 * @param totalPrivateRepos the totalPrivateRepos to set
	 */
	public void setTotalPrivateRepos(int totalPrivateRepos) {
		this.totalPrivateRepos = totalPrivateRepos;
	}

	/**
	 * @return the avatarUrl
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * @param avatarUrl the avatarUrl to set
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * @return the blog
	 */
	public String getBlog() {
		return blog;
	}

	/**
	 * @param blog the blog to set
	 */
	public void setBlog(String blog) {
		this.blog = blog;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the followersList
	 */
	public List<Integer> getFollowersList() {
		return followersList;
	}

	/**
	 * @param followersList the followersList to set
	 */
	public void setFollowersList(List<Integer> followersList) {
		this.followersList = followersList;
	}

	/**
	 * @return the followingList
	 */
	public List<Integer> getFollowingList() {
		return followingList;
	}

	/**
	 * @param followingList the followingList to set
	 */
	public void setFollowingList(List<Integer> followingList) {
		this.followingList = followingList;
	}

	/**
	 * @return the watchList
	 */
	public List<Long> getWatchList() {
		return watchList;
	}

	/**
	 * @param watchList the watchList to set
	 */
	public void setWatchList(List<Long> watchList) {
		this.watchList = watchList;
	}

	/**
	 * @return the repoList
	 */
	public List<Long> getRepoList() {
		return repoList;
	}

	/**
	 * @param repoList the repoList to set
	 */
	public void setRepoList(List<Long> repoList) {
		this.repoList = repoList;
	}

	/**
	 * @return the memberList
	 */
	public List<Users> getMemberList() {
		return memberList;
	}

	/**
	 * @param memberList the memberList to set
	 */
	public void setMemberList(List<Users> memberList) {
		this.memberList = memberList;
	}
	
	public boolean isOrganziation() {
		return type.equals(TYPE_ORGANIZATION);
	}
}
