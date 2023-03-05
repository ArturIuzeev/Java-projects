package info.kgeorgiy.ja.iuzeev.walk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class FileVisitor extends SimpleFileVisitor<Path> {

    public final List<String> list;
    public FileVisitor(List<String> list) {
        this.list = list;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        String str = "0000000000000000000000000000000000000000000000000000000000000000 " + file;
        list.add(str);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        try (InputStream reader = Files.newInputStream(file)) {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] array = new byte[1024];
            int countElements;
            while ((countElements = reader.read(array)) != -1) {
                messageDigest.update(array, 0, countElements);
            }
            byte[] bytes = messageDigest.digest();
            list.add(String.format("%0" + (bytes.length << 1) + "x", new BigInteger(1, bytes)) + " " + file);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            list.add("0000000000000000000000000000000000000000000000000000000000000000 " + file);
        }
        return FileVisitResult.CONTINUE;
    }
}
