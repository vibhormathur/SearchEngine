package ir.assignments.two.tests;

import static org.junit.Assert.*;
import ir.assignments.two.a.Frequency;
import ir.assignments.two.c.TwoGramFrequencyCounter;
import ir.assignments.two.d.PalindromeFrequencyCounter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PartDTest {

	@Test
	public void testComputePalindromeFrequencies() {
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(new String[] { "do", "geese", "see", "god", "abba", "bat", "tab" }));
		List<Frequency> actual = computePalindromeFrequencies(words);

		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		expected.add(new Frequency("do geese see god", 1));
		expected.add(new Frequency("bat tab", 1));
		expected.add(new Frequency("abba", 1));

		TestUtils.compareFrequencyLists(expected, actual);
	}
	
	@Test
	public void testComputePalindromeFrequencies_TiesOrderAlphabetical() {
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(new String[] { "abba", "abba", "cbbc", "cbbc" }));
		List<Frequency> actual = computePalindromeFrequencies(words);

		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		expected.add(new Frequency("abba abba", 1));
		expected.add(new Frequency("cbbc cbbc", 1));
		expected.add(new Frequency("abba", 2));
		expected.add(new Frequency("cbbc", 2));

		TestUtils.compareFrequencyLists(expected, actual);
	}
	
	@Test
	public void testComputePalindromeFrequencies_TiesOrderFrequency() {
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(new String[] { "kayak", "kayak", "aabaa" }));
		List<Frequency> actual = computePalindromeFrequencies(words);

		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		expected.add(new Frequency("kayak kayak", 1));
		expected.add(new Frequency("kayak", 2));
		expected.add(new Frequency("aabaa", 1));

		TestUtils.compareFrequencyLists(expected, actual);
	}

	@Test
	public void testComputePalindromeFrequencies_Null() {
		List<Frequency> actual = computePalindromeFrequencies(null);
		ArrayList<Frequency> expected = new ArrayList<Frequency>();

		TestUtils.compareFrequencyLists(expected, actual);
	}

	private List<Frequency> computePalindromeFrequencies(ArrayList<String> words) {
		//Since the computePalindromeFrequencies method is private, have to use reflection to call it
		try {
			return TestUtils.callPrivateStaticMethod(PalindromeFrequencyCounter.class, "computePalindromeFrequencies", new Object[] { words });
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}
}
