package ie.atu.sw;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The SentimentParser class parses a list of words into a map of words to sentiment values.
 */
public class SentimentParser extends ExecutorAwareService {
	/**
     * Constructor for SentimentParser.
     *
     * @param executor The ExecutorService used for concurrent execution.
     */
    public SentimentParser(ExecutorService executor) {
        super(executor);
    }

    /**
     * Parse a list of words into a map of words to sentiment values.
     * The time complexity of the parse method is O(n)
     * @param input The list of words to parse.
     * @return The map of words to sentiment values.
     */
    public ConcurrentMap<String, Double> parse(String[] input) {
        ConcurrentMap<String, Double> map = new ConcurrentHashMap<>();
        for (String s : input) {
            executor.execute(() -> {
                String[] parts = s.split(",");
                if (parts.length == 2) {
                    String key = parts[0];
                    Double value = Double.valueOf(parts[1]);
                    map.put(key, value);
                }
            });
        }

        return map;
    }
}