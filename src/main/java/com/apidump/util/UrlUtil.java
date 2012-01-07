package com.apidump.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlUtil {

	/**
	 * Takes a html_url field and returns a list of Strings in order of:
	 * repo owner
	 * repo name
	 * other field (usually the id)
	 * 
	 * @param word in url to ignore (i.e. pull/issues/etc)
	 */
	public static List<String> parseUrl(URL url, String ignoreWord) {
		if (url == null)
			return null;
		
		String owner = null;
		String name = null;
		String other = null;
		for (String segment : url.getPath().split("/")) //$NON-NLS-1$
			if (segment.length() > 0)
				if (segment.equals(ignoreWord))
					continue;
				else if (owner == null)
					owner = segment;
				else if (name == null)
					name = segment;
				else if (other == null)
					other = segment;
				else
					break;
		
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
	
	public static List<String> parseUrl(String url, String ignoreWord) {
		if (url == null || url.length() == 0)
			return null;
		try {
			return parseUrl(new URL(url), ignoreWord);
		} catch (MalformedURLException e) {
			return null;
		}
	}
}
