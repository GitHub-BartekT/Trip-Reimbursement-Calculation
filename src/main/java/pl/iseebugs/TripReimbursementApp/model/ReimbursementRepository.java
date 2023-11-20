package pl.iseebugs.TripReimbursementApp.model;

import java.util.List;
import java.util.Optional;

public interface ReimbursementRepository {

    List<Reimbursement> findAll();

    Optional<Reimbursement> findById(Integer id);

    List<Reimbursement> findAllByUser_Id(Integer userId);

    boolean existsById(int id);

    Reimbursement save(Reimbursement entity);

    void deleteById(int id);

    void deleteAll();

}
