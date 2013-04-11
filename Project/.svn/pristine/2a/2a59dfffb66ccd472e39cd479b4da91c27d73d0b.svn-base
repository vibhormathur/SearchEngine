package ir.assignments.three.stats;

import ir.assignments.three.helpers.URLHelper;
import ir.assignments.three.storage.IDocumentStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.net.*;

public class UrlStatistics {
	private long totalPages;
	private long totalUniquePages;
	private List<Frequency> subdomainFrequencies;

	public long getTotalPages() {
		return this.totalPages;
	}

	public long getTotalUniquePages() {
		return this.totalUniquePages;
	}

	public List<Frequency> getSubdomainFrequencies() {
		return this.subdomainFrequencies;
	}

	public void runStats(IDocumentStorage docStorage) {

		// Keep track of all pages and unique pages (don't count different URL queries as different pages)
		long totalCount = 0;
		HashSet<String> uniquePages = new HashSet<String>();
		for (String url : docStorage.getCrawledUrls()) {
			// Display number of processed documents to keep track of progress				
			if (totalCount % 100 == 0)
				System.out.println(totalCount);

			totalCount++;

			String urlWithoutQuery = URLHelper.removeQuery(url);
			if (urlWithoutQuery != null && urlWithoutQuery.length() > 0) {
				uniquePages.add(urlWithoutQuery);
			}
		}

		// Count the number of unique subdomains
		HashMap<String, Integer> subdomainMap = new HashMap<String, Integer>();
		for (String uniqueUrl : uniquePages) {
			String urlWithoutPath = URLHelper.removePath(uniqueUrl);
			if (urlWithoutPath == null)
				continue;

			// Increment count
			Integer currentCount = subdomainMap.get(urlWithoutPath);
			if (currentCount == null)
				currentCount = 0;
			subdomainMap.put(urlWithoutPath, currentCount + 1);
		}

		//-- Convert to frequency list
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		for (String url : subdomainMap.keySet()) {
			Frequency frequency = new Frequency(url, subdomainMap.get(url));
			frequencies.add(frequency);
		}

		//-- Order by frequency (desc) and break ties with alphabetical order (asc)
		FrequencyComparator comparator = new FrequencyComparator();
		Collections.sort(frequencies, comparator);

		// Results
		this.totalPages = totalCount;
		this.totalUniquePages = uniquePages.size();
		this.subdomainFrequencies = frequencies;
	}
}
