package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.user.UserDTO;
import pl.iseebugs.TripReimbursementApp.model.projection.user.UserMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.user.UserReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.user.UserWriteModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    public final UserRepository repository;
    public final UserGroupRepository userGroupRepository;

    public UserService(UserRepository repository, UserGroupRepository userGroupRepository)
    {
        this.repository = repository;
        this.userGroupRepository = userGroupRepository;
    }

    public List<UserDTO> readAll(){
        return repository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public UserDTO readById(int id) throws UserNotFoundException {
        UserDTO toRead = repository.findById(id).map(UserDTO::new)
                .orElseThrow(UserNotFoundException::new);
        logger.info("Read user with ID {}, User group ID {}", toRead.getId(), toRead.getUserGroup().getId());
        return toRead;
    }

    public UserReadModel createUserWithGroupId(UserWriteModel userWriteModel) throws UserGroupNotFoundException {
        if(repository.existsById(userWriteModel.getId())){
            throw new IllegalArgumentException("This User already exists.");
        }
        UserReadModel created = UserMapper.roReadModel(repository.save(toEntity(userWriteModel)));
        logger.info("Created user with ID {}, User group ID {}", created.getId(), created.getUserGroupId());
        return created;
    }

    public UserDTO createUser(UserDTO userDTO) throws UserGroupNotFoundException {
        if(repository.existsById(userDTO.getId())){
            throw new IllegalArgumentException("This User already exists.");
        }
        User user = repository.save(userDTO.toUser());
        logger.info("Created user with ID {}, User group ID {}", user.getId(), user.getUserGroup().getId());
        return new UserDTO(user);
    }

    public UserReadModel updateUserByIdWithGroupId(UserWriteModel userWriteModel) throws UserNotFoundException, UserGroupNotFoundException {
        if(!repository.existsById(userWriteModel.getId())){
            throw new UserNotFoundException();
        }
        UserReadModel updated = UserMapper.roReadModel(repository.save(toEntity(userWriteModel)));
        logger.info("Updated user with ID {}, User group ID {}", updated.getId(), updated.getUserGroupId());
        return updated;
    }

    public UserDTO updateUserById(UserDTO userDTO) throws UserNotFoundException, UserGroupNotFoundException {
        if(!repository.existsById(userDTO.getId())){
            throw new UserNotFoundException();
        }
        User toUpdate = repository.save(userDTO.toUser());
        logger.info("Updated user with ID {}, User group ID {}", toUpdate.getId(), toUpdate.getUserGroup());
        return new UserDTO(toUpdate);
    }

    public void deleteUser(int id) throws UserNotFoundException {
        repository.findById(id).orElseThrow(UserNotFoundException::new);

        try {
            repository.deleteById(id);
            logger.info("Deleted user with ID {}", id);
        } catch (Exception e){
            logger.error("Error deleting user with ID {}: {}", id, e.getMessage());
        }
    }

    public User toEntity(UserWriteModel userWriteModel) throws UserGroupNotFoundException {
        var result = new User();
        result.setId(userWriteModel.getId());
        result.setName(userWriteModel.getName());
        result.setAdmin(userWriteModel.isAdmin());
        UserGroup userGroup = userGroupRepository
                .findById(userWriteModel.getUserGroupId())
                .orElseThrow(UserGroupNotFoundException::new);
        result.setUserGroup(userGroup);
        return result;
    }
}
