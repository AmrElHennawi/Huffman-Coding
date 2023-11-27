import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Huffman {
    private Node root;
    private final String text;
    private Map<Character, Integer> charFreq;
    private Map<Character,String> codes;

    Huffman(String filePath){
        this.text = readFileToString(filePath) + "~";
        fillFreq();
        codes = new HashMap<>();
    }

    private void fillFreq(){
        charFreq = new HashMap<>();
        for (char character:text.toCharArray()) {
            Integer integer = charFreq.get(character);
            charFreq.put(character,integer != null ? integer+1 : 1); //if the char is already existed increment by one else add one
        }
    }

    public String compress() throws IOException {
        Queue<Node> queue = new PriorityQueue<>(); //to make ot easy to know the lowest freq chars
        charFreq.forEach(((character, freq) -> queue.add(new LeafNode(character,freq)))); //filling the queue

        while(queue.size() > 1){
            queue.add(new Node(queue.poll(),queue.poll())); //creating a new node by merging the least 2 freq nodes
        }

        generateCodes(root = queue.poll(), "");

        String commpressedText = getCompressedText();

        writeBinFile(commpressedText);

        return commpressedText;
    }

    private void generateCodes(Node node, String code){
        if(node instanceof LeafNode){ //if we reached a leaf
            codes.put(((LeafNode)node).getCharacter(),code);
            return;
        }
        //recursively adding codes to the tree
        generateCodes(node.getLeft(),code.concat("0"));
        generateCodes(node.getRight(),code.concat("1"));
    }

    private String getCompressedText(){
        String compressedText = "";
        for (char character: text.toCharArray()) {
            compressedText += codes.get(character);
        }

        return compressedText;
    }

    public static String readFileToString(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return null;
        }

        // Remove the last newline character
        if (!content.isEmpty()) {
            content.setLength(content.length() - 1);
        }

        return content.toString();
    }

    private String addLeadingZeros(String binaryNumber, int desiredLength) {
        // Make sure the binaryNumber is not longer than the desired length
        if (binaryNumber.length() > desiredLength) {
            throw new IllegalArgumentException("Binary number is longer than the desired length");
        }

        // Calculate the number of leading zeros needed
        int numberOfZeros = desiredLength - binaryNumber.length();

        // Add leading zeros
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numberOfZeros; i++) {
            result.append("0");
        }
        result.append(binaryNumber);

        return result.toString();
    }

    private String addTrailingZeros(String binaryNumber, int desiredLength) {
        // Make sure the binaryNumber is not longer than the desired length
        if (binaryNumber.length() > desiredLength) {
            throw new IllegalArgumentException("Binary number is longer than the desired length");
        }

        // Calculate the number of trailing zeros needed
        int numberOfZeros = desiredLength - binaryNumber.length();

        // Add trailing zeros
        StringBuilder result = new StringBuilder(binaryNumber);
        for (int i = 0; i < numberOfZeros; i++) {
            result.append("0");
        }

        return result.toString();
    }


    public void printCodes(){
        for (char character: codes.keySet()) {
            System.out.println(character + "->" + codes.get(character));
        }
    }

    public void writeBinFile(String compressedStream) throws IOException {
        String fileName = "compressed.bin";
        OutputStream os = new FileOutputStream(fileName);

        String stream = "";

        //adding table size
        stream += addLeadingZeros(Integer.toBinaryString(codes.size()),8);

        for(Character character: codes.keySet()){
            //adding code length
            stream += addLeadingZeros(Integer.toBinaryString(codes.get(character).length()),8);
            //adding char
            stream += addLeadingZeros(Integer.toBinaryString(character),8);
            //adding code
            stream += codes.get(character);
        }

        //adding compressed stream
        stream += compressedStream;

        System.out.println(stream);

        //adding 8 bits by 8 bits
        int i;
        for(i = 0; i < stream.length() - 8; i += 8)
        {
            String window = addLeadingZeros(stream.substring(i,i+8) , 8);
            os.write(Integer.parseInt(window,2));
        }
        os.write(Integer.parseInt(addTrailingZeros(stream.substring(i),8),2));
        os.close();
    }

    public String decompress(String filepath) throws IOException {
        try (InputStream is = new FileInputStream(filepath)) {

            int tableSize = is.read();
            Map<String, Character> huffmanCodes = new HashMap<>();

            int temp;
            String content = "";
            while ((temp = is.read()) != -1) {
                content += addLeadingZeros(Integer.toString(temp, 2),8);
            }

            int i = 0; //pointer to the string
            //building the table
            for(int j = 0; j < tableSize; j++){
                int codeSize = Integer.parseInt(content.substring(i, i+8),2);
                i+=8; //move to the next byte
                char character = (char) Integer.parseInt(content.substring(i, i+8),2);
                i+=8;//move to the next byte
                String code = content.substring(i,i+codeSize);

                huffmanCodes.put(code,character);

                i+=codeSize;
            }

            //Creating the original text
            String tempCode = "";
            String originalText = "";
            for(; i<content.length(); i++){
                tempCode += content.charAt(i);
                if(huffmanCodes.containsKey(tempCode)){
                    if(huffmanCodes.get(tempCode).equals('~'))
                    {
                        break;
                    }
                    originalText += huffmanCodes.get(tempCode);
                    tempCode = "";
                }
            }

            writeTxtFile(originalText);

            return originalText;
        }
    }

    private void writeTxtFile(String input){
        try (FileWriter writer = new FileWriter("decompressed.txt")) {
            writer.write(input);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

}
