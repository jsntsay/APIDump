package com.apidump.models.payloads;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.WatchPayload;


@Entity
@Table(name = "EVENTPAYLOADS")
@DiscriminatorValue("WATCH")
public class WatchPayloads extends EventPayloads {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String action;
	
	public WatchPayloads(EventPayload payload) {
		WatchPayload watch = (WatchPayload) payload;
		action = watch.getAction();
	}
	
	public WatchPayloads() {
		
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
}
