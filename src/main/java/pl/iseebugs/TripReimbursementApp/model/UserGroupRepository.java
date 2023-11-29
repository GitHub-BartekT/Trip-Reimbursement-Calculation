package pl.iseebugs.TripReimbursementApp.model;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository {

    List<UserGroup> findAll();

    Optional<UserGroup> findById(Integer id);

    List<UserGroup> findAllById(Iterable<Integer> idList);

    List<UserGroup> findAllByReceiptTypes_Id(int id);

    boolean existsByName(String name);

    boolean existsById(int id);

    UserGroup save(UserGroup entity);

    void deleteById(int id);

    void deleteAll();
}
