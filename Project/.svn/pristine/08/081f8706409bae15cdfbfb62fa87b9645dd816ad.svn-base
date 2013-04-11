package ir.assignments.two.tests;

import static org.junit.Assert.*;
import ir.assignments.two.a.Utilities;
import ir.assignments.two.a.Frequency;
import ir.assignments.two.d.StringHelpers;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PartATest {
	private String lineSeparator = System.getProperty("line.separator");

	@Test
	public void testTokenizeFile() {
		String[] words = tokenize("An input string, this is! (or is it?)");
		String[] expected = new String[] { "an", "input", "string", "this", "is", "or", "is", "it" };
		assertArrayEquals(expected, words);
	}
	
	@Test
	public void testTokenizeFile_Empty() {
		String[] words = tokenize("");
		String[] expected = new String[0];
		assertArrayEquals(expected, words);
	}

	@Test
	public void testPrintFrequencies_Words() {
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		frequencies.add(new Frequency("sentence", 2));
		frequencies.add(new Frequency("the", 1));
		frequencies.add(new Frequency("this", 1));
		frequencies.add(new Frequency("repeats", 1));
		frequencies.add(new Frequency("word", 1));

		String output = getPrintedFrequencies(frequencies);
		String expected = StringHelpers.join(new String[] 
   		                                    { 
   												"Total item count: 6",
   												"Unique item count: 5",
   												"",
   												"sentence  2",
   												"the       1",
   												"this      1",
   												"repeats   1",
   												"word      1"
   		                                    }, lineSeparator);

		assertEquals(expected, output);
	}

	@Test
	public void testPrintFrequencies_2Gram() {
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		frequencies.add(new Frequency("you think", 2));
		frequencies.add(new Frequency("how you", 1));
		frequencies.add(new Frequency("know how", 1));
		frequencies.add(new Frequency("think you", 1));
		frequencies.add(new Frequency("you know", 1));

		String output = getPrintedFrequencies(frequencies);
		String expected = StringHelpers.join(new String[]
		                                    { 
												"Total 2-gram count: 6",
												"Unique 2-gram count: 5",
												"",
												"you think  2",
												"how you    1",
												"know how   1",
												"think you  1",
												"you know   1"
		                                    }, lineSeparator);
		assertEquals(expected, output);
	}
	
	@Test
	public void testPrintFrequencies_Null() {
		String output = getPrintedFrequencies(null);
		String expected = "";
		assertEquals(expected, output);
	}
	
	@Test
	public void testPrintFrequencies_EmptyList() {
		String output = getPrintedFrequencies(new ArrayList<Frequency>());
		String expected = StringHelpers.join(new String[]
                { 
					"Total item count: 0",
					"Unique item count: 0",
					""
                }, lineSeparator);
		assertEquals(expected, output);
	}

	private String[] tokenize(String content) {
		// Tokenize the string by creating a temporary file to pass to the tokenize function
		File file = TestUtils.getTempFile(content);
		ArrayList<String> words = null;
		
		try {
			words = Utilities.tokenizeFile(file);
		}
		finally {
			TestUtils.deleteTempFile(file);
		}
		
		return words.toArray(new String[words.size()]);
	}

	private String getPrintedFrequencies(ArrayList<Frequency> frequencies) {
		// Capture the output stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		
		// Run the print
		Utilities.printFrequencies(frequencies);

		// Restore output stream to console window
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

		return baos.toString();
	}
}
