package pl.iseebugs.TripReimbursementApp.model;

import org.springframework.scheduling.config.Task;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository {

    List<UserGroup> findAll();

    Optional<UserGroup> findById(Integer i);

    boolean existsByName(String name);

    UserGroup save(UserGroup entity);

    void delete(UserGroup entity);
}
