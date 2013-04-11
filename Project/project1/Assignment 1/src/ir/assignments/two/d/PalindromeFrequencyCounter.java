package ir.assignments.two.d;

import ir.assignments.two.a.Utilities;
import ir.assignments.two.a.Frequency;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class PalindromeFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private PalindromeFrequencyCounter() {
	}

	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique palindrome found in the original list. The frequency of each palindrome
	 * is equal to the number of times that palindrome occurs in the original list.
	 * 
	 * Palindromes can span sequential words in the input list.
	 * 
	 * The returned list is ordered by decreasing size, with tied palindromes sorted
	 * by frequency and further tied palindromes sorted alphabetically. 
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["do", "geese", "see", "god", "abba", "bat", "tab"]
	 * 
	 * The output list of palindromes should be 
	 * ["do geese see god:1", "bat tab:1", "abba:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of palindrome frequencies, ordered by decreasing frequency.
	 */
	private static List<Frequency> computePalindromeFrequencies(ArrayList<String> words) {
		ArrayList<Frequency> freqs = new ArrayList<Frequency>();
		if (words == null)
			return freqs;

		HashMap<String, Integer> palindromeDict = new HashMap<String, Integer>();
		for (int i = 0; i < words.size(); i++) {
			// Combine with all sequential words to find palindromes
			String currentWords = "";
			for (int j = i; j < words.size(); j++) { // start at i to check if single word is palindrome
				currentWords += " " + words.get(j);
				currentWords = currentWords.trim();
				if (StringHelpers.isPalindrome(currentWords)) {
					// Add to dictionary or increase frequency
					int currentFreq = palindromeDict.containsKey(currentWords) ? palindromeDict.get(currentWords) : 0;
					palindromeDict.put(currentWords, currentFreq + 1);
				}
			}
		}

		// Convert the dictionary to an array
		for (String palindrome : palindromeDict.keySet()) {
			Frequency freq = new Frequency(palindrome, palindromeDict.get(palindrome));
			freqs.add(freq);
		}

		// Order by string length (desc), then by freq, then by alphabetical order
		PalindromeFrequencyComparator comparator = new PalindromeFrequencyComparator();
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
		List<Frequency> frequencies = computePalindromeFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
