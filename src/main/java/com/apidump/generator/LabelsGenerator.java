package com.apidump.generator;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.client.RequestException;

import com.apidump.models.Labels;

public class LabelsGenerator {

	private static LabelsGenerator instance = null;
	
	protected LabelsGenerator() {
		
	}
   
	public static LabelsGenerator getInstance() {
		if(instance == null) {
			instance = new LabelsGenerator();
			}
		return instance;
		}

	private ConcurrentHashMap<String, Labels> cache = new ConcurrentHashMap<String, Labels>();
	
	public Labels getLabels(Label label) throws RequestException, IOException {
		if (label == null)
			return null;
		String url = label.getUrl();
		if (cache.containsKey(url))
			return cache.get(url);
		
		//System.out.println("label: " + url);
		
		Labels l = new Labels(label);
		cache.put(url, l);
		return l;
	}
}
