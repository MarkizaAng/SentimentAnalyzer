package ie.atu.sw;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * The Output class provides functionality to output sentiment analysis results
 * to a file. Time complexity for outputResultsToFile method: O(n), where n is
 * the number of entries in result.wordSentiments.
 */

public class Output {
	/**
	 * Outputs sentiment analysis results to a specified file.
	 *
	 * @param result         The SentimentsResult object containing sentiment
	 *                       analysis results.
	 * @param outputFileName The name of the file to which results will be written.
	 * @throws IOException If an I/O error occurs while writing to the file.
	 */

	public void outputResultsToFile(SentimentAnalyzer.SentimentsResult result, String outputFileName)
			throws IOException {
		try (FileWriter writer = new FileWriter(outputFileName)) {
			double absNegative = Math.abs(result.negativeSentiment);

			// Writing wordSentiments as a column in the file
			writer.write("Word Sentiments:\n");
			for (Map.Entry<String, Double> entry : result.wordSentiments.entrySet()) {
				writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
			}

			writer.write("\nSas: " + (result.negativeSentiment + result.positiveSentiment) + "\n");
			writer.write("Negative: " + absNegative + "\n");
			writer.write("Positive: " + result.positiveSentiment + "\n");
			double averageSentiment = result.calculateAverageSentiment(); // Рассчет среднего эмоционального показателя
			writer.write("SfT: " + Math.ceil(averageSentiment));

			System.out.println("Result saved in the file " + outputFileName);
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}

}
