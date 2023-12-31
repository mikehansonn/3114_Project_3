import student.TestCase;

/**
 * @author {Your Name Here}
 * @version {Put Something Here}
 */
public class QuicksortTest extends TestCase {
    private CheckFile fileChecker;

    /**
     * Sets up the tests that follow. In general, used for initialization.
     */
    public void setUp() {
        fileChecker = new CheckFile();
    }

    /**
     * This method is a demonstration of the file generator and file checker
     * functionality. It calles generateFile to create a small "ascii" file.
     * It then calls the file checker to see if it is sorted (presumably not
     * since we don't call a sort method in this test, so we assertFalse).
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testFileGenerator()
        throws Exception
    {
        String[] args = new String[3];
        args[0] = "input.txt";
        args[1] = "1000";
        args[2] = "statFile.txt";
        Quicksort.generateFile("input.txt", "100", 'a');
        assertFalse(fileChecker.checkFile("input.txt"));
    }

    /**
     * Get code coverage of the class declaration.
     * @throws Exception 
     */
    public void testQInit() throws Exception {
        Quicksort tree = new Quicksort("input.txt", 10, "input.txt");
        assertNotNull(tree);
        String[] args = {"input.txt", "10", "input.txt"};
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("input.txt"));
    }
}