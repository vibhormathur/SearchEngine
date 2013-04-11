package ir.assignments.four.storage;

import java.io.File;
import java.io.IOException;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class LinkDataStorage {
	private RecordManager database;
	private PrimaryHashMap<String, DocumentLinkData> databaseMap;
	private long storedCount;

	public LinkDataStorage(String storagePath) {
		try {
			// Make sure storage directory exists
			new File(new File(storagePath).getParent()).mkdir();
			
			// Initialize a file-based hash map (using jdbm2 [https://code.google.com/p/jdbm2/]) to store the visited pages
			this.database = RecordManagerFactory.createRecordManager(storagePath);
			this.databaseMap = this.database.hashMap("linkDataStorage"); // "table" name
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void putDocumentLinkData(DocumentLinkData linkData) {
		this.databaseMap.put(linkData.url, linkData);
		storedCount++;
		
		// Commit after every 50 pages are added to release memory and save to disk (in case crawler is stopped)
		if (storedCount % 20000 == 0) {
			try {
				this.database.commit();
				System.out.println("Committed " + storedCount);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public DocumentLinkData getDocumentLinkData(String url) {
		return this.databaseMap.get(url);
	}

	public Iterable<String> getCrawledUrls() {
		// The keys are all URLs
		return this.databaseMap.keySet();
	}

	public Iterable<DocumentLinkData> getAll() {
		return this.databaseMap.values();
	}
	
	public int getSize() {
		return this.databaseMap.size();
	}

	public void close() {
		try {
			this.database.commit(); // commit any leftover
			System.out.println("Committed " + storedCount);
			this.database.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
