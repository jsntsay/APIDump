package com.apidump.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.IssueService;

import com.apidump.APIDump;
import com.apidump.models.Comments;
import com.apidump.models.Issues;
import com.apidump.util.UrlUtil;

/**
 * Generates Issue models and comments from API
 */
public class IssuesGenerator {

	private static IssuesGenerator instance = null;

	private static IssueService service = new IssueService(APIDump.getClient());

	protected IssuesGenerator() {

	}

	public static IssuesGenerator getInstance() {
		if (instance == null) {
			instance = new IssuesGenerator();
		}
		return instance;
	}

	// String here is the html_url in Issues model
	// private ConcurrentHashMap<String, Issues> cache = new ConcurrentHashMap<String, Issues>();

	public List<Issues> getIssuesForRepo(IRepositoryIdProvider repoId) throws RequestException, IOException {
		if (repoId == null)
			return null;
		// System.out.println("Issues: " + login);

		return getIssuesFromService(repoId);
	}

	private List<Issues> getIssuesFromService(IRepositoryIdProvider repoId)
			throws RequestException, IOException {
		List<Issues> result = null;
		List<Issue> issueList = new ArrayList<Issue>();
		Map<String, String> filter = new HashMap<String, String>();
		filter.put("state", "open");
		List<Issue> openIssues = service.getIssues(repoId, filter);
		filter.put("state", "closed");
		List<Issue> closedIssues = service.getIssues(repoId, filter);
		if (openIssues != null)
			issueList.addAll(openIssues);
		if (closedIssues != null)
			issueList.addAll(closedIssues);
		if (!issueList.isEmpty()) {
			result = new ArrayList<Issues>();
			for (Issue issue : issueList) {
				result.add(new Issues(issue));
			}
		}
		
		return result;
	}
	
	public List<Comments> getComments(String url) throws RequestException, IOException {
		if (url == null)
			return null;
		
		List<String> fields = UrlUtil.parseUrl(url);
		
		if (fields == null)
			return null;

		return getCommentsFromService(fields.get(0), fields.get(1), fields.get(2));
	}
	
	// Dunno if services are threadsafe, if so, don't need to do this
	private synchronized List<Comments> getCommentsFromService(String user, String repo, 
			String id) throws RequestException, IOException {
		List<Comment> comments = service.getComments(user, repo, id);
		if (comments == null || comments.isEmpty())
			return null;
		List<Comments> results = new ArrayList<Comments>();
		for (Comment c : comments)
			results.add(new Comments(c));
		
		return results;
	}
	
}
