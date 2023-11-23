package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.model.ReimbursementRepository;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.ReimbursementReadModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.*;
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
    void createReimbursement() {
    }

    @Test
    void updateReimbursementById() {
    }

    @Test
    void deleteReimbursementById() {
    }

    @Test
    void toEntity() {
    }
}