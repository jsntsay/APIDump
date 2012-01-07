package com.apidump.models;

import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.client.RequestException;

import com.apidump.generator.UsersGenerator;

@Entity
public class PullRequestMarkers {

	@Id
	private String sha;

	//to prevent recursion of repo > pull request > marker > repo
	private long repoId;

	private String label;

	private String ref;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private Users user;
	
	public PullRequestMarkers (PullRequestMarker p) throws RequestException, IOException {
		sha = p.getSha();
		label = p.getLabel();
		ref = p.getRef();
		
		if (p.getRepo() != null && !p.getRepo().getName().equals("/"))
			repoId = p.getRepo().getId();
		if (p.getUser().getLogin() != null)
			user = UsersGenerator.getInstance().getUsers(p.getUser().getLogin());
	}
	
	public PullRequestMarkers() {
		
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
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
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
	 * @return the repoId
	 */
	public long getRepoId() {
		return repoId;
	}

	/**
	 * @param repoId the repoId to set
	 */
	public void setRepoId(long repoId) {
		this.repoId = repoId;
	}
}
