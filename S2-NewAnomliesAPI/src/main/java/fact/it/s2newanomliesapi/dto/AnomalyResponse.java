package fact.it.s2newanomliesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnomalyResponse {
    private Integer id;
    private OffsetDateTime timestamp;
    private String anomalyLocation;
    private String photo;
    private Boolean isFixed;
    private Boolean isFalse;
    private Integer count;
}
