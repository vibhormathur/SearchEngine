Team Members:
  1) Armando Pensado Valle <apensado@uci.edu>
  2) Vibhor Mathur <mathurv@uci.edu>

Setup:
 The setup of the search engine would consist of:
   > Running the crawler (see previous project)
   > Running "Indexer.java search" to create the Lucene index
   > Running "Indexer.java autocomplete" to create the autocomplete index
   > Running "Indexer.java spellchecker" to create the spellcheck index
   > Deploying the app to an Apache Tomcat 7 server
   Note: Paths are set by default to be within the project folder.

External Libraries Used:
 > Lucene
 > jdbm2 (https://code.google.com/p/jdbm2/)
   Library for file-backed HashMap and TreeMap (used for storing and retrieving crawled pages)
 > jsoup (http://jsoup.org/)
   Library for parsing HTML documents (better results than crawler4j HTML parser)
 > Bootstrap (http://twitter.github.com/bootstrap/)
   Styling of "Search" button
 > Google.com (http://google.com/)
   Design (including layout and colors) borrowed for front-end