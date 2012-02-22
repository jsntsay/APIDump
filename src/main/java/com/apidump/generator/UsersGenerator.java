package com.apidump.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.service.WatcherService;

import com.apidump.APIDump;
import com.apidump.models.Users;

/**
 * Use this to generate a Users model
 * Includes organziation data
 */
public class UsersGenerator {
	
   private static UsersGenerator instance = null;
   
   private static UserService service = new UserService(APIDump.getClient());
   private static WatcherService watcherService = new WatcherService(APIDump.getClient());
   private static RepositoryService repoService = new RepositoryService(APIDump.getClient());
   private static OrganizationService orgService = new OrganizationService(APIDump.getClient());
   
   protected UsersGenerator() {

   }
   
   public static UsersGenerator getInstance() {
      if(instance == null) {
         instance = new UsersGenerator();
      }
      return instance;
   }
	
	private ConcurrentHashMap<String, Users> cache = new ConcurrentHashMap<String, Users>();
	
	public Users getUsers(String login) throws RequestException, IOException {
		if (login == null)
			return null;
		
		if (cache.containsKey(login)) {
			return cache.get(login);
		}
		
		//System.out.println("users: " + login);
		
		Users users = getUsersFromService(login);
		
		cache.put(login, users);
		return users;
	}
	
	// Returns a list of user ids as the model just stores ids.
	public List<Integer> getFollowers(String login) throws IOException {
		return getUserIds(service.getFollowers(login));
	}
	
	// Returns a list of user ids as the model just stores ids.
	public List<Integer> getFollowing(String login) throws IOException {
		return getUserIds(service.getFollowing(login));
	}
	
	// Returns a list of repo ids as the model just stores ids.
	public List<Long> getWatching(String login) throws IOException {
		return getRepoIds(watcherService.getWatched(login));
	}
	
	// Returns a list of repo ids as the model just stores ids.
	public List<Long> getRepositories(String login) throws IOException {
		return getRepoIds(repoService.getRepositories(login));
	}
	
	public List<Users> getOrgMembers(String login) throws RequestException, IOException {
		if (login == null)
			return null;
		
		Users u = null;
		
		if (cache.containsKey(login)) {
			u = cache.get(login);
			if (!u.isOrganziation())
				return null;
			List<Users> memberList = u.getMemberList();
			if (memberList != null)
				return memberList;
		}
		
		List<Users> memberList = getMembersFromService(login);
		
		if (u != null) {
			u.setMemberList(memberList);
			cache.put(login, u);
		}
		
		return memberList;
	}
	
	private List<Integer> getUserIds(List<User> userList) {
		List<Integer> results = new ArrayList<Integer>();
		
		if (userList == null || userList.isEmpty())
			return null;
		for (User u : userList) {
			results.add(u.getId());
		}
		return results;
	}
	
	private List<Long> getRepoIds(List<Repository> repoList) {
		List<Long> results = new ArrayList<Long>();
		
		if (repoList == null || repoList.isEmpty())
			return null;
		for (Repository r : repoList) {
			results.add(r.getId());
		}
		return results;
	}
	
	// Dunno if services are threadsafe, if so, don't need to do this
	private synchronized Users getUsersFromService(String login) throws RequestException, IOException {
		User u = service.getUser(login);
		return new Users(u);
	}
	
	// Dunno if services are threadsafe, if so, don't need to do this
	private synchronized List<Users> getMembersFromService(String login) throws RequestException, IOException {
		List<User> userList = orgService.getMembers(login);
		if (userList == null || userList.isEmpty())
			return null;
		
		List<Users> results = new ArrayList<Users>();
		for (User u : userList)
			results.add(getUsers(u.getLogin()));
		return results;
	}
}
