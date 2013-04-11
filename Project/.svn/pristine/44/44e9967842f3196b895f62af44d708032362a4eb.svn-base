Team Members:
  1) Armando Pensado Valle <apensado@uci.edu>
  2) Vibhor Mathur <mathurv@uci.edu>

How to Run:
  > To run the web crawler, run class MainCrawler
  > To run the analysis on the crawled documents, run class MainStatistics
  > The class IntegrationTest runs a 1-depth crawl and analysis on www.vcskicks.com (Armando's site)

Assumptions:
  > Crawling:
    - URLs of the following file formats were not visited:
      css,js,bmp,gif,jpg,jpeg,png,tif,tiff,mid,mp2,mp3,mp4,wav,avi,mov,mpeg,ram,m4v,pdf,rm,smil,wmv,swf,wma,zip,rar,gz,ico,pfm,c,h,o
    - Only visited documents with HTML content were saved
    - Only documents up to ~2MB were saved
    - A single page was visited at most about 20 times with different query strings (to prevent infinite loops)
      (might be slightly more since the count was lost when crawler was stopped and resumed)
    - Entire domain could not be crawled due to time constraints

  > Analysis:
    - Stop words were filtered based on the "Default English stopwords list" in http://www.ranks.nl/resources/stopwords.html
    - Words that were shorter than 4 characters were considered uninteresting (and treated the same as stop words)
    - Words that contained at least one number (0-9) were considered uninteresting
    - The longest document length excluded stop and uninteresting words

External Libraries Used:
  > crawler4j (https://code.google.com/p/crawler4j/)
    Library for crawling pages
  > jdbm2 (https://code.google.com/p/jdbm2/)
    Library for file-backed HashMap and TreeMap (used for storing and retrieving crawled pages)
  > jsoup (http://jsoup.org/)
    Library for parsing HTML documents (better results than crawler4j HTML parser)