package pl.iseebugs.TripReimbursementApp.model;

import org.springframework.scheduling.config.Task;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository {

    List<UserGroup> findAll();

    Optional<UserGroup> findById(Integer i);

    boolean existsById(Integer id);

    UserGroup save(UserGroup entity);
}
