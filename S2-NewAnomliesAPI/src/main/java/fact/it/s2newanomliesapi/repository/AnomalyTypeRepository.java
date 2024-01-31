package fact.it.s2newanomliesapi.repository;

import fact.it.s2newanomliesapi.model.AnomalyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnomalyTypeRepository extends JpaRepository<AnomalyType, Integer> {
    AnomalyType findByName(String name);
}
