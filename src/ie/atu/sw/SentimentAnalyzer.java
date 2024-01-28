package ie.atu.sw;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The SentimentAnalyzer class performs sentiment analysis on a list of words.
 */
public class SentimentAnalyzer extends ExecutorAwareService {
	/**
	 * Constructor for SentimentAnalyzer.
	 *
	 * @param executor The ExecutorService used for concurrent execution.
	 */
	public SentimentAnalyzer(ExecutorService executor) {
		super(executor);
	}

	/**
	 * Analyze a list of words and return the positive and negative sentiment.
	 * Time complexity is O(n) where n is the number of words
	 * 
	 * @param words      The list of words to analyze.
	 * @param sentiments The map of words to sentiment values.
	 * @return The positive and negative sentiment.
	 *	 
	 */

	public SentimentsResult analyze(List<String> words, Map<String, Double> sentiments) {
		AtomicLong positiveSum = new AtomicLong(Double.doubleToRawLongBits(0.0));
		AtomicLong negativeSum = new AtomicLong(Double.doubleToRawLongBits(0.0));
		ConcurrentHashMap<String, Double> map = new ConcurrentHashMap<>();

		for (String word : words) {
			executor.execute(() -> {
				if (sentiments.containsKey(word)) {
					var wordScore = sentiments.get(word);

					if (wordScore >= 0) {
						positiveSum.getAndAccumulate(Double.doubleToRawLongBits(wordScore), (prev, update) -> Double
								.doubleToRawLongBits(Double.longBitsToDouble(prev) + Double.longBitsToDouble(update)));
					} else {
						negativeSum.getAndAccumulate(Double.doubleToRawLongBits(wordScore), (prev, update) -> Double
								.doubleToRawLongBits(Double.longBitsToDouble(prev) + Double.longBitsToDouble(update)));
					}
					map.put(word, wordScore);
				}
			});
		}
		/**
		 * The SentimentsResult class represents the result of sentiment analysis.
		 */
		return new SentimentsResult(Double.longBitsToDouble(positiveSum.get()),
				Double.longBitsToDouble(negativeSum.get()), map);
	}

	/**
	 * The SentimentsResult class represents the result of sentiment analysis.
	 */
	public static class SentimentsResult {
		/**
		 * Represents the negative sentiment value.
		 */
		public Double negativeSentiment;
		/**
		 * Represents the positive sentiment value.
		 */
		public Double positiveSentiment;
		/**
		 * Represents word-wise sentiment mapping.
		 */
		public Map<String, Double> wordSentiments;

		SentimentsResult(Double positiveSentiment, Double negativeSentiment, Map<String, Double> wordSentiments) {
			this.negativeSentiment = negativeSentiment;
			this.positiveSentiment = positiveSentiment;
			this.wordSentiments = wordSentiments;
		}

		/**
		 * Calculates and returns the average sentiment.
		 *
		 * @return The average sentiment value.
		 */
		// The calculateAverageSentiment method typically has O(1) complexity for simple arithmetic operations.
		public Double calculateAverageSentiment() {
			return (positiveSentiment - negativeSentiment) / wordSentiments.size();
		}

	}
}