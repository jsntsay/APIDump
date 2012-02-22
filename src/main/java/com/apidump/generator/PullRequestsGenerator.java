package com.apidump.generator;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.PullRequestService;

import com.apidump.APIDump;
import com.apidump.models.PullRequests;
import com.apidump.util.UrlUtil;

public class PullRequestsGenerator {

	private static PullRequestsGenerator instance = null;
	
	protected PullRequestsGenerator() {
		
	}
   
	public static PullRequestsGenerator getInstance() {
		if(instance == null) {
			instance = new PullRequestsGenerator();
			}
		return instance;
		}

	private ConcurrentHashMap<String, PullRequests> cache = new ConcurrentHashMap<String, PullRequests>();
	
	private PullRequestService service = new PullRequestService(APIDump.getClient());
	
	public PullRequests getPullRequests(IRepositoryIdProvider repository, int id) throws RequestException, IOException {
		if (repository == null)
			return null;
		
		String key = repository.generateId() + id;
		if (cache.containsKey(key))
			return cache.get(key);
		
		//System.out.println("pull request: " + key);
		
		PullRequests pull = getPullsFromService(repository, id);
			
		cache.put(key, pull);
		return pull;
	}
	
	// This is meant for html_url field from pull requests.
	public PullRequests getPullRequests(String url) throws RequestException, IOException {
		List<String> fields = UrlUtil.parseUrl(url);
		
		if (fields == null)
			return null;
		
		IRepositoryIdProvider repoId = RepositoryId.create(fields.get(0), fields.get(1));
		return getPullsFromService(repoId, Integer.parseInt(fields.get(2)));
	}
	
	private synchronized PullRequests getPullsFromService(IRepositoryIdProvider repository, int id) throws RequestException, IOException {
		PullRequest p = service.getPullRequest(repository, id);
		return new PullRequests(p);
	}
}
