package project.filemonitoring;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHashCalculator {

    private static final int BUFFER_SIZE = 1024;
    private final MessageDigest messageDigest;

    public FileHashCalculator(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }

    public static FileHashCalculator createSha256Calculator() throws NoSuchAlgorithmException {
        return new FileHashCalculator(MessageDigest.getInstance("SHA-256"));
    }

    public String calculateHash(Path filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
            byte[] byteArray = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = fis.read(byteArray)) != -1) {
                messageDigest.update(byteArray, 0, bytesRead);
            }

            return formatHash(messageDigest.digest());
        }
    }

    private String formatHash(byte[] hashBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
