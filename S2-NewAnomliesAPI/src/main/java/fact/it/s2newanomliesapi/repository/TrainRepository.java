package fact.it.s2newanomliesapi.repository;

import fact.it.s2newanomliesapi.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Integer> {
    Train findByName(String name);
}
