package fact.it.s2newanomliesapi.service;

import fact.it.s2newanomliesapi.dto.AnomalyRequest;
import fact.it.s2newanomliesapi.dto.AnomalyResponse;
import fact.it.s2newanomliesapi.model.Anomaly;
import fact.it.s2newanomliesapi.model.AnomalyType;
import fact.it.s2newanomliesapi.model.Train;
import fact.it.s2newanomliesapi.model.TrainTrack;
import fact.it.s2newanomliesapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AnomalyService {
    private final AnomalyRepository anomalyRepository;
    private final AnomalyTypeRepository anomalyTypeRepository;
    private final CountryRepository countryRepository;
    private final TrainRepository trainRepository;
    private final TrainTrackRepository trainTrackRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326); //4326 is used for longitude and latitude coordinate systems
    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    private String fileURL = endpointUrl + "/" + bucketName + "/";


    private boolean isSame(Coordinate coord1, Coordinate coord2, double thresholdDistance, String anomalyType1, String anomalyType2) {
        double distance = coord1.distance(coord2);
        boolean typeEquals = anomalyType1.equals(anomalyType2);
        return typeEquals && distance <= thresholdDistance;
    }

    private String generateFileName(OffsetDateTime date, String filename) {
        return date + "-" + filename.replace(" ", "_");
    }

    private AnomalyResponse mapToResponse(Anomaly anomaly) {
        return AnomalyResponse.builder()
                .photo(anomaly.getPhoto())
                .timestamp(anomaly.getTimestamp())
                .anomalyLocation(anomaly.getAnomalyLocation().toString())
                .build();
    }

    public AnomalyResponse addAnomaly(AnomalyRequest anomalyRequest, String fileName) {
        Coordinate coordinate = new Coordinate(Double.parseDouble(anomalyRequest.getLongitude()), Double.parseDouble(anomalyRequest.getLatitude()));
        Point anomalyPoint = geometryFactory.createPoint(coordinate);
        Anomaly closestAnomaly = anomalyRepository.findClosestAnomaly(anomalyPoint, anomalyRequest.getAnomalyType());

        // Define a threshold distance
        double thresholdDistanceMeters = 5.0;
        double thresholdDistance = thresholdDistanceMeters / 111000;
        //this means that if an anomaly is within 5 meters from another anomaly then the count is increased of the already existing anomaly and no new anomaly is created because it is very likely the same one.

        if (closestAnomaly != null) {
            // Check if the closest anomaly is within the threshold distance and that both anomalies have the same anomaly type
            if (isSame(coordinate, closestAnomaly.getAnomalyLocation().getCoordinate(), thresholdDistance, closestAnomaly.getAnomalyType().getName(), anomalyRequest.getAnomalyType())) {
                if (anomalyRequest.getAnomalyType().equals("Vegetation")) {
                    //increment the number of detections of that anomaly
                    int count = closestAnomaly.getCount() + 1;
                    closestAnomaly.setCount(count);
                    anomalyRepository.save(closestAnomaly);
                }
                return mapToResponse(closestAnomaly);
            } else {
                // create new anomaly
                Anomaly anomaly = new Anomaly();
                anomaly.setTimestamp(anomalyRequest.getTimestamp());
                String newFileName = generateFileName(anomalyRequest.getTimestamp(), fileName);
                anomaly.setPhoto(newFileName);
                anomaly.setCount(1);
                anomaly.setIsFixed(false);
                anomaly.setIsFalse(false);

                // Set AnomalyLocation based on coordinate
                Point anomalyLocation = geometryFactory.createPoint(coordinate);
                anomaly.setAnomalyLocation(anomalyLocation);

                //add connection to train
                Train train = trainRepository.findByName(anomalyRequest.getTrain());
                anomaly.setTrain(train);
                //add connection to anomaly type
                AnomalyType anomalyType = anomalyTypeRepository.findByName(anomalyRequest.getAnomalyType());
                anomaly.setAnomalyType(anomalyType);

                //add connection to traintrack
                //Find TrainTrack based on spatial relationship
                TrainTrack trainTrack = trainTrackRepository.findClosestTrainTrack(anomalyLocation);
                anomaly.setTrainTrack(trainTrack);
                //add connection to country
                anomaly.setCountry(countryRepository.findByGeometryContains(anomalyLocation));

                anomalyRepository.save(anomaly);
                return mapToResponse(anomaly);
            }
        }
        return null;
    }
}