package com.apidump.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.eclipse.egit.github.core.CommitFile;

@Entity
public class CommitFiles {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	private String sha;
	
	private int additions;

	private int changes;

	private int deletions;

	@Lob
	private String blobUrl;

	private String filename;

	@Lob
	private String patch;

	@Lob
	private String rawUrl;
	
	private String status;

	public CommitFiles(CommitFile f) {
		sha = f.getSha();
		
		additions = f.getAdditions();
		changes = f.getChanges();
		deletions = f.getDeletions();
		
		blobUrl = f.getBlobUrl();
		filename = f.getFilename();
		patch = f.getPatch();
		rawUrl = f.getRawUrl();
		status = f.getStatus();
	}
	
	public CommitFiles() {
		
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
	 * @return the changes
	 */
	public int getChanges() {
		return changes;
	}

	/**
	 * @param changes the changes to set
	 */
	public void setChanges(int changes) {
		this.changes = changes;
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
	 * @return the blobUrl
	 */
	public String getBlobUrl() {
		return blobUrl;
	}

	/**
	 * @param blobUrl the blobUrl to set
	 */
	public void setBlobUrl(String blobUrl) {
		this.blobUrl = blobUrl;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the patch
	 */
	public String getPatch() {
		return patch;
	}

	/**
	 * @param patch the patch to set
	 */
	public void setPatch(String patch) {
		this.patch = patch;
	}

	/**
	 * @return the rawUrl
	 */
	public String getRawUrl() {
		return rawUrl;
	}

	/**
	 * @param rawUrl the rawUrl to set
	 */
	public void setRawUrl(String rawUrl) {
		this.rawUrl = rawUrl;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
}
