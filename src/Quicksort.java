/**
 * {Project Description Here}
 */

import java.io.IOException;

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
        // This is the main file for the program.
    }
}

class Swap {
    public static void swap(Comparable[] arr, int i, int j) {
        Comparable temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
