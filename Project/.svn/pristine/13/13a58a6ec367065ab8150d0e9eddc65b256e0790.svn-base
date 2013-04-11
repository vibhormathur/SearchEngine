package ir.assignments.three;

import ir.assignments.three.crawling.ICSCrawler;
import ir.assignments.three.crawling.ICSCrawlerParameters;
import ir.assignments.three.crawling.ICSCrawlerStatistics;
import ir.assignments.three.storage.DocumentStorage;
import ir.assignments.three.storage.IDocumentStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Crawler {
	private static final int MAX = -1;
	
	/**
	 * This method is for testing purposes only. It does not need to be used
	 * to answer any of the questions in the assignment. However, it must
	 * function as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns a
	 * collection containing all URLs visited during the crawl.
	 * 
	 * General code structure borrowed from: http://code.google.com/p/crawler4j/source/browse/src/test/java/edu/uci/ics/crawler4j/examples/basic/BasicCrawlController.java 
	 */
	public static Collection<String> crawl(String seedURL) {
		return crawl(seedURL, new DocumentStorage("docStorage"));
	}
	
	public static Collection<String> crawl(String seedURL, IDocumentStorage docStorage) {
		return crawl(seedURL, "intermediateStorage", docStorage, MAX, MAX);
	}
	
	public static Collection<String> crawl(String seedURL, String intermediateStoragePath, IDocumentStorage documentStorage, int maxDepth, int maxPages) {
		HashSet<String> crawledUrls = new HashSet<String>();

		try {
			// Setup the crawler configuration
			CrawlConfig config = new CrawlConfig();
			config.setCrawlStorageFolder(intermediateStoragePath);
			config.setPolitenessDelay(300);
			config.setMaxDepthOfCrawling(maxDepth);
			config.setMaxPagesToFetch(maxPages);
			config.setResumableCrawling(true);
			config.setUserAgentString("UCI IR crawler 34043453 10902614");
			config.setIncludeBinaryContentInCrawling(false);
			
			ICSCrawlerParameters params = new ICSCrawlerParameters();
			params.setSeedUrl(seedURL);
			params.setDocumentStorage(documentStorage);

			// Instantiate controller
			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
			CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

			controller.addSeed(seedURL);
			controller.setCustomData(params);
			
			// Start crawling
			controller.start(ICSCrawler.class, 5); // 5 threads

			// Get list of crawled URLs for each crawler
			List<Object> crawlersLocalData = controller.getCrawlersLocalData();

			for (Object localData : crawlersLocalData) {
				ICSCrawlerStatistics stats = (ICSCrawlerStatistics) localData;
				crawledUrls.addAll(stats.getUrlsCrawled());
			}
		}
		catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());			
		}

		return crawledUrls;
	}
}
