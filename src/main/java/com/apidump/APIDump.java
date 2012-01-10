package com.apidump;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.service.EventService;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import com.apidump.models.Repositories;

//TODO: implement workers/producer&consumer system
//TODO: api rate limiting and retry on 403
//TODO: logging (log4j)

public class APIDump {
	private static final String PERSISTENCE_UNIT_NAME = "apidump";
	private static EntityManagerFactory factory;
	
	// Generate an event list from API.
	private static List<Event> populateEventList() throws IOException {
		List<Event> eventList = new ArrayList<Event>();
		EventService es = new EventService();
		PageIterator<Event> pages = es.pagePublicEvents();
		while (pages.hasNext()) {
			eventList.addAll(pages.next());
		}
		return eventList;
	}
	
	// Generate a gist list from API.
	private static List<Gist> populateGistList() throws IOException {
		List<Gist> gistList = new ArrayList<Gist>();
		GistService gs = new GistService();
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
		UserService us = new UserService();
		for (User u : us.getFollowers("jtsay")) {
			userList.add(us.getUser(u.getLogin()));
		}
		return userList;
	}
	
	private static List<Repository> populateRepositoryList() throws IOException {
		List<Repository> repoList = new ArrayList<Repository>();
		RepositoryService rs = new RepositoryService();
		//repoList.add(rs.getRepository("mootools", "mootools-core"));
		repoList.add(rs.getRepository("octocat", "hello-world"));
		return repoList;
	}
	
	private static List<Comment> getGistCommentList(Gist g) throws IOException {
		List<Comment> commentList = new ArrayList<Comment>();
		GistService gs = new GistService();
		commentList.addAll(gs.getComments(g.getId()));
		return commentList;
	}

	@SuppressWarnings("rawtypes")
	public static void runDump() throws IOException {
		Map properties = Properties.getProperties();
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
		EntityManager em = factory.createEntityManager();
		
		// Extract user model classes from API
		//List<Event> eventList = populateEventList();
		//List<Gist> gistList = populateGistList();
		//List<User> userList = populateUserList();
		List<Repository> repoList = populateRepositoryList();

		//for (Event e : eventList)
		//for (Gist g : gistList)
		//for (User u : userList)
		for (Repository r : repoList)
		{
			
			try {
				em.getTransaction().begin();
				//Events ev = new Events(e);
				//Gists gi = new Gists(g);
				//Users us = new Users(u);
				Repositories re = new Repositories(r);
				
				//em.merge(ev);
				//em.merge(gi);
				//em.merge(us);
				em.merge(re);
				
			} catch (RequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				if (e1.getStatus() == 404) {
					// continue;
				} else if (e1.getStatus() == 403) {
					// need to delay and retry or something
				}
			} finally {
			
				em.flush();
				
				if (em.getTransaction().getRollbackOnly())
					em.getTransaction().rollback();
				else
					em.getTransaction().commit();
			}
			
		}
		
		/*
		for (APIModel m : modelList)
			em.persist(m);
			*/
		
		//System.out.println("flushing to database");

		em.close();
		
		System.out.println("API Dump complete");
	}
}
