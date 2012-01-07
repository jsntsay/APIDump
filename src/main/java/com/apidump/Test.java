package com.apidump;
import java.io.IOException;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

// Can reasonably get all repos using v2 api
// Not as reasonable for users (limit on pagination, it seems)
// Gists easy to get via v3

public class Test {

	public static void main(String[] args) throws IOException {
		
		//GitHubClient client = new GitHubClient("github.com");
		/*
		RepositoryService rsv2 = new RepositoryService(client);
		int page = 1;
		List<SearchRepository> repos = new ArrayList<SearchRepository>();
		while (true)
		{
			List<SearchRepository> results = rsv2.searchRepositories("pushed:[NOW-2HOURS TO NOW]", page);
			if (results.size() == 0)
				break;
			repos.addAll(results);
			System.out.println("page: " + page + " added");
			page++;
		}
		for (SearchRepository s : repos) {
			System.out.println(s.generateId());
		}
		System.out.println("size: " + repos.size());
		*/
		/*
		UserService usv2 = new UserService(client);
		int page = 1;
		List<SearchUser> users = new ArrayList<SearchUser>();
		users = usv2.searchUsers("created:[NOW-5HOURS TO NOW]", 1);
		for (SearchUser u : users) {
			System.out.println(u.getLogin());
		}
		System.out.println("size: " + users.size());
		*/
		/*
		int count = 0;
		GistService gs = new GistService();
		PageIterator<Gist> gists = gs.pagePublicGists(1, 1000);
		while (gists.hasNext()) {
			Collection<Gist> gistList = gists.next();
			for (Gist g : gistList) {
				System.out.println(g.getId());
				count++;
			}
		}
		System.out.println("count: " + count);
		*/
		
		GitHubClient client = new GitHubClient();
		IssueService is = new IssueService(client);
		Issue i = is.getIssue("rails", "rails", 4336);
		System.out.println(i.getId());
	}
}
