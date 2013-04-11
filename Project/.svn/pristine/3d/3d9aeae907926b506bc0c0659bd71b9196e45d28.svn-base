package ir.assignments.three.storage;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MemoryDocumentStorage implements IDocumentStorage {
	private HashSet<HtmlDocument> urlPageDictionary = new HashSet<HtmlDocument>();

	public void storeDocument(String url, String text) {
		this.urlPageDictionary.add(new HtmlDocument(url, text));
	}

	public Iterable<HtmlDocument> getAll() {
		return urlPageDictionary;
	}

	public void close() {
		this.urlPageDictionary = null;
	}
	
	public List<String> getCrawledUrls() {
		ArrayList<String> urls = new ArrayList<String>();
		for (HtmlDocument doc : this.urlPageDictionary) {
			urls.add(doc.getUrl());
		}
		
		return urls;
	}
}
