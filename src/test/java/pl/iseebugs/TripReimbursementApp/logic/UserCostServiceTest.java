package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.exception.ReceiptTypeNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.ReimbursementNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserCostNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostReadModel;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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

    @Test
    @DisplayName("should throws UserCostNotFoundException when given id not found")
    void readById_throwsUserCostNotFoundException() throws UserCostNotFoundException {
        //given
        var mockUserCostRepository = mock(InMemoryUserCostRepository.class);
        var mockReimbursementRepository = mock(InMemoryReimbursementRepository.class);
        var mockReceiptTypeRepository = mock(InMemoryReceiptTypeRepository.class);
        when(mockUserCostRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new UserCostService(mockUserCostRepository, mockReimbursementRepository, mockReceiptTypeRepository);

        //when
        var exception = catchThrowable(() -> toTest.readById(756));

        //then
        assertThat(exception).isInstanceOf(UserCostNotFoundException.class);
    }

    @Test
    @DisplayName("should returns User Cost")
    void readById_returnsUserCost() throws ReimbursementNotFoundException, UserCostNotFoundException {
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
        UserCostReadModel result = toTest.readById(3);

        //then
        assertThat(result.getName()).isEqualTo("user_2_hotel");
        assertThat(result.getCostValue()).isEqualTo(456);
        assertThat(result.getReceiptId()).isEqualTo(5);
        assertThat(result.getReimbursementId()).isEqualTo(8);
    }

    @Test
    @DisplayName("should throws ReimbursementNotFoundException when given id not found")
    void readAllByReimbursement_Id_throwsReimbursementNotFoundException() {
        //given
        var mockUserCostRepository = mock(InMemoryUserCostRepository.class);
        var mockReimbursementRepository = mock(InMemoryReimbursementRepository.class);
        var mockReceiptTypeRepository = mock(InMemoryReceiptTypeRepository.class);
        when(mockUserCostRepository.existsById(anyInt())).thenReturn(false);
        //system under test
        var toTest = new UserCostService(mockUserCostRepository, mockReimbursementRepository, mockReceiptTypeRepository);

        //when
        var exception = catchThrowable(() -> toTest.readAllByReimbursement_Id(756));

        //then
        assertThat(exception).isInstanceOf(ReimbursementNotFoundException.class);
    }

    @Test
    @DisplayName("should returns User Costs")
    void readAllByReimbursement_Id_returnsUserCosts() throws ReimbursementNotFoundException {
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
        List<UserCostReadModel> result = toTest.readAllByReimbursement_Id(1);

        //then
        assertThat(result.get(0).getName()).isEqualTo("receipt_1_train");
        assertThat(result.get(0).getCostValue()).isEqualTo(56);
        assertThat(result.get(0).getReceiptId()).isEqualTo(1);
        assertThat(result.get(1).getName()).isEqualTo("receipt_2_food");
        assertThat(result.get(1).getCostValue()).isEqualTo(56);
        assertThat(result.get(1).getReceiptId()).isEqualTo(3);
        assertThat(result.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("should throws ReceiptTypeNotFoundException when given id not found")
    void readAllByReceiptType_Id_throwsReceiptTypeNotFoundException() {
        //given
        var mockUserCostRepository = mock(InMemoryUserCostRepository.class);
        var mockReimbursementRepository = mock(InMemoryReimbursementRepository.class);
        var mockReceiptTypeRepository = mock(InMemoryReceiptTypeRepository.class);
        when(mockReceiptTypeRepository.existsById(anyInt())).thenReturn(false);
        //system under test
        var toTest = new UserCostService(mockUserCostRepository, mockReimbursementRepository, mockReceiptTypeRepository);

        //when
        var exception = catchThrowable(() -> toTest.readAllByReceiptType_Id(756));

        //then
        assertThat(exception).isInstanceOf(ReceiptTypeNotFoundException.class);
    }

    @Test
    @DisplayName("should returns User Costs")
    void readAllByReceiptType_Id_returnsUserCosts() throws ReimbursementNotFoundException, ReceiptTypeNotFoundException {
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
        List<UserCostReadModel> result = toTest.readAllByReceiptType_Id(1);

        //then
        assertThat(result.get(0).getName()).isEqualTo("receipt_1_train");
        assertThat(result.get(0).getCostValue()).isEqualTo(56);
        assertThat(result.get(0).getReimbursementId()).isEqualTo(1);
        assertThat(result.get(1).getName()).isEqualTo("user_4_hotel");
        assertThat(result.get(1).getCostValue()).isEqualTo(10000);
        assertThat(result.get(1).getReimbursementId()).isEqualTo(8);
        assertThat(result.get(2).getName()).isEqualTo("cost");
        assertThat(result.get(2).getCostValue()).isEqualTo(23);
        assertThat(result.get(2).getReimbursementId()).isEqualTo(12);
        assertThat(result.size()).isEqualTo(3);
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