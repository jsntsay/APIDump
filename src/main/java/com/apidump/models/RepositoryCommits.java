package com.apidump.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.RequestException;

import com.apidump.generator.CommitsGenerator;
import com.apidump.generator.UsersGenerator;

@Entity
public class RepositoryCommits {

	@Id
	private String sha;
	
	@OneToOne
	private Commits commit;

	// Fold in CommitStats here
	private int statsAdditions;
	
	private int statsDeletions;
	
	private int statsTotal;

	// Just includes sha of parent commits.
	@ElementCollection
	private List<String> parents;

	@OneToMany
	private List<CommitFiles> files;

	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTHOR_ID")
	private Users author;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMMITTER_ID")
	private Users committer;
	
	// Add commit comments to model.
	@OneToMany
	private List<CommitComments> comments;
	
	public RepositoryCommits(RepositoryCommit rc) throws RequestException, IOException {
		sha = rc.getSha();
		url = rc.getUrl();
		
		if (rc.getCommit() != null)
			commit = CommitsGenerator.getInstance().getCommits(rc.getCommit());

		if (rc.getStats() != null) {
			statsAdditions = rc.getStats().getAdditions();
			statsDeletions = rc.getStats().getDeletions();
			statsTotal = rc.getStats().getTotal();
		}
		if (rc.getAuthor() != null)
			author = UsersGenerator.getInstance().getUsers(rc.getAuthor().getLogin());
		
		if (rc.getCommitter() != null)
			committer = UsersGenerator.getInstance().getUsers(rc.getCommitter().getLogin());
		
		if (rc.getParents() != null) {
			parents = new ArrayList<String>();
			for (Commit c : rc.getParents())
				parents.add(c.getSha());
		}
		
		if (rc.getFiles() != null) {
			files = new ArrayList<CommitFiles>();
			for (CommitFile f : rc.getFiles())
				files.add(new CommitFiles(f));
		}
		
		comments = CommitsGenerator.getInstance().getCommitComments(url);
	}
	
	public RepositoryCommits() {
		
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
	 * @return the commit
	 */
	public Commits getCommit() {
		return commit;
	}

	/**
	 * @param commit the commit to set
	 */
	public void setCommit(Commits commit) {
		this.commit = commit;
	}

	/**
	 * @return the statsAdditions
	 */
	public int getStatsAdditions() {
		return statsAdditions;
	}

	/**
	 * @param statsAdditions the statsAdditions to set
	 */
	public void setStatsAdditions(int statsAdditions) {
		this.statsAdditions = statsAdditions;
	}

	/**
	 * @return the statsDeletions
	 */
	public int getStatsDeletions() {
		return statsDeletions;
	}

	/**
	 * @param statsDeletions the statsDeletions to set
	 */
	public void setStatsDeletions(int statsDeletions) {
		this.statsDeletions = statsDeletions;
	}

	/**
	 * @return the statsTotal
	 */
	public int getStatsTotal() {
		return statsTotal;
	}

	/**
	 * @param statsTotal the statsTotal to set
	 */
	public void setStatsTotal(int statsTotal) {
		this.statsTotal = statsTotal;
	}

	/**
	 * @return the parents
	 */
	public List<String> getParents() {
		return parents;
	}

	/**
	 * @param parents the parents to set
	 */
	public void setParents(List<String> parents) {
		this.parents = parents;
	}

	/**
	 * @return the files
	 */
	public List<CommitFiles> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<CommitFiles> files) {
		this.files = files;
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
	 * @return the author
	 */
	public Users getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Users author) {
		this.author = author;
	}

	/**
	 * @return the committer
	 */
	public Users getCommitter() {
		return committer;
	}

	/**
	 * @param committer the committer to set
	 */
	public void setCommitter(Users committer) {
		this.committer = committer;
	}

	/**
	 * @return the comments
	 */
	public List<CommitComments> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<CommitComments> comments) {
		this.comments = comments;
	}
}
