package fact.it.s2newanomliesapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;

@Entity
@Table(name = "anomaly")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"anomalyType", "train", "sign", "country", "trainTrack"}, allowSetters = true)
public class Anomaly {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private OffsetDateTime timestamp;
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point anomalyLocation;
    private String photo = "2024-02-01T15:11:30.663Z-trainRailImage2.png";
    private Boolean isFixed;
    private Boolean isFalse;
    private Integer count;
    @ManyToOne
    @JoinColumn(name="trainId")
    private Train train;
    @ManyToOne
    @JoinColumn(name="typeId")
    private AnomalyType anomalyType;
    @ManyToOne
    @JoinColumn(name="countryId")
    private Country country;
    @ManyToOne
    @JoinColumn(name="trainTrackId")
    private TrainTrack trainTrack;
}
