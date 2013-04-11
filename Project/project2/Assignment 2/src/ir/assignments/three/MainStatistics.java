package ir.assignments.three;

import ir.assignments.three.storage.DocumentStorage;

public class MainStatistics {
	public static void main(String[] args) {
		DocumentStorage documentStorage = new DocumentStorage("docStorage\\docStorage");
		try {
			AnswerQuestions.Answer(documentStorage);
		}
		finally {
			documentStorage.close();
		}
	}
}
