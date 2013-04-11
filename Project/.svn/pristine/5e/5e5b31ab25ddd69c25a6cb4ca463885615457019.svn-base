package ir.assignments.two.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.b.WordFrequencyCounter;

import org.junit.Test;

public class PartBTest {

	@Test
	public void testComputeWordFrequencies() {
		List<String> words = Arrays.asList(new String[] { "this", "sentence", "repeats", "the", "word", "sentence" });
		List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words);
		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		expected.add(new Frequency("sentence", 2));
		expected.add(new Frequency("repeats", 1));
		expected.add(new Frequency("the", 1));
		expected.add(new Frequency("this", 1));
		expected.add(new Frequency("word", 1));
		
		// Can't use assertArrayEquals because we want to compare the objects (not the object references)
		TestUtils.compareFrequencyLists(expected, frequencies);
	}
	
	@Test
	public void testComputeWordFrequencies_Null() {
		List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(null);
		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		
		// Can't use assertArrayEquals because we want to compare the objects (not the object references)
		TestUtils.compareFrequencyLists(expected, frequencies);
	}
	
	@Test
	public void testComputeWordFrequencies_EmptyList() {
		List<String> words = Arrays.asList(new String[0]);
		List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words);
		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		
		// Can't use assertArrayEquals because we want to compare the objects (not the object references)
		TestUtils.compareFrequencyLists(expected, frequencies);
	}
}
