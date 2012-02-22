package com.apidump.generator;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.UserService;

import com.apidump.APIDump;
import com.apidump.models.Users;

/**
 * Use this to generate a Users model
 */
public class UsersGenerator {
	
   private static UsersGenerator instance = null;
   
   private static UserService service = new UserService(APIDump.getClient());
   
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
	
	// Dunno if services are threadsafe, if so, don't need to do this
	private synchronized Users getUsersFromService(String login) throws RequestException, IOException {
		User u = service.getUser(login);
		return new Users(u);
	}
}
