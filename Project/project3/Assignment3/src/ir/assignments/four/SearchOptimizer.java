package ir.assignments.four;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Helps optimize the NDCG@5 score of the test queries by doing hill-climb on the query-time field boosting values.
 */
public class SearchOptimizer {
	public static void main(String[] args) {
		SearchOptimizer optimizer = new SearchOptimizer();
		optimizer.optimize();
	}
	
	private int maxRecentVectors = 50;
	private HashSet<float[]> recentVectors = new HashSet<float[]>(maxRecentVectors);
	private Random rand = new Random();
	
	public void optimize() {
		// Start with random boosting values
		float[] initialVector = randomStartWeights(20);
		 
		float[] bestVector = initialVector;
		double bestVectorScore = getOverallScore(initialVector);
		
		// Re-start search 30 times
		for (int i = 0; i < 30; i++) {
			System.out.println("Optimizer iteration " + (i + 1) + " (best so far: " + bestVectorScore + ")");
			System.out.println(vectorToString(bestVector));
			
			// Create k successors
			float[] lastSuccessor = bestVector;
			double lastSuccessorScore = bestVectorScore;
			
			for (int k = 0; k < 100; k++) {
				System.out.println(" " + k);
				float[] successor = getSuccessor(lastSuccessor);
				double successorScore = getOverallScore(successor);
				
				// Keep the global best
				if (successorScore > bestVectorScore) {
					bestVector = successor;
					bestVectorScore = successorScore;
				}
				
				// If it's improvement, use this for next successor
				if (successorScore >= lastSuccessorScore) {
					lastSuccessor = successor;
					lastSuccessorScore = successorScore;
				}
			}
		}
		
		System.out.println("Best vector: " + vectorToString(bestVector));
		System.out.println("Score: " + bestVectorScore);
	}
	
	private float[] randomStartWeights(int count) {
		// Generate a random vector of weights
		float[] weights = new float[count];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = rand.nextFloat() * 40.0f; // random float between 0 and 40
		}
		return weights;
	}
	
	private String vectorToString(float[] vector) {
		String vectorString = "{ ";
		for (float weight : vector) {
			vectorString += weight + "f, ";
		}		
		vectorString += " }";
		
		return vectorString;
	}
	
	private float[] getSuccessor(float[] weightVector) {
		// Generate a random successor to the current vector
		float[] newVector = null;
		
		do {
			newVector = weightVector.clone();
			
			// Randomly tweak some number of fields in the vector (between 1 and half the total fields)
			int fieldsToTweak = rand.nextInt(newVector.length / 2) + 1;
			HashSet<Integer> indexes = new HashSet<Integer>();
			for (int i = 0; i < fieldsToTweak; i++) {
				// Random step size between -20 and 20
				float min = -100.0f;
				float max = 100.0f;
				float stepSize = (rand.nextFloat() * (max - min)) + min;
				
				// Change random field (no duplicates)
				int fieldIndex = rand.nextInt(newVector.length);
				while (indexes.contains(fieldIndex)) {
					fieldIndex = rand.nextInt(newVector.length);
				}
				indexes.add(fieldIndex);
				
				newVector[fieldIndex] += stepSize;
				if (newVector[fieldIndex] < 0)
					newVector[fieldIndex] = 0.0f;
			}
		} while (recentVectors.contains(newVector));
		
		// Add to recent vectors to prevent revisit
		if (recentVectors.size() + 1 > maxRecentVectors) {
			recentVectors = new HashSet<float[]>(maxRecentVectors); // to prevent out-of-memory errors limit the number of vectors to keep track of
		}		
		recentVectors.add(newVector);
		
		return newVector;
	}
	
	private double getOverallScore(float[] weightVector) {
		// The overall score being optimized is the average of all the test query NDCG@5 scores
		String[] queries = new String[] { "mondego", "machine learning", "software engineering", "security", "student affairs",
                                          "graduate courses", "Crista Lopes", "REST", "computer games", "information retrieval" };

		double totalScore = 0;
		for (String query : queries) {
			double score = getScore(query, weightVector);			
			totalScore += score;
		}
		
		double average = totalScore / queries.length;
		return average;
	}
	
	public double getScore(String query, float[] weightVector) {
		SearchFiles localSearch = null;
		
		try {
			GoogleSearcher google = new GoogleSearcher();
			localSearch = new SearchFiles("docIndexEnhanced");
			
			List<String> googleResults = google.getSearchResults(query, 5);
			List<String> localResults = localSearch.search(query, 0, 5, weightVector).getUrls();
			
			// Calculate NDCG@5
			return NDCG.getNDCG(localResults, googleResults, 5);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (localSearch != null)
				localSearch.close();
		}
		
		return -1; // means an error occurred, usually because local search did not return enough results
	}
}
