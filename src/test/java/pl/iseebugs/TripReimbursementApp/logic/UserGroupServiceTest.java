package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.UserGroupDTO;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.iseebugs.TripReimbursementApp.logic.TestHelper.*;

class UserGroupServiceTest {

    @Test
    @DisplayName("should returns empty list when no objects")
    void readAll_returnEmptyList() {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository);

        //when
        List<UserGroupDTO> result = toTest.readAll();

        //then
        assertThat(result.size()).isEqualTo(0);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("should returns all objects")
    void readAll_readAllUserGroups() {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userGroupRepositoryWith(inMemoryUserGroupRepository, List.of("foo","bar", "foobar"));
        int beforeSize = inMemoryUserGroupRepository.count();

        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository);

        //when
        List<UserGroupDTO> result = toTest.readAll();
        int afterSize = result.size();

        //then
        assertThat(result.get(0).getName()).isEqualTo("foo");
        assertThat(result.get(0).getCostPerKm()).isEqualTo(0.0);
        assertThat(result.get(0).getMaxRefund()).isEqualTo(0.0);
        assertThat(result.get(0).getMaxMileage()).isEqualTo(0.0);
        assertThat(result.get(0).getMaxMileage()).isEqualTo(0.0);
        assertThat(result.get(1).getName()).isEqualTo("bar");
        assertThat(result.get(2).getName()).isEqualTo("foobar");
        assertThat(afterSize).isEqualTo(beforeSize);
    }

    @Test
    @DisplayName("should throws UserGroupNotFoundException when given id not found")
    void readById_givenIdNotFound_throwsUserGroupNotFoundException() {
        //given
        var mockRepository = mock(UserGroupRepository.class);
        var mockUserRepository = mock(UserRepository.class);
        when(mockRepository.findById(any())).thenReturn(Optional.empty());
        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);
        //when
        var exception = catchThrowable(() -> toTest.readById(7));
        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    @Test
    @DisplayName("should reads user group")
    void readById_returnsUserGroup() throws UserGroupNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userGroupRepositoryWith(inMemoryUserGroupRepository, List.of("fooGroup","barGroup", "foobarGroup"));

        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository);

        //when
        UserGroupDTO result = toTest.readById(2);

        //then
        assertThat(result.getName()).isEqualTo("barGroup");
        assertThat(result.getId()).isEqualTo(2);
        assertThat(result.getCostPerKm()).isEqualTo(0.0);
        assertThat(result.getMaxRefund()).isEqualTo(0.0);
        assertThat(result.getMaxMileage()).isEqualTo(0.0);
        assertThat(result.getMaxMileage()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given id already exists")
    void createUserGroup_whenGivenIdAlreadyExist_throwsIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(1);
        userGroupDTO.setName("foo");
        UserGroup entity = userGroupDTO.toUserGroup();
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);

        //when
        UserGroupDTO userGroupToCheck = new UserGroupDTO();
        userGroupToCheck.setId(1);

        var exception = catchThrowable(() -> toTest.createUserGroup(userGroupToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("This User Group already exists.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given name is empty or has only white-space characters")
    void createUserGroup_emptyNameParam_throwsIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);

        //when
        UserGroupDTO userGroupToCheck = new UserGroupDTO();
        userGroupToCheck.setName("   ");

        var exception = catchThrowable(() -> toTest.createUserGroup(userGroupToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User Group name couldn't be empty.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given name has more then 100 characters")
    void createUserGroup_givenNameHasMoreThen_100_Characters_throwsIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);

        //when
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(1);
        String groupName = TestHelper.createLongString(101);
        userGroupDTO.setName(groupName);

        var exception = catchThrowable(() -> toTest.createUserGroup(userGroupDTO));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User Group name is too long.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given name already exists")
    void createUserGroup_givenNameExists_throwIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        //and
        when(mockRepository.existsByName(anyString())).thenReturn(true);

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);

        //when
        UserGroupDTO userGroupToCheck = new UserGroupDTO();
        userGroupToCheck.setId(1);
        userGroupToCheck.setName("bar");

        var exception = catchThrowable(() -> toTest.createUserGroup(userGroupToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User Group with that name already exist.");
    }

    @Test
    @DisplayName("should create new User Group")
    void createUserGroup_createsUserGroup() {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        int beforeSize = inMemoryUserGroupRepository.count();

        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository);

        //when
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("foobar");
        toTest.createUserGroup(userGroupDTO);
        var afterCreate = inMemoryUserGroupRepository.count();
        UserGroup afterSave = inMemoryUserGroupRepository.findById(inMemoryUserGroupRepository.count()).orElse(null);

        assert afterSave != null;
        UserGroupDTO userGroupDTOAfter  = new UserGroupDTO(afterSave);

        //then
        assertThat(afterCreate).isEqualTo(beforeSize + 1);
        assertThat(userGroupDTOAfter.getId()).isNotEqualTo(userGroupDTO);
        assertThat(userGroupDTOAfter.getName()).isEqualTo(userGroupDTO.getName());
    }

    @Test
    @DisplayName("should create new User Group when given name has 100 characters")
    void createUserGroup_givenNameHasMaxValue_createsUserGroup() {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userGroupRepositoryWith(inMemoryUserGroupRepository, List.of("foo","bar"));
        int beforeSize = inMemoryUserGroupRepository.count();

        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository);

        //when
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        String groupName = TestHelper.createLongString(100);
        userGroupDTO.setName(groupName);

        toTest.createUserGroup(userGroupDTO);
        var afterCreate = inMemoryUserGroupRepository.count();
        UserGroup afterSave = inMemoryUserGroupRepository.findById(inMemoryUserGroupRepository.count()).orElse(null);

        assert afterSave != null;
        UserGroupDTO userGroupDTOAfter  = new UserGroupDTO(afterSave);

        //then
        assertThat(afterCreate).isEqualTo(beforeSize + 1);
        assertThat(userGroupDTOAfter.getId()).isNotEqualTo(userGroupDTO.getId());
        assertThat(userGroupDTOAfter.getName()).isEqualTo(userGroupDTO.getName());
    }

    @Test
    @DisplayName("should throw UserGroupNotFoundException when given id not found")
    void updateUserGroupById_noUserGroup_throwsUserGroupNotFoundException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(1);
        userGroupDTO.setName("bar");

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);

        //when
        var exception = catchThrowable(() -> toTest.updateUserGroupById(userGroupDTO));

        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given name is empty or has only white-space characters")
    void updateUserGroupById_emptyNameParam_throwsIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(1);
        userGroupDTO.setName("foo");
        UserGroup entity = userGroupDTO.toUserGroup();
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);

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
    @DisplayName("should throws IllegalArgumentException when given name has more then 100 characters")
    void updateUserGroup_givenNameHasMoreThen_100_Characters_throwsIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(1);
        userGroupDTO.setName("foo");
        UserGroup entity = userGroupDTO.toUserGroup();
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //and
        when(mockRepository.existsByName(anyString())).thenReturn(false);

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);

        //when
        UserGroupDTO userGroupDTOtoCheck = new UserGroupDTO();
        userGroupDTOtoCheck.setId(1);
        String groupName = TestHelper.createLongString(101);
        userGroupDTOtoCheck.setName(groupName);

        var exception = catchThrowable(() -> toTest.updateUserGroupById(userGroupDTOtoCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User Group name is too long.");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given name already exists")
    void updateUserGroupById_userGroupsNameAlreadyExists_throwsIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(1);
        userGroupDTO.setName("foo");
        UserGroup entity = userGroupDTO.toUserGroup();
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //and
        when(mockRepository.existsByName(anyString())).thenReturn(true);

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);

        //when
        UserGroupDTO userGroupToCheck = new UserGroupDTO();
        userGroupToCheck.setId(1);
        userGroupToCheck.setName("bar");

        var exception = catchThrowable(() -> toTest.updateUserGroupById(userGroupToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User Group with that name already exist.");
    }

    @Test
    @DisplayName("should rename User Group")
    void updateUserGroupById_updatesUserGroup() throws UserGroupNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userGroupRepositoryWith(inMemoryUserGroupRepository, List.of("foo","bar"));
        int beforeSize = inMemoryUserGroupRepository.count();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository);

        //and
        UserGroupDTO userGroupToCheck = new UserGroupDTO();
        userGroupToCheck.setId(1);
        userGroupToCheck.setName("foobar");

        //when
        UserGroupDTO result = toTest.updateUserGroupById(userGroupToCheck);
        int afterSize = inMemoryUserGroupRepository.count();
        //then
        assertThat(result.getName()).isEqualTo("foobar");
        assertThat(afterSize).isEqualTo(beforeSize);
    }

    @Test
    @DisplayName("should throw UserGroupNotFoundException when given id not found")
    void deleteUserGroup_noUserGroup_throwsUserGroupNotFoundException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        int userGroupToDelete = 1;

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository);

        //when
        var exception = catchThrowable(() -> toTest.deleteUserGroup(userGroupToDelete));

        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    @Test
    @DisplayName("should delete exists entity")
    void deleteUserGroup_deleteEntity() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userGroupRepositoryWith(inMemoryUserGroupRepository, List.of("foo","bar", "foobar"));
        int beforeSize = inMemoryUserGroupRepository.count();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository);

        //and
        int userGroupToDelete = 1;

        //when
        toTest.deleteUserGroup(userGroupToDelete);
        int afterSize = inMemoryUserGroupRepository.count();
        //then
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    @Test
    @DisplayName("should delete the first entity")
    void deleteUserGroup_deleteTheFirstEntity() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userGroupRepositoryWith(inMemoryUserGroupRepository, List.of("foo","bar", "foobar"));
        int beforeSize = inMemoryUserGroupRepository.count();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository);

        //and
        int userGroupToDelete = 1;

        //when
        toTest.deleteUserGroup(userGroupToDelete);
        int afterSize = inMemoryUserGroupRepository.count();
        //then
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    @Test
    @DisplayName("should delete the last entity")
    void deleteUserGroup_deleteTheLastEntity() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userGroupRepositoryWith(inMemoryUserGroupRepository, List.of("foo","bar", "foobar"));
        int beforeSize = inMemoryUserGroupRepository.count();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository);

        //and
        int userGroupToDelete = 1;

        //when
        toTest.deleteUserGroup(userGroupToDelete);
        int afterSize = inMemoryUserGroupRepository.count();
        //then
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

}