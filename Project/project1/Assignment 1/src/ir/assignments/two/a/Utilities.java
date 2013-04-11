package ir.assignments.two.a;

import ir.assignments.two.a.Frequency;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of utility methods for text processing.
 */

public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 * @throws FileNotFoundException 
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		String strLine;
		String[] tokens;
		ArrayList<String> tokenize = new ArrayList<String>();

		if (input == null)
			return tokenize;

		try {
			FileInputStream fstream = new FileInputStream(input);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null) {
				if ( strLine.trim().length() != 0) {
					// Convert the input data string to lower case and then tokenize it
					strLine = strLine.toLowerCase();
					String delims = "[^a-zA-Z0-9']+";
					tokens = strLine.split(delims);
					int tokenLength = tokens.length;

					for (int j = 1; j <= tokenLength; j++) {
						tokenize.add(tokens[j - 1]);
					}
				}
			}

			br.close();
			in.close();
		}
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		return tokenize;
	}

	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		if (frequencies == null)
			return;

		String s;
		int freq;
		int total = 0;
		int longestLength = 0;

		// Count total number and number of unique elements in string
		// and the get longest word length
		for (int i = 0; i < frequencies.size(); i++) {
			Frequency freqObj = frequencies.get(i);

			freq = freqObj.getFrequency();
			total = total + freq;

			if (freqObj.getText().length() > longestLength)
				longestLength = freqObj.getText().length();
		}

		// Print total number and number of unique item or 2-grams to standard out
		s = (frequencies.size() > 0) ? frequencies.get(0).getText() : "";
		if (s.contains(" ")) {
			System.out.println("Total 2-gram count: " + total);
			System.out.println("Unique 2-gram count: " + frequencies.size());
			System.out.println("");
		}
		else {
			System.out.println("Total item count: " + total);
			System.out.println("Unique item count: " + frequencies.size());
			System.out.println("");
		}

		// Print the string and its frequency count to standard out
		for (int i = 0; i <= frequencies.size() - 1; i++) {
			s = frequencies.get(i).getText();
			freq = frequencies.get(i).getFrequency();

			// Left justify numbers
			System.out.println(String.format("%-" + longestLength + "s %2d", s, freq));
		}
	}
}
