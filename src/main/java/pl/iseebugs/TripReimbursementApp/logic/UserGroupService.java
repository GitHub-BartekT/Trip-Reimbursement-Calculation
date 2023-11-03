package pl.iseebugs.TripReimbursementApp.logic;

import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;

@Service
public class UserGroupService {

    public final UserGroupRepository repository;

    public UserGroupService(UserGroupRepository repository) {
        this.repository = repository;
    }

}
