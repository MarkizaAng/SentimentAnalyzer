package ie.atu.sw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * The UrlFileReader class implements the IFileReader interface to read files
 * from a URL. Time complexity for read method: O(n), where n is the number of
 * lines in the file.
 * 
 */
public class UrlFileReader implements IFileReader {
	/**
	 * Reads a file from a URL.
	 *
	 * @param filePath the URL of the file to read.
	 * @return a list of lines from the file.
	 * @throws IOException If an I/O error occurs while reading the file.
	 * @see IFileReader
	 */
	@Override
	public List<String> read(String filePath) throws IOException {
		URL url = new URL(filePath);
		URLConnection conn = url.openConnection();
		List<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		}

		return lines;
	}
}