package ie.atu.sw;

import java.io.IOException;
import java.util.List;

/**
 * The interface IFileReader specifies the contract for classes that read files.
 * Time complexity for read method: O(n), where n is the size or complexity of
 * the file being read.
 */

public interface IFileReader {

	/**
	 * Reads the content of a file specified by the provided file path.
	 *
	 * @param filePath The path to the file to be read.
	 * @return A list of strings representing the lines read from the file.
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	List<String> read(String filePath) throws IOException;
}