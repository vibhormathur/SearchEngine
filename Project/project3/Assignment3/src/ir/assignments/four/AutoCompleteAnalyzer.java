package ir.assignments.four;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter.Side;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

/*
 * Code and idea borrowed from: http://stackoverflow.com/questions/120180/how-to-do-query-auto-completion-suggestions-in-lucene.
 * 
 */
public class AutoCompleteAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		// Do the same things as StandardAnalyzer, but also include n-gram tokenizer
		StandardTokenizer source = new StandardTokenizer(Version.LUCENE_41, reader);

		TokenStream filters = new StandardFilter(Version.LUCENE_41, source);
		filters = new LowerCaseFilter(Version.LUCENE_41, filters);
		filters = new StopFilter(Version.LUCENE_41, filters, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		filters = new EdgeNGramTokenFilter(filters, Side.FRONT, 1, 10); // 1-gram to 10-grams

		return new TokenStreamComponents(source, filters);
	}
}