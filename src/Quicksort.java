/**
 * {Project Description Here}
 */

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The class containing the main method.
 *
 * @author {Your Name Here}
 * @version {Put Something Here}
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class Quicksort {
<<<<<<< HEAD
=======
    private BufferPool bufferPool;
    private int numBuffers;
    private String statFileName;
    private long startTime, endTime;
    private int cacheHits = 0, diskReads = 0, diskWrites = 0;
    
    
    public Quicksort(String dataFileName, int numBuffers, String statFileName) throws Exception {
        this.bufferPool = new BufferPool(dataFileName, numBuffers);
        this.numBuffers = numBuffers;
        this.statFileName = statFileName;
    }

>>>>>>> b6e350dac14f6dc5c8d159c189b8278f4e858778
    /**
     * This method is used to generate a file of a certain size, containing a
     * specified number of records.
     *
     * @param filename the name of the file to create/write to
     * @param blockSize the size of the file to generate
     * @param format the format of file to create
     * @throws IOException throw if the file is not open and proper
     */
    public static void generateFile(String filename, String blockSize,
        char format) throws IOException {
        FileGenerator generator = new FileGenerator();
        String[] inputs = new String[3];
        inputs[0] = "-" + format;
        inputs[1] = filename;
        inputs[2] = blockSize;
        generator.generateFile(inputs);
    }
    
    public void sort() throws Exception {
        startTime = System.currentTimeMillis();
        // Calculate the number of items based on file size
        int numItems = (int) (bufferPool.getFileSize() / 4) - 1;
        quicksort(0, numItems);
        endTime = System.currentTimeMillis();
        bufferPool.flushAll();
        // TODO: Write statistics to statFileName
    }

    private void quicksort(int i, int j) throws Exception {
        int pivotIndex = findpivot(i, j);
        short pivot = getShort(pivotIndex);
        int k = partition(i, j - 1, pivot);
        setShort(k, pivot);
        if ((k - i) > 1) quicksort(i, k - 1);
        if ((j - k) > 1) quicksort(k + 1, j);
    }

    private int findpivot(int i, int j) {
        return (i + j) / 2;
    }

    private int partition(int left, int right, short pivot) throws Exception {
        while (left <= right) {
            while (getShort(left) < pivot) left++;
            while (right >= left && getShort(right) >= pivot) right--;
            if (right > left) swap(left, right);
        }
        return left;
    }

    private short getShort(int index) throws Exception {
        int blockIndex = index / 1024;
        int offset = (index % 1024) * 4;
        byte[] block = bufferPool.getBlock(blockIndex);
        ByteBuffer wrapped = ByteBuffer.wrap(block, offset, 2);
        return wrapped.getShort();
    }

    private void setShort(int index, short value) throws Exception {
        int blockIndex = index / 1024;
        int offset = (index % 1024) * 4;
        byte[] block = bufferPool.getBlock(blockIndex);
        ByteBuffer wrapped = ByteBuffer.wrap(block);
        wrapped.putShort(offset, value);
    }

    private void swap(int i, int j) throws Exception {
        short temp = getShort(i);
        setShort(i, getShort(j));
        setShort(j, temp);
    }
    
    

    public static void quicksort(Comparable[] A, int i, int j) {
        if (i < j) {
            int pivotindex = findpivot(A, i, j);
            Swap.swap(A, pivotindex, j);
            int k = partition(A, i, j - 1, A[j]);
            Swap.swap(A, k, j);
            if ((k - i) > 1) {
                quicksort(A, i, k - 1);
            }
            if ((j - k) > 1) {
                quicksort(A, k + 1, j);
            }
        }
    }

    private static int findpivot(Comparable[] A, int i, int j) {
        return (i + j) / 2;
    }

    private static int partition(Comparable[] A, int left, int right, Comparable pivot) {
        while (left <= right) {
            while (A[left].compareTo(pivot) < 0) {
                left++;
            }
            while ((right >= left) && (A[right].compareTo(pivot) >= 0)) {
                right--;
            }
            if (right > left) {
                Swap.swap(A, left, right);
            }
        }
        return left;
    }

    /**
     * @param args
     *      Command line parameters.
     */
    public static void main(String[] args) {
        try {
            String dataFileName = args[0];
            int numBuffers = Integer.parseInt(args[1]);
            String statFileName = args[2];
            Quicksort quicksort = new Quicksort(dataFileName, numBuffers, statFileName);
            quicksort.sort();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}

class Swap {
    public static void swap(Comparable[] arr, int i, int j) {
        Comparable temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
