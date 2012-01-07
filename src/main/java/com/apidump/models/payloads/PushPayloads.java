package com.apidump.models.payloads;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.PushPayload;

import com.apidump.models.Commits;

@Entity
@Table(name = "EVENTPAYLOADS")
@DiscriminatorValue("PUSH")
public class PushPayloads extends EventPayloads {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String head;

	private String ref;

	private int size;
	
	@OneToMany
	private List<Commits> commits;
	
	public PushPayloads(EventPayload payload) {
		PushPayload push = (PushPayload) payload;
		head = push.getHead();
		ref = push.getRef();
		size = push.getSize();
		commits = transferCommits(push.getCommits());
	}
	
	public PushPayloads() {
		
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
	 * @return the head
	 */
	public String getHead() {
		return head;
	}

	/**
	 * @param head the head to set
	 */
	public void setHead(String head) {
		this.head = head;
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
	 * @return the commits
	 */
	public List<Commits> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<Commits> commits) {
		this.commits = commits;
	}

	private List<Commits> transferCommits(List<Commit> sourceList) {
		List<Commits> commitsList = new ArrayList<Commits>();
		for (Commit c : sourceList) {
			try {
				Commits commits = new Commits(c);
				commitsList.add(commits);
			} catch (IllegalStateException e) {
				// Skip, it's a duplicate.
			}
		}
		return commitsList;
	}
}
