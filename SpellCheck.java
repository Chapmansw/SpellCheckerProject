import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Spell checks files against the words that occur in the Wikipedia data set.
 */
public class SpellCheck {

    private WordCountsTrie trie;

    /**
     * Creates a spell checker whose dictionary is the wikipedia data.
     */
    public SpellCheck() {
        trie=new WordCountsTrie();
        Stream<String> stream=Parsing.wordsFromWikipedia("wikipedia.zip");
        stream.forEach(str -> trie.addWord(str));
    }

    /**
     * Returns the fraction of the words in the file that are misspelled, i.e.,
     * that do not occur in the Wikipedia data set. Repeated misspelled words are
     * counted each time they occur. Returns 0 for a file with no words.
     */
    public double spellCheck(String filename) {
        Stream<String> stream1=Parsing.wordsFromTextFile(filename);
        long count=stream1.count();
        if(count==0){
            return 0;
        }
        Stream<String> stream2=Parsing.wordsFromTextFile(filename);
        long countMispelled=stream2.filter(Predicate.not(trie::isKnownWord)).count();
        return countMispelled/(count+0.0);
    }

    /**
     * Returns the misspelled word that occurs most often in the file (ties go to
     * the alphabetically first word), or null if no words are misspelled.
     */
    public String mostCommonMisspelledWord(String filename) {
        WordCountsTrie fileTrie=new WordCountsTrie();
        Stream<String> words = Parsing.wordsFromTextFile(filename);
        String mostCommonMispelledWord="";
        long countW=0;
        for (String word : (Iterable<String>) words::iterator) {
            fileTrie.addWord(word);
        }
        words = Parsing.wordsFromTextFile(filename);
        for (String word : (Iterable<String>) words::iterator) {
            if(!trie.isKnownWord(word)){
                if(fileTrie.getWordCount(word)>countW){
                    mostCommonMispelledWord=word;
                    countW=fileTrie.getWordCount(word);
                }
            }
        }
        return mostCommonMispelledWord;
       
    }
    /**
     * prints the word count of every word found in the file
     * 
     */
    public static void printWordCounts(String filename) {
        WordCountsTrie fileTrie=new WordCountsTrie();
        Stream<String> words = Parsing.wordsFromTextFile(filename);
        for (String word : (Iterable<String>) words::iterator) {
            fileTrie.addWord(word);
        }
        fileTrie.printWordCounts();
    }
    /**
     * prints the number of words found in file
     * 
     */
    public static long numWords(String filename){
        Stream<String> words = Parsing.wordsFromTextFile(filename);
        return words.count();
    }

    public static void main(String[] args) {
        SpellCheck checker = new SpellCheck();
        String fileToCheck = "TransactionBean.java.txt";
        System.out.println("Hi");
        System.out.println("Fraction misspelled in " + fileToCheck + ": " + checker.spellCheck(fileToCheck));
        System.out.println("Most common misspelled word: "+ checker.mostCommonMisspelledWord(fileToCheck));

        printWordCounts(fileToCheck);
        System.out.println(numWords(fileToCheck));
    }
}