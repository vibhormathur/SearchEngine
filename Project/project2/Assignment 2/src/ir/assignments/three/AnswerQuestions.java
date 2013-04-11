package ir.assignments.three;

import ir.assignments.three.helpers.FileHelper;
import ir.assignments.three.stats.DocumentStatistics;
import ir.assignments.three.stats.Frequency;
import ir.assignments.three.stats.UrlStatistics;
import ir.assignments.three.storage.IDocumentStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnswerQuestions {
	public static void Answer(IDocumentStorage documentStorage) {
		File txtAnswers = new File("answers.txt");
		File txtSubdomains = new File("Subdomains.txt");
		File txtCommonWords = new File("CommonWords.txt");
		File txtCommon2Grams = new File("Common2Grams.txt");
		
		// Analyze URLs first (Questions 2-3)
		UrlStatistics urlStats = new UrlStatistics();
		urlStats.runStats(documentStorage);
		
		//-- Results
		displayAndStore(txtAnswers, "Total Pages Crawled: " + urlStats.getTotalPages());
		displayAndStore(txtAnswers, "Total Unique Pages Crawled: " + urlStats.getTotalUniquePages());
		displayAndStore(txtAnswers, "Total Unique Subdomains Crawled: " + urlStats.getSubdomainFrequencies().size());
		
		ArrayList<String> subdomainsDisplay = new ArrayList<String>();
		for (Frequency freq : urlStats.getSubdomainFrequencies()) {
			subdomainsDisplay.add(freq.getText() + ", " + freq.getFrequency());
		}
		Collections.sort(subdomainsDisplay); // sort alphabetically
		store(txtSubdomains, subdomainsDisplay);
		
		// Analyze document content (Questions 4-6)
		DocumentStatistics docStats = new DocumentStatistics();
		docStats.runStats(documentStorage);
		
		//-- Results
		displayAndStore(txtAnswers, "Longest page: " + docStats.getLongestDocumentUrl());		
		store(txtCommonWords, docStats.getMostCommonWords());
		store(txtCommon2Grams, docStats.getMostCommonTwoGrams());
		
		System.out.println("Done");
	}

	private static void displayAndStore(File file, String line) {
		// Save to file and print to console
		try {
			FileHelper.appendToFile(file, line);
		}
		catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println(line);
	}

	private static void store(File file, List<String> lines) {
		// Save to file
		try {
			FileHelper.writeFile(file, lines.toArray(new String[lines.size()]));
		}
		catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
