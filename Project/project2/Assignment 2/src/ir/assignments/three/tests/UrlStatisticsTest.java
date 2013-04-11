package ir.assignments.three.tests;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import ir.assignments.three.helpers.TestUtils;
import ir.assignments.three.stats.Frequency;
import ir.assignments.three.stats.UrlStatistics;
import ir.assignments.three.storage.MemoryDocumentStorage;

import org.junit.Before;
import org.junit.Test;

public class UrlStatisticsTest {
	private UrlStatistics urlStats;

	@Before
	public void setup() {
		urlStats = new UrlStatistics();
	}

	@Test
	public void testCountUniquePages() {
		MemoryDocumentStorage docStorage = new MemoryDocumentStorage();
		docStorage.storeDocument("http://www.example.com", "");
		docStorage.storeDocument("http://www.example.com/", "");
		docStorage.storeDocument("http://www.otherexample.com", "");

		urlStats.runStats(docStorage);
		long expectedCount = 3;
		long actualCount = urlStats.getTotalUniquePages();

		assertEquals(expectedCount, actualCount);
	}

	@Test
	public void testCountUniquePages_QueriesAndTags() {
		MemoryDocumentStorage docStorage = new MemoryDocumentStorage();
		docStorage.storeDocument("http://www.example.com/page.php", "");
		docStorage.storeDocument("http://www.example.com/page.php?query=1", "");
		docStorage.storeDocument("http://www.example.com/page.php?query=1&query=2&query=3", "");
		docStorage.storeDocument("http://www.example.com/page.php#tag", "");
		docStorage.storeDocument("http://www.example.com/page.php?query#tag", "");

		urlStats.runStats(docStorage);
		long expectedCount = 1;
		long actualCount = urlStats.getTotalUniquePages();

		assertEquals(expectedCount, actualCount);
	}

	@Test
	public void testCountUniquePages_Empty() {
		MemoryDocumentStorage docStorage = new MemoryDocumentStorage();
		urlStats.runStats(docStorage);
		long expectedCount = 0;
		long actualCount = urlStats.getTotalUniquePages();

		assertEquals(expectedCount, actualCount);
	}

	@Test
	public void testCountSubdomains() {
		MemoryDocumentStorage docStorage = new MemoryDocumentStorage();
		docStorage.storeDocument("http://vision.ics.uci.edu/", "");
		docStorage.storeDocument("http://vision.ics.uci.edu/page.php", "");
		docStorage.storeDocument("http://vision.ics.uci.edu/page.php?query", "");
		docStorage.storeDocument("http://other.ics.uci.edu/", "");
		docStorage.storeDocument("http://other.ics.uci.edu/page.php", "");

		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		expected.add(new Frequency("http://other.ics.uci.edu", 2));
		expected.add(new Frequency("http://vision.ics.uci.edu", 2));

		urlStats.runStats(docStorage);
		List<Frequency> actual = urlStats.getSubdomainFrequencies();

		TestUtils.compareFrequencyLists(expected, actual);
	}

	@Test
	public void testCountSubdomains_Tricky() {
		MemoryDocumentStorage docStorage = new MemoryDocumentStorage();
		docStorage.storeDocument("http://vision.ics.uci.edu", "");
		docStorage.storeDocument("http://vision.ics.uci.edu/", "");
		docStorage.storeDocument("http://vision.ics.uci.edu/page.php", "");
		docStorage.storeDocument("http://vision.ics.uci.edu/page.php?query", "");

		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		expected.add(new Frequency("http://vision.ics.uci.edu", 3));

		urlStats.runStats(docStorage);
		List<Frequency> actual = urlStats.getSubdomainFrequencies();

		TestUtils.compareFrequencyLists(expected, actual);
	}

	@Test
	public void testCountSubdomains_EmptyList() {
		MemoryDocumentStorage docStorage = new MemoryDocumentStorage();
		ArrayList<Frequency> expected = new ArrayList<Frequency>();

		urlStats.runStats(docStorage);
		List<Frequency> actual = urlStats.getSubdomainFrequencies();

		TestUtils.compareFrequencyLists(expected, actual);
	}
}
