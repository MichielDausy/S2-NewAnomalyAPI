package fact.it.s2newanomliesapi.repository;

import fact.it.s2newanomliesapi.model.Country;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    @Query("SELECT c FROM Country c WHERE CONTAINS(c.countryArea, :point) = true")
    Country findByGeometryContains(@Param("point") Point point);
}
