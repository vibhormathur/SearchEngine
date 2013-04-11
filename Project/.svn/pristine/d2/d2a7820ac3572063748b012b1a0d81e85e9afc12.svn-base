package ir.assignments.two.c;

import ir.assignments.two.a.Utilities;
import ir.assignments.two.a.Frequency;
import ir.assignments.two.b.FrequencyComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Count the total number of 2-grams and their frequencies in a text file.
 */
public final class TwoGramFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private TwoGramFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique 2-gram in the original list. The frequency of each 2-grams
	 * is equal to the number of times that two-gram occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied 2-grams sorted
	 * alphabetically. 
	 * 
	 * 
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["you", "think", "you", "know", "how", "you", "think"]
	 * 
	 * The output list of 2-gram frequencies should be 
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of two gram frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeTwoGramFrequencies(ArrayList<String> words) {
		ArrayList<Frequency> freqs = new ArrayList<Frequency>();
		if (words == null)
			return freqs;

		// Form 2-grams by combining each entry with the following entry
		HashMap<String, Integer> palindromeDict = new HashMap<String, Integer>();		
		for (int i = 0; i < words.size() - 1; i++) {
			String current = words.get(i);
			String next = words.get(i + 1);
			
			//Either add a new entry in the dictionary or increase the frequency by 1
			String twoGram = current + " " + next;
			int currentValue = palindromeDict.containsKey(twoGram) ? palindromeDict.get(twoGram) : 0;
			palindromeDict.put(twoGram, currentValue + 1);
		}
		
		// Convert the dictionary to an array
		for (String palindrome : palindromeDict.keySet()) {
			Frequency freq = new Frequency(palindrome, palindromeDict.get(palindrome));
			freqs.add(freq);
		}
		
		// Order by frequency (desc) and break ties with alphabetical order (asc)
		FrequencyComparator comparator = new FrequencyComparator();
		Collections.sort(freqs, comparator);
		return freqs;
	}
	
	/**
	 * Runs the 2-gram counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		File file = new File(args[0]);
		ArrayList<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computeTwoGramFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
