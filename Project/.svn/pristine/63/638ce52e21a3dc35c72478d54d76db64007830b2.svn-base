package ir.assignments.three.tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import ir.assignments.three.stats.DocumentStatistics;
import ir.assignments.three.stats.UrlStatistics;
import ir.assignments.three.storage.MemoryDocumentStorage;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DocumentStatisticsTest {

	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	private DocumentStatistics docStats;

	@Before
	public void setup() {
		docStats = new DocumentStatistics();
	}

	@Test
	public void testGetLongestPage() {
		try {
			MemoryDocumentStorage docStorage = getTestDocumentStorage();
			docStats.runStats(docStorage, tmpFolder.newFile().getAbsolutePath(), tmpFolder.newFile().getAbsolutePath());			

			String actualLongestPageUrl = docStats.getLongestDocumentUrl();
			String expectedLongestPageUrl = "http://www.fake.com/page3.php";

			assertEquals(expectedLongestPageUrl, actualLongestPageUrl);
		}
		catch (Exception e) {
			fail("Error: " + e.getMessage());
		}
	}

	@Test
	public void testGetMostCommonWords() {
		try {
			MemoryDocumentStorage docStorage = getTestDocumentStorage();
			docStats.runStats(docStorage, tmpFolder.newFile().getAbsolutePath(), tmpFolder.newFile().getAbsolutePath());

			String[] actualMostCommonWords = toStringArray(docStats.getMostCommonWords());
			String[] expectedMostCommonWords = new String[] { "word", "page", "test", "another", "three" }; // Order matters (sorted by freq)

			assertArrayEquals(expectedMostCommonWords, actualMostCommonWords);
		}
		catch (IOException e) {
			fail("Error: " + e.getMessage());
		}
	}

	@Test
	public void testGetMostCommon2Grams() {
		try {
			MemoryDocumentStorage docStorage = getTestDocumentStorage();
			docStats.runStats(docStorage, tmpFolder.newFile().getAbsolutePath(), tmpFolder.newFile().getAbsolutePath());

			String[] actualMostCommon2Grams = toStringArray(docStats.getMostCommonTwoGrams());
			String[] expectedMostCommon2Grams = new String[] { "word word", "test page", "another test", "page word" }; // Order matters (sorted by freq)

			assertArrayEquals(expectedMostCommon2Grams, actualMostCommon2Grams);
		}
		catch (IOException e) {
			fail("Error: " + e.getMessage());
		}
	}

	private MemoryDocumentStorage getTestDocumentStorage() {
		MemoryDocumentStorage docStorage = new MemoryDocumentStorage();
		docStorage.storeDocument("http://www.fake.com/page1.php", toHtml("Test Page", "This is a test page")); // Stop words
		docStorage.storeDocument("http://www.fake.com/page2.php", toHtml("Another Test Page", "One two one two One three")); // 2-grams 
		docStorage.storeDocument("http://www.fake.com/page3.php", toHtml("Yet Another Test Page", "Word word word word word word")); // high frequency word 

		return docStorage;
	}

	private String toHtml(String title, String body) {
		return "<html><title>" + title + "</title><body>" + body + "</body></html>";
	}

	private String[] toStringArray(List<String> elements) {
		return elements.toArray(new String[elements.size()]);
	}
}
