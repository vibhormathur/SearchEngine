package ir.assignments.three.stats;

import java.util.Comparator;

public class FrequencyComparator implements Comparator<Frequency>
{
    @Override
    public int compare(Frequency x, Frequency y)
    {
    	// Order by frequency (descending)
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
