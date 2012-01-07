package com.apidump.models.payloads;

import java.io.IOException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.egit.github.core.event.CommitCommentPayload;
import org.eclipse.egit.github.core.event.EventPayload;

import com.apidump.models.CommitComments;


@Entity
@Table(name = "EVENTPAYLOADS")
@DiscriminatorValue("COMMIT_COMMENT")
public class CommitCommentPayloads extends EventPayloads {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPO_ID")
	private CommitComments comment;
	
	public CommitCommentPayloads(EventPayload payload) throws IOException {
		CommitCommentPayload ccp = (CommitCommentPayload) payload;
		comment = new CommitComments(ccp.getComment());
	}
	
	public CommitCommentPayloads() {
		
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
	 * @return the comment
	 */
	public CommitComments getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(CommitComments comment) {
		this.comment = comment;
	}
}
