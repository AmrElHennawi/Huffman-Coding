import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Huffman h = new Huffman(//filePath);
        h.compress();
        h.decompress(//filePath);
    }
}