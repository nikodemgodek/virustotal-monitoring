package project.filemonitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.filemonitoring.service.VirusTotalService;

@RestController
@RequestMapping("/api/v1")
public class VirusTotalController {

    @Autowired
    private VirusTotalService virusTotalService;

    @GetMapping("/alert")
    public String checkFile(@RequestParam("hash") String sha256hash) {
        return virusTotalService.checkFile(sha256hash);
    }
}
