package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.UserDTO;
import pl.iseebugs.TripReimbursementApp.model.projection.UserGroupDTO;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.iseebugs.TripReimbursementApp.logic.TestHelper.*;

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
        userRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository, inMemoryUserRepository);
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
        assertThat(result.get(0).getUserGroup().getId()).isEqualTo(1);
        assertThat(result.get(3).getUserGroup().getId()).isEqualTo(2);
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
        userRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository, inMemoryUserRepository);

        //system under test
        var toTest = new UserService(inMemoryUserRepository);

        //when
        UserDTO result = toTest.readById(5);

        //then
        assertThat(result.getName()).isEqualTo("bar");
        assertThat(result.getUserGroup().getId()).isEqualTo(2);
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
        String userName = TestHelper.createLongString(101);
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

    @Test
    @DisplayName("should throws UserGroupNotFound when no user group id")
    void createUser_noUserGroupId_throwsUserGroupNotFoundException() throws UserGroupNotFoundException {
        //given
        var mockRepository = mock(UserRepository.class);

        //and
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("fooGroup");
        UserDTO userToCheck = new UserDTO();
        userToCheck.setName("foo");
        userToCheck.setUserGroup(userGroupDTO);
        // userToCheck.setId(1);
        var exception = catchThrowable(() -> toTest.createUser(userToCheck));

        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    @Test
    @DisplayName("should create new User")
    void createUser_createsUser() throws UserGroupNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository, inMemoryUserRepository);
        int beforeSize = inMemoryUserRepository.count();

        //system under test
        var toTest = new UserService(inMemoryUserRepository);

        //when
        UserGroup userGroup = inMemoryUserGroupRepository.findById(1).orElse(null);
        assert userGroup != null;
        UserGroupDTO userGroupDTO = new UserGroupDTO(userGroup);
        UserDTO userToCheck = new UserDTO();
        userToCheck.setName("foo");
        userToCheck.setUserGroup(userGroupDTO);
        UserDTO result = toTest.createUser(userToCheck);
        List<UserDTO> resultList = toTest.readAll();
        int afterSize = resultList.size();

        //then
        assertThat(result.getName()).isEqualTo("foo");
        assertThat(result.getUserGroup().getName()).isEqualTo("fooGroup");
        assertThat(result.getUserGroup().getId()).isEqualTo(1);
        assertThat(result.getId()).isEqualTo(afterSize);
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    @DisplayName("should throws UserNotFoundException when no User id")
    void updateUserById_noUserId_throwsUserNotFoundException() {
        //given
        var mockRepository = mock(UserRepository.class);
        //and
        when(mockRepository.existsById(anyInt())).thenReturn(false);
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserDTO userToCheck = new UserDTO();
        var exception = catchThrowable(() -> toTest.updateUserById(userToCheck));
        //then
        assertThat(exception).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given name is empty or has only white-space characters")
    void updateUserById_emptyUserNameParam_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(UserRepository.class);

        //and
        when(mockRepository.existsById(anyInt())).thenReturn(true);
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserDTO userToCheck = new UserDTO();
        userToCheck.setName("  ");

        var exception = catchThrowable(() -> toTest.updateUserById(userToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User name couldn't be empty.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given name has more then 100 characters")
    void updateUserById_givenUserNameHasMoreThen_100_Characters_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(UserRepository.class);

        //and
        when(mockRepository.existsById(anyInt())).thenReturn(true);
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserDTO userToCheck = new UserDTO();
        String userName = TestHelper.createLongString(101);
        userToCheck.setName(userName);
        // userToCheck.setId(1);
        var exception = catchThrowable(() -> toTest.updateUserById(userToCheck));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User name is too long.");
    }

    @Test
    @DisplayName("should throws UserGroupNotFound when no user group")
    void updateUserById_noUserGroupParam_throwsUserGroupNotFoundException() {
        //given
        var mockRepository = mock(UserRepository.class);

        //and
        when(mockRepository.existsById(anyInt())).thenReturn(true);
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserDTO userToCheck = new UserDTO();
        userToCheck.setName("foo");
        var exception = catchThrowable(() -> toTest.updateUserById(userToCheck));

        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    @Test
    @DisplayName("should throws UserGroupNotFound when no user group id")
    void updateUserById_noUserGroupId_throwsUserGroupNotFoundException() {
        //given
        var mockRepository = mock(UserRepository.class);

        //and
        when(mockRepository.existsById(anyInt())).thenReturn(true);
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("fooGroup");
        UserDTO userToCheck = new UserDTO();
        userToCheck.setName("foo");
        userToCheck.setUserGroup(userGroupDTO);
        var exception = catchThrowable(() -> toTest.updateUserById(userToCheck));

        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    @Test
    @DisplayName("should update new User")
    void updateUserById_updatesUser() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository, inMemoryUserRepository);
        int beforeSize = inMemoryUserRepository.count();

        //system under test
        var toTest = new UserService(inMemoryUserRepository);

        //when
        UserGroup userGroup = inMemoryUserGroupRepository.findById(3).orElse(null);
        assert userGroup != null;
        UserGroupDTO userGroupDTO = new UserGroupDTO(userGroup);
        UserDTO userToUpdate = new UserDTO();
        userToUpdate.setName("newFoo");
        userToUpdate.setId(2);
        userToUpdate.setUserGroup(userGroupDTO);
        toTest.updateUserById(userToUpdate);
        List<UserDTO> resultList = toTest.readAll();
        int afterSize = resultList.size();

        //then
        assertThat(resultList.get(1).getName()).isEqualTo("newFoo");
        assertThat(resultList.get(1).getUserGroup().getName()).isEqualTo("foobarGroup");
        assertThat(resultList.get(1).getUserGroup().getId()).isEqualTo(3);
        assertThat(afterSize).isEqualTo(beforeSize);
    }

    @Test
    @DisplayName("should throw UserNotFoundException when given id not found")
    void deleteUser_noUser_throwsUserNotFoundException() {
        //given
        var mockRepository =mock(UserRepository.class);
        when(mockRepository.existsById(anyInt())).thenReturn(false);
        //system under test
        var toTest = new UserService(mockRepository);

        //when
        var exception = catchThrowable(() -> toTest.deleteUser(5));
        //then
        assertThat(exception).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("should delete exists entity")
    void deleteUser_deletesUser() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository, inMemoryUserRepository);
        int beforeSize = inMemoryUserRepository.count();
        //system under test
        var toTest = new UserService(inMemoryUserRepository);

        //when
        int userToDelete = 5;
        toTest.deleteUser(userToDelete);
        List<UserDTO> resultList = toTest.readAll();
        int afterSize = resultList.size();
        //then
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }

    @Test
    @DisplayName("should delete the first entity")
    void deleteUser_deletesTheFirstUser_throwsUserNotFoundException() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository, inMemoryUserRepository);
        int beforeSize = inMemoryUserRepository.count();
        //system under test
        var toTest = new UserService(inMemoryUserRepository);

        //when
        int userToDelete = 1;
        toTest.deleteUser(userToDelete);
        List<UserDTO> resultList = toTest.readAll();
        int afterSize = resultList.size();
        //then
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }

    @Test
    @DisplayName("should delete the last entity")
    void deleteUser_deletesTheLastUser_throwsUserNotFoundException() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        userRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository, inMemoryUserRepository);
        int beforeSize = inMemoryUserRepository.count();
        //system under test
        var toTest = new UserService(inMemoryUserRepository);

        //when
        int userToDelete = 9;
        toTest.deleteUser(userToDelete);
        List<UserDTO> resultList = toTest.readAll();
        int afterSize = resultList.size();
        //then
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }
}