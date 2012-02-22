package com.apidump.generator;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.RepositoryService;

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
	
	// Dunno if services are threadsafe, if so, don't need to do this
	private synchronized Repositories getReposFromService(RepositoryId id) throws RequestException, IOException {
		Repository r = service.getRepository(id);
		return new Repositories(r);
	}
}
