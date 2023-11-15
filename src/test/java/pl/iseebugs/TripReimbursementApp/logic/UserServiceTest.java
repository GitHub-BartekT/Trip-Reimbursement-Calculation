package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.iseebugs.TripReimbursementApp.logic.UserGroupServiceTest.InMemoryUserGroupRepository;
import static pl.iseebugs.TripReimbursementApp.logic.UserGroupServiceTest.inMemoryUserGroupRepository;

class UserServiceTest {


    @Test
    @DisplayName("should returns empty list when no objects")
    void readAll_returnsEmptyList() throws UserGroupNotFoundException{
        //given
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();

        //system under test
        var toTest = new UserService(inMemoryUserRepository);

        //when
        List<UserDTO> result = toTest.readAll();

        //then
        assertThat(result.size()).isEqualTo(0);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("should returns all objects")
    void readAll_returnsAllUsers() throws UserGroupNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        repositoryWith(inMemoryUserGroupRepository, inMemoryUserRepository, List.of("fooGroup","barGroup", "foobarGroup"), List.of("foo","bar", "foobar"));
        int beforeSize = inMemoryUserRepository.count();

        //system under test
        var toTest = new UserService(inMemoryUserRepository);

        //when
        List<UserDTO> result = toTest.readAll();
        int afterSize = result.size();

        //then
        assertThat(result.get(0).getName()).isEqualTo("foo");
        assertThat(result.get(1).getName()).isEqualTo("bar");
        assertThat(result.get(2).getName()).isEqualTo("foobar");
        assertThat(result.get(0).getUserGroupId()).isEqualTo(1);
        assertThat(result.get(3).getUserGroupId()).isEqualTo(2);
        assertThat(result.get(3).getUserGroup().getName()).isEqualTo("barGroup");
        assertThat(afterSize).isEqualTo(beforeSize);
    }

    @Test
    @DisplayName("should throws UserNotFoundException when given id not found")
    void readById_givenIdNotFound_throwsUserNotFoundException() {
        //given
        var mockRepository = mock(UserRepository.class);
        when(mockRepository.findById(any())).thenReturn(Optional.empty());
        //system under test
        var toTest = new UserService(mockRepository);
        //when
        var exception = catchThrowable(() -> toTest.readById(7));
        //then
        assertThat(exception).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("should reads user")
    void readById_returnsUser() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        repositoryWith(inMemoryUserGroupRepository, inMemoryUserRepository, List.of("fooGroup","barGroup", "foobarGroup"), List.of("foo","bar", "foobar"));

        //system under test
        var toTest = new UserService(inMemoryUserRepository);

        //when
        UserDTO result = toTest.readById(5);

        //then
        assertThat(result.getName()).isEqualTo("bar");
        assertThat(result.getUserGroupId()).isEqualTo(2);
        assertThat(result.getUserGroup().getName()).isEqualTo("barGroup");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given User id exists")
    void createUser_givenUserIdExists_throwsIllegalArgumentException() throws UserGroupNotFoundException {
        //given
        var mockRepository = mock(UserRepository.class);
        //and
        when(mockRepository.existsById(anyInt())).thenReturn(true);
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserDTO userToCheck = new UserDTO();
        var exception = catchThrowable(() -> toTest.createUser(userToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("This User already exists.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given name is empty or has only white-space characters")
    void createUser_emptyUserNameParam_throwsIllegalArgumentException() throws UserGroupNotFoundException {
        //given
        var mockRepository = mock(UserRepository.class);

        //and
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserDTO userToCheck = new UserDTO();
        userToCheck.setName("  ");

        var exception = catchThrowable(() -> toTest.createUser(userToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User name couldn't be empty.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given name has more then 100 characters")
    void createUser_givenUserNameHasMoreThen_100_Characters_throwsIllegalArgumentException() throws UserGroupNotFoundException {
        //given
        var mockRepository = mock(UserRepository.class);

        //and
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserDTO userToCheck = new UserDTO();
        String userName = createLongString(101);
        userToCheck.setName(userName);
        // userToCheck.setId(1);
        var exception = catchThrowable(() -> toTest.createUser(userToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User name is too long.");
    }

    @Test
    @DisplayName("should throws UserGroupNotFound when no user group")
    void createUser_emptyUserGroupParam_throwsUserGroupNotFoundException() throws UserGroupNotFoundException {
        //given
        var mockRepository = mock(UserRepository.class);

        //and
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserDTO userToCheck = new UserDTO();
        userToCheck.setName("foo");
        // userToCheck.setId(1);
        var exception = catchThrowable(() -> toTest.createUser(userToCheck));

        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    //TODO
    @Test
    void updateUserById() {
    }

    //TODO
    @Test
    void deleteUser() {
    }

    private void repositoryWith(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository, List<String> userGroups, List<String> users) throws UserGroupNotFoundException {
        for (String entity : userGroups) {
            UserGroupDTO userGroupDTO = new UserGroupDTO();
            userGroupDTO.setName(entity);
            UserGroup userGroup = inMemoryUserGroupRepository.save(userGroupDTO.toUserGroup());
            for (String entityUser : users) {
                UserDTO user = new UserDTO();
                user.setName(entityUser);
                user.setUserGroup(new UserGroupDTO(userGroup));
                inMemoryUserRepository.save(user.toUser());
            }
        }
    }

    private String createLongString(int length){
        if (length <=0 ){
            return "";
        }

        return String.valueOf('A').repeat(length);
    }

    private UserServiceTest.InMemoryUserRepository inMemoryUserRepository(){
        return new UserServiceTest.InMemoryUserRepository();
    }


    private static class InMemoryUserRepository implements UserRepository {
        private final AtomicInteger index = new AtomicInteger(1);
        private final Map<Integer, User> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(map.values());
        }

        //TODO
        @Override
        public List<User> findAllByUserGroup_Id(Integer userGroupId) {
            return map.values().stream()
                    .filter((user) -> user.getUserGroup().getId() == userGroupId)
                    .collect(Collectors.toList());
        }

        @Override
        public Optional<User> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public User save(User entity) {
            if (entity.getId() == 0) {
                int id = index.getAndIncrement();
                entity.setId(id);
            }
            try {
                map.put(entity.getId(), entity);
            } catch (Exception e){
                throw new RuntimeException("Failed to save the entity to the database.");
            }
            return entity;
        }

        @Override
        public void deleteById(int id) {
            map.remove(id);
        }

        @Override
        public void deleteAll() {
        }

        @Override
        public boolean existsById(int id) {
            return false;
        }
    }
}