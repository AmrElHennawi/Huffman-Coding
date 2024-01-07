import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Huffman {
    private Node root;
    private String text;
    private Map<Character, Integer> charFreq;
    private Map<Character, String> codes;

    Huffman() {
        codes = new HashMap<>();
    }

    private void fillFreq() {
        charFreq = new HashMap<>();
        for (char character : text.toCharArray()) {
            Integer integer = charFreq.get(character);
            charFreq.put(character, integer != null ? integer + 1 : 1); // if the char is already existed increment by
                                                                        // one else add one
        }
    }

    public String compress(String inputFilePath, String outFileName) throws IOException {
        this.text = TextFileHandler.read(inputFilePath) + "~";
        fillFreq();
        Queue<Node> queue = new PriorityQueue<>(); // to make ot easy to know the lowest freq chars
        charFreq.forEach(((character, freq) -> queue.add(new LeafNode(character, freq)))); // filling the queue

        while (queue.size() > 1) {
            queue.add(new Node(queue.poll(), queue.poll())); // creating a new node by merging the least 2 freq nodes
        }

        generateCodes(root = queue.poll(), "");

        String commpressedText = getCompressedText();

        BinFileHandler.write(commpressedText, outFileName, codes);

        return commpressedText;
    }

    private void generateCodes(Node node, String code) {
        if (node instanceof LeafNode) { // if we reached a leaf
            codes.put(((LeafNode) node).getCharacter(), code);
            return;
        }
        // recursively adding codes to the tree
        generateCodes(node.getLeft(), code.concat("0"));
        generateCodes(node.getRight(), code.concat("1"));
    }

    private String getCompressedText() {
        String compressedText = "";
        for (char character : text.toCharArray()) {
            compressedText += codes.get(character);
        }

        return compressedText;
    }





    public void printCodes() {
        for (char character : codes.keySet()) {
            System.out.println(character + "->" + codes.get(character));
        }
    }



    public String decompress(String inputFilePath, String outFileName) throws IOException {
        try (InputStream is = new FileInputStream(inputFilePath)) {

            int tableSize = BinFileHandler.getTableSize(inputFilePath, is);
            Map<String, Character> huffmanCodes = new HashMap<>();

            String content = BinFileHandler.read(inputFilePath, is);

            int i = 0; // pointer to the string
            // building the table
            for (int j = 0; j < tableSize; j++) {
                int codeSize = Integer.parseInt(content.substring(i, i + 8), 2);
                i += 8; // move to the next byte
                char character = (char) Integer.parseInt(content.substring(i, i + 8), 2);
                i += 8;// move to the next byte
                String code = content.substring(i, i + codeSize);

                huffmanCodes.put(code, character);

                i += codeSize;
            }

            // Creating the original text
            String tempCode = "";
            String originalText = "";
            for (; i < content.length(); i++) {
                tempCode += content.charAt(i);
                if (huffmanCodes.containsKey(tempCode)) {
                    if (huffmanCodes.get(tempCode).equals('~')) {
                        break;
                    }
                    originalText += huffmanCodes.get(tempCode);
                    tempCode = "";
                }
            }

            TextFileHandler.write(originalText, outFileName);

            return originalText;
        }
    }

}
