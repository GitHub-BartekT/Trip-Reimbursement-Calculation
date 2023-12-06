package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.exception.ReimbursementNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostReadModel;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.*;
import static pl.iseebugs.TripReimbursementApp.logic.TestHelper.userCostInitialDataForUserCosts;

class UserCostServiceTest {


    @Test
    @DisplayName("should returns empty list when no objects")
    void readAll_returnEmptyList() {
        //given
        InMemoryRepositories.InMemoryUserCostRepository inMemoryUserCostRepository = inMemoryUserCostRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        InMemoryRepositories.InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        //system under test
        var toTest = new UserCostService(inMemoryUserCostRepository, inMemoryReimbursementRepository, inMemoryReceiptTypeRepository);

        //when
        List<UserCostReadModel> result = toTest.readAll();

        //then
        assertThat(result.size()).isEqualTo(0);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("should returns all objects")
    void readAll_returnsUserCosts() throws ReimbursementNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository = inMemoryReceiptTypeRepository();
        InMemoryUserCostRepository inMemoryUserCostRepository = inMemoryUserCostRepository();
        //system under test
        var toTest = new UserCostService(inMemoryUserCostRepository, inMemoryReimbursementRepository, inMemoryReceiptTypeRepository);
        userCostInitialDataForUserCosts(inMemoryUserGroupRepository, inMemoryUserRepository,
                inMemoryReimbursementRepository, inMemoryReceiptTypeRepository, inMemoryUserCostRepository);

        //when
        List<UserCostReadModel> result = toTest.readAll();

        //then
        assertThat(result.get(0).getName()).isEqualTo("receipt_1_train");
        assertThat(result.get(0).getCostValue()).isEqualTo(56);
        assertThat(result.get(1).getName()).isEqualTo("receipt_2_food");
        assertThat(result.get(1).getReceiptId()).isEqualTo(3);
        assertThat(result.get(1).getReimbursementId()).isEqualTo(1);
        assertThat(result.get(4).getName()).isEqualTo("cost");
        assertThat(result.size()).isEqualTo(5);
    }

    //TODO:
    @Test
    @DisplayName("should throws UserCostNotFoundException when given id not found")
    void readById_throwsUserCostNotFoundException() {
    }

    //TODO:
    @Test
    @DisplayName("should returns User Cost")
    void readById_returnsUserCost() {
    }

    //TODO:
    @Test
    @DisplayName("should throws ReimbursementNotFoundException when given id not found")
    void readAllByReimbursement_Id_throwsReimbursementNotFoundException() {
    }

    //TODO:
    @Test
    @DisplayName("should returns User Costs")
    void readAllByReimbursement_Id_returnsUserCosts() {
    }

    //TODO:
    @Test
    @DisplayName("should throws ReceiptTypeNotFoundException when given id not found")
    void readAllByReceiptType_Id_throwsReceiptTypeNotFoundException() {
    }

    //TODO:
    @Test
    @DisplayName("should returns User Costs")
    void readAllByReceiptType_Id_returnsUserCosts() {
    }

    //TODO:
    @Test
    @DisplayName("should throws IllegalArgumentException when given id found")
    void createUserCost_throwsIllegalArgument() {
    }

    //TODO:
    @Test
    @DisplayName("should creates User Cost")
    void createUserCost_createsUserCost() {
    }

    //TODO:
    @Test
    @DisplayName("should throws UserCostNotFoundException when given id found")
    void updateUserCost_throwsUserCostNotFoundException() {
    }

    //TODO:
    @Test
    @DisplayName("should updates User Cost")
    void updateUserCost_updatesUserCost() {
    }

    //TODO:
    @Test
    @DisplayName("should throws UserCostNotFoundException when given id found")
    void deleteById_throwsUserCostNotFoundException() {
    }

    //TODO:
    @Test
    @DisplayName("should deletes User Cost")
    void deleteById_updatesUserCost() {
    }
}