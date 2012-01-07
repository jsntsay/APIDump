package com.apidump.models;

import java.io.IOException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.event.CommitCommentPayload;
import org.eclipse.egit.github.core.event.CreatePayload;
import org.eclipse.egit.github.core.event.DeletePayload;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.GistPayload;
import org.eclipse.egit.github.core.event.PushPayload;
import org.eclipse.egit.github.core.event.WatchPayload;

import com.apidump.generator.RepositoriesGenerator;
import com.apidump.generator.UsersGenerator;
import com.apidump.models.payloads.CommitCommentPayloads;
import com.apidump.models.payloads.CreatePayloads;
import com.apidump.models.payloads.DeletePayloads;
import com.apidump.models.payloads.EventPayloads;
import com.apidump.models.payloads.GistPayloads;
import com.apidump.models.payloads.PushPayloads;
import com.apidump.models.payloads.WatchPayloads;

@Entity
public class Events {

	@Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String type;
	
	@OneToOne
	@JoinColumn(name = "EVENT_ID")
	private EventPayloads payload;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPO_ID")
	private Repositories repo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTOR_ID")
	private Users actor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private Users org;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	public Events(Event e) throws RequestException, IOException {
		type = e.getType();
		payload = convertPayload(e.getPayload());
		System.out.println("event type: " + type);
		if (e.getRepo() != null && !e.getRepo().getName().equals("/"))
			repo = RepositoriesGenerator.getInstance().getRepositories(e.getRepo().getName());
		if (e.getActor().getLogin() != null)
			actor = UsersGenerator.getInstance().getUsers(e.getActor().getLogin());
		if (e.getOrg().getLogin() != null)
			org = UsersGenerator.getInstance().getUsers(e.getOrg().getLogin());

		createdAt = e.getCreatedAt();
	}
	
	public Events() {
		
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
	 * @return the actor
	 */
	public Users getActor() {
		return actor;
	}

	/**
	 * @param actor the actor to set
	 */
	public void setActor(Users actor) {
		this.actor = actor;
	}

	/**
	 * @return the org
	 */
	public Users getOrg() {
		return org;
	}

	/**
	 * @param org the org to set
	 */
	public void setOrg(Users org) {
		this.org = org;
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
	 * @return the repo
	 */
	public Repositories getRepo() {
		return repo;
	}

	/**
	 * @param repo the repo to set
	 */
	public void setRepo(Repositories repo) {
		this.repo = repo;
	}
	
	/**
	 * @return the payload
	 */
	public EventPayloads getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(EventPayloads payload) {
		this.payload = payload;
	}

	/**
	 * @param payload to convert
	 * @throws IOException 
	 * @throws RequestException 
	 */
	private EventPayloads convertPayload(EventPayload payload) throws RequestException, IOException {
		if (payload instanceof WatchPayload) {
			return new WatchPayloads(payload);
		} else if (payload instanceof CommitCommentPayload) {
			return new CommitCommentPayloads(payload);
		} else if (payload instanceof CreatePayload) {
			return new CreatePayloads(payload);
		} else if (payload instanceof DeletePayload) {
			return new DeletePayloads(payload);
		} else if (payload instanceof PushPayload) {
			return new PushPayloads(payload);
		} else if (payload instanceof GistPayload) {
			return new GistPayloads(payload);
		} else
			return new EventPayloads();
	}
}
