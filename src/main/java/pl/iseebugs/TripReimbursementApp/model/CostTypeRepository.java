package pl.iseebugs.TripReimbursementApp.model;

import java.util.List;
import java.util.Optional;

public interface CostTypeRepository {
    List<CostType> findAll();

    Optional<CostType> findById(Integer id);

    boolean existsById(int id);

    CostType save(CostType entity);

    void deleteById(int id);

    void deleteAll();
}
