package project.filemonitoring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class VirusTotalService {

    @Value("${virustotal.api.url}")
    private String vtApiUrl;

    @Value("${virustotal.api.key}")
    private String vtApiKey;

    private final RestTemplate restTemplate;

    public VirusTotalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String checkFile(String sha256Hash) {
        URI uri = UriComponentsBuilder.fromHttpUrl(vtApiUrl)
                .queryParam("apikey", vtApiKey)
                .queryParam("resource", sha256Hash)
                .build()
                .toUri();

        Map<String, Object> response = restTemplate.getForObject(uri, HashMap.class);

        if(response != null && response.containsKey("positives")) {
            int positives = (Integer) response.get("positives");
            if(positives > 0) {
                System.out.println("File " + sha256Hash + " is suspicious");
                return "suspicious";
            }
        }
        System.out.println("File with hash " + sha256Hash + " is ok.");
        return "ok";
    }

}
