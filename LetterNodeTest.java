import

public class LetterNodeTest {

    private LetterNode node;

    @BeforeEach
    public void setUp() {
        node = new LetterNode();
    }

    @Test
    public void testInitialState() {
        assertEquals(0, node.getCount());
        assertTrue(node.isEnd(), "A newly instantiated node should have no children.");
    }

    @Test
    public void testIncrementCount() {
        node.incrementCount();
        assertEquals(1, node.getCount());
        node.incrementCount();
        assertEquals(2, node.getCount());
    }

    @Test
    public void testAddAndGetChildValid() {
        assertNull(node.getChild('a'));

        node.addChild('a');
        LetterNode child = node.getChild('a');

        assertNotNull(child);
        assertEquals(0, child.getCount());
        assertFalse(node.isEnd());
    }

    @Test
    public void testGetChildNonExistentReturnsNull() {
        node.addChild('a');
        assertNull(node.getChild('b'));
        assertNull(node.getChild('z'));
    }

    @Test
    public void testAddChildInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> node.addChild('A'));
        assertThrows(IllegalArgumentException.class, () -> node.addChild('1'));
        assertThrows(IllegalArgumentException.class, () -> node.addChild('!'));
        
    }


    @Test
    public void testGetChildInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> node.getChild('Z'));
        assertThrows(IllegalArgumentException.class, () -> node.getChild(' '));
    }

    @Test
    public void testNumWordsInSubtreeLeafNode() {
        // A single leaf node with a non-zero count
        node.incrementCount();
        assertTrue(node.isEnd());
        assertEquals(1, node.numWordsInSubtree());
    }

    @Test
    public void testNumWordsInSubtreeBranches() {
        // Build: root -> 'c' (count 1) -> 'a' (count 0) -> 't' (count 2)
        node.addChild('c');
        LetterNode cNode = node.getChild('c');
        cNode.incrementCount();

        cNode.addChild('a');
        LetterNode aNode = cNode.getChild('a');

        aNode.addChild('t');
        LetterNode tNode = aNode.getChild('t');
        tNode.incrementCount();
        tNode.incrementCount();

        // Under root, we have valid words: "c" and "cat"
        assertEquals(2, node.numWordsInSubtree());
    }

    @Test
    public void testMostCommonWordInSubtree() {
        // Branch 1: "in" with count 2
        node.addChild('i');
        LetterNode iNode = node.getChild('i');
        iNode.addChild('n');
        LetterNode inNode = iNode.getChild('n');
        inNode.incrementCount();
        inNode.incrementCount();

        // Branch 2: "is" with count 5
        iNode.addChild('s');
        LetterNode isNode = iNode.getChild('s');
        for (int k = 0; k < 5; k++) {
            isNode.incrementCount();
        }

        WordAndCount mostCommon = node.mostCommonWordInSubtree("");
        assertNotNull(mostCommon);
        // Supports either getter method or record accessor syntax
        assertEquals("is", mostCommon.word() != null ? mostCommon.word() : mostCommon.word());
        assertEquals(5, mostCommon.count() != 0 ? mostCommon.count() : mostCommon.count());
    }
}