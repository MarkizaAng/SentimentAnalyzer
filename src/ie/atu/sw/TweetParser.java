package ie.atu.sw;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
/**
 * This class represents a TweetParser that parses a list of sentences into words.
 * It utilizes an ExecutorService for concurrent execution.
 */
public class TweetParser extends ExecutorAwareService {

	/**
	 * Constructor for TweetParser.
	 *
	 * @param executor The ExecutorService used for concurrent execution.
	 * @see ExecutorAwareService
	 */
	public TweetParser(ExecutorService executor) {
		super(executor);
	}

	/**
	 * Parse a list of sentences into a list of words. The time complexity of the
	 * parse method is O(n*m), where n is the number of sentences and m is the
	 * average number of words in a sentence
	 * 
	 * @param sentences The list of sentences to parse.
	 * @return The list of words.
	 * 
	 */
	public List<String> parse(String[] sentences) {
		CopyOnWriteArrayList<String> wordsList = new CopyOnWriteArrayList<>();

		for (String sentence : sentences) {
			executor.execute(() -> {
				String[] words = Arrays.stream(sentence.split("\\s+")).map(this::sanitize).toArray(String[]::new);
				wordsList.addAll(Arrays.asList(words));
			});
		}

		return wordsList;
	}

	/**
	 * Sanitize a word by removing all non-alphanumeric characters. The time
	 * complexity of the sanitize method is O(k), where k is the length of the input
	 * word
	 * 
	 * @param word The word to sanitize.
	 * @return The sanitized word.
	 */
	public String sanitize(String word) {
		return word.replaceAll("[^a-zA-Z0-9]", "");
	}
}
