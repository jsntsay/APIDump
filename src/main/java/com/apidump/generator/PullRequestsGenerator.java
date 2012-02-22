package com.apidump.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.PullRequestService;

import com.apidump.APIDump;
import com.apidump.models.CommitComments;
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

	// Key here is: repoId + number
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
	
	public List<PullRequests> getPullRequestsFromRepo(IRepositoryIdProvider repoId) throws RequestException, IOException {
		if (repoId == null)
			return null;
		
		return getPullsFromServiceFromRepo(repoId);
	}
	
	private synchronized List<PullRequests> getPullsFromServiceFromRepo(IRepositoryIdProvider repoId) throws RequestException, IOException {
		List<PullRequests> result = null;
		
		List<PullRequest> pullReqs = new ArrayList<PullRequest>();
		List<PullRequest> openPulls = service.getPullRequests(repoId, "open");
		List<PullRequest> closedPulls = service.getPullRequests(repoId, "closed");
		if (openPulls != null)
			pullReqs.addAll(openPulls);
		if (closedPulls != null)
			pullReqs.addAll(closedPulls);
		if (!pullReqs.isEmpty()) {
			result = new ArrayList<PullRequests>();
			for (PullRequest pull : pullReqs) {
				result.add(getPullRequests(repoId, pull.getNumber()));
			}
		}
		
		return result;
	}
	
	private synchronized PullRequests getPullsFromService(IRepositoryIdProvider repository, int id) throws RequestException, IOException {
		PullRequest p = service.getPullRequest(repository, id);
		return new PullRequests(p);
	}
	
	public List<CommitComments> getComments(IRepositoryIdProvider repoId, int id) throws RequestException, IOException {
		if (repoId == null)
			return null;

		String key = repoId.generateId() + id;
		PullRequests pr = null;
		
		if (cache.containsKey(key)) {
			pr = cache.get(key);
			List<CommitComments> commentsList = pr.getCommentsList();
			if (commentsList != null)
				return commentsList;
		}

		List<CommitComments> commentsList = getCommentsFromService(repoId, id);

		if (pr != null) {
			pr.setCommentsList(commentsList);
			cache.put(key, pr);
		}
		
		return commentsList;
	}
	
	// Dunno if services are threadsafe, if so, don't need to do this
	private synchronized List<CommitComments> getCommentsFromService(IRepositoryIdProvider repoId, int id)
			throws RequestException, IOException {
		List<CommitComment> comments = service.getComments(repoId, id);
		if (comments == null || comments.isEmpty())
			return null;
		
		List<CommitComments> results = new ArrayList<CommitComments>();
		for (CommitComment c : comments)
			results.add(new CommitComments(c));
		return results;
	}
}
