package ir.assignments.two.d;

import ir.assignments.two.a.Frequency;
import java.util.Comparator;

public class PalindromeFrequencyComparator implements Comparator<Frequency> {
	@Override
	public int compare(Frequency x, Frequency y) {
		// Order by string length (descending)
		if (x.getText().length() < y.getText().length()) {
			return 1;
		}
		else if (x.getText().length() > y.getText().length()) {
			return -1;
		}
		else {
			// Order by frequency on ties (descending)
			if (x.getFrequency() < y.getFrequency()) {
				return 1;
			}
			else if (x.getFrequency() > y.getFrequency()) {
				return -1;
			}
			else {
				// Alphabetical order for ties (ascending)
				return x.getText().compareTo(y.getText());
			}
		}
	}
}
