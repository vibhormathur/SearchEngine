package ir.assignments.four;

import ir.assignments.four.storage.DocumentLinkData;
import ir.assignments.four.storage.DocumentStorage;
import ir.assignments.four.storage.HtmlDocument;
import ir.assignments.four.storage.LinkDataStorage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefIterator;
import org.apache.lucene.util.Version;

/**
 * Creates the Lucene index from the locally stored crawled pages.
 */
public class Indexer {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("");
			return;
		}
		
		String indexType = args[0].toLowerCase();
		if (indexType.equals("search")) {
			DocumentStorage docStorage = new DocumentStorage("docStorage\\docStorage");
			LinkDataStorage linkDataStorage = new LinkDataStorage("linkDataStorage\\linkDataStorage");
			try {
				Indexer indexer = new Indexer();
				//indexer.indexDocuments(docStorage, linkDataStorage, "docIndex");
				indexer.indexDocumentsEnhanced(docStorage, linkDataStorage, "docIndexEnhanced");
			}
			finally {
				docStorage.close();
				linkDataStorage.close();
			}
		} else if (indexType.equals("autocomplete")) {
			Indexer indexer = new Indexer();
			indexer.indexAutoComplete("docIndexEnhanced", "docIndexAutoComplete");
		} else if (indexType.equals("spellchecker")) {
			Indexer indexer = new Indexer();
			indexer.indexSpellChecker("spellChecker");
		}	
	}
	
	public void indexDocuments(DocumentStorage docStorage, String indexPath) {
		// This is the original indexing code
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);

		try {
			Directory indexDir = FSDirectory.open(new File(indexPath));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer);
			IndexWriter indexWriter = new IndexWriter(indexDir, config);

			try {
				int i = 0;
				for (HtmlDocument crawledDoc : docStorage.getAll()) {
					
					if (i % 500 == 0)
						System.out.println(i + "");
					
					i++;

					Document indexDoc = new Document();
					
					indexDoc.add(new StringField("url", crawledDoc.getUrl(), Field.Store.YES));
					indexDoc.add(new TextField("title", crawledDoc.getTitle(), Field.Store.YES));
					indexDoc.add(new TextField("content", tokenize(crawledDoc.getBody()), Field.Store.NO));

					// Update document based on URL
					indexWriter.updateDocument(new Term("url",  crawledDoc.getUrl()), indexDoc);
				}
			}
			finally {
				indexWriter.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}
	
	public void indexDocumentsEnhanced(DocumentStorage docStorage, LinkDataStorage linkDataStorage, String indexPath) {		
		
		// This is the indexing code for the improved version
		Analyzer regularAnalyzer = new StandardAnalyzer(Version.LUCENE_41);	
		Analyzer stemAnalyzer = new EnglishAnalyzer(Version.LUCENE_41);
		
		// Use the stem analyzer for some fields, the regular analyzer for the rest
		Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();
		analyzerPerField.put("stemcontent", stemAnalyzer);
		analyzerPerField.put("stemtitle", stemAnalyzer);
		analyzerPerField.put("stemanchortext", stemAnalyzer);

		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(regularAnalyzer, analyzerPerField);
		
		try {
			Directory indexDir = FSDirectory.open(new File(indexPath));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer);
			IndexWriter indexWriter = new IndexWriter(indexDir, config);

			try {
				int i = 0;
	
				// Index all the crawled documents
				for (HtmlDocument crawledDoc : docStorage.getAll()) {					
					// Display the current number to track progress
					if (i % 500 == 0)
						System.out.println(i + "");
					
					i++;
					
					// Get the link data for the current page (want anchor text and outgoing links' text)
					DocumentLinkData linkData = linkDataStorage.getDocumentLinkData(crawledDoc.getUrl());
					String anchorText = (linkData != null && linkData.anchorText != null) ? linkData.anchorText : "";					
					String outgoingLinkText = "";
					for (String text : crawledDoc.getOutgoingLinks().values()) {
						outgoingLinkText += " " + text;
					}
					
					// Create index document with the several fields
					// Note: Fields are not boosted since they will be boosted at query-time
					Document indexDoc = new Document();					
					Field[] fields = new Field[]
					{
						new TextField("url", crawledDoc.getUrl(), Field.Store.YES),
						new TextField("urldomain", crawledDoc.getUrlDomain(), Field.Store.NO),
						
						new TextField("title", crawledDoc.getTitle(), Field.Store.YES),
						new TextField("stemtitle", crawledDoc.getTitle(), Field.Store.NO),
						
						new TextField("content", crawledDoc.getBody(), Field.Store.NO),
						new TextField("stemcontent", crawledDoc.getBody(), Field.Store.NO),						
						
						new TextField("contentheaders", crawledDoc.getHeaders(), Field.Store.NO),						
						new TextField("importantcontent", crawledDoc.getImportantBody(), Field.Store.NO),
						
						new TextField("outgoingtext", outgoingLinkText, Field.Store.NO), // anchor text from this page to another
						new TextField("anchortext", anchorText, Field.Store.NO), // anchor text from other pages to this page
						new TextField("stemanchortext", anchorText, Field.Store.NO)
					};
					
					for (Field field : fields) {
						indexDoc.add(field);
					}
					
					// Update document based on URL
					indexWriter.updateDocument(new Term("url",  crawledDoc.getUrl()), indexDoc);
				}
			}
			finally {
				indexWriter.close();
				analyzer.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}
	
	// Code borrowed from: http://stackoverflow.com/questions/120180/how-to-do-query-auto-completion-suggestions-in-lucene
	public void indexAutoComplete(String indexPath, String autoCompleteIndexPath) {
		
		// Use an existing index to create an n-gram index to use for autocomplete
		try {
			Directory originalIndexDir = FSDirectory.open(new File(indexPath));
			DirectoryReader originalIndexReader = DirectoryReader.open(originalIndexDir);
			
			AutoCompleteAnalyzer autoCompleteAnalyzer = new AutoCompleteAnalyzer();
			//ShingleAnalyzerWrapper analyzer = new ShingleAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_41), 3);
			Directory newIndexDir = FSDirectory.open(new File(autoCompleteIndexPath));	
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, autoCompleteAnalyzer);
			IndexWriter newIndexWriter = new IndexWriter(newIndexDir, config);

			try {
				int i = 0;
				
				// Build a dictionary of all the terms in the "content" field
				LuceneDictionary contentDict = new LuceneDictionary(originalIndexReader, "title");
				
				// For all the terms in the dictionary, store the word, it's n-grams, and it's doc frequency
				BytesRefIterator wordIterator = contentDict.getWordsIterator();
				BytesRef currentTermBytes = null;
				while ((currentTermBytes = wordIterator.next()) != null) {
					// Display the current number to track progress
					if (i % 1000 == 0)
						System.out.println(i + "");
					
					i++;
					
					String currentTerm = currentTermBytes.utf8ToString();
					for (String term : tokenize(currentTerm).split(" ")) {
						
						// Remove non-letters
						String filteredTerm = "";
						for (char c : term.toCharArray()) {
							if (Character.isLetter(c))
								filteredTerm += c;
						}

						if (filteredTerm.length() < 4) // skip short words
							continue;
						
						int docFreq = originalIndexReader.docFreq(new Term("title", filteredTerm));
						
						// Index
						Document doc = new Document();
						doc.add(new StringField("original", filteredTerm, Field.Store.YES)); // indexed, not tokenized
						doc.add(new TextField("ngram", filteredTerm, Field.Store.YES)); // indexed and tokenized (by n-gram analyzer)
						doc.add(new IntField("docFreq", docFreq, Field.Store.NO));
						
						newIndexWriter.addDocument(doc);
					}
				}				
			}
			finally {
				originalIndexReader.close();
				newIndexWriter.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}
	
	// Code borrowed from: http://www.javacodegeeks.com/2010/05/did-you-mean-feature-lucene-spell.html
	public void indexSpellChecker(String spellCheckerIndexPath) {
		// Build an index from a dictionary of English words		
		try {
			
			Directory spellCheckIndexDir = FSDirectory.open(new File(spellCheckerIndexPath));
			IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_41, new StandardAnalyzer(Version.LUCENE_41));
			SpellChecker spellChecker = new SpellChecker(spellCheckIndexDir);
			
			try {
				boolean fullMerge = false;
				spellChecker.indexDictionary(new PlainTextDictionary(new File("wordlistDictionary.txt")), indexConfig, fullMerge);
			}
			finally {
				spellChecker.close();
				spellCheckIndexDir.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Pattern EXACTLY_ONE_NUMBER = Pattern.compile("^[^\\d]*\\d[^\\d]*$"); // compile only use
	private static Matcher EXACTLY_ONE_NUMBER_MATCHER = EXACTLY_ONE_NUMBER.matcher("");
	
	private String tokenize(String input) {
		// Tokenize by filtering out words and returning a single string for lucene to tokenize
		StringBuilder tokens = new StringBuilder();
		if (input == null)
			return tokens.toString();

		input = input.toLowerCase();
		String[] parts = input.split("[^a-zA-Z0-9'-]+"); // alphanumeric words
		for (String part : parts) {
			String tmp = part.trim();
			if (isInterestingWord(tmp))
			{
				tokens.append(tmp);
				tokens.append(" ");
			}
		}
		
		return tokens.toString();
	}	

	private boolean isInterestingWord(String token) {
		return token.length() >= 3 && // filter out words shorter than 3 letters
			   !EXACTLY_ONE_NUMBER_MATCHER.reset(token).matches(); // don't allow tokens with a single number
	}
}
