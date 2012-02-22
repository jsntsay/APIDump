package com.apidump.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlUtil {

	private static final int DEFAULT_OWNER_INDEX = 1;
	private static final int DEFAULT_NAME_INDEX = 2;
	private static final int DEFAULT_OTHER_INDEX = 4;
	
	private static final int DEFAULT_API_OWNER_INDEX = 2;
	private static final int DEFAULT_API_NAME_INDEX = 3;
	private static final int DEFAULT_API_OTHER_INDEX = 5;
	
	/**
	 * Takes a url field and returns a list of Strings in order of:
	 * repo owner
	 * repo name
	 * other field (usually the id)
	 * 
	 * @param index of segments in url to for fields to return
	 * (i.e. repos/octocat/hello-world/commits/sha - 1 returns owner, 2 returns name, 4 returns other)
	 */
	public static List<String> parseUrl(URL url, int ownerIndex, int nameIndex, int otherIndex) {
		if (url == null)
			return null;
		
		String owner = null;
		String name = null;
		String other = null;
		String[] segments = url.getPath().split("/"); //$NON-NLS-1$
		if (segments[ownerIndex].length() > 0)
			owner = segments[ownerIndex];
		if (segments[nameIndex].length() > 0)
			name = segments[nameIndex];
		if (segments[otherIndex].length() > 0)
			other = segments[otherIndex];
		
		if (owner == null || owner.length() == 0 ||  
				name == null || name.length() == 0 || 
				other == null || other.length() == 0)
			return null;
		
		List<String> results = new ArrayList<String>();
		results.add(owner);
		results.add(name);
		results.add(other);
		return results;
	}
	
	public static List<String> parseUrl(String url, int ownerIndex, int nameIndex, int otherIndex) {
		if (url == null || url.length() == 0)
			return null;
		try {
			return parseUrl(new URL(url), ownerIndex, nameIndex, otherIndex);
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	/**
	 * Takes a url field and returns a list of 
	 *  Strings in order of:
	 * repo owner
	 * repo name
	 * other field (usually the id)
	 * 
	 * assumes segments are owner = 0, name = 1, other = 3 in the path
	 */
	public static List<String> parseUrl(String url) {
		return parseUrl(url, DEFAULT_OWNER_INDEX, DEFAULT_NAME_INDEX, DEFAULT_OTHER_INDEX);
	}
	
	/**
	 * Takes a url field in form of api.github.com and returns a list of 
	 *  Strings in order of:
	 * repo owner
	 * repo name
	 * other field (usually the id)
	 * 
	 * assumes segments are owner = 1, name = 2, other = 4 in the path
	 */
	public static List<String> parseApiUrl(String url) {
		return parseUrl(url, DEFAULT_API_OWNER_INDEX, DEFAULT_API_NAME_INDEX, DEFAULT_API_OTHER_INDEX);
	}
}
