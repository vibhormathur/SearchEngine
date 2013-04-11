package ir.assignments.three.tests;

import static org.junit.Assert.*;
import ir.assignments.three.*;
import ir.assignments.three.helpers.TestUtils;
import ir.assignments.three.stats.DocumentStatistics;
import ir.assignments.three.stats.Frequency;
import ir.assignments.three.stats.UrlStatistics;
import ir.assignments.three.storage.DocumentStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class IntegrationTest {
	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void testCrawl() {
		Collection<String> crawledUrls = new ArrayList<String>();
		DocumentStorage documentStorage = null;
		
		try {
			// Parameters (use temporary folders to storage)
			String seedURL = "http://www.vcskicks.com";
			String intermediateStoragePath = tmpFolder.newFolder().getAbsolutePath();
			String documentStoragePath = tmpFolder.newFile().getAbsolutePath();
			int maxDepth = 1;
			int maxPages = -1; //unlimited

			documentStorage = new DocumentStorage(documentStoragePath);
			crawledUrls = Crawler.crawl(seedURL, intermediateStoragePath, documentStorage, maxDepth, maxPages);
		}
		catch (IOException ex) {
			fail("IOException: " + ex.getMessage());
		}
		
		try {
			assertTrue(crawledUrls.size() >= 17);
			
			// Analyze URLs first (Questions 2-3)
			UrlStatistics urlStats = new UrlStatistics();
			urlStats.runStats(documentStorage);
			
			//-- Results
			assertTrue(urlStats.getTotalPages() >= 17);	
			assertTrue(urlStats.getTotalUniquePages() >= 17 && urlStats.getTotalUniquePages() <= 25);
			
			ArrayList<Frequency> expectedSubdomains = new ArrayList<Frequency>();
			expectedSubdomains.add(new Frequency("http://www.vcskicks.com", (int)urlStats.getTotalUniquePages()));
			TestUtils.compareFrequencyLists(expectedSubdomains, urlStats.getSubdomainFrequencies());
			
			// Analyze document content (Questions 4-6)
			DocumentStatistics docStats = new DocumentStatistics();
			docStats.runStats(documentStorage);
			
			//-- Results
			assertEquals("http://www.vcskicks.com/csharp-programming.php", docStats.getLongestDocumentUrl());
			assertTrue(docStats.getMostCommonWords().size() >= 20 && docStats.getMostCommonWords().size() <= 500);
			assertTrue(docStats.getMostCommonTwoGrams().size() >= 10 && docStats.getMostCommonTwoGrams().size() <= 20);			
		}
		finally {
			if (documentStorage != null)
				documentStorage.close();
		}
	}
}
