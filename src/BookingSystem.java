import java.io.*;
import java.util.*;

public class BookingSystem {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: Java BookingSystem <inputFile> <outputFile>");
        }

        String inputFile = args[0];
        String outputFile = args[1];

        FileIO fileIO = new FileIO(inputFile, outputFile);
        List<String> lines = fileIO.readFile();

        VoyageController controller = new VoyageController();
        for (String line : lines) {
            Arguments arguments = new Arguments(line, controller);
            String content = "COMMAND: " + line;
            fileIO.writeFile(content);
            content = arguments.toString();
            fileIO.writeFile(content);
        }

    }
}