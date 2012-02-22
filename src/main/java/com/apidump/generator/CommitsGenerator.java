package com.apidump.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.CommitService;

import com.apidump.APIDump;
import com.apidump.models.CommitComments;
import com.apidump.models.Commits;
import com.apidump.models.RepositoryCommits;
import com.apidump.util.UrlUtil;

public class CommitsGenerator {
	
	private static CommitsGenerator instance = null;
	
	protected CommitsGenerator() {
		
	}

	public static CommitsGenerator getInstance() {
		if(instance == null) {
			instance = new CommitsGenerator();
		}
		return instance;
	}

	private ConcurrentHashMap<String, Commits> cache = new ConcurrentHashMap<String, Commits>();
	
	private CommitService service = new CommitService(APIDump.getClient());
	
	// No service for Commit lookup, so just pass it in.
	public Commits getCommits(Commit c) {
		if (c == null)
			return null;
		String sha = c.getSha();
		// RepositoryCommits.commit doesn't seem to have a sha
		if (sha == null) {
			if (c.getUrl() == null)
				return null;
			else
				sha = UrlUtil.parseUrl(c.getUrl(), 2, 3, 6).get(2);
		}
		if (cache.containsKey(sha)) {
			return cache.get(sha);
		}
		
		Commits commits = new Commits(c);
		cache.put(sha, commits);
		return commits;
	}
	
	public List<RepositoryCommits> getAllRepositoryCommits(IRepositoryIdProvider repoId) {
		if (repoId == null)
			return null;
		
		List<RepositoryCommit> commits = getAllCommitsFromService(repoId);
		
		if (commits == null || commits.size() == 0)
			return null;
		
		List<RepositoryCommits> rcList = new ArrayList<RepositoryCommits>();
		for (RepositoryCommit commit : commits) {
			RepositoryCommits rc = getRepositoryCommits(repoId, commit.getSha());
			rcList.add(rc);
			
			System.out.println(rc.getSha());
		}
		
		return rcList;
	}
	
	public RepositoryCommits getRepositoryCommits(IRepositoryIdProvider repoId, String sha) {
		if (repoId == null || sha == null)
			return null;
		
		RepositoryCommits rc = getCommitsFromService(repoId, sha);
		
		return rc;
	}
	
	public List<CommitComments> getCommitComments(IRepositoryIdProvider repoId, String sha) throws IOException {
		if (repoId == null || sha == null)
			return null;
		
		List<CommitComment> comments = getCommentsFromService(repoId, sha);
		
		if (comments == null || comments.size() == 0)
			return null;
		
		List<CommitComments> cList = new ArrayList<CommitComments>();
		for (CommitComment comment : comments) {
			cList.add(new CommitComments(comment));
		}
		
		return cList;
	}
	
	// Meant for RepositoryCommits.url
	public List<CommitComments> getCommitComments(String url) throws IOException {
		if (url == null || url.length() == 0)
			return null;
		
		List<String> fields = UrlUtil.parseApiUrl(url);
		IRepositoryIdProvider repoId = RepositoryId.create(fields.get(0), fields.get(1));
		return getCommitComments(repoId, fields.get(2));
	}
	
	private List<RepositoryCommit> getAllCommitsFromService(IRepositoryIdProvider repoId) {
		while (true) {
			try {
				List<RepositoryCommit> rcList = service.getCommits(repoId);
				return rcList;
			} catch (RequestException e) {
				if (e.getStatus() == 404) {
					System.out.println("Commits not found: " + repoId.generateId());
					return null;
				} else if (e.getStatus() == 403) {
					try {
						System.out.println(e.getMessage() + " delay for 60s and retry");
						Thread.sleep(60000);
					} catch (InterruptedException e1) {
						// Should continue if interrupted (?).
					}
				} else {
					System.out.println(e.getMessage() + " for " + repoId.generateId());
					return null;
				}
			}
			catch (IOException e) {
				System.out.println(e.getMessage() + " for " + repoId.generateId());
				return null;
			}
		}
	}
	
	private RepositoryCommits getCommitsFromService(IRepositoryIdProvider repoId, String sha) {
		while (true) {
			try {
				RepositoryCommit c = service.getCommit(repoId, sha);
				return new RepositoryCommits(c);
			} catch (RequestException e) {
				if (e.getStatus() == 404) {
					System.out.println("Commit not found: " + sha + " in " + repoId.generateId());
					return null;
				} else if (e.getStatus() == 403) {
					try {
						System.out.println(e.getMessage() + " delay for 60s and retry");
						Thread.sleep(60000);
					} catch (InterruptedException e1) {
						// Should continue if interrupted (?).
					}
				} else {
					System.out.println(e.getMessage() + " for " + sha + " in " + repoId.generateId());
					return null;
				}
			}
			catch (IOException e) {
				System.out.println(e.getMessage() + " for " + sha + " in " + repoId.generateId());
				return null;
			}
		}
	}
	
	private List<CommitComment> getCommentsFromService(IRepositoryIdProvider repoId, String sha) {
		while (true) {
			try {
				List<CommitComment> comments = service.getComments(repoId, sha);
				return comments;
			} catch (RequestException e) {
				if (e.getStatus() == 404) {
					System.out.println("Comments not found: " + sha + " in " + repoId.generateId());
					return null;
				} else if (e.getStatus() == 403) {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e1) {
						// Should continue if interrupted (?).
					}
				} else {
					System.out.println(e.getMessage() + " for " + sha + " in " + repoId.generateId());
					return null;
				}
			}
			catch (IOException e) {
				System.out.println(e.getMessage() + " for " + sha + " in " + repoId.generateId());
				return null;
			}
		}
	}
}
