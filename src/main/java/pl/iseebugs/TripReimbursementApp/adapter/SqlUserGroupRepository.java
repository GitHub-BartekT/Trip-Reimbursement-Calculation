package pl.iseebugs.TripReimbursementApp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;

@Repository
public interface SqlUserGroupRepository extends UserGroupRepository, JpaRepository<UserGroup, Integer> {


}
