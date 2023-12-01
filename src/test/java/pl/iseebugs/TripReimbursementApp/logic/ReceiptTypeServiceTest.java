package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.exception.ReceiptTypeNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.projection.ReceiptTypeReadModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.*;
import static pl.iseebugs.TripReimbursementApp.logic.TestHelper.receiptTypesRepositoryInitialDataAllParams;

class ReceiptTypeServiceTest {

    @Test
    @DisplayName("should returns empty list when no objects")
    void readAll_returnEmptyList() {
        //given
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        //system under test
        var toTest = new ReceiptTypeService(inMemoryReceiptTypeRepository, inMemoryUserGroupRepository);

        //when
        List<ReceiptTypeReadModel> result = toTest.readAll();

        //then
        assertThat(result.size()).isEqualTo(0);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("should returns all objects")
    void readAll_returnsAllUsers() {
        //given
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        receiptTypesRepositoryInitialDataAllParams(inMemoryReceiptTypeRepository, inMemoryUserGroupRepository);
        //system under test
        var toTest = new ReceiptTypeService(inMemoryReceiptTypeRepository, inMemoryUserGroupRepository);

        //when
        List<ReceiptTypeReadModel> result = toTest.readAll();
        Set<UserGroup> userGroupList =
        result.stream().flatMap(receiptType -> receiptType.getUserGroups().stream()).collect(Collectors.toSet());

        //then
        assertThat(result.get(0).getName()).isEqualTo("Train_AllUsers");
        assertThat(result.get(1).getName()).isEqualTo("Aeroplane_CEO");
        assertThat(result.get(2).getName()).isEqualTo("Food_AllUsers");
        assertThat(result.get(2).getMaxValue()).isEqualTo(45);
        assertThat(result.get(2).getUserGroups().size()).isEqualTo(userGroupList.size());
        assertThat(result.get(3).getName()).isEqualTo("Food_CEO");
        assertThat(result.get(4).getName()).isEqualTo("Hotels_Sellers");
        assertThat(result.get(4).getUserGroups().size()).isEqualTo(1);
        assertThat(result.get(4).getName()).isEqualTo("Hotels_Sellers");
        assertThat(result.get(4).getMaxValue()).isEqualTo(450);
        assertThat(result.get(4).getUserGroups().size()).isEqualTo(1);
        assertThat(result.get(5).getName()).isEqualTo("Other_Directors");
        assertThat(result.get(5).getUserGroups().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("should throws UserGroupNotFoundException")
    void readAllByUserGroup_Id_throwsUserGroupNotFoundException() throws UserGroupNotFoundException {
        //given
        var mockRepository = mock(InMemoryReceiptTypeRepository.class);
        var mockUserGroupRepository = mock (InMemoryUserGroupRepository.class);
        when(mockUserGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new ReceiptTypeService(mockRepository, mockUserGroupRepository);

        //when
        var exception = catchThrowable(() -> toTest.readAllByUserGroup_Id(5));
        //then
        assertThat(exception).isInstanceOf(UserGroupNotFoundException.class);
    }

    @Test
    @DisplayName("should returns list objects")
    void readAllByUserGroup_Id_returnsObjectsWhichContainUserGroup_Id() throws UserGroupNotFoundException {
        //given
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        receiptTypesRepositoryInitialDataAllParams(inMemoryReceiptTypeRepository, inMemoryUserGroupRepository);
        //system under test
        var toTest = new ReceiptTypeService(inMemoryReceiptTypeRepository, inMemoryUserGroupRepository);

        //when
        int userGroupID = 2;
        List<ReceiptTypeReadModel> result = toTest.readAllByUserGroup_Id(userGroupID);

        //then
        assertThat(result.get(0).getName()).isEqualTo("Train_AllUsers");
        assertThat(result.get(1).getName()).isEqualTo("Food_AllUsers");
        assertThat(result.get(2).getName()).isEqualTo("Hotels_Sellers");
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("should returns UserGroupNotFoundException")
    void readById_throwsUserGroupNotFoundException(){
        //given
        var mockRepository = mock(InMemoryReceiptTypeRepository.class);
        var mockUserGroupRepository = mock(InMemoryUserGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        //system under test
        var toTest = new ReceiptTypeService(mockRepository, mockUserGroupRepository);

        //when
        int receiptTypeIndex = 2;
        var exception = catchThrowable(() -> toTest.readById(2));

        //then
        assertThat(exception).isInstanceOf(ReceiptTypeNotFoundException.class);
    }

    @Test
    @DisplayName("should returns Receipt Type")
    void readAllByUserGroup_Id_returnsReceiptType() throws ReceiptTypeNotFoundException {
        //given
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        receiptTypesRepositoryInitialDataAllParams(inMemoryReceiptTypeRepository, inMemoryUserGroupRepository);
        //system under test
        var toTest = new ReceiptTypeService(inMemoryReceiptTypeRepository, inMemoryUserGroupRepository);

        //when
        int userGroupID = 2;
        ReceiptTypeReadModel result = toTest.readById(userGroupID);

        //then
        assertThat(result.getName()).isEqualTo("Aeroplane_CEO");
        assertThat(result.getMaxValue()).isEqualTo(2000);
        assertThat(result.getUserGroups().size()).isEqualTo(1);
        assertThat(result.getUserGroups().stream().findFirst().get().getName()).isEqualTo("CEO");
    }


    @Test
    void saveReceiptTypeWithUserGroupIds() {
    }

    @Test
    void saveReceiptTypeToAllUserGroup() {
    }

    @Test
    void updateReceiptTypeWithUserGroupIds() {
    }

    @Test
    void deleteById() {
    }
}