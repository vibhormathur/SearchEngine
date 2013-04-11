package ir.assignments.two.d;

public class StringHelpers {
	public static boolean isPalindrome(String words) {
		// Remove spaces since they don't count
		String cleanedWords = "";
		for (int i = 0; i < words.length(); i++) {
			char currentChar = words.charAt(i);
			if (currentChar != ' ')
				cleanedWords += currentChar;
		}
			
		// Check that string is same reversed as normal
		// (First half of string should be the same as second half)
		for (int i = 0; i < Math.floor(cleanedWords.length() / 2); i++) {
			if (cleanedWords.charAt(i) != cleanedWords.charAt(cleanedWords.length() - i - 1))
				return false;
		}
		
		return true;
	}
	
	public static String join(String[] strs, String separator) {
		// Join the separate strings into one string with the given separator
		String output = "";
		for (String str : strs)
		{
			output += str + separator;
		}
		return output;
	}
}
