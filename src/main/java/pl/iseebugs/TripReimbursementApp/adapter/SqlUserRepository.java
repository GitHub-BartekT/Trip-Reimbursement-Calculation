package pl.iseebugs.TripReimbursementApp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;

import java.util.List;

@Repository
interface SqlUserRepository extends UserRepository, JpaRepository<User, Integer> {

    @Override
    List<User> findAllByUserGroup_Id(Integer userGroupId);

}
