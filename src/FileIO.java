import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
    private String inputFile;
    private String outputFile;
    private boolean isFirstWrite;

    public FileIO (String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.isFirstWrite = false;
    }

    public List<String> readFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        return lines;
    }

    public void writeFile(String content) {
        boolean append = isFirstWrite;
        String filename = outputFile;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            if (isFirstWrite) { writer.newLine(); }
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isFirstWrite = true;
    }
}
