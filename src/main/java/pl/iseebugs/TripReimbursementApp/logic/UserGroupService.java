package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupDTO;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGroupService {

    private static final Logger logger = LoggerFactory.getLogger(UserGroupService.class);
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
        if(repository.findById(group.getId()).isPresent()){
            throw new IllegalArgumentException("This User Group already exists.");
        } else if (repository.existsByName(group.getName())){
            throw new IllegalArgumentException("User Group with that name already exist.");
        }
        UserGroup userGroup = repository.save(group.toUserGroup());
        logger.info("Created user group with ID {}", userGroup.getId());
        return new UserGroupDTO(userGroup);
    }

    public UserGroupDTO updateUserGroupById(UserGroupDTO group) throws UserGroupNotFoundException{
        if(repository.findById(group.getId()).isEmpty()){
            throw new UserGroupNotFoundException();
        } else if (repository.existsByName(group.getName())) {
            throw new IllegalArgumentException("User Group with that name already exist.");
        }
        UserGroup toUpdate = repository.save(group.toUserGroup());
        return new UserGroupDTO(toUpdate);
    }

    public void deleteUserGroup(int id) throws UserGroupNotFoundException {
        repository.findById(id).orElseThrow(UserGroupNotFoundException::new);

        try {
            repository.deleteById(id);
            logger.info("Deleted user group with ID {}", id);
        } catch (Exception e){
            logger.error("Error deleting user group with ID {}: {}", id, e.getMessage());
        }
    }
}
