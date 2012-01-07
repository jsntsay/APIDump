package com.apidump.generator;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.MilestoneService;

import com.apidump.models.Milestones;
import com.apidump.util.UrlUtil;

public class MilestonesGenerator {

	private static MilestonesGenerator instance = null;
	
	protected MilestonesGenerator() {
		
	}
   
	public static MilestonesGenerator getInstance() {
		if(instance == null) {
			instance = new MilestonesGenerator();
			}
		return instance;
		}

	private ConcurrentHashMap<String, Milestones> cache = new ConcurrentHashMap<String, Milestones>();
	
	private MilestoneService service = new MilestoneService();
	
	// This is meant for html_url field from pull requests.
	public Milestones getMilestones(String url) throws RequestException, IOException {
		List<String> fields = UrlUtil.parseUrl(url, "milestones");
		
		if (fields == null)
			return null;
		
		IRepositoryIdProvider repoId = RepositoryId.create(fields.get(0), fields.get(1));
		Milestones mile = getMilestonesFromService(repoId, fields.get(2));
		
		cache.put(url, mile);
		return mile;
	}
	
	public Milestones getMilestones(Milestone m) throws RequestException, IOException {
		return getMilestones(m.getUrl());
	}
	
	private synchronized Milestones getMilestonesFromService(IRepositoryIdProvider repository, String id) throws RequestException, IOException {
		Milestone m = service.getMilestone(repository, id);
		return new Milestones(m);
	}
}
