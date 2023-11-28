package pl.iseebugs.TripReimbursementApp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iseebugs.TripReimbursementApp.model.ReceiptType;
import pl.iseebugs.TripReimbursementApp.model.ReceiptTypeRepository;

@Repository
public interface SqlReceiptTypeRepository extends ReceiptTypeRepository, JpaRepository<ReceiptType, Integer> {
}
