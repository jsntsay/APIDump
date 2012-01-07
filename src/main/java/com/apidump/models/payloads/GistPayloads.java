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

import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.GistPayload;

import com.apidump.models.Gists;

@Entity
@Table(name = "EVENTPAYLOADS")
@DiscriminatorValue("GIST")
public class GistPayloads extends EventPayloads {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String action;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GIST_ID")
	private Gists gist;
	
	public GistPayloads(EventPayload payload) throws RequestException, IOException {
		GistPayload g = (GistPayload) payload;
		action = g.getAction();
		
		if (g.getGist() != null)
			gist = new Gists(g.getGist());
	}
	
	public GistPayloads() {
		
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
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the gist
	 */
	public Gists getGist() {
		return gist;
	}

	/**
	 * @param gist the gist to set
	 */
	public void setGist(Gists gist) {
		this.gist = gist;
	}
}
