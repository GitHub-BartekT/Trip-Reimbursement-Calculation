package pl.iseebugs.TripReimbursementApp.model;

import java.util.List;
import java.util.Optional;

public interface UserCostRepository {
    List<UserCost> findAll();

    Optional<UserCost> findById(Integer id);

    boolean existsById(int id);

    UserCost save(UserCost entity);

    void deleteById(int id);

    void deleteAll();
}
