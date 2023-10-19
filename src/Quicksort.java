/**
 * implement a quicksort and
 * bufferpool to sort an index
 * file of characters or binary
 */

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The class containing the main method.
 *
 * @author mikehanson matt02
 * @version 10/18/23
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

    // need to make sure that pivot is swapped to the end
    private void quicksort(int i, int j) throws Exception {
        int pivotIndex = findpivot(i, j);
        short pivot = getShort(pivotIndex);
        swap(pivotIndex, j);
        int k = partition(i, j - 1, pivot);
        swap(k, j);
        if ((k - i) > 1) quicksort(i, k - 1);
        if ((j - k) > 1) quicksort(k + 1, j);
    }

    private int findpivot(int i, int j) throws Exception {
        int mid = (i + j) / 2;

        short first = getShort(i);
        short middle = getShort(mid);
        short last = getShort(j);

        if ((first >= middle && first <= last) || (first >= last && first <= middle)) {
            return i;
        } else if ((middle >= first && middle <= last) || (middle >= last && middle <= first)) {
            return mid;
        } else {
            return j;
        }
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
        bufferPool.markAsDirty(blockIndex); 
    }

    private void swap(int i, int j) throws Exception {
        short temp = getShort(i);
        setShort(i, getShort(j));
        setShort(j, temp);  
        int blockIndexI = i / 1024;
        int blockIndexJ = j / 1024;
        bufferPool.markAsDirty(blockIndexI);  // Mark the block as dirty
        if (blockIndexI != blockIndexJ) {
            bufferPool.markAsDirty(blockIndexJ); // Mark the block as dirty
        }
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