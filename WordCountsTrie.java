/**
 * An implementation of WordCounts that stores the words in a trie.
 */
public class WordCountsTrie implements WordCounts {

    LetterNode topNode;

    public WordCountsTrie(){
        topNode=new LetterNode();
    }

    @Override
    public void addWord(String s) {
        LetterNode current=topNode;
        for(char x : s.toCharArray()) {
            LetterNode temp=current.getChild(x);
            if(temp==null){
                current.addChild(x);
            }
            current = current.getChild(x);
        }
        current.incrementCount();
    }

    @Override
    public boolean isKnownWord(String s) {
        LetterNode current=topNode;
        for(char x : s.toCharArray()) {
            current = current.getChild(x);
            if(current==null) return false;//if getChild(x) is null return false
        }
        if(current.getCount()==0) return false;
        return true;
    }

    @Override
    public int getWordCount(String s) {
        LetterNode current=topNode;
        for(char x : s.toCharArray()) {
            current = current.getChild(x); //if getChild(x) is null return 0;
            if(current==null) return 0;
        }
        return current.getCount();
    }

    @Override
    public int numWordsStartingWith(String s) {
        LetterNode current=topNode;
        for(char x : s.toCharArray()) {
            current = current.getChild(x); //if getChild(x) is null return 0;
            if(current==null) return 0;
        }
        return current.numWordsInSubtree();
    }

    @Override
    public WordAndCount mostCommonWord() {
        return topNode.mostCommonWordInSubtree("");
    }

    @Override
    public void printWordCounts() {
        topNode.printWordsInSubtree("");
    }

    /**
     * Builds the example trie from the assignment instructions.
     */
    public static void main(String[] args) {
        WordCountsTrie trie = new WordCountsTrie();
        String[] words = {"a", "a", "as", "as", "as", "as", "at", "at",
                "i", "i", "i", "me", "me", "me", "me", "me", "me", "me"};
        for (String word : words) {
            System.out.print(word+", ");
            trie.addWord(word);
        }
        System.out.println("");


        trie.printWordCounts();
        System.out.println("getWordCount(\"m\") = " + trie.getWordCount("m"));
        System.out.println("getWordCount(\"a\") = " + trie.getWordCount("a"));
        System.out.println("getWordCount(\"as\") = " + trie.getWordCount("as"));
        System.out.println("isKnownWord(\"me\") = " + trie.isKnownWord("me"));
        System.out.println("isKnownWord(\"m\") = " + trie.isKnownWord("m"));
        System.out.println("isKnownWord(\"zebra\") = " + trie.isKnownWord("zebra"));
        System.out.println("numWordsStartingWith(\"a\") = " + trie.numWordsStartingWith("a"));
        System.out.println("numWordsStartingWith(\"m\") = " + trie.numWordsStartingWith("m"));
        System.out.println("numWordsStartingWith(\"as\") = " + trie.numWordsStartingWith("as"));
        System.out.println("numWordsStartingWith(\"ap\") = " + trie.numWordsStartingWith("ap"));
        System.out.println("numWordsStartingWith(\"me\") = " + trie.numWordsStartingWith("me"));
        System.out.println("numWordsStartingWith(\"i\") = " + trie.numWordsStartingWith("i"));

        System.out.println("mostCommonWord() = " + trie.mostCommonWord());
    }
}

/**
 * A node in a trie. The letter for a node is not stored in the node; it is
 * encoded by the node's index in its parent's children array.
 */
class LetterNode {

    public static final int ALPHABET_SIZE = 26;

    private int count;
    private LetterNode[] children;

    public LetterNode() {
        children = new LetterNode[ALPHABET_SIZE];

        count=0;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    public void addChild(char letter){
        if(!String.valueOf(letter).toLowerCase().equals(String.valueOf(letter))){
            throw new IllegalArgumentException("This is not lowercase");
        }
        int result=Parsing.letterToIndex(letter);
        if(result>=0 && result <ALPHABET_SIZE){
            children[result]=new LetterNode();
        }else{
            throw new IllegalArgumentException("This is not a character");
        }
    }

    /**
     * Returns the child for the given letter or null if there is no such child.
     * @throws IllegalArgumentException if letter is not a lower-case letter
     */
    public LetterNode getChild(char letter) {
        if(!String.valueOf(letter).toLowerCase().equals(String.valueOf(letter))){
            throw new IllegalArgumentException("This is not lowercase");
        }
        int result=Parsing.letterToIndex(letter);
        if(result>=0 && result <ALPHABET_SIZE){
            return children[result];
        }
        else{
            throw new IllegalArgumentException("This is not the right character");
        }

    }
    /**
     * determines if the current letternode is at the end of the try
     * 
     */
    public boolean isEnd(){
        for(LetterNode x: children){
            if(x!=null) return false;
        }
        return true;
    }

    /**
     * Returns the number of words with non-zero counts at this node and all its descendants.
     */
    public int numWordsInSubtree() {
        int countW = (count > 0) ? 1 : 0;
        for (LetterNode child : children) {
            if (child != null) {
                countW += child.numWordsInSubtree();
            }
        }
        return countW;
    }

    /**
     * Prints each word (and its count) at this node and all its descendants, in
     * alphabetical order. thisSubword is the string for the path from the root
     * of the trie to this node.
     */
    public void printWordsInSubtree(String thisSubword) {
        if(this.count!=0){
            System.out.println(thisSubword+" "+this.getCount());
        }
        for(int x=0;x<ALPHABET_SIZE;x++){
            if(children[x]!=null) {
                children[x].printWordsInSubtree(thisSubword + Parsing.indexToLetter(x));
            }
        }
    }
    /**
     * returns a wordandcount of the most common word and it's count of for this node and all it's descendants
     * 
     */
    public WordAndCount mostCommonWordInSubtree(String thisSubword){
        WordAndCount mostCommon = new WordAndCount(thisSubword,this.count);
        for (int x = 0; x < ALPHABET_SIZE; x++) {
            if (children[x] != null) {
                String childWord = thisSubword + Parsing.indexToLetter(x);
                WordAndCount childMostCommon = children[x].mostCommonWordInSubtree(childWord);

                if (childMostCommon != null && childMostCommon.count() > mostCommon.count()) {
                    mostCommon=childMostCommon;
                }
            }
        }
        return mostCommon;
    }
}
