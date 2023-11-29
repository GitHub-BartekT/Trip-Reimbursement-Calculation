package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.projection.ReceiptTypeReadModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    void readAllByUserGroup_Id() {
    }

    @Test
    void readById() {
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