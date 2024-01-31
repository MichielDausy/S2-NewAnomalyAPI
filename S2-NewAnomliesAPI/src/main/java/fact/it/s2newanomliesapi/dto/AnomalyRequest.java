package fact.it.s2newanomliesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnomalyRequest {
    private Integer id;
    private OffsetDateTime timestamp;
    private String longitude;
    private String latitude;
    private String anomalyType;
    private String train;
}
