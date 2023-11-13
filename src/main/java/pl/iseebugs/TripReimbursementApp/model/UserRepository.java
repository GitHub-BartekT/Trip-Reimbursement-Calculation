package pl.iseebugs.TripReimbursementApp.model;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    List<User> findAll();

    List<User> findAllAndUserGroup_Id(Integer userGroupId);

    Optional<User> findById(Integer id);

    boolean existsByName(String name);

    User save(User entity);

    void deleteById(int id);

    void deleteAll();
}
