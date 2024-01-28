package ie.atu.sw;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static java.lang.Math.abs;

/**
 * The Runner class contains the main method to run the application.
 * 
 * @author Karolina Bronnikova
 * @version 1.0
 * @since 21
 */

public class Runner {

	/**
	 * Main method for running the Virtual Threaded Sentiment Analyzer application.
	 *
	 * @param args Command line arguments.
	 * @throws IOException If an I/O error occurs while reading or writing files.
	 */
	public static void main(String[] args) throws IOException {
		// Code for user input and setting up the sentiment analysis process...
		Scanner scanner = new Scanner(System.in);
		// Menu-driven user interface for sentiment analysis configuration
		int choice;

		IFileReader tweetsFileReader = null;

		var tweetsFile = "";
		var sentimentsFile = "";
		var outputFileName = "./out.txt";

		// Display the menu options and process user input
		do {
			// Display menu options and process user input
			System.out.println("************************************************************");
			System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
			System.out.println("*                                                          *");
			System.out.println("*             Virtual Threaded Sentiment Analyser          *");
			System.out.println("*                                                          *");
			System.out.println("************************************************************");
			System.out.println("(1) Specify a Text File");
			System.out.println("(2) Specify a URL");
			System.out.println("(3) Specify an Output File (default: ./out.txt)");
			System.out.println("(4) Configure Lexicons");
			System.out.println("(5) Run analyze");

			System.out.print("Select Option [1-5]>");
			System.out.println();

			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				System.out.println("Enter the path of the file to read>");
				tweetsFile = scanner.nextLine();
				tweetsFileReader = new LocalFileReader();
				break;
			case 2:
				System.out.println("Enter the url of the file to read>");
				tweetsFile = scanner.nextLine();
				tweetsFileReader = new UrlFileReader();
				break;
			case 3:
				System.out.println("Enter the path of the file to write>");
				outputFileName = scanner.nextLine();
				break;
			case 4:
				System.out.println("Enter the path of the file to write: ");
				sentimentsFile = scanner.nextLine();
			case 5:
				break;
			default:
				System.out.println("Invalid choice. Try again.");
			}
		} while (choice != 5);

		// Checking if a file reader is specified
		if (tweetsFileReader == null) {
			System.out.println("No file specified. Exiting.");
			System.exit(0);
		}

		var sentimentsFileReader = new LocalFileReader();
		// Reading file content using file readers
		var tweetLines = tweetsFileReader.read(tweetsFile);
		var sentimentsLines = sentimentsFileReader.read(sentimentsFile);

		Map<String, Double> sentimentsLexicon;
		// Parsing sentiment data using virtual thread executor
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			var sentimentParser = new SentimentParser(executor);
			sentimentsLexicon = sentimentParser.parse(sentimentsLines.toArray(String[]::new));
			// Shutting down executor service
			executor.shutdown();
			while (!executor.isTerminated()) {
				// waiting for all tasks to finish
			}
		} catch (Exception e) {
			throw e;
		}

		List<String> tweetWords;
		// Parsing tweet data using virtual thread executor
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			var tweetParser = new TweetParser(executor);
			tweetWords = tweetParser.parse(tweetLines.toArray(String[]::new));
			// Shutting down executor service
			executor.shutdown();
			while (!executor.isTerminated()) {
				// waiting for all tasks to finish
			}

		} catch (Exception e) {
			throw e;
		}

		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

			// Analyzing sentiments
			var sentimentAnalyzer = new SentimentAnalyzer(executor);
			var sentimentScores = sentimentAnalyzer.analyze(tweetWords, sentimentsLexicon);

			// Writing results to output file
			Output outputFile = new Output();
			outputFile.outputResultsToFile(sentimentScores, outputFileName);

			// Displaying sentiment analysis summary
			System.out.println("Sas: " + (sentimentScores.negativeSentiment + sentimentScores.positiveSentiment));
			System.out.println("Negative: " + abs(sentimentScores.negativeSentiment));
			System.out.println("Positive: " + sentimentScores.positiveSentiment);

			// Calculating and displaying average sentiment
			double averageSentiment = sentimentScores.calculateAverageSentiment();
			System.out.println("Average Sentiment: " + Math.ceil(averageSentiment));

			// Displaying word sentiments
			System.out.println("Word Sentiments:");
			sentimentScores.wordSentiments.forEach((word, score) -> System.out.println(word + ": " + score));

			// Shutting down executor service
			executor.shutdown();
			while (!executor.isTerminated()) {
				// waiting for all tasks to finish
			}
		} catch (Exception e) {
			throw e;
		} finally {
			scanner.close();
		}

	}

}