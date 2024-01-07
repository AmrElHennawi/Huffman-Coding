import java.io.*;

public class TextFileHandler {
    public static String read(String inputFileName) throws IOException {
        String content = "";
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + inputFileName);
        } catch (IOException e) {
            System.err.println("An error occurred while reading from the file: " + e.getMessage());
        }
        return content;
    }

    public static void write(String input, String outFileName) {
        try (FileWriter writer = new FileWriter(outFileName + ".txt")) {
            writer.write(input);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
