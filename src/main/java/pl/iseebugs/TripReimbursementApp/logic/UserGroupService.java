package pl.iseebugs.TripReimbursementApp.logic;

import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupDTO;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGroupService {

    public final UserGroupRepository repository;

    public UserGroupService(UserGroupRepository repository) {
        this.repository = repository;
    }

    public List<UserGroupDTO> readAll(){
        return repository.findAll().stream()
                .map(UserGroupDTO::new)
                .collect(Collectors.toList());
    }

    public UserGroupDTO createUserGroup(UserGroupDTO group){
        if (repository.existsByName(group.getName())){
            throw new IllegalArgumentException("This User Group already exists.");
        }
        UserGroup userGroup = repository.save(group.toUserGroup());
        return new UserGroupDTO(userGroup);
    }

    public UserGroupDTO updateUserGroupByName(UserGroupDTO group){
        if (!repository.existsByName(group.getName())){
            throw new IllegalArgumentException("User Group not found.");
        }
        UserGroup userGroup = repository.update(group.toUserGroup());
        return new UserGroupDTO(userGroup);
    }

    public void deleteUserGroup(UserGroupDTO group) throws UserGroupNotFoundException {
        UserGroup toDelete = repository.findById(group.getId()).orElseThrow(
                () -> new UserGroupNotFoundException("User Group not found.")
        );
        repository.delete(toDelete);
    }
}
