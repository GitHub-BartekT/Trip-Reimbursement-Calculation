package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.model.Reimbursement;
import pl.iseebugs.TripReimbursementApp.model.ReimbursementRepository;
import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.ReimbursementMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.ReimbursementReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.ReimbursementWriteModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.*;
import static pl.iseebugs.TripReimbursementApp.logic.ReimbursementService.validate;
import static pl.iseebugs.TripReimbursementApp.logic.TestHelper.createLongString;
import static pl.iseebugs.TripReimbursementApp.logic.TestHelper.reimbursementRepositoryInitialDataAllParams;

class ReimbursementServiceTest {

    @Test
    @DisplayName("should returns empty list when no objects")
    void readAll_returnsEmptyList() throws ReimbursementNotFoundException{
        //given
        InMemoryReimbursementRepository inMemoryReimbursementRepository =
                inMemoryReimbursementRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);

        //when
        List<ReimbursementReadModel> result = toTest.readAll();
        //then
        assertThat(result.size()).isEqualTo(0);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("should returns all objects")
    void readAll_readAllReimbursements() throws UserGroupNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        int beforeSize = inMemoryReimbursementRepository.count();
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);

        //when
        var result = toTest.readAll();
        int afterSize = result.size();

        //then
        assertThat(afterSize).isEqualTo(beforeSize);
        assertThat(result.get(0).getName()).isEqualTo("reimbursement_001_zeroDaysNoRefund");
        assertThat(result.get(0).getStartDate()).isNull();
        assertThat(result.get(0).getEndDate()).isEqualTo(LocalDate.of(2022,3,20));
        assertThat(result.get(0).getDistance()).isEqualTo(0);
        assertThat(result.get(0).isPushedToAccept()).isEqualTo(false);
    }

    @Test
    @DisplayName("should throws UserNotFoundException when user not found")
    void readAllByUser_Id_givenUserNotFound_throwsUserNotFoundException() throws UserNotFoundException {
        //given
        var mockRepository = mock(ReimbursementRepository.class);
        var mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.existsById(anyInt())).thenReturn(false);

        //test under control
        var toTest = new ReimbursementService(mockRepository,mockUserRepository);

        //when
        var exception = catchThrowable(() ->toTest.readAllByUser_Id(1));
        //then
        assertThat(exception).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("should returns empty list when no objects")
    void readAllByUserId_returnsEmptyList() throws ReimbursementNotFoundException, UserNotFoundException, UserGroupNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);

        //when
        List<ReimbursementReadModel> result = toTest.readAllByUser_Id(7);
        //then
        assertThat(result.size()).isEqualTo(0);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("should returns all objects")
    void readAllById_readAllReimbursements() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        int beforeSize = inMemoryReimbursementRepository.count();
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);

        //when
        var result = toTest.readAllByUser_Id(5);
        int afterSize = result.size();

        //then
        assertThat(afterSize).isNotEqualTo(beforeSize);
        assertThat(result.get(0).getName()).isEqualTo("reimbursement_004_zeroDaysNoRefund");
        assertThat(result.get(0).getStartDate()).isNull();
        assertThat(result.get(0).getEndDate()).isEqualTo(LocalDate.of(2022,3,20));
        assertThat(result.get(0).getDistance()).isEqualTo(0);
        assertThat(result.get(0).isPushedToAccept()).isEqualTo(false);
        assertThat(result.get(1).getName()).isEqualTo("reimbursement_005_oneDayRefund");
        assertThat(result.get(5).getName()).isEqualTo("reimbursement_011_noMaxMileage");
    }

    @Test
    @DisplayName("should throws ReimbursementNotFoundException when given id not found")
    void readById_givenIdNotFound_throwsReimbursementNotFoundException() {
        //given
        var mockRepository = mock(ReimbursementRepository.class);
        var mockUserRepository = mock(UserRepository.class);
        when(mockRepository.findById(any())).thenReturn(Optional.empty());
        //system under test
        var toTest = new ReimbursementService(mockRepository, mockUserRepository);
        //when
        var exception = catchThrowable(() -> toTest.readById(7));
        //then
        assertThat(exception).isInstanceOf(ReimbursementNotFoundException.class);
    }

    @Test
    @DisplayName("should reads reimbursement")
    void readById_returnsReimbursement() throws UserGroupNotFoundException, ReimbursementNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository,inMemoryUserRepository );

        //when
        var result = toTest.readById(1);
        //then
        assertThat(result.getName()).isEqualTo("reimbursement_001_zeroDaysNoRefund");
        assertThat(result.getStartDate()).isNull();
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2022,3,20));
        assertThat(result.getDistance()).isEqualTo(0);
        assertThat(result.isPushedToAccept()).isEqualTo(false);
}

    @Test
    @DisplayName("should throws IllegalArgumentException when reimbursement already exists")
    void createReimbursement_givenReimbursementIdExists_throwsIllegalArgumentException(){
        //given
        var mockRepository = mock(ReimbursementRepository.class);
        var mockUserRepository = mock(UserRepository.class);
        //and
        when(mockRepository.existsById(anyInt())).thenReturn(true);
        //test under control
        var toTest = new ReimbursementService(mockRepository,mockUserRepository);

        //when
        ReimbursementWriteModel reimbursement = new ReimbursementWriteModel();
        var exception = catchThrowable(() -> toTest.createReimbursement(reimbursement));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("This Reimbursement already exists.");
    }

    @Test
    @DisplayName("should throws UserNotFoundException when user not found")
    void createReimbursement_givenUserNotFound_throwsUserNotFoundException() throws UserNotFoundException {
        //given
        var mockRepository = mock(ReimbursementRepository.class);
        var mockUserRepository = mock(UserRepository.class);
        when(mockRepository.existsById(anyInt())).thenReturn(false);
        when(mockUserRepository.existsById(anyInt())).thenReturn(false);
        //and
        ReimbursementWriteModel reimbursement = new ReimbursementWriteModel();
        reimbursement.setUserId(1);

        //test under control
        var toTest = new ReimbursementService(mockRepository,mockUserRepository);

        //when
        var exception = catchThrowable(() ->toTest.createReimbursement(reimbursement));
        //then
        assertThat(exception).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("should create new Reimbursement")
    void createReimbursement_createsReimbursement() throws UserGroupNotFoundException, UserNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        int beforeSize = inMemoryReimbursementRepository.count();
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);

        User user = new User();
        user.setId(1);
        LocalDate startDay = LocalDate.of(2022, 1, 20);
        LocalDate endDay = LocalDate.of(2022, 1, 21);
        Reimbursement reimbursement = new Reimbursement("foo", startDay, endDay,
                5, false,user);
        //when
        var result = toTest.createReimbursement(ReimbursementMapper.toWriteModel(reimbursement));
        int afterSize = toTest.readAll().size();

        //then
        assertThat(afterSize).isEqualTo(beforeSize + 1);
        assertThat(result.getName()).isEqualTo("foo");
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2022,1,20));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2022,1,21));
        assertThat(result.getDistance()).isEqualTo(5);
        assertThat(result.isPushedToAccept()).isEqualTo(false);
    }

    @Test
    @DisplayName("should throws ReimbursementNotFoundException when reimbursement not found")
    void updateReimbursementById_givenReimbursementIdNoFound_throwsReimbursementNotFoundException() {
        //given
        var mockRepository = mock(ReimbursementRepository.class);
        var mockUserRepository = mock(UserRepository.class);
        //and
        when(mockRepository.existsById(anyInt())).thenReturn(false);
        //test under control
        var toTest = new ReimbursementService(mockRepository,mockUserRepository);
        //when
        ReimbursementWriteModel reimbursement = new ReimbursementWriteModel();
        var exception = catchThrowable(() -> toTest.updateReimbursementById(reimbursement));
        //then
        assertThat(exception).isInstanceOf(ReimbursementNotFoundException.class);
    }

    @Test
    @DisplayName("should throws UserNotFoundException when reimbursement not found")
    void updateReimbursementById_givenUserNotFound_throwsUserNotFoundException() {
        //given
        var mockRepository = mock(ReimbursementRepository.class);
        var mockUserRepository = mock(UserRepository.class);
        //and
        when(mockRepository.existsById(anyInt())).thenReturn(true);
        when(mockUserRepository.existsById(anyInt())).thenReturn(false);
        //test under control
        var toTest = new ReimbursementService(mockRepository,mockUserRepository);
        //when
        ReimbursementWriteModel reimbursement = new ReimbursementWriteModel();
        var exception = catchThrowable(() -> toTest.updateReimbursementById(reimbursement));
        //then
        assertThat(exception).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("should update new Reimbursement")
    void updateReimbursementById_updatesReimbursement() throws UserGroupNotFoundException, UserNotFoundException, ReimbursementNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        int beforeSize = inMemoryReimbursementRepository.count();
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);

        User user = inMemoryUserRepository.findById(2).orElseThrow(UserNotFoundException::new);

        LocalDate startDay = LocalDate.of(2022, 1, 20);
        LocalDate endDay = LocalDate.of(2022, 1, 21);
        Reimbursement reimbursement = new Reimbursement("foo", startDay, endDay,
                5, false,user);
        reimbursement.setId(3);
        //when
        toTest.updateReimbursementById(ReimbursementMapper.toWriteModel(reimbursement));
        int afterSize = toTest.readAll().size();
        var result =toTest.readById(3);

        //then
        assertThat(afterSize).isEqualTo(beforeSize);
        assertThat(result.getName()).isEqualTo("foo");
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2022,1,20));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2022,1,21));
        assertThat(result.getDistance()).isEqualTo(5);
        assertThat(result.isPushedToAccept()).isEqualTo(false);
    }

    @Test
    @DisplayName("should throw ReimbursementNotFoundException when given reimbursement id not found")
    void deleteReimbursementById_givenReimbursementIdNotFound_throwsReimbursementNotFoundException() {
        //given
        var mockRepository = mock(ReimbursementRepository.class);
        var mockUserRepository = mock(UserRepository.class);
        //and
        when(mockRepository.existsById(anyInt())).thenReturn(false);
        //test under control
        var toTest = new ReimbursementService(mockRepository,mockUserRepository);

        //when
        var exception = catchThrowable(() -> toTest.deleteReimbursementById(156));
        //then
        assertThat(exception).isInstanceOf(ReimbursementNotFoundException.class);
    }

    @Test
    @DisplayName("should delete exists entity")
    void deleteReimbursementById_deletesReimbursement() throws UserGroupNotFoundException, ReimbursementNotFoundException {
        //given
        InMemoryUserGroupRepository inMemoryUserGroupRepository = inMemoryUserGroupRepository();
        InMemoryUserRepository inMemoryUserRepository = inMemoryUserRepository();
        InMemoryReimbursementRepository inMemoryReimbursementRepository = inMemoryReimbursementRepository();
        reimbursementRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        int beforeSize = inMemoryReimbursementRepository.count();
        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository, inMemoryUserRepository);

        //when
        int reimbursementToDelete = 1;
        toTest.deleteReimbursementById(reimbursementToDelete);
        int afterSize = inMemoryReimbursementRepository.count();
        //then
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }


    @Test
    @DisplayName("should throws IllegalArgumentException when given EndDay is null")
    void validate_givenEndDayIsNull_throwsIllegalArgumentException(){
        //given
        User user = new User();
        user.setId(1);
        LocalDate startDay = LocalDate.of(2022, 1, 11);
        Reimbursement reimbursement = new Reimbursement("foo", startDay, null,
                -1, true,user);

        //when
        var exception = catchThrowable(() ->validate(
                ReimbursementMapper.toWriteModel(reimbursement)));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No end date.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given StartDay is form future")
    void validate_givenStartDayIsFromFuture_throwsIllegalArgumentException(){
        //given
        User user = new User();
        user.setId(1);
        LocalDate startDay = LocalDate.now().plusDays(10);
        LocalDate endDay = LocalDate.of(2022, 1, 11);
        Reimbursement reimbursement = new Reimbursement("foo", startDay, endDay,
                -1, true,user);

        //when
        var exception = catchThrowable(() ->validate(
                ReimbursementMapper.toWriteModel(reimbursement)));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start date should be in the past or present.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given EndDay is form future")
    void validate_givenEndDayIsFromFuture_throwsIllegalArgumentException(){
        //given
        User user = new User();
        user.setId(1);
        LocalDate startDay = LocalDate.of(2022, 1, 11);
        LocalDate endDay = LocalDate.now().plusDays(10);
        Reimbursement reimbursement = new Reimbursement("foo", startDay, endDay,
                -1, true,user);

        //when
        var exception = catchThrowable(() ->validate(
                ReimbursementMapper.toWriteModel(reimbursement)));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("End date should be in the past or present.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given StartDay is after EndDay")
    void validate_givenStartDayIsAfterEndDay_throwsIllegalArgumentException(){
        //given
        User user = new User();
        user.setId(1);
        LocalDate startDay = LocalDate.of(2022, 1, 20);
        LocalDate endDay = LocalDate.of(2022, 1, 10);
        Reimbursement reimbursement = new Reimbursement("foo", startDay, endDay,
                -1, true,user);

        //when
        var exception = catchThrowable(() ->validate(
                ReimbursementMapper.toWriteModel(reimbursement)));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start Day is after End Day.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given name is empty or has only white-space characters")
    void validate_emptyNameParam_throwsIllegalArgumentException(){
        //given
        User user = new User();
        user.setId(1);
        LocalDate startDay = LocalDate.of(2022, 1, 11);
        LocalDate endDay = LocalDate.of(2022, 1, 11);
        Reimbursement reimbursement = new Reimbursement("   ", startDay, endDay,
                0, true,user);
        //when
        var exception = catchThrowable(() -> validate (
                ReimbursementMapper.toWriteModel(reimbursement)));
        //then
        assertThat(exception)//.isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Reimbursement name couldn't be empty.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given name is too long")
    void validate_givenNameHasMoreThen_100_Characters_throwsIllegalArgumentException(){
        //given
        User user = new User();
        user.setId(1);
        String name = createLongString(101);
        LocalDate startDay = LocalDate.of(2022, 1, 11);
        LocalDate endDay = LocalDate.of(2022, 1, 11);
        Reimbursement reimbursement = new Reimbursement(name, startDay, endDay,
                0, true,user);
        //when
        var exception = catchThrowable(() -> validate(ReimbursementMapper.toWriteModel(reimbursement)));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Reimbursement name is too long.");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when given Distance is less than 0")
    void validate_givenDistanceIsLessThen_0_throwsIllegalArgumentException(){
        //given
        User user = new User();
        user.setId(1);
        LocalDate startDay = LocalDate.of(2022, 1, 11);
        LocalDate endDay = LocalDate.of(2022, 1, 11);
        Reimbursement reimbursement = new Reimbursement("foo", startDay, endDay,
                -1, true,user);

       //when
        var exception = catchThrowable(() ->validate(
                ReimbursementMapper.toWriteModel(reimbursement)));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Distance should be positive.");
    }
}