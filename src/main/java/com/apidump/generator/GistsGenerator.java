package com.apidump.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.GistService;

import com.apidump.APIDump;
import com.apidump.models.Comments;
import com.apidump.models.Gists;

/**
 * Use this to generate Gists model or comments
 */
public class GistsGenerator {

	private static GistsGenerator instance = null;

	private static GistService service = new GistService(APIDump.getClient());

	protected GistsGenerator() {

	}

	public static GistsGenerator getInstance() {
		if (instance == null) {
			instance = new GistsGenerator();
		}
		return instance;
	}

	private ConcurrentHashMap<String, Gists> cache = new ConcurrentHashMap<String, Gists>();

	public Gists getGists(String id) throws RequestException, IOException {
		if (id == null)
			return null;

		if (cache.containsKey(id)) {
			return cache.get(id);
		}

		// System.out.println("Gists: " + login);

		Gists Gists = getGistsFromService(id);

		cache.put(id, Gists);
		return Gists;
	}

	// Dunno if services are threadsafe, if so, don't need to do this
	private synchronized Gists getGistsFromService(String id)
			throws RequestException, IOException {
		Gist u = service.getGist(id);
		return new Gists(u);
	}
	
	public List<Comments> getComments(String id) throws RequestException, IOException {
		if (id == null)
			return null;

		Gists g = null;
		
		if (cache.containsKey(id)) {
			g = cache.get(id);
			List<Comments> commentsList = g.getCommentsList();
			if (commentsList != null)
				return commentsList;
		}

		// System.out.println("Gists: " + login);

		List<Comments> commentsList = getCommentsFromService(id);

		if (g != null) {
			g.setCommentsList(commentsList);
			cache.put(id, g);
		}
		
		return commentsList;
	}
	
	// Dunno if services are threadsafe, if so, don't need to do this
	private synchronized List<Comments> getCommentsFromService(String id)
			throws RequestException, IOException {
		List<Comment> comments = service.getComments(id);
		if (comments == null || comments.isEmpty())
			return null;
		List<Comments> results = new ArrayList<Comments>();
		for (Comment c : comments)
			results.add(new Comments(c));
		return results;
	}
}
