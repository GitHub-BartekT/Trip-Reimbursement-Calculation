package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.*;
import static pl.iseebugs.TripReimbursementApp.logic.TestHelper.reimbursementRepositoryInitialDataAllParams;

class ReimbursementServiceReturnValueTest {

    @Test
    @DisplayName("should return Zero when DailyAllowance is equal Zero, and duration is equal Zero")
    void readById_givenDailyAllowanceIs_Zero_durationIs_Zero_returnsReturnValue_Zero() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(1);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_001_zeroDaysNoRefund");
        assertThat(resultUser.getName()).isEqualTo("user_01_noDailyRefund");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_001_NoDailyAllowance");
        assertThat(resultUser.getUserGroup().getDailyAllowance()).isEqualTo(0);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(100);
        assertThat(result.getReturnValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("should return Zero when DailyAllowance is equal Zero, and duration is equal One")
    void readById_givenDailyAllowanceIs_Zero_durationIs_One_returnsReturnValue_Zero() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(2);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_002_oneDayNoRefund");
        assertThat(resultUser.getName()).isEqualTo("user_01_noDailyRefund");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_001_NoDailyAllowance");
        assertThat(resultUser.getUserGroup().getDailyAllowance()).isEqualTo(0);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(100);
        assertThat(result.getReturnValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("should return Zero when DailyAllowance is equal Zero, and duration is equal more than one day")
    void readById_givenDailyAllowanceIs_Zero_durationIs_moreThanZero_returnsReturnValue_Zero() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(3);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_003_moreDaysNoRefund");
        assertThat(resultUser.getName()).isEqualTo("user_01_noDailyRefund");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_001_NoDailyAllowance");
        assertThat(resultUser.getUserGroup().getDailyAllowance()).isEqualTo(0);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(100);
        assertThat(result.getReturnValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("should return Zero when DailyAllowance is equal Ten, and duration is Zero")
    void readById_givenDailyAllowanceIs_Ten_durationIs_Zero_returnsReturnValue_Zero() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(4);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_004_zeroDaysNoRefund");
        assertThat(resultUser.getName()).isEqualTo("user_05_ok");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_005_Ok");
        assertThat(resultUser.getUserGroup().getDailyAllowance()).isEqualTo(10);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(100);
        assertThat(result.getReturnValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("should return Ten when DailyAllowance is equal Ten, and duration is One")
    void readById_givenDailyAllowanceIs_Ten_durationIs_One_returnsReturnValue_Ten() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(5);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_005_oneDayRefund");
        assertThat(resultUser.getName()).isEqualTo("user_05_ok");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_005_Ok");
        assertThat(resultUser.getUserGroup().getDailyAllowance()).isEqualTo(10);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(100);
        assertThat(result.getReturnValue()).isEqualTo(10);
    }

    @Test
    @DisplayName("should return 50 when DailyAllowance is equal Ten, and duration is Five")
    void readById_givenDailyAllowanceIs_Ten_durationIs_Five_returnsReturnValue_50() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(6);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_006_moreDaysRefund");
        assertThat(resultUser.getName()).isEqualTo("user_05_ok");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_005_Ok");
        assertThat(resultUser.getUserGroup().getDailyAllowance()).isEqualTo(10);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(100);
        assertThat(result.getReturnValue()).isEqualTo(50);
    }

    @Test
    @DisplayName("should return maxRefund (100) when DailyAllowance is equal Ten, and duration is 15")
    void readById_givenDailyAllowanceIs_Ten_durationIs_Fifteen_returnsMaxRefund_50() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(7);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_007_moreDaysRefundMaxRefund");
        assertThat(resultUser.getName()).isEqualTo("user_05_ok");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_005_Ok");
        assertThat(resultUser.getUserGroup().getDailyAllowance()).isEqualTo(10);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(100);
        assertThat(result.getReturnValue()).isEqualTo(100);
    }

    @Test
    @DisplayName("should return 0 when Distance is equal 100, and costPerKm is Zero")
    void readById_givenCostPerKmIs_Zero_distanceIs_100_returnsReturnValue_0() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(8);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_008_noCostPerKm");
        assertThat(result.getDistance()).isEqualTo(100);
        assertThat(resultUser.getName()).isEqualTo("user_02_noCostPerKm");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_002_NoCostPerKm");
        assertThat(resultUser.getUserGroup().getCostPerKm()).isEqualTo(0);
        assertThat(resultUser.getUserGroup().getMaxMileage()).isEqualTo(100);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(10);
        assertThat(result.getReturnValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("should return 0 when MaxMileage is equal Zero, and Distance is 100")
    void readById_givenMaxMileageIs_Zero_distanceIs_100_returnsReturnValue_0() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(9);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_009_noMaxMileage");
        assertThat(result.getDistance()).isEqualTo(100);
        assertThat(resultUser.getName()).isEqualTo("user_03_noMaxMileage");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_003_NoMaxMileage");
        assertThat(resultUser.getUserGroup().getCostPerKm()).isEqualTo(0.25);
        assertThat(resultUser.getUserGroup().getMaxMileage()).isEqualTo(0);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(500);
        assertThat(result.getReturnValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("should return 25 when MaxMileage is 100, CostPerKm is 0.25 and Distance is 100")
    void readById_givenMaxMileageIs_100_CostPerKmIs_025_distanceIs_100_returnsReturnValue_25() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(10);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_010_ok");
        assertThat(result.getDistance()).isEqualTo(100);
        assertThat(resultUser.getName()).isEqualTo("user_05_ok");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_005_Ok");
        assertThat(resultUser.getUserGroup().getCostPerKm()).isEqualTo(0.25);
        assertThat(resultUser.getUserGroup().getMaxMileage()).isEqualTo(500);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(100);
        assertThat(result.getReturnValue()).isEqualTo(25);
    }

    @Test
    @DisplayName("should return maxRefund (100) when MaxMileage is 500, CostPerKm is 0.25 and Distance is 100")
    void readById_givenMaxMileageIs_500_CostPerKmIs_025_distanceIs_500_returnsMaxRefund_100() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(11);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_011_noMaxMileage");
        assertThat(result.getDistance()).isEqualTo(500);
        assertThat(resultUser.getName()).isEqualTo("user_05_ok");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_005_Ok");
        assertThat(resultUser.getUserGroup().getCostPerKm()).isEqualTo(0.25);
        assertThat(resultUser.getUserGroup().getMaxMileage()).isEqualTo(500);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(100);
        assertThat(result.getReturnValue()).isEqualTo(100);
    }

    @Test
    @DisplayName("should return Zero when MaxRefund is Zero")
    void readById_givenMaxRefundIs_Zero_returnsReturnValue_0() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(12);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_012_noMaxRefund");
        assertThat(result.getDistance()).isEqualTo(100);
        assertThat(resultUser.getName()).isEqualTo("user_04_noRefund");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_004_NoMaxRefund");
        assertThat(resultUser.getUserGroup().getCostPerKm()).isEqualTo(0.25);
        assertThat(resultUser.getUserGroup().getMaxMileage()).isEqualTo(100);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(0);
        assertThat(result.getReturnValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("should return (125) when all parameters are not zero")
    void readById_givenAllParametersAreNotZero_returnsReturnValue_125() throws UserGroupNotFoundException, ReimbursementNotFoundException, UserNotFoundException {
        //given
        InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryRepositories.InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryRepositories.InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);
        //when
        var result = toTest.readById(13);
        User resultUser = inMemoryUserRepository
                .findById(result.getUserId())
                .orElseThrow(UserNotFoundException::new);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_013_ok");
        assertThat(result.getDistance()).isEqualTo(100);
        assertThat(resultUser.getName()).isEqualTo("user_06_ok");
        assertThat(resultUser.getUserGroup().getName()).isEqualTo("group_006_Ok");
        assertThat(resultUser.getUserGroup().getCostPerKm()).isEqualTo(0.25);
        assertThat(resultUser.getUserGroup().getMaxMileage()).isEqualTo(500);
        assertThat(resultUser.getUserGroup().getMaxRefund()).isEqualTo(200);
        assertThat(result.getReturnValue()).isEqualTo(125);
    }

}