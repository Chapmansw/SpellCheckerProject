/**
 * An ADT that keeps track of how many times each word has been seen.
 * Words consist only of lower-case letters.
 */
public interface WordCounts {

    /** Adds the word s if it is not already present, and increments its count. */
    void addWord(String s);

    /** Returns true if the word s has a non-zero count. */
    boolean isKnownWord(String s);

    /** Returns the count of the word s (0 if it has never been added). */
    int getWordCount(String s);

    /** Returns the number of distinct words with non-zero counts that start with s. */
    int numWordsStartingWith(String s);

    /**
     * Returns the word with the highest count and its count. Ties go to the word that comes
     * first alphabetically. Returns null if there are no words.
     */
    WordAndCount mostCommonWord();

    /** Prints each word and its count, one per line, in alphabetical order, to System.out. */
    void printWordCounts();
}