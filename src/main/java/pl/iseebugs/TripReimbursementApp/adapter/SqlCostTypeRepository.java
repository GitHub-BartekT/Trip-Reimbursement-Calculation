package pl.iseebugs.TripReimbursementApp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iseebugs.TripReimbursementApp.model.CostType;
import pl.iseebugs.TripReimbursementApp.model.CostTypeRepository;

@Repository
public interface SqlCostTypeRepository extends CostTypeRepository, JpaRepository<CostType, Integer> {
}
