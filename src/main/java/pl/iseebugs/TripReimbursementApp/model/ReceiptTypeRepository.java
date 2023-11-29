package pl.iseebugs.TripReimbursementApp.model;

import java.util.List;
import java.util.Optional;

public interface ReceiptTypeRepository {
    List<ReceiptType> findAll();

    Optional<ReceiptType> findById(Integer id);

    List<ReceiptType> findAllById(List<Integer> idList);

    List<ReceiptType>  findAllByUserGroups_Id(int id);

    boolean existsById(int id);

    ReceiptType save(ReceiptType entity);

    void deleteById(int id);

    void deleteAll();
}
