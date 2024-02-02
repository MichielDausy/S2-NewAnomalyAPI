package fact.it.s2newanomliesapi.controller;

import fact.it.s2newanomliesapi.dto.AnomalyRequest;
import fact.it.s2newanomliesapi.dto.AnomalyResponse;
import fact.it.s2newanomliesapi.service.AmazonClient;
import fact.it.s2newanomliesapi.service.AnomalyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class NewAnomalycontroller {
    private final AnomalyService anomalyService;
    private final AmazonClient amazonClient;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public AnomalyResponse addAnomaly(@ModelAttribute AnomalyRequest data, @RequestParam("file") MultipartFile file) {
        try {
            AnomalyResponse anomalyResponse = anomalyService.addAnomaly(data, file.getOriginalFilename());
            amazonClient.uploadFile(data.getTimestamp(), file);
            return anomalyResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}