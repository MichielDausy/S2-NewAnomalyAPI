package fact.it.s2newanomliesapi.controller;

import fact.it.s2newanomliesapi.dto.AnomalyRequest;
import fact.it.s2newanomliesapi.service.AmazonClient;
import fact.it.s2newanomliesapi.service.AnomalyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class NewAnomalycontroller {
    private AnomalyService anomalyService;
    private AmazonClient amazonClient;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public void addAnomaly(@ModelAttribute AnomalyRequest data, @RequestParam("file") MultipartFile file) {
        try {
            anomalyService.addAnomaly(data, file.getOriginalFilename());
            amazonClient.uploadFile(data.getTimestamp(), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}