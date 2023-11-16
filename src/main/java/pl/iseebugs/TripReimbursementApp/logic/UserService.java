package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserDTO;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    public final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDTO> readAll(){
        return repository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public UserDTO readById(int id) throws UserNotFoundException {
        UserDTO toRead = repository.findById(id).map(UserDTO::new)
                .orElseThrow(UserNotFoundException::new);
        logger.info("Read user with ID {}, User group ID {}", toRead.getId(), toRead.getUserGroupId());
        return toRead;
    }

    public UserDTO createUser(UserDTO userDTO) throws UserGroupNotFoundException {
        if(repository.existsById(userDTO.getId())){
            throw new IllegalArgumentException("This User already exists.");
        }
        User user = repository.save(userDTO.toUser());
        logger.info("Created user with ID {}, User group ID {}", user.getId(), user.getUserGroup().getId());
        return new UserDTO(user);
    }

    public UserDTO updateUserById(UserDTO userDTO) throws UserNotFoundException, UserGroupNotFoundException {
        if(!repository.existsById(userDTO.getId())){
            throw new UserNotFoundException();
        } else if (userDTO.getName().trim().isEmpty()){
            throw new IllegalArgumentException("User name couldn't be empty.");
        } else if (userDTO.getName().length() > 100) {
            throw new IllegalArgumentException("User name is too long.");
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
}
