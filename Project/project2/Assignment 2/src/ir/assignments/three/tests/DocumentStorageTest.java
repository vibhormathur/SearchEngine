package ir.assignments.three.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import ir.assignments.three.storage.DocumentStorage;
import ir.assignments.three.storage.HtmlDocument;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DocumentStorageTest {
	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void testWriteAndRead() {
		DocumentStorage docStorage = null;
		try {
			docStorage = new DocumentStorage(tmpFolder.newFile().getAbsolutePath());
			docStorage.storeDocument("http://www.google.com", "<html><title>Title</title><body>Body</body></html>");

			HtmlDocument doc = null;
			for (HtmlDocument d : docStorage.getAll()) {
				doc = d;
			}
			
			assertEquals("Title", doc.getTitle());
			assertEquals("Body", doc.getBody());
		}
		catch (IOException e) {
			fail(e.getMessage());
		}
		finally {
			if (docStorage != null)
				docStorage.close();
		}
	}

	@Test
	public void testWriteSamePageTwice() {
		DocumentStorage docStorage = null;
		try {
			File tmpFile = tmpFolder.newFile();
			docStorage = new DocumentStorage(tmpFile.getAbsolutePath());
			docStorage.storeDocument("http://www.google.com", "<html><title>Title</title><body>Body</body></html>");
			docStorage.storeDocument("http://www.google.com", "<html><title>Title 2</title><body>Body 2</body></html>");

			HtmlDocument doc = null;
			for (HtmlDocument d : docStorage.getAll()) {
				doc = d;
			}
			
			assertEquals("Title 2", doc.getTitle());
			assertEquals("Body 2", doc.getBody());
		}
		catch (IOException e) {
			fail(e.getMessage());
		}
		finally {
			if (docStorage != null)
				docStorage.close();
		}
	}
}
