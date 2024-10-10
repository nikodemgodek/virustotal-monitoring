package project.filemonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.filemonitoring.FileHashCalculator;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DirectoryMonitorService {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private VirusTotalService virusTotalService;

    public void watchDirectoryPath(Path path) {
        executorService.submit(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);

                System.out.println("Monitoring folder: " + path);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        Path changedFilePath = path.resolve((Path) event.context());

                        if(Files.isDirectory(changedFilePath)) continue;

                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            String fileHash = calculateFileHash(changedFilePath);
                            virusTotalService.checkFile(fileHash);
                            System.out.println("File added: " + changedFilePath);
                        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                            System.out.println("File deleted: " + changedFilePath);
                        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            String fileHash = calculateFileHash(changedFilePath);
                            virusTotalService.checkFile(fileHash);
                            System.out.println("File modified: " + changedFilePath);
                        }
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        System.out.println("Klucz monitorujący folder został unieważniony");
                        break;
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private String calculateFileHash(Path filePath) {
        try {
            FileHashCalculator fileHashCalculator = FileHashCalculator.createSha256Calculator();
            String sha256Hash = fileHashCalculator.calculateHash(filePath);
            return sha256Hash;
        } catch (IOException | NoSuchAlgorithmException e) {
            return "Error";
        }
    }
}