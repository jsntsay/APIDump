package com.apidump.models.payloads;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EVENTPAYLOADS")
@DiscriminatorValue("DOWNLOAD")
public class DownloadPayloads extends EventPayloads {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOWNLOAD_ID")
	private Downloads download;
	
	public DownloadPayloads (DownloadPayload d) {
		download = new Downloads(d);
	}
	*/
}
