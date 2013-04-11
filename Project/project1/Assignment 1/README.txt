How to run:
 > The application can be run either using the pre-defined main() functions or by running the jUnit tests in package ir.assignments.two.tests
 
Modifications:
 > The original project folder "one" was renamed to "two" to match the package names
 
Assumptions:
 > Utilities:
   > printFrequencies: 
     - We assumed the frequency numbers should be aligned with whitespaces. Numbers are aligned two spaces away from the longest word.
     - For null input, don't print anything
     - For empty list input, print "Total item count" and "Unique item count" with zero
 > WordFrequencyCounter:
   > computeWordFrequencies: The description says to order by alphabetical order on ties, but the example is not ordered alphabetically. We assumed:
     - sort by decreasing frequency
     - sort by increasing alphabetical order next (on ties)
 > PalindromeFrequencyCounter:
   > computePalindromeFrequencies: The description gave some conflicting info about the sorting. We assumed it meant:
      - sort by decreasing word length first
      - sort by decreasing frequency next (on ties)
      - sort by increasing alphabetical order last (on ties)

Team Members:
1) Armando Pensado Valle <apensado@uci.edu>
2) Vibhor Mathur <mathurv@uci.edu>