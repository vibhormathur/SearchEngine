package ir.assignments.three;

import ir.assignments.three.storage.DocumentStorage;
import ir.assignments.three.storage.HtmlDocument;

public class MergeDocumentStorage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DocumentStorage documentStorage1 = new DocumentStorage("docStorage\\docStorage1"); //put smaller one here!
		DocumentStorage documentStorage2 = new DocumentStorage("docStorage\\docStorage2");
		
		try {			
			int i = 0;
			for (HtmlDocument doc : documentStorage1.getAll()) {
				if (i % 500 == 0)
					System.out.println(i + "");
				
				i++;				
				documentStorage2.storeDocument(doc.getUrl(), doc.getHtml());				
			}
		}
		finally {
			documentStorage1.close();
			documentStorage2.close();
		}
	}

}
