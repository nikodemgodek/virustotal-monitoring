package project.filemonitoring.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.filemonitoring.service.DirectoryMonitorService;

import java.nio.file.Paths;

@Component
public class DirectoryWatcherRunner implements CommandLineRunner {

    @Autowired
    private DirectoryMonitorService directoryMonitorService;

    @Override
    public void run(String... args) throws Exception {
        String folderPath = "C:\\Users\\nikodem\\Desktop";

        directoryMonitorService.watchDirectoryPath(Paths.get(folderPath));
    }
}