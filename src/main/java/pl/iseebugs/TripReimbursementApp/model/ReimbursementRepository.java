package pl.iseebugs.TripReimbursementApp.model;

import java.util.List;
import java.util.Optional;

public interface ReimbursementRepository {

    List<Reimbursement> findAll();

    Optional<Reimbursement> findById(Integer id);

    List<Reimbursement> findAllByUser_Id(Integer userId);

    List<Reimbursement>  findAllByPushedToAcceptAndUser_Id(boolean pushedToAccept, int user_id);

    boolean existsById(int id);

    boolean existsByUser_Id(int id);

    Reimbursement save(Reimbursement entity);

    void deleteById(int id);

    void deleteAll();

}
