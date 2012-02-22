package com.apidump.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.Contributor;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.WatcherService;

import com.apidump.APIDump;
import com.apidump.models.Repositories;

/**
 * Use this to generate a Repositories model
 */
public class RepositoriesGenerator {
	
   private static RepositoriesGenerator instance = null;
   
   protected RepositoriesGenerator() {

   }
   
   public static RepositoriesGenerator getInstance() {
      if(instance == null) {
         instance = new RepositoriesGenerator();
      }
      return instance;
   }
	
	private ConcurrentHashMap<RepositoryId, Repositories> cache = new ConcurrentHashMap<RepositoryId, Repositories>();
	
	private RepositoryService service = new RepositoryService(APIDump.getClient());
	private WatcherService watcherService = new WatcherService(APIDump.getClient());
	private CollaboratorService collabService = new CollaboratorService(APIDump.getClient());
	
	public Repositories getRepositories(RepositoryId id) throws RequestException, IOException {
		if (id == null)
			return null;
		if (cache.containsKey(id)) {
			return cache.get(id);
		}
		
		//System.out.println("repos: " + id);

		Repositories repos = getReposFromService(id);
		// Assumes that even if the thing is replaced, it'll be with the same/equilavent model from the API
		cache.put(id, repos);
		return repos;
	}
	
	public Repositories getRepositories(String owner, String name) throws RequestException, IOException {
		return getRepositories(RepositoryId.create(owner, name));
	}
	
	public Repositories getRepositories(String id) throws RequestException, IOException {
		return getRepositories(RepositoryId.createFromId(id));
	}
	
	// Returns a list of repo ids since that's what the model uses to store forks.
	public List<Long> getForks(IRepositoryIdProvider repoId) throws IOException {
		List<Long> results = new ArrayList<Long>();
		
		List<Repository> forks = service.getForks(repoId);
		if (forks == null || forks.isEmpty())
			return null;
		for (Repository fr : forks) {
			results.add(fr.getId());
		}
		return results;
	}
	
	// Returns a list of user ids since that's what the model uses to store watchers
	public List<Integer> getWatchers(IRepositoryIdProvider repoId) throws IOException {
		List<Integer> results = new ArrayList<Integer>();
		
		List<User> watchers = watcherService.getWatchers(repoId);
		if (watchers == null || watchers.isEmpty())
			return null;
		for (User u : watchers) {
			results.add(u.getId());
		}
		return results;
	}
	
	// Returns a list of repo ids since that's what the model uses to store collaborators.
	public List<Integer> getCollaborators(IRepositoryIdProvider repoId) throws IOException {
		List<Integer> results = new ArrayList<Integer>();
		
		List<User> collab = collabService.getCollaborators(repoId);
		if (collab == null || collab.isEmpty())
			return null;
		for (User u : collab) {
			results.add(u.getId());
		}
		return results;
	}
	
	// Returns a list of repo ids since that's what the model uses to store contributors.
	// Excludes anonymous contributrors
	public List<Integer> getContributors(IRepositoryIdProvider repoId) throws IOException {
		List<Integer> results = new ArrayList<Integer>();
		
		List<Contributor> contrib = service.getContributors(repoId, false);
		if (contrib == null || contrib.isEmpty())
			return null;
		for (Contributor c : contrib) {
			results.add(c.getId());
		}
		return results;
	}
	
	// Dunno if services are threadsafe, if so, don't need to do this
	private synchronized Repositories getReposFromService(RepositoryId id) throws RequestException, IOException {
		Repository r = service.getRepository(id);
		return new Repositories(r);
	}
}
