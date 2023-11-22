package pl.iseebugs.TripReimbursementApp.model;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    List<User> findAll();

    List<User> findAllByUserGroup_Id(Integer userGroupId);

    boolean existsByUserGroup_Id(int id);

    Optional<User> findById(Integer id);

    boolean existsById (int id);

    User save(User entity);

    void deleteById(int id);

    void deleteAll();
}
