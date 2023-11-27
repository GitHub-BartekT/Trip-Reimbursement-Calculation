package pl.iseebugs.TripReimbursementApp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iseebugs.TripReimbursementApp.model.UserCost;
import pl.iseebugs.TripReimbursementApp.model.UserCostRepository;

@Repository
public interface SqlUserCostRepository extends UserCostRepository, JpaRepository<UserCost, Integer> {
}
