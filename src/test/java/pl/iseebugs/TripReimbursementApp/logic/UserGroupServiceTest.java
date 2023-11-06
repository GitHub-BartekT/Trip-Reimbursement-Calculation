package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupDTO;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserGroupServiceTest {

/*    @Test
    void readAll() {
    }

    @Test
    void createUserGroup() {
    }*/

    @Test
    @DisplayName("should throw UserGroupNotFoundException when given id not found")
    void updateUserGroupById_noUserGroup_throwsUserGroupNotFoundException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        when(mockRepository.findById(any())).thenReturn(Optional.empty());

        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(1);
        userGroupDTO.setName("bar");

        //system under test
        var toTest = new UserGroupService(mockRepository);

        //when
        var exception = catchThrowable(() -> toTest.updateUserGroupById(userGroupDTO));

        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given name is empty or has only white marks")
    void updateUserGroupById_emptyNameParam_throwsIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(1);
        userGroupDTO.setName("foo");
        UserGroup entity = userGroupDTO.toUserGroup();
        when(mockRepository.findById(any())).thenReturn(Optional.of(entity));

        //system under test
        var toTest = new UserGroupService(mockRepository);

        //when
        UserGroupDTO userGroupToCheck = new UserGroupDTO();
        userGroupToCheck.setId(1);
        userGroupToCheck.setName("   ");

        var exception = catchThrowable(() -> toTest.updateUserGroupById(userGroupToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User Group name couldn't be empty.");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given name already exists")
    void updateUserGroupById_userGroupsNameAlreadyExists_throwsIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(1);
        userGroupDTO.setName("foo");
        UserGroup entity = userGroupDTO.toUserGroup();
        when(mockRepository.findById(any())).thenReturn(Optional.of(entity));

        //and
        when(mockRepository.existsByName(any())).thenReturn(true);

        //system under test
        var toTest = new UserGroupService(mockRepository);

        //when
        UserGroupDTO userGroupToCheck = new UserGroupDTO();
        userGroupToCheck.setId(1);
        userGroupToCheck.setName("bar");

        var exception = catchThrowable(() -> toTest.updateUserGroupById(userGroupToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User Group with that name already exist.");
    }

    //TODO:
    @Test
    @DisplayName("should rename user group")
    void updateUserGroupById_updatesUserGroup() throws UserGroupNotFoundException {
    }


    /*@Test
    void deleteUserGroup() {
    }*/

    private InMemoryUserGroupRepository inMemoryUserGroupRepository(){
        return new InMemoryUserGroupRepository();
    }

    private static class InMemoryUserGroupRepository implements UserGroupRepository {
        private AtomicInteger index = new AtomicInteger(1);
        private Map<Integer,UserGroup> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
        public List<UserGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<UserGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public boolean existsByName(String name) {
            return map.values().stream()
                    .anyMatch(userGroup -> name.equals(userGroup.getName()));
        }

        @Override
        public UserGroup save(UserGroup entity) {
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
        public void delete(UserGroup entity) {
            map.remove(entity.getId());
        }
    }


}