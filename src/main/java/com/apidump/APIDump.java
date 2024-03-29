package com.apidump;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.NoSuchPageException;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.service.EventService;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import com.apidump.generator.RepositoriesGenerator;
import com.apidump.models.Repositories;

//TODO: use workers to hit api/dump into db
//TODO: concurrently dump everything
//TODO: api rate limiting and retry on 403
//TODO: logging (log4j or stdout)
//TODO: not sure if naive caching method can handle dumping *everything*
//TODO: relational tables like repo_issue, etc don't have PKs, is that bad?

//TODO: commits for repos
//TODO: handle 403, 404, 502
//TODO: replace user and password with token

public class APIDump {
	private static final String PERSISTENCE_UNIT_NAME = "apidump";
	private static EntityManagerFactory factory;

	private static final String API_USER = System.getenv("APIUSER");
	private static final String API_PASS = System.getenv("APIPASS");
	private static GitHubClient client = new GitHubClient();

	public static GitHubClient getClient() {
		return client;
	}

	// Generate an event list from API.
	private static List<Event> populateEventList() throws IOException {
		List<Event> eventList = new ArrayList<Event>();
		EventService es = new EventService(client);
		PageIterator<Event> pages = es.pagePublicEvents();
		while (pages.hasNext()) {
			eventList.addAll(pages.next());
		}
		return eventList;
	}
	
	// Generate a gist list from API.
	private static List<Gist> populateGistList() throws IOException {
		List<Gist> gistList = new ArrayList<Gist>();
		GistService gs = new GistService(client);
		PageIterator<Gist> pages = gs.pagePublicGists();
		int count = 0;
		while (pages.hasNext()) {
			//gistList.addAll(pages.next());
			for (Gist g : pages.next()) {
				gistList.add(gs.getGist(g.getId()));
			}
			count++;
			if (count >= 1)
				return gistList;
		}
		return gistList;
	}
	
	// Generate a user list from API.
	private static List<User> populateUserList() throws IOException {
		List<User> userList = new ArrayList<User>();
		UserService us = new UserService(client);
		for (User u : us.getFollowers("jtsay")) {
			userList.add(us.getUser(u.getLogin()));
		}
		return userList;
	}
	
	private static List<SearchRepository> populateRepositoryList() throws IOException {
		GitHubClient client = new GitHubClient("github.com");
		if (API_USER != null && API_PASS != null)
			client.setCredentials(API_USER, API_PASS);
		
		RepositoryService rsv2 = new RepositoryService(client);
		int page = 1;
		List<SearchRepository> repos = new ArrayList<SearchRepository>();
		while (true)
		{
			List<SearchRepository> results = rsv2.searchRepositories("created:[NOW-1MONTH TO NOW]", page);
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
		return repos;
	}
	
	private static List<Comment> getGistCommentList(Gist g) throws IOException {
		List<Comment> commentList = new ArrayList<Comment>();
		GistService gs = new GistService(client);
		commentList.addAll(gs.getComments(g.getId()));
		return commentList;
	}
	
	/**
	 * Uses v2 API to get all repositories
	 * Returned SearchRepository does not include all info about a repository
	 * Use RepositoriesGenerator to generate full Repositories model
	 * @return list of SearchRepository
	 * @throws IOException
	 */
	private static List<SearchRepository> getAllRepositories() throws IOException {
		GitHubClient client = new GitHubClient("github.com");
		if (API_USER != null && API_PASS != null)
			client.setCredentials(API_USER, API_PASS);
		
		RepositoryService rsv2 = new RepositoryService(client);
		int page = 1;
		List<SearchRepository> repos = new ArrayList<SearchRepository>();
		while (true)
		{
			List<SearchRepository> results = rsv2.searchRepositories("fork:true", page);
			if (results.size() == 0)
				break;
			repos.addAll(results);
			System.out.println("true page: " + page + " added");
			page++;
		}
		// Heap died a painful death, split this up
		page = 1;
		while (true)
		{
			List<SearchRepository> results = rsv2.searchRepositories("fork:false", page);
			if (results.size() == 0)
				break;
			repos.addAll(results);
			System.out.println("false page: " + page + " added");
			page++;
		}
		for (SearchRepository s : repos) {
			System.out.println(s.generateId());
		}
		System.out.println("size: " + repos.size());
		return repos;
	}
	
	/**
	 * Uses APIv3 to get list of all Gists
	 * List returned does not include all Gist information
	 * Use GistsGenerator to generate complete Gists model
	 * @return list of Gist
	 */
	private static List<Gist> getAllGists() {
		GistService gs = new GistService(client);
		PageIterator<Gist> gists = gs.pagePublicGists(1, 100);
		List<Gist> gistList = new ArrayList<Gist>();
		int page = 1;
		int count = 0;
		try {
			while (gists.hasNext()) {
				Collection<Gist> gistPage = gists.next();
				gistList.addAll(gistPage);
				System.out.println("page: " + page++);
				count += gistPage.size();
				System.out.println("current size: " + count);
			}
		} catch (NoSuchPageException e) {
			e.printStackTrace();
		}
		return gistList;
	}

	@SuppressWarnings("rawtypes")
	public static void runDump() throws IOException {
		Map properties = Properties.getProperties();
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
		EntityManager em = factory.createEntityManager();
		if (API_USER != null && API_PASS != null)
			client.setCredentials(API_USER, API_PASS);
		
		System.out.println("Starting API Dump");
		
		// Extract user model classes from API
		//List<Event> eventList = populateEventList();
		//List<Gist> gistList = populateGistList();
		//List<Gist> gistList = getAllGists();
		//List<SearchRepository> repoList = getAllRepositories();
		//List<User> userList = populateUserList();
		List<SearchRepository> repoList = populateRepositoryList();

		//for (Event e : eventList)
		//for (Gist g : gistList)
		//for (User u : userList)
		for (SearchRepository r : repoList)
		{
			
			try {
				em.getTransaction().begin();
				//Events ev = new Events(e);
				//Gists gi = GistsGenerator.getInstance().getGists(g.getId());
				//Users us = new Users(u);
				//Repositories re = new Repositories(r);
				Repositories re = RepositoriesGenerator.getInstance().getRepositories(r.getOwner(), r.getName());
				
				//em.merge(ev);
				//em.merge(gi);
				//em.merge(us);
				em.merge(re);
				
			} catch (RequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				if (e1.getStatus() == 404) {
					System.out.println("404 error!");
					// continue;
				} else if (e1.getStatus() == 403) {
					System.out.println("403 error!");
					// need to delay and retry or something
				} else if (e1.getStatus() == 410) {
					System.out.println("410 error!");
					// treat this like 404
				}
			} finally {
			
				em.flush();
				
				if (em.getTransaction().getRollbackOnly())
					em.getTransaction().rollback();
				else
					em.getTransaction().commit();
			}
			
		}

		em.close();
		
		System.out.println("API Dump complete");
	}
	
	public static void main(String[] args) throws IOException {
		runDump();
	}
}
