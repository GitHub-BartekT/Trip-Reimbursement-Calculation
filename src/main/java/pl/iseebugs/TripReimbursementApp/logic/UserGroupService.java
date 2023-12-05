package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.UserGroupMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.UserGroupReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.UserGroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGroupService {

    private static final Logger logger = LoggerFactory.getLogger(UserGroupService.class);
    public final UserGroupRepository repository;
    private final UserRepository userRepository;

    public UserGroupService(UserGroupRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }


    public List<UserGroupReadModel> readAll(){
        return repository.findAll().stream()
                .map(UserGroupMapper::toReadModel)
                .collect(Collectors.toList());
    }

    public UserGroupReadModel readById(int id) throws UserGroupNotFoundException {
        UserGroupReadModel toRead = repository.findById(id).map(UserGroupMapper::toReadModel)
                .orElseThrow(UserGroupNotFoundException::new);
        logger.info("Read User Group with ID {}", toRead.getId());
        return toRead;
    }

    public UserGroupReadModel createUserGroup(UserGroupWriteModel group){
        if(repository.findById(group.getId()).isPresent()){
            throw new IllegalArgumentException("This User Group already exists.");
        } else if (repository.existsByName(group.getName())){
            throw new IllegalArgumentException("User Group with that name already exist.");
        }
        UserGroup userGroup = repository.save(group.toUserGroup());
        logger.info("Created user group with ID {}", userGroup.getId());
        return UserGroupMapper.toReadModel(userGroup);
    }

    public UserGroupReadModel updateUserGroupById(UserGroupWriteModel group) throws UserGroupNotFoundException{
        if(repository.findById(group.getId()).isEmpty()){
            throw new UserGroupNotFoundException();
        } else if (repository.existsByName(group.getName())) {
            throw new IllegalArgumentException("User Group with that name already exist.");
        }
        UserGroup toUpdate = repository.save(group.toUserGroup());
        return UserGroupMapper.toReadModel(toUpdate);
    }

    public void deleteUserGroup(int id) throws UserGroupNotFoundException, UserNotFoundException {
        repository.findById(id).orElseThrow(UserGroupNotFoundException::new);

        try {
            if (userRepository.existsByUserGroup_Id(id)){
                throw new IllegalStateException("User Group contains user.");
            }
            repository.deleteById(id);
            logger.info("Deleted user group with ID {}", id);
        } catch (Exception e){
            logger.error("Error deleting user group with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}
