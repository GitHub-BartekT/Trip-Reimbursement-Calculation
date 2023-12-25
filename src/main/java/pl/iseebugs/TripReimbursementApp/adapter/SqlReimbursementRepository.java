package pl.iseebugs.TripReimbursementApp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iseebugs.TripReimbursementApp.model.Reimbursement;
import pl.iseebugs.TripReimbursementApp.model.ReimbursementRepository;

@Repository
public interface SqlReimbursementRepository extends ReimbursementRepository, JpaRepository<Reimbursement, Integer> {

}
