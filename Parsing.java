import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Static helpers for converting between letters and array indices, and for
 * reading words out of data files.
 *
 * The word streams hold resources (open files), so use them in a
 * try-with-resources block:
 *
 *     try (Stream<String> words = Parsing.wordsFromTextFile("DATA.txt")) {
 *         for (String word : (Iterable<String>) words::iterator) {
 *             ...
 *         }
 *     }
 *
 * A Stream is not itself Iterable, so the enhanced for loop needs the
 * {@code (Iterable<String>) words::iterator} cast shown above. Alternatively,
 * use {@code words.forEach(word -> ...)}.
 * 
 * @author Claude code
 */
public class Parsing {

    // Anything that isn't a letter or an apostrophe separates words.
    private static final Pattern WORD_SEPARATOR = Pattern.compile("[^a-z']+");

    /**
     * Converts a lower-case letter to its index in the alphabet ('a' -> 0, 'b' -> 1, ...).
     * Returns -1 if the character is not a lower-case letter.
     */
    public static int letterToIndex(char letter) {
        if (letter < 'a' || letter > 'z') {
            return -1;
        }
        return letter - 'a';
    }

    /**
     * Converts an index in the alphabet to its lower-case letter (0 -> 'a', 1 -> 'b', ...).
     */
    public static char indexToLetter(int index) {
        if (index < 0 || index > 'z' - 'a') {
            throw new IllegalArgumentException("Index out of range: " + index);
        }
        return (char) ('a' + index);
    }

    /**
     * Returns a Stream of the lower-case words in a plain text file.
     */
    public static Stream<String> wordsFromTextFile(String filename) {
        try {
            return wordsFromInputStream(new FileInputStream(filename));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Returns a Stream of the lower-case words in the Wikipedia data set.
     * The zip file should contain a single text file in which each article is
     * a title line, a line of article text, and a blank line.
     * Article titles are included as words.
     */
    public static Stream<String> wordsFromWikipedia(String zipFilename) {
        try {
            ZipFile zip = new ZipFile(zipFilename);
            ZipEntry entry = zip.stream()
                    .filter(e -> !e.isDirectory())
                    .findFirst()
                    .orElseThrow(() -> new IOException("No files in " + zipFilename));
            return wordsFromInputStream(zip.getInputStream(entry))
                    .onClose(() -> {
                        try {
                            zip.close();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Splits a line of text into lower-case words: letters are lower-cased,
     * apostrophes are dropped (so "won't" becomes "wont"), and every other
     * character separates words.
     */
    public static Stream<String> wordsFromLine(String line) {
        return Arrays.stream(WORD_SEPARATOR.split(line.toLowerCase()))
                .map(word -> word.replace("'", ""))
                .filter(word -> !word.isEmpty());
    }

    private static Stream<String> wordsFromInputStream(InputStream in) {
        // InputStreamReader replaces malformed bytes instead of throwing, so this
        // works for both UTF-8 and ANSI files; non-ASCII characters just become
        // word separators.
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        return reader.lines()
                .flatMap(Parsing::wordsFromLine)
                .onClose(() -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
    }
}