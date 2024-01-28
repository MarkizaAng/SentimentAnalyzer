package ie.atu.sw;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

/**
 * The LocalFileReader class implements the IFileReader interface
 * to read files from the local file system.
 * Time complexity for read method: O(n), where n is the number of lines in the file.
 */

public class LocalFileReader implements IFileReader {
    /**
     * Reads a file from the local file system
     * @param filePath the path of the file to read
     * @return a list of lines from the file
     * @see IFileReader
     */
    @Override
    public List<String> read(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
}