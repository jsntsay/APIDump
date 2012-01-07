package com.apidump.models.payloads;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eclipse.egit.github.core.event.DeletePayload;
import org.eclipse.egit.github.core.event.EventPayload;

@Entity
@Table(name = "EVENTPAYLOADS")
@DiscriminatorValue("DELETE")
public class DeletePayloads extends EventPayloads {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String refType;

	private String ref;
	
	public DeletePayloads(EventPayload payload) {
		DeletePayload delete = (DeletePayload) payload;
		refType = delete.getRefType();
		ref = delete.getRef();
	}
	
	public DeletePayloads() {
		
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
	 * @return the refType
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * @param refType the refType to set
	 */
	public void setRefType(String refType) {
		this.refType = refType;
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

}
