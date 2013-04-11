package ir.assignments.three.storage;

import ir.assignments.three.helpers.URLHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlDocument {
	private final static String lineSeparator = System.getProperty("line.separator");
	private boolean hasLoaded;
	private Document parsedDoc;
	private String url;
	private String html;

	public HtmlDocument(String url, String html) {
		// Parse document using jsoup (http://jsoup.org/)
		this.url = url;
		this.html = html;
		this.hasLoaded = false;
	}

	private void load() {
		if (this.hasLoaded)
			return;

		this.hasLoaded = true;
		this.parsedDoc = Jsoup.parse(html);
		
		// Follow meta tag redirects
		try {
			Elements meta = this.parsedDoc.select("html head meta");
			if (meta.attr("http-equiv").contains("REFRESH") || meta.attr("http-equiv").contains("refresh")) {
				String content = meta.attr("content");
				if (content.contains("=")) {
					String[] parts = content.split("=");
					if (parts.length > 1) {
						String redirectUrl = parts[1].trim();
						String mergedUrl = combineUrls(url, redirectUrl);
						if (mergedUrl != null) {
							this.parsedDoc = Jsoup.connect(mergedUrl.toString()).get();
							this.html = parsedDoc.html();
						}
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println("Error on URL: " + url);
			e.printStackTrace();
		}
	}

	public String getUrl() {
		return this.url;
	}
	
	public String getUrlDomain() {
		return URLHelper.getDomain(getUrl());
	}

	public String getHtml() {
		this.load();
		return this.html;
	}

	public String getTitle() {
		this.load();
		return this.parsedDoc.title();
	}

	public String getDescription() {
		this.load();
		String description = "";

		// Get description from meta tag(s) (e.g. <meta name="description" content="This is the description" />)
		Elements metaTags = this.parsedDoc.select("meta[name=description]");
		if (metaTags != null && metaTags.size() > 0) {
			for (Element metaTag : metaTags) {
				description += metaTag.attr("content");
			}
		}

		return description;
	}

	public String getBody() {
		this.load();
		if (this.parsedDoc.body() != null)
			return this.parsedDoc.body().text();
		return "";
	}
	
	public String getHeaders() {
		this.load();
		StringBuilder text = new StringBuilder();

		Elements elements = this.parsedDoc.select("h1,h2,h3,h4,div[id*=title],div[class*=title],span[id*=title],span[class*=title]");
		if (elements != null) {
			for (Element element : elements) {
				text.append(element.text());
				text.append(lineSeparator);
			}
		}

		return text.toString();
	}

	public String getImportantBody() {
		this.load();
		StringBuilder text = new StringBuilder();

		Elements elements = this.parsedDoc.select("b,strong,em");
		if (elements != null) {
			for (Element element : elements) {
				text.append(element.text());
				text.append(lineSeparator);
			}
		}

		return text.toString();
	}

	public String getAllText() {
		return getTitle() + lineSeparator + getDescription() + lineSeparator + getBody();
	}

	public HashMap<String, String> getOutgoingLinks() {
		this.load();
		HashMap<String, String> outgoingLinks = new HashMap<String, String>(); // (URL, anchor text)

		Elements links = this.parsedDoc.select("a");
		if (links != null) {
			for (Element link : links) {
				String href = link.attr("href");
				if (href.contains("javascript:") || href.contains("mailto:") || href.contains("https"))
					continue;

				// Get absolute URL
				String absoluteHref = combineUrls(this.url, href);

				if (absoluteHref == null)
					continue;

				// Make sure URL within same domain
				if (!absoluteHref.contains("ics.uci.edu") || absoluteHref.contains("https"))
					continue;

				// Append to the anchor text
				String currentAnchorText = outgoingLinks.get(absoluteHref);
				if (currentAnchorText == null)
					currentAnchorText = "";

				currentAnchorText += " " + link.text();
				outgoingLinks.put(absoluteHref, currentAnchorText.trim());
			}
		}

		return outgoingLinks;
	}

	private String combineUrls(String url, String relativeUrl) {
		if (relativeUrl.contains("http"))
			return relativeUrl;

		try {
			URI baseUri = new URI(url);
			return baseUri.resolve(new URI(relativeUrl)).toString();
		}
		catch (URISyntaxException e) {
			//			System.out.println("Error on URL: (" + url + ", " + relativeUrl + ")");
			//			e.printStackTrace();
		}

		return null;
	}
}
