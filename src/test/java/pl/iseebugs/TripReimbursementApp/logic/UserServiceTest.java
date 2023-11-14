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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.iseebugs.TripReimbursementApp.logic.UserGroupServiceTest.InMemoryUserGroupRepository;
import static pl.iseebugs.TripReimbursementApp.logic.UserGroupServiceTest.inMemoryUserGroupRepository;

class UserServiceTest {


    @Test
    @DisplayName("should returns empty list when no objects")
    void readAll_returnsEmptyList() {
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

    //TODO
    @Test
    @DisplayName("should reads user")
    void readById_returnsUser(){
    }

    //TODO
    @Test
    void createUser() {
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
            UserGroupDTO userGroup = new UserGroupDTO();
            userGroup.setName(entity);
            UserGroup userGroup1 = inMemoryUserGroupRepository.save(userGroup.toUserGroup());
            for (String entityUser : users) {
                UserDTO user = new UserDTO();
                user.setName(entityUser);
                user.setUserGroup(userGroup1);
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
    }
}