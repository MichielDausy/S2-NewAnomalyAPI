package fact.it.s2newanomliesapi.repository;

import fact.it.s2newanomliesapi.model.Anomaly;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, Integer> {
    @Query(value = "SELECT a FROM Anomaly a WHERE a.anomalyType.name like :anomalyType and a.isFalse = false and a.isFixed = false ORDER BY ST_DISTANCE(a.anomalyLocation, :anomalyPoint) LIMIT 1")
    Anomaly findClosestAnomaly(@Param("anomalyPoint") Point anomalyPoint, @Param("anomalyType") String anomalyType);
}
