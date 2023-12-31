import java.io.IOException;
import java.io.RandomAccessFile;

public class BufferPool {
    private final int blockSize = 4096; // Size of each block in bytes
    private final int numBuffers; // Number of buffers in the pool
    private final RandomAccessFile diskFile; // The disk file to read from and write to

    private Node head, tail; // Head and tail of the LRU list
    private Node[] allNodes;// Array to keep track of block indices
    private int currentBuffers; // Current number of buffers in use

    private class Node {
        int blockIndex;
        byte[] blockData;
        boolean isDirty;
        Node prev, next;
    }

    public BufferPool(String fileName, int numBuffers) throws Exception {
        this.numBuffers = numBuffers;
        this.currentBuffers = 0;
        this.diskFile = new RandomAccessFile(fileName, "rw");
        this.allNodes = new Node[numBuffers];
    }

    public byte[] getBlock(int blockIndex) throws Exception {   
        Node node = null;
        for (Node n : allNodes) {
            if (n != null && n.blockIndex == blockIndex) {
                node = n;
                break;
            }
        }

        if (node != null) {
            moveToHead(node);
            return node.blockData;
        } else {
            byte[] block = new byte[blockSize];
            readBlockFromDisk(blockIndex, block);

            if (currentBuffers < numBuffers) {
                node = new Node();
                node.blockData = block;
                node.blockIndex = blockIndex;
                node.isDirty = false;
                allNodes[currentBuffers] = node;
                currentBuffers++;
            } else {
                // Evict the least recently used block
                Node toEvict = tail;
                if (toEvict.isDirty) {
                    writeBlockToDisk(toEvict.blockIndex, toEvict.blockData);
                }
                
                int indexToReplace = 0;
                for (int i = 0; i < allNodes.length; i++) {
                    if (allNodes[i] == toEvict) {
                        indexToReplace = i;
                        break;
                    }
                }
             
                node = toEvict;
                node.blockData = block;
                node.blockIndex = blockIndex;
                node.isDirty = false;
                allNodes[indexToReplace] = node;
            }

            moveToHead(node);
            return block;
        }
    }

    private void moveToHead(Node node) {
        if (node == head) return;

        if (node.prev != null) node.prev.next = node.next;
        if (node.next != null) node.next.prev = node.prev;
        if (node == tail) tail = node.prev;

        node.prev = null; 
        node.next = head;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = head;
    }

    private void readBlockFromDisk(int blockIndex, byte[] block) throws Exception {
        diskFile.seek(blockIndex * blockSize);
        diskFile.read(block);
    }

    private void writeBlockToDisk(int blockIndex, byte[] block) throws Exception {
        diskFile.seek(blockIndex * blockSize);
        diskFile.write(block);
    }

    public void flushAll() throws Exception {
        Node node = head;
        while (node != null) {
            if (node.isDirty) {
                writeBlockToDisk(node.blockIndex, node.blockData);
            }
            node = node.next;
        }
    }
    
    public long getFileSize() throws Exception {
        return diskFile.length();
    }
    
    public void markAsDirty(int blockIndex) {
        for (Node node : allNodes) {
            if (node != null && node.blockIndex == blockIndex) {
                node.isDirty = true;
                break;
            }
        }
    }
    
    public void close() throws IOException {
        // Close the RandomAccessFile here
        diskFile.close();
    }

}