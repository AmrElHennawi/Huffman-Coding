import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BinFileHandler {


    public static String read(String inputFilePath,InputStream is) throws IOException {
        int temp;
        String content = "";
        while ((temp = is.read()) != -1) {
            content += AddZeros.addLeadingZeros(Integer.toString(temp, 2), 8);
        }

        return content;
    }

    public static int getTableSize(String inputFilePath, InputStream is) throws IOException {
        int tableSize = is.read();
        return tableSize;
    }

    public static void write(String compressedStream, String outFileName, Map<Character, String> codes) throws IOException {
        String fileName = outFileName + ".bin";
        OutputStream os = new FileOutputStream(fileName);

        String stream = "";

        // adding table size
        stream += AddZeros.addLeadingZeros(Integer.toBinaryString(codes.size()), 8);

        for (Character character : codes.keySet()) {
            // adding code length
            stream += AddZeros.addLeadingZeros(Integer.toBinaryString(codes.get(character).length()), 8);
            // adding char
            stream += AddZeros.addLeadingZeros(Integer.toBinaryString(character), 8);
            // adding code
            stream += codes.get(character);
        }

        // adding compressed stream
        stream += compressedStream;

        // adding 8 bits by 8 bits
        int i;
        for (i = 0; i < stream.length() - 8; i += 8) {
            String window = AddZeros.addLeadingZeros(stream.substring(i, i + 8), 8);
            os.write(Integer.parseInt(window, 2));
        }
        os.write(Integer.parseInt(AddZeros.addTrailingZeros(stream.substring(i), 8), 2));
        os.close();
    }
}
