package ir.assignments.two.tests;

import static org.junit.Assert.*;
import ir.assignments.two.a.Utilities;
import ir.assignments.two.a.Frequency;
import ir.assignments.two.c.TwoGramFrequencyCounter;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PartCTest {

	@Test
	public void testComputerTwoGramFrequencies() {
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(new String[] { "you", "think", "you", "know", "how", "you", "think" }));
		List<Frequency> actual = computeTwoGramFrequencies(words);

		ArrayList<Frequency> expected = new ArrayList<Frequency>();
		expected.add(new Frequency("you think", 2));
		expected.add(new Frequency("how you", 1));
		expected.add(new Frequency("know how", 1));
		expected.add(new Frequency("think you", 1));
		expected.add(new Frequency("you know", 1));

		TestUtils.compareFrequencyLists(expected, actual);
	}

	@Test
	public void testComputerTwoGramFrequencies_Null() {
		List<Frequency> actual = computeTwoGramFrequencies(null);
		ArrayList<Frequency> expected = new ArrayList<Frequency>();

		TestUtils.compareFrequencyLists(expected, actual);
	}
	
	@Test
	public void testComputerTwoGramFrequencies_EmptyList() {
		List<Frequency> actual = computeTwoGramFrequencies(new ArrayList<String>());
		ArrayList<Frequency> expected = new ArrayList<Frequency>();

		TestUtils.compareFrequencyLists(expected, actual);
	}

	private List<Frequency> computeTwoGramFrequencies(ArrayList<String> words) {
		//Since the computeTwoGramFrequencies method is private, have to use reflection to call it
		try {
			return TestUtils.callPrivateStaticMethod(TwoGramFrequencyCounter.class, "computeTwoGramFrequencies", new Object[] { words });
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
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
