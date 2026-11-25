import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class WordCountsTrieTest {

    private WordCountsTrie trie;

    @BeforeEach
    public void setUp() {
        trie = new WordCountsTrie();
    }

    @Test
    public void testEmptyTrie() {
        assertFalse(trie.isKnownWord("a"));
        assertEquals(0, trie.getWordCount("a"));
        assertEquals(0, trie.numWordsStartingWith("a"));
    }

    @Test
    public void testAddAndGetWordCountSingleWord() {
        trie.addWord("apple");
        trie.addWord("apple");

        assertTrue(trie.isKnownWord("apple"));
        assertEquals(2, trie.getWordCount("apple"));

        // Prefixes should not be known words unless explicitly added
        assertFalse(trie.isKnownWord("app"));
        assertEquals(0, trie.getWordCount("app"));
    }

    @Test
    public void testPrefixAsKnownWord() {
        trie.addWord("app");
        trie.addWord("apple");

        assertTrue(trie.isKnownWord("app"));
        assertTrue(trie.isKnownWord("apple"));
        assertEquals(1, trie.getWordCount("app"));
        assertEquals(1, trie.getWordCount("apple"));
    }

    @Test
    public void testWordNotFoundGracefulHandling() {
        trie.addWord("hello");

        assertFalse(trie.isKnownWord("world"));
        assertFalse(trie.isKnownWord("hell"));
        assertFalse(trie.isKnownWord("helloo"));

        assertEquals(0, trie.getWordCount("world"));
        assertEquals(0, trie.numWordsStartingWith("z"));
    }

    @Test
    public void testNumWordsStartingWith() {
        trie.addWord("cat");
        trie.addWord("car");
        trie.addWord("cart");
        trie.addWord("dog");

        assertEquals(3, trie.numWordsStartingWith("ca"));
        assertEquals(2, trie.numWordsStartingWith("car"));
        assertEquals(1, trie.numWordsStartingWith("cat"));
        assertEquals(1, trie.numWordsStartingWith("dog"));
        assertEquals(0, trie.numWordsStartingWith("m"));
    }

    @Test
    public void testMostCommonWord() {
        String[] words = {"a", "a", "as", "as", "as", "as", "at", "at",
                          "i", "i", "i", "me", "me", "me", "me", "me", "me", "me"};
        for (String w : words) {
            trie.addWord(w);
        }

        WordAndCount mostCommon = trie.mostCommonWord();
        assertNotNull(mostCommon);

        String resultWord = mostCommon.word() != null ? mostCommon.word() : mostCommon.word();
        int resultCount = mostCommon.count() != 0 ? mostCommon.count() : mostCommon.count();

        assertEquals("me", resultWord);
        assertEquals(7, resultCount);
    }

    @Test
    public void testCaseSensitivityThrowsException() {
        // Uppercase or special characters should propagate IllegalArgumentException from LetterNode
        assertThrows(IllegalArgumentException.class, () -> trie.addWord("Apple"));
        assertThrows(IllegalArgumentException.class, () -> trie.isKnownWord("CAT"));
        assertThrows(IllegalArgumentException.class, () -> trie.getWordCount("123"));
    }
}