package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.ReceiptTypeRepository;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.UserGroupReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.UserGroupReadModelFull;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.UserGroupWriteModel;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.*;
import static pl.iseebugs.TripReimbursementApp.logic.TestHelper.userGroupRepositoryInitialDataOnlyNames;

class UserGroupServiceTest {

    @Test
    @DisplayName("should returns empty list when no objects")
    void readAll_returnEmptyList() {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReceiptTypeRepository);

        //when
        List<UserGroupReadModel> result = toTest.readAll();

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
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);
        int beforeSize = inMemoryUserGroupRepository.count();

        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReceiptTypeRepository);

        //when
        List<UserGroupReadModel> result = toTest.readAll();
        int afterSize = result.size();

        //then
        assertThat(result.get(0).getName()).isEqualTo("fooGroup");
        assertThat(result.get(0).getCostPerKm()).isEqualTo(0.0);
        assertThat(result.get(0).getMaxRefund()).isEqualTo(0.0);
        assertThat(result.get(0).getMaxMileage()).isEqualTo(0.0);
        assertThat(result.get(0).getMaxMileage()).isEqualTo(0.0);
        assertThat(result.get(1).getName()).isEqualTo("barGroup");
        assertThat(result.get(2).getName()).isEqualTo("foobarGroup");
        assertThat(afterSize).isEqualTo(beforeSize);
    }

    @Test
    @DisplayName("should throws UserGroupNotFoundException when given id not found")
    void readById_givenIdNotFound_throwsUserGroupNotFoundException() {
        //given
        var mockRepository = mock(UserGroupRepository.class);
        var mockUserRepository = mock(UserRepository.class);
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        when(mockRepository.findById(any())).thenReturn(Optional.empty());
        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);
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
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);

        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReceiptTypeRepository);

        //when
        UserGroupReadModelFull result = toTest.readById(2);

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
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        UserGroupWriteModel toCreate = new UserGroupWriteModel();
        toCreate.setId(1);
        toCreate.setName("foo");
        UserGroup entity = toCreate.toUserGroup();
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);

        //when
        UserGroupWriteModel userGroupToCheck = new UserGroupWriteModel();
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
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);

        //when
        UserGroupWriteModel userGroupToCheck = new UserGroupWriteModel();
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
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);

        //when
        UserGroupWriteModel toWrite = new UserGroupWriteModel();
        toWrite.setId(1);
        String groupName = TestHelper.createLongString(101);
        toWrite.setName(groupName);

        var exception = catchThrowable(() -> toTest.createUserGroup(toWrite));

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
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        //and
        when(mockRepository.existsByName(anyString())).thenReturn(true);

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);

        //when
        UserGroupWriteModel toCheck = new UserGroupWriteModel();
        toCheck.setId(1);
        toCheck.setName("bar");

        var exception = catchThrowable(() -> toTest.createUserGroup(toCheck));

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
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        int beforeSize = inMemoryUserGroupRepository.count();

        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReceiptTypeRepository);

        //when
        UserGroupWriteModel toCreate = new UserGroupWriteModel();
        toCreate.setName("foobar");
        toTest.createUserGroup(toCreate);
        var afterCreate = inMemoryUserGroupRepository.count();
        UserGroup afterSave = inMemoryUserGroupRepository.findById(inMemoryUserGroupRepository.count()).orElse(null);

        assert afterSave != null;
        UserGroupWriteModel userGroupDTOAfter  = new UserGroupWriteModel(afterSave);

        //then
        assertThat(afterCreate).isEqualTo(beforeSize + 1);
        assertThat(userGroupDTOAfter.getId()).isNotEqualTo(toCreate);
        assertThat(userGroupDTOAfter.getName()).isEqualTo(toCreate.getName());
    }

    @Test
    @DisplayName("should create new User Group when given name has 100 characters")
    void createUserGroup_givenNameHasMaxValue_createsUserGroup() {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);
        int beforeSize = inMemoryUserGroupRepository.count();

        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReceiptTypeRepository);

        //when
        UserGroupWriteModel toCreate = new UserGroupWriteModel();
        String groupName = TestHelper.createLongString(100);
        toCreate.setName(groupName);

        toTest.createUserGroup(toCreate);
        var afterCreate = inMemoryUserGroupRepository.count();
        UserGroup afterSave = inMemoryUserGroupRepository.findById(inMemoryUserGroupRepository.count()).orElse(null);

        assert afterSave != null;
        UserGroupWriteModel userGroupDTOAfter  = new UserGroupWriteModel(afterSave);

        //then
        assertThat(afterCreate).isEqualTo(beforeSize + 1);
        assertThat(userGroupDTOAfter.getId()).isNotEqualTo(toCreate.getId());
        assertThat(userGroupDTOAfter.getName()).isEqualTo(toCreate.getName());
    }

    @Test
    @DisplayName("should throw UserGroupNotFoundException when given id not found")
    void updateUserGroupById_noUserGroup_throwsUserGroupNotFoundException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(1);
        toUpdate.setName("bar");

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);

        //when
        var exception = catchThrowable(() -> toTest.updateUserGroupById(toUpdate));

        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given name is empty or has only white-space characters")
    void updateUserGroupById_emptyNameParam_throwsIllegalArgumentException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        UserGroupWriteModel dataUserGroup = new UserGroupWriteModel();
        dataUserGroup.setId(1);
        dataUserGroup.setName("foo");
        UserGroup entity = dataUserGroup.toUserGroup();
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);

        //when
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(1);
        toUpdate.setName("   ");

        var exception = catchThrowable(() -> toTest.updateUserGroupById(toUpdate));

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
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        UserGroupWriteModel dataUserGroup = new UserGroupWriteModel();
        dataUserGroup.setId(1);
        dataUserGroup.setName("foo");
        UserGroup entity = dataUserGroup.toUserGroup();
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //and
        when(mockRepository.existsByName(anyString())).thenReturn(false);

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);

        //when
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(1);
        String groupName = TestHelper.createLongString(101);
        toUpdate.setName(groupName);

        var exception = catchThrowable(() -> toTest.updateUserGroupById(toUpdate));

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
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        UserGroupWriteModel dataUserGroup = new UserGroupWriteModel();
        dataUserGroup.setId(1);
        dataUserGroup.setName("foo");
        UserGroup entity = dataUserGroup.toUserGroup();
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //and
        when(mockRepository.existsByName(anyString())).thenReturn(true);

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);

        //when
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(1);
        toUpdate.setName("bar");

        var exception = catchThrowable(() -> toTest.updateUserGroupById(toUpdate));

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
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);
        int beforeSize = inMemoryUserGroupRepository.count();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReceiptTypeRepository);

        //and
        UserGroupWriteModel toCheck = new UserGroupWriteModel();
        toCheck.setId(1);
        toCheck.setName("foo");

        //when
        UserGroupReadModel result = toTest.updateUserGroupById(toCheck);
        int afterSize = inMemoryUserGroupRepository.count();
        //then
        assertThat(result.getName()).isEqualTo("foo");
        assertThat(afterSize).isEqualTo(beforeSize);
    }

    @Test
    @DisplayName("should throw UserGroupNotFoundException when given id not found")
    void deleteUserGroup_noUserGroup_throwsUserGroupNotFoundException() {
        //given
        var mockRepository =mock(UserGroupRepository.class);
        var mockUserRepository =mock(UserRepository.class);
        var mockReceiptTypeRepository =mock(ReceiptTypeRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        int userGroupToDelete = 1;

        //system under test
        var toTest = new UserGroupService(mockRepository, mockUserRepository, mockReceiptTypeRepository);

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
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);
        int beforeSize = inMemoryUserGroupRepository.count();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReceiptTypeRepository);

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
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);
        int beforeSize = inMemoryUserGroupRepository.count();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReceiptTypeRepository);

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
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);
        int beforeSize = inMemoryUserGroupRepository.count();
        //system under test
        var toTest = new UserGroupService(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReceiptTypeRepository);

        //and
        int userGroupToDelete = 1;

        //when
        toTest.deleteUserGroup(userGroupToDelete);
        int afterSize = inMemoryUserGroupRepository.count();
        //then
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

}