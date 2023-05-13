package info.kgeorgiy.ja.iuzeev.walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RecursiveWalk {
    public static void main(String[] args) {
        check(args, true);
    }

    protected static void check(String[] args, boolean flag) {
        try {
            if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
                throw new IOException("Wrong input arguments ");
            }

            Path inputFile = Path.of(args[0]);
            Path outputFile = Path.of(args[1]);
            if (outputFile.getParent() != null && Files.notExists(outputFile.getParent())) {
                try {
                    Files.createDirectories(outputFile.getParent());
                } catch (IOException e) {
                    throw new IOException("Can't create directory " + e.getMessage());
                }
            }

            walk(inputFile, outputFile, flag);
        } catch (IOException | InvalidPathException e) {
            System.err.println("Wrong path " + e.getMessage());
        }
    }
    protected static void walk(Path inputFile, Path outputFile, boolean rec) {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(inputFile);
             BufferedWriter writer = Files.newBufferedWriter(outputFile)) {

            FileVisitor fileVisitor = new FileVisitor(list);
            String path;

            while ((path = reader.readLine()) != null) {
                Path helper;
                try {
                    helper = Paths.get(path);
                } catch (InvalidPathException e) {
                    list.add("0".repeat(64) + " " + path);
                    continue;
                }
                if (rec) {
                    Files.walkFileTree(helper, fileVisitor);
                } else {
                    fileVisitor.visitFile(helper, null);
                }
            }

            for (String s : list) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Can't read file or write in file" + e.getMessage());
        }
    }
}
