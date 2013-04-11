package ir.assignments.four;

import ir.assignments.four.storage.DocumentLinkData;
import ir.assignments.four.storage.DocumentStorage;
import ir.assignments.four.storage.HtmlDocument;
import ir.assignments.four.storage.LinkDataStorage;

import java.util.HashMap;

/*
 * Analyzes the crawled documents for pages' incoming anchor text
 */
public class GraphAnalyzer {

	public static void main(String[] args) {
		GraphAnalyzer analyzer = new GraphAnalyzer();
		try {
			analyzer.runAnchorTextAnalysis();
		}
		finally {
			analyzer.close();
		}
	}

	private DocumentStorage docStorage;
	private LinkDataStorage linkDataStorage;

	public GraphAnalyzer() {
		this.docStorage = new DocumentStorage("docStorage\\docStorage");
		this.linkDataStorage = new LinkDataStorage("linkDataStorage\\linkDataStorage");
	}

	public void runAnchorTextAnalysis() {

		int i = 0;
		for (HtmlDocument crawledDoc : docStorage.getAll()) {

			if (i % 100 == 0)
				System.out.println(i + "");

			i++;

			// Propagate the current documents anchor text to the corresponding link
			HashMap<String, String> outgoingLinks = crawledDoc.getOutgoingLinks();			
			for (String outgoingUrl : outgoingLinks.keySet()) {
				// Only worry about links to pages that were crawled
				if (!checkDocStorage.check(outgoingUrl))
					continue;
				
				// Either update or create the entry for the outgoing URL
				DocumentLinkData outgoingLinkData = linkDataStorage.getDocumentLinkData(outgoingUrl);
				if (outgoingLinkData == null)
					outgoingLinkData = new DocumentLinkData();

				outgoingLinkData.url = outgoingUrl;
				if (outgoingLinkData.anchorText == null)
					outgoingLinkData.anchorText = outgoingLinks.get(outgoingUrl);
				else
					outgoingLinkData.anchorText += " " + outgoingLinks.get(outgoingUrl);

				linkDataStorage.putDocumentLinkData(outgoingLinkData);
			}
		}

		System.out.println("Done");
	}

	public void close() {
		if (this.docStorage != null)
			docStorage.close();

		if (this.linkDataStorage != null)
			linkDataStorage.close();
	}
}
