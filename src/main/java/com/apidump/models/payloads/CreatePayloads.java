package com.apidump.models.payloads;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.eclipse.egit.github.core.event.CreatePayload;
import org.eclipse.egit.github.core.event.EventPayload;

@Entity
@Table(name = "EVENTPAYLOADS")
@DiscriminatorValue("CREATE")
public class CreatePayloads extends EventPayloads {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String refType;

	private String ref;

	private String masterBranch;

	@Lob
	private String description;
	
	public CreatePayloads(EventPayload payload) {
		CreatePayload create = (CreatePayload) payload;
		refType = create.getRefType();
		ref = create.getRef();
		masterBranch = create.getMasterBranch();
		description = create.getDescription();
	}
	
	public CreatePayloads() {
		
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

	/**
	 * @return the masterBranch
	 */
	public String getMasterBranch() {
		return masterBranch;
	}

	/**
	 * @param masterBranch the masterBranch to set
	 */
	public void setMasterBranch(String masterBranch) {
		this.masterBranch = masterBranch;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
