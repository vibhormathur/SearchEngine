package ir.assignments.four;

import ir.assignments.four.storage.DocumentStorage;

public class checkDocStorage {
	public static Boolean check(String targetUrl){
		// Check if the URL was crawled
		DocumentStorage docStorage = null; 
		
		try {
			docStorage = new DocumentStorage("docStorage\\docStorage");			
			return docStorage.getDocument(targetUrl) != null;
		}
		finally {
			if (docStorage != null)
				docStorage.close();
		}
	}
}
