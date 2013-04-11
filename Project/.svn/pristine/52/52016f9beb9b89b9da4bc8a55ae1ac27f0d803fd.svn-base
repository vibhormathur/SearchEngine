package ir.assignments.four.storage;

import java.util.Iterator;
import java.util.Map.Entry;

public class HtmlDocumentIterable implements Iterable<HtmlDocument> {
	private Iterator<Entry<String, String>> iterator;

	public HtmlDocumentIterable(Iterator<Entry<String, String>> iterator) {
		this.iterator = iterator;
	}
	
	@Override
	public Iterator<HtmlDocument> iterator() {
		return new HtmlDocumentIterator(iterator);
	}
	
	public class HtmlDocumentIterator implements Iterator<HtmlDocument> {
		private Iterator<Entry<String, String>> iterator;

		public HtmlDocumentIterator(Iterator<Entry<String, String>> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public HtmlDocument next() {
			Entry<String, String> entry = iterator.next();
			return new HtmlDocument(entry.getKey(), entry.getValue());			
		}

		@Override
		public void remove() {
			iterator.remove();
		}
	}
}
